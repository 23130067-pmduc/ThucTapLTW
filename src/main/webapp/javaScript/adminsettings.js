function openDeleteModal(button) {
    const adminId = button.dataset.adminId;
    const row = button.closest("tr");
    const username = row ? row.querySelector(".admin-username").textContent.trim() : "";

    document.getElementById("deleteAdminId").value = adminId;
    document.getElementById("deleteAdminName").textContent = username || ("#" + adminId);
    document.getElementById("deleteAdminModal").style.display = "flex";
}

function closeDeleteModal() {
    document.getElementById("deleteAdminModal").style.display = "none";
}
