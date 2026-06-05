function updateHiddenFieldsFromRadio(radio) {
    document.querySelectorAll('.address-item-radio').forEach((el) => el.classList.remove('active'));
    radio.closest('.address-item-radio').classList.add('active');

    const rName = radio.getAttribute('data-name') || '';
    const rPhone = radio.getAttribute('data-phone') || '';
    const rAddr = radio.getAttribute('data-address') || '';

    const hiddenName = document.getElementById('hiddenName');
    const hiddenPhone = document.getElementById('hiddenPhone');
    const hiddenAddress = document.getElementById('hiddenAddress');

    if (hiddenName) hiddenName.value = rName;
    if (hiddenPhone) hiddenPhone.value = rPhone;
    if (hiddenAddress) hiddenAddress.value = rAddr;

    const sumClient = document.getElementById('summaryClientInfo');
    if (sumClient) sumClient.innerText = `${rName}, ${rPhone}`;

    const sumAddr = document.getElementById('summaryAddressInfo');
    if (sumAddr) sumAddr.innerText = rAddr;

    fetchShippingFeeForSelectedAddress();
}

function toggleAddress() {
    const detailView = document.getElementById('addressDetailView');
    const sumView = document.getElementById('addressSummaryView');
    const icon = document.getElementById('addressToggleIcon');

    if (detailView.style.display === 'none') {
        detailView.style.display = 'block';
        sumView.style.display = 'none';
        icon.style.transform = 'rotate(180deg)';
    } else {
        detailView.style.display = 'none';
        sumView.style.display = 'block';
        icon.style.transform = 'rotate(0deg)';
    }
}

function updatePaymentUI(radio) {
    document.querySelectorAll('.payment-method label').forEach((lbl) => lbl.classList.remove('active'));
    radio.closest('label').classList.add('active');

    const msg = document.getElementById('vnpay-message');
    if (msg) {
        msg.style.display = radio.value === 'VNPAY' ? 'block' : 'none';
    }
}

function openCheckoutModal() {
    const modal = document.getElementById('checkoutAddressModal');
    if (modal) {
        modal.style.display = 'flex';
    }
}

function closeCheckoutModal() {
    const modal = document.getElementById('checkoutAddressModal');
    if (modal) {
        modal.style.display = 'none';
    }
}

function parseAmountFromText(text) {
    const onlyDigits = (text || '').replace(/[^0-9]/g, '');
    return onlyDigits ? parseInt(onlyDigits, 10) : 0;
}

function formatVnd(value) {
    return `${Number(value || 0).toLocaleString('vi-VN')}₫`;
}

function getContextPath() {
    if (window.APP_CONTEXT_PATH) {
        return window.APP_CONTEXT_PATH;
    }

    const segments = window.location.pathname.split('/').filter(Boolean);
    if (segments.length <= 1) {
        return '';
    }

    return `/${segments[0]}`;
}

function getCurrentShippingFee() {
    const shippingFeeEl = document.getElementById('hiddenShippingFee');
    return parseAmountFromText(shippingFeeEl ? shippingFeeEl.value : '0');
}

function updateSummaryAmounts(shippingFee) {
    const subtotalEl = document.getElementById('subtotalAmount');
    const shippingEl = document.getElementById('shippingFeeAmount');
    const finalEl = document.getElementById('finalAmount');
    const hiddenShippingFeeEl = document.getElementById('hiddenShippingFee');
    const hiddenDiscountEl = document.getElementById('hiddenDiscount');
    const hiddenShippingDiscountEl = document.getElementById('hiddenShippingDiscount');

    if (!subtotalEl || !shippingEl || !finalEl) {
        return;
    }

    const subtotal = parseAmountFromText(subtotalEl.textContent);
    const safeShippingFee = Number.isFinite(Number(shippingFee)) ? Math.max(0, Math.round(Number(shippingFee))) : 0;
    const currentDiscount = hiddenDiscountEl ? parseAmountFromText(hiddenDiscountEl.value) : 0;
    const currentShippingDiscount = hiddenShippingDiscountEl ? parseAmountFromText(hiddenShippingDiscountEl.value) : 0;

    shippingEl.textContent = formatVnd(safeShippingFee);
    finalEl.textContent = formatVnd(
        Math.max(0, subtotal + safeShippingFee - currentDiscount - currentShippingDiscount)
    );

    if (hiddenShippingFeeEl) {
        hiddenShippingFeeEl.value = String(safeShippingFee);
    }
}

function setShippingHint(message) {
    const hintEl = document.getElementById('shippingFeeHint');
    if (!hintEl) return;

    hintEl.textContent = message || '';
}

async function fetchShippingFeeForSelectedAddress() {
    const selectedRadio = document.querySelector('input[name="selectedAddress"]:checked');
    const priceFastEl = document.getElementById('priceFast');

    if (!selectedRadio || !selectedRadio.value) {
        if (priceFastEl) priceFastEl.textContent = 'Miễn phí';
        updateSummaryAmounts(0);
        setShippingHint('');
        return;
    }

    if (priceFastEl) priceFastEl.textContent = 'Đang tính...';

    try {
        const contextPath = getContextPath();
        const response = await fetch(`${contextPath}/ghn_fee?addressId=${encodeURIComponent(selectedRadio.value)}`, {
            headers: {
                Accept: 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Không gọi được API phí vận chuyển');
        }

        const data = await response.json();

        if (data.success && typeof data.fee === 'number') {
            if (priceFastEl) priceFastEl.textContent = formatVnd(data.fee);
            updateSummaryAmounts(data.fee);
            setShippingHint('');
        } else {
            if (priceFastEl) priceFastEl.textContent = '--';
            updateSummaryAmounts(0);
            setShippingHint(data.message || 'Không tính được phí vận chuyển cho địa chỉ này.');
        }
    } catch (e) {
        if (priceFastEl) priceFastEl.textContent = '--';
        updateSummaryAmounts(0);
        setShippingHint('Có lỗi khi tính phí vận chuyển.');
    }
}

function setVoucherMessage(message, type) {
    const messageEl = document.getElementById('voucherMessage');
    if (!messageEl) return;

    messageEl.textContent = message || '';
    messageEl.classList.remove('success', 'error', 'show');

    if (message) {
        messageEl.classList.add('show');
    }

    if (type) {
        messageEl.classList.add(type);
    }
}

function setShippingVoucherMessage(message, type) {
    const messageEl = document.getElementById('shippingVoucherMessage');
    if (!messageEl) return;

    messageEl.textContent = message || '';
    messageEl.classList.remove('success', 'error', 'show');

    if (message) {
        messageEl.classList.add('show');
    }

    if (type) {
        messageEl.classList.add(type);
    }
}
function setDiscountAmount(discount) {
    const discountRow = document.getElementById('discountRow');
    const discountAmountEl = document.getElementById('discountAmount');
    const hiddenDiscountEl = document.getElementById('hiddenDiscount');

    const safeDiscount = Math.max(0, Math.round(Number(discount) || 0));

    if (discountRow) {
        discountRow.style.display = safeDiscount > 0 ? 'flex' : 'none';
    }

    if (discountAmountEl) {
        discountAmountEl.textContent = '-' + formatVnd(safeDiscount);
    }

    if (hiddenDiscountEl) {
        hiddenDiscountEl.value = String(safeDiscount);
    }
}

function setShippingDiscountAmount(discount) {
    const discountRow = document.getElementById('shippingDiscountRow');
    const discountAmountEl = document.getElementById('shippingDiscountAmount');
    const hiddenDiscountEl = document.getElementById('hiddenShippingDiscount');

    const safeDiscount = Math.max(0, Math.round(Number(discount) || 0));

    if (discountRow) {
        discountRow.style.display = safeDiscount > 0 ? 'flex' : 'none';
    }

    if (discountAmountEl) {
        discountAmountEl.textContent = '-' + formatVnd(safeDiscount);
    }

    if (hiddenDiscountEl) {
        hiddenDiscountEl.value = String(safeDiscount);
    }
}

async function applyVoucher() {
    const voucherInput = document.getElementById('voucherCode');
    const shippingFeeEl = document.getElementById('hiddenShippingFee');

    if (!voucherInput) return false;

    const voucherCode = voucherInput.value.trim();
    const currentShippingFee = getCurrentShippingFee();

    if (!voucherCode) {
        setDiscountAmount(0);
        updateSummaryAmounts(currentShippingFee);
        setVoucherMessage('', '');
        return true;
    }

    try {
        const contextPath = getContextPath();
        const body = new URLSearchParams();

        body.append('voucherCode', voucherCode);
        body.append('shippingFee', shippingFeeEl ? shippingFeeEl.value : '0');

        const response = await fetch(`${contextPath}/apply-voucher`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
                Accept: 'application/json'
            },
            body
        });

        if (!response.ok) {
            throw new Error('Không gọi được API áp dụng voucher');
        }

        const data = await response.json();

        if (data.success) {
            setDiscountAmount(data.discount);
            updateSummaryAmounts(currentShippingFee);
            setVoucherMessage(data.message, 'success');
            return true;
        }

        setDiscountAmount(0);
        updateSummaryAmounts(currentShippingFee);
        setVoucherMessage(data.message, 'error');
        return false;
    } catch (e) {
        setDiscountAmount(0);
        updateSummaryAmounts(currentShippingFee);
        setVoucherMessage('Có lỗi khi áp dụng mã giảm giá.', 'error');
        return false;
    }
}

async function applyShippingVoucher() {
    const voucherInput = document.getElementById('shippingVoucherCode');
    const shippingFeeEl = document.getElementById('hiddenShippingFee');
    const orderDiscountEl = document.getElementById('hiddenDiscount');

    if (!voucherInput) return false;

    const shippingVoucherCode = voucherInput.value.trim();
    const currentShippingFee = getCurrentShippingFee();
    const currentOrderDiscount = parseAmountFromText(orderDiscountEl ? orderDiscountEl.value : '0');

    if (!shippingVoucherCode) {
        setShippingDiscountAmount(0);
        updateSummaryAmounts(currentShippingFee);
        setShippingVoucherMessage('', '');
        return true;
    }

    try {
        const contextPath = getContextPath();
        const body = new URLSearchParams();

        body.append('shippingVoucherCode', shippingVoucherCode);
        body.append('shippingFee', shippingFeeEl ? shippingFeeEl.value : '0');
        body.append('orderDiscount', String(currentOrderDiscount));

        const response = await fetch(`${contextPath}/apply-shipping-voucher`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
                Accept: 'application/json'
            },
            body
        });

        if (!response.ok) {
            throw new Error('Không gọi được API áp dụng voucher vận chuyển');
        }

        const data = await response.json();

        if (data.success) {
            setShippingDiscountAmount(data.shippingDiscount);
            updateSummaryAmounts(currentShippingFee);
            setShippingVoucherMessage(data.message, 'success');
            return true;
        }

        setShippingDiscountAmount(0);
        updateSummaryAmounts(currentShippingFee);
        setShippingVoucherMessage(data.message, 'error');
        return false;
    } catch (e) {
        setShippingDiscountAmount(0);
        updateSummaryAmounts(currentShippingFee);
        setShippingVoucherMessage('Có lỗi khi áp dụng mã giảm giá vận chuyển.', 'error');
        return false;
    }
}

async function applyAllVouchers() {
    const voucherInput = document.getElementById('voucherCode');
    const shippingVoucherInput = document.getElementById('shippingVoucherCode');

    const voucherCode = voucherInput ? voucherInput.value.trim() : '';
    const shippingVoucherCode = shippingVoucherInput ? shippingVoucherInput.value.trim() : '';

    if (!voucherCode && !shippingVoucherCode) {
        setDiscountAmount(0);
        setShippingDiscountAmount(0);
        updateSummaryAmounts(getCurrentShippingFee());
        setVoucherMessage('Vui lòng nhập ít nhất một mã giảm giá.', 'error');
        setShippingVoucherMessage('', '');
        return;
    }

    if (voucherCode) {
        await applyVoucher();
    } else {
        setDiscountAmount(0);
        setVoucherMessage('', '');
        updateSummaryAmounts(getCurrentShippingFee());
    }

    if (shippingVoucherCode) {
        await applyShippingVoucher();
    } else {
        setShippingDiscountAmount(0);
        setShippingVoucherMessage('', '');
        updateSummaryAmounts(getCurrentShippingFee());
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const checkedRadio = document.querySelector('input[name="selectedAddress"]:checked');

    if (checkedRadio) {
        updateHiddenFieldsFromRadio(checkedRadio);
    } else {
        updateSummaryAmounts(0);
    }

    const applyAllBtn = document.getElementById('applyAllVoucherBtn');
    const voucherInput = document.getElementById('voucherCode');
    const shippingVoucherInput = document.getElementById('shippingVoucherCode');

    if (applyAllBtn) {
        applyAllBtn.addEventListener('click', applyAllVouchers);
    }

    [voucherInput, shippingVoucherInput].forEach(function (input) {
        if (!input) return;

        input.addEventListener('keydown', function (event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                applyAllVouchers();
            }
        });
    });
});
