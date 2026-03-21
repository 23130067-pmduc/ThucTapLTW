document.addEventListener('DOMContentLoaded', function () {
    const deleteModal = document.getElementById('deleteModal');
    const viewModal = document.getElementById('viewModal');

    const deleteMessage = document.getElementById('deleteMessage');
    const deleteVariantId = document.getElementById('deleteVariantId');

    const deleteOpenButtons = document.querySelectorAll('.js-open-delete-modal');
    const deleteCloseButtons = document.querySelectorAll('.js-close-delete-modal');

    const viewOpenButtons = document.querySelectorAll('.js-open-view-modal');
    const viewCloseButtons = document.querySelectorAll('.js-close-view-modal');

    const viewFields = {
        id: document.getElementById('viewVariantId'),
        size: document.getElementById('viewVariantSize'),
        color: document.getElementById('viewVariantColor'),
        price: document.getElementById('viewVariantPrice'),
        salePrice: document.getElementById('viewVariantSalePrice'),
        stock: document.getElementById('viewVariantStock')
    };

    function formatCurrency(value) {
        const number = Number(value || 0);
        return number.toLocaleString('vi-VN') + ' ₫';
    }

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
        deleteVariantId.value = id;
        deleteMessage.innerHTML = 'Bạn có chắc muốn xóa biến thể <b>' + name + '</b> không?';
        openModal(deleteModal);
    }

    function openViewModal(button) {
        viewFields.id.textContent = button.dataset.id || '';
        viewFields.size.textContent = button.dataset.size || '';
        viewFields.color.textContent = button.dataset.color || '';
        viewFields.price.textContent = formatCurrency(button.dataset.price);
        viewFields.salePrice.textContent = formatCurrency(button.dataset.salePrice);
        viewFields.stock.textContent = button.dataset.stock || '0';
        openModal(viewModal);
    }

    deleteOpenButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            openDeleteModal(button.dataset.id, button.dataset.name);
        });
    });

    viewOpenButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            openViewModal(button);
        });
    });

    deleteCloseButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            closeModal(deleteModal);
        });
    });

    viewCloseButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            closeModal(viewModal);
        });
    });

    [deleteModal, viewModal].forEach(function (modal) {
        if (!modal) {
            return;
        }

        modal.addEventListener('click', function (event) {
            if (event.target === modal) {
                closeModal(modal);
            }
        });
    });

    document.addEventListener('keydown', function (event) {
        if (event.key !== 'Escape') {
            return;
        }

        closeModal(deleteModal);
        closeModal(viewModal);
    });
});
