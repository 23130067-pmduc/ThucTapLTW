function updateHiddenFieldsFromRadio(radio) {
    document.querySelectorAll('.address-item-radio').forEach(el => el.classList.remove('active'));
    radio.closest('.address-item-radio').classList.add('active');

    let rName = radio.getAttribute('data-name');
    let rPhone = radio.getAttribute('data-phone');
    let rAddr = radio.getAttribute('data-address');

    document.getElementById('hiddenName').value = rName;
    document.getElementById('hiddenPhone').value = rPhone;
    document.getElementById('hiddenAddress').value = rAddr;

    const sumClient = document.getElementById('summaryClientInfo');
    if (sumClient) sumClient.innerText = rName + ', ' + rPhone;
    const sumAddr = document.getElementById('summaryAddressInfo');
    if (sumAddr) sumAddr.innerText = rAddr;
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

document.addEventListener('DOMContentLoaded', function() {
    const checkedRadio = document.querySelector('input[name="selectedAddress"]:checked');
    if(checkedRadio) {
        updateHiddenFieldsFromRadio(checkedRadio);
    }
});

function updatePaymentUI(radio) {
    document.querySelectorAll('.payment-method label').forEach(lbl => lbl.classList.remove('active'));
    radio.closest('label').classList.add('active');
    const msg = document.getElementById('vnpay-message');
    if(msg) {
        if(radio.value === 'VNPAY') {
            msg.style.display = 'block';
        } else {
            msg.style.display = 'none';
        }
    }
}

function openCheckoutModal() {
    document.getElementById('checkoutAddressModal').style.display = 'flex';
}
function closeCheckoutModal() {
    document.getElementById('checkoutAddressModal').style.display = 'none';
}