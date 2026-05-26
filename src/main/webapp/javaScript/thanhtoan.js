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

    setDiscountAmount(0);
    setVoucherMessage('', '');
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

function getOriginalShippingFee() {
    const hiddenShippingFeeEl = document.getElementById('hiddenShippingFee');
    return parseAmountFromText(hiddenShippingFeeEl ? hiddenShippingFeeEl.value : '0');
}

function updateSummaryAmounts(shippingFee) {
    const subtotalEl = document.getElementById('subtotalAmount');
    const shippingEl = document.getElementById('shippingFeeAmount');
    const finalEl = document.getElementById('finalAmount');
    const hiddenShippingFeeEl = document.getElementById('hiddenShippingFee');
    const hiddenDiscountEl = document.getElementById('hiddenDiscount');

    if (!subtotalEl || !shippingEl || !finalEl) {
        return;
    }

    const subtotal = parseAmountFromText(subtotalEl.textContent);
    const safeShippingFee = Number.isFinite(Number(shippingFee)) ? Math.max(0, Math.round(Number(shippingFee))) : 0;
    const currentDiscount = hiddenDiscountEl ? parseAmountFromText(hiddenDiscountEl.value) : 0;

    shippingEl.textContent = formatVnd(safeShippingFee);
    finalEl.textContent = formatVnd(Math.max(0, subtotal + safeShippingFee - currentDiscount));

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

    if (!selectedRadio || !selectedRadio.value) {
        updateSummaryAmounts(0);
        setShippingHint('');
        return;
    }

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
            updateSummaryAmounts(data.fee);
            setShippingHint('');
        } else {
            updateSummaryAmounts(0);
            setShippingHint(data.message || 'Không tính được phí vận chuyển cho địa chỉ này.');
        }
    } catch (e) {
        updateSummaryAmounts(0);
        setShippingHint('Có lỗi khi tính phí vận chuyển.');
    }
}

function setVoucherMessage(message, type) {
    const messageEl = document.getElementById('voucherMessage');
    if (!messageEl) return;

    messageEl.textContent = message || '';
    messageEl.classList.remove('success', 'error');

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

async function applyVoucher() {
    const voucherInput = document.getElementById('voucherCode');
    const shippingFeeEl = document.getElementById('hiddenShippingFee');

    if (!voucherInput) {
        return;
    }

    const voucherCode = voucherInput.value.trim();
    const originalShippingFee = parseAmountFromText(shippingFeeEl ? shippingFeeEl.value : '0');

    if (!voucherCode) {
        setDiscountAmount(0);
        updateSummaryAmounts(originalShippingFee);
        setVoucherMessage('Vui lòng nhập mã giảm giá.', 'error');
        return;
    }

    try {
        const contextPath = getContextPath();
        const body = new URLSearchParams();

        body.append('voucherCode', voucherCode);
        body.append('shippingFee', String(originalShippingFee));

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
            setDiscountAmount(data.totalDiscount || data.discount || 0);

            const shippingEl = document.getElementById('shippingFeeAmount');
            if (shippingEl && data.voucherScope === 'SHIPPING') {
                shippingEl.textContent = formatVnd(data.shippingFeeAfterDiscount || 0);
            }

            const finalEl = document.getElementById('finalAmount');
            if (finalEl) {
                finalEl.textContent = formatVnd(data.finalAmount);
            }

            setVoucherMessage(data.message, 'success');
        } else {
            setDiscountAmount(0);
            updateSummaryAmounts(originalShippingFee);
            setVoucherMessage(data.message, 'error');
        }
    } catch (e) {
        setDiscountAmount(0);
        updateSummaryAmounts(originalShippingFee);
        setVoucherMessage('Có lỗi khi áp dụng mã giảm giá.', 'error');
    }
}

document.addEventListener('DOMContentLoaded', function () {
    const checkedRadio = document.querySelector('input[name="selectedAddress"]:checked');

    if (checkedRadio) {
        updateHiddenFieldsFromRadio(checkedRadio);
    } else {
        updateSummaryAmounts(0);
    }

    const applyBtn = document.getElementById('applyVoucherBtn');
    const voucherInput = document.getElementById('voucherCode');

    if (applyBtn) {
        applyBtn.addEventListener('click', applyVoucher);
    }

    if (voucherInput) {
        voucherInput.addEventListener('keydown', function (event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                applyVoucher();
            }
        });
    }
});
