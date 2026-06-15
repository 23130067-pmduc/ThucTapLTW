document.addEventListener('DOMContentLoaded', function () {
    const statusModal = document.getElementById('statusModal');
    const statusMessage = document.getElementById('statusMessage');
    const statusVariantId = document.getElementById('statusVariantId');
    const statusVariantValue = document.getElementById('statusVariantValue');
    const statusSubmitBtn = document.getElementById('statusSubmitBtn');
    const statusModalTitle = document.getElementById('statusModalTitle');

    const statusOpenButtons = document.querySelectorAll('.js-open-status-modal');
    const statusCloseButtons = document.querySelectorAll('.js-close-status-modal');

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

    function openStatusModal(id, name, status, actionLabel) {
        if (!statusVariantId || !statusVariantValue || !statusMessage || !statusSubmitBtn || !statusModalTitle) {
            return;
        }

        statusVariantId.value = id;
        statusVariantValue.value = status;

        if (status === 'Đang bán') {
            statusModalTitle.innerText = 'Xác nhận mở biến thể';
            statusMessage.innerHTML = 'Bạn có chắc muốn mở lại biến thể <b>' + name + '</b> không?';
            statusSubmitBtn.innerText = 'Mở biến thể';
            statusSubmitBtn.classList.remove('btn-danger');
            statusSubmitBtn.classList.add('btn-success');
        } else {
            statusModalTitle.innerText = 'Xác nhận khóa biến thể';
            statusMessage.innerHTML = 'Bạn có chắc muốn khóa biến thể <b>' + name + '</b> không? Biến thể sẽ không hiển thị cho khách hàng chọn mua.';
            statusSubmitBtn.innerText = 'Khóa biến thể';
            statusSubmitBtn.classList.remove('btn-success');
            statusSubmitBtn.classList.add('btn-danger');
        }

        openModal(statusModal);
    }

    statusOpenButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            openStatusModal(button.dataset.id, button.dataset.name, button.dataset.status, button.dataset.actionLabel);
        });
    });

    statusCloseButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            closeModal(statusModal);
        });
    });

    if (statusModal) {
        statusModal.addEventListener('click', function (event) {
            if (event.target === statusModal) {
                closeModal(statusModal);
            }
        });
    }

    document.addEventListener('keydown', function (event) {
        if (event.key === 'Escape') {
            closeModal(statusModal);
        }
    });
});
