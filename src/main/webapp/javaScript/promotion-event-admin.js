document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById('promotionStatusModal');
    const message = document.getElementById('promotionStatusModalMessage');
    const promotionId = document.getElementById('promotionStatusId');
    const statusValue = document.getElementById('promotionStatusValue');
    const submitButton = document.getElementById('promotionStatusSubmit');
    const icon = document.getElementById('promotionStatusModalIcon');

    function closeModal() {
        if (!modal) return;
        modal.classList.remove('show');
        modal.setAttribute('aria-hidden', 'true');
    }

    document.querySelectorAll('.js-open-promotion-status-modal').forEach(function (button) {
        button.addEventListener('click', function () {
            const actionLabel = button.dataset.actionLabel;
            const isUnlock = button.dataset.status === '1';

            promotionId.value = button.dataset.id;
            statusValue.value = button.dataset.status;
            message.innerHTML = 'Bạn có chắc muốn <strong>' + actionLabel.toLowerCase()
                + '</strong> chương trình <strong>' + button.dataset.title + '</strong> không?';
            submitButton.textContent = actionLabel;
            submitButton.className = 'btn-promotion-confirm ' + (isUnlock ? 'unlock' : 'lock');
            icon.className = 'fa-solid ' + (isUnlock ? 'fa-lock-open' : 'fa-lock');

            modal.classList.add('show');
            modal.setAttribute('aria-hidden', 'false');
        });
    });

    document.querySelectorAll('.js-close-promotion-status-modal').forEach(function (button) {
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
