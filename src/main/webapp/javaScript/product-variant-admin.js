document.addEventListener('DOMContentLoaded', function () {
    const deleteModal = document.getElementById('deleteModal');
    const deleteMessage = document.getElementById('deleteMessage');
    const deleteVariantId = document.getElementById('deleteVariantId');

    const deleteOpenButtons = document.querySelectorAll('.js-open-delete-modal');
    const deleteCloseButtons = document.querySelectorAll('.js-close-delete-modal');

    function openModal(modal) {
        if (!modal) {
            return;
        }

        modal.classList.add('show');
        modal.setAttribute('aria-hidden', 'false');
    }

    function closeModal(modal) {
        if (!modal) {
            return;
        }

        modal.classList.remove('show');
        modal.setAttribute('aria-hidden', 'true');
    }

    function openDeleteModal(id, name) {
        if (!deleteVariantId || !deleteMessage) {
            return;
        }

        deleteVariantId.value = id;
        deleteMessage.innerHTML = 'Bạn có chắc muốn xóa biến thể <b>' + name + '</b> không?';
        openModal(deleteModal);
    }

    deleteOpenButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            openDeleteModal(button.dataset.id, button.dataset.name);
        });
    });

    deleteCloseButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            closeModal(deleteModal);
        });
    });

    if (deleteModal) {
        deleteModal.addEventListener('click', function (event) {
            if (event.target === deleteModal) {
                closeModal(deleteModal);
            }
        });
    }

    document.addEventListener('keydown', function (event) {
        if (event.key === 'Escape') {
            closeModal(deleteModal);
        }
    });
});
