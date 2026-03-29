document.addEventListener("DOMContentLoaded", function () {
    const cancelModal = document.getElementById("cancelOrderModal");
    const closeBtn = document.getElementById("cancelOrderCloseBtn");
    const confirmBtn = document.getElementById("confirmCancelOrderBtn");

    let currentCancelForm = null;

    document.querySelectorAll(".btn-open-cancel-modal").forEach((button) => {
        button.addEventListener("click", function () {
            currentCancelForm = this.closest("form");

            if (!currentCancelForm) {
                console.error("Không tìm thấy form huỷ đơn.");
                return;
            }

            cancelModal.classList.add("show");
        });
    });

    if (closeBtn) {
        closeBtn.addEventListener("click", function () {
            cancelModal.classList.remove("show");
            currentCancelForm = null;
        });
    }

    if (confirmBtn) {
        confirmBtn.addEventListener("click", function () {
            if (currentCancelForm) {
                currentCancelForm.submit();
            }
        });
    }

    if (cancelModal) {
        cancelModal.addEventListener("click", function (e) {
            if (e.target === cancelModal) {
                cancelModal.classList.remove("show");
                currentCancelForm = null;
            }
        });
    }
});