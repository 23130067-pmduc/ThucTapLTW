document.addEventListener('DOMContentLoaded', () => {
    const statusModal = document.getElementById('statusModal');
    const statusMessage = document.getElementById('statusModalMessage');
    const statusTransactionId = document.getElementById('statusTransactionId');
    const statusValue = document.getElementById('statusValue');
    const statusRedirect = document.getElementById('statusRedirect');

    if (!statusModal || !statusMessage || !statusTransactionId || !statusValue) {
        return;
    }

    function closeModal() {
        statusModal.classList.remove('show');
        statusModal.setAttribute('aria-hidden', 'true');
        statusTransactionId.value = '';
        statusValue.value = '';
    }

    document.querySelectorAll('.js-open-status-modal').forEach(button => {
        button.addEventListener('click', () => {
            statusTransactionId.value = button.dataset.transactionId || '';
            statusValue.value = button.dataset.status || '';
            statusMessage.textContent = button.dataset.message || 'Bạn có chắc muốn thực hiện thao tác này không?';

            if (statusRedirect) {
                statusRedirect.value = button.dataset.redirect || '/inventory-history-admin';
            }

            statusModal.classList.add('show');
            statusModal.setAttribute('aria-hidden', 'false');
        });
    });

    document.querySelectorAll('.js-close-status-modal').forEach(button => {
        button.addEventListener('click', closeModal);
    });

    statusModal.addEventListener('click', event => {
        if (event.target === statusModal) {
            closeModal();
        }
    });

    document.addEventListener('keydown', event => {
        if (event.key === 'Escape' && statusModal.classList.contains('show')) {
            closeModal();
        }
    });
});
