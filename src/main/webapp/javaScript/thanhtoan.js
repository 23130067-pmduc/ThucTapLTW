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
    document.getElementById('checkoutAddressModal').style.display = 'flex';
}

function closeCheckoutModal() {
    document.getElementById('checkoutAddressModal').style.display = 'none';
}

function parseAmountFromText(text) {
    const onlyDigits = (text || '').replace(/[^0-9]/g, '');
    return onlyDigits ? parseInt(onlyDigits, 10) : 0;
}

function formatVnd(value) {
    return `${Number(value || 0).toLocaleString('vi-VN')}₫`;
}

function updateSummaryAmounts(shippingFee) {
    const subtotalEl = document.getElementById('subtotalAmount');
    const shippingEl = document.getElementById('shippingFeeAmount');
    const finalEl = document.getElementById('finalAmount');
    const hiddenShippingFeeEl = document.getElementById('hiddenShippingFee');

    if (!subtotalEl || !shippingEl || !finalEl) {
        return;
    }

    const subtotal = parseAmountFromText(subtotalEl.textContent);
    const safeShippingFee = Number.isFinite(shippingFee) ? Math.max(0, Math.round(shippingFee)) : 0;

    shippingEl.textContent = formatVnd(safeShippingFee);
    finalEl.textContent = formatVnd(subtotal + safeShippingFee);

    if (hiddenShippingFeeEl) {
        hiddenShippingFeeEl.value = String(safeShippingFee);
    }
}

function setShippingHint(message) {
    const hintEl = document.getElementById('shippingFeeHint');
    if (!hintEl) return;
    hintEl.textContent = message || '';
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
            headers: { Accept: 'application/json' }
        });

        if (!response.ok) throw new Error();

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

document.addEventListener('DOMContentLoaded', function () {
    const checkedRadio = document.querySelector('input[name="selectedAddress"]:checked');
    if (checkedRadio) {
        updateHiddenFieldsFromRadio(checkedRadio);
    } else {
        updateSummaryAmounts(0);
    }
});
