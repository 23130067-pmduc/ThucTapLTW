document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("confirmModal");
    const notificationIdInput = document.getElementById("confirmNotificationId");
    const openButtons = document.querySelectorAll(".open-confirm-btn");
    const closeButton = document.getElementById("closeConfirmModal");

    openButtons.forEach(function (button) {
        button.addEventListener("click", function () {
            const id = this.getAttribute("data-id");
            notificationIdInput.value = id;
            modal.classList.add("show");
        });
    });

    closeButton.addEventListener("click", function () {
        modal.classList.remove("show");
    });

    modal.addEventListener("click", function (event) {
        if (event.target === modal) {
            modal.classList.remove("show");
        }
    });
});