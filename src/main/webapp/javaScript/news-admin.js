document.addEventListener("DOMContentLoaded", () => {
    const deleteModal = document.getElementById("deleteModal");
    const deleteMessage = document.getElementById("deleteMessage");
    const deleteNewsId = document.getElementById("deleteNewsId");
    const btnCloseDeleteModal = document.getElementById("btnCloseDeleteModal");
    const btnCancelDelete = document.getElementById("btnCancelDelete");

    document.querySelectorAll(".js-open-delete").forEach((button) => {
        button.addEventListener("click", () => {
            const id = button.dataset.id;
            const title = button.dataset.title || "bài viết này";

            deleteNewsId.value = id;
            deleteMessage.innerHTML = `Bạn có chắc muốn xóa bài viết <b>${escapeHtml(title)}</b> không?`;
            deleteModal.classList.add("show");
        });
    });

    btnCloseDeleteModal?.addEventListener("click", closeDeleteModal);
    btnCancelDelete?.addEventListener("click", closeDeleteModal);

    deleteModal?.addEventListener("click", (event) => {
        if (event.target === deleteModal) {
            closeDeleteModal();
        }
    });

    function closeDeleteModal() {
        deleteModal.classList.remove("show");
    }

    function escapeHtml(value) {
        return value
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/\"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
});
