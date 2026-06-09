document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('voucherStatusModal');
    const message = document.getElementById('voucherStatusModalMessage');
    const voucherId = document.getElementById('voucherStatusId');
    const statusValue = document.getElementById('voucherStatusValue');
    const submitButton = document.getElementById('voucherStatusSubmit');
    const icon = document.getElementById('voucherStatusModalIcon');

    function closeModal() {
        if (!modal) return;
        modal.classList.remove('show');
        modal.setAttribute('aria-hidden', 'true');
    }

    document.querySelectorAll('.js-open-voucher-status-modal').forEach(function (button) {
        button.addEventListener('click', function () {
            const actionLabel = button.dataset.actionLabel;
            const isUnlock = button.dataset.status === '1';

            voucherId.value = button.dataset.id;
            statusValue.value = button.dataset.status;
            message.innerHTML = 'Bạn có chắc muốn <strong>' + actionLabel.toLowerCase()
                + '</strong> mã giảm giá <strong>' + button.dataset.code + '</strong> không?';
            submitButton.textContent = actionLabel;
            submitButton.className = 'btn-voucher-confirm ' + (isUnlock ? 'unlock' : 'lock');
            icon.className = 'fa-solid ' + (isUnlock ? 'fa-lock-open' : 'fa-lock');

            modal.classList.add('show');
            modal.setAttribute('aria-hidden', 'false');
        });
    });

    document.querySelectorAll('.js-close-voucher-status-modal').forEach(function (button) {
        button.addEventListener('click', closeModal);
    });

    if (modal) {
        modal.addEventListener('click', function (event) {
            if (event.target === modal) closeModal();
        });
    }

    document.addEventListener('keydown', function (event) {
        if (event.key === 'Escape') closeModal();
    });
});
