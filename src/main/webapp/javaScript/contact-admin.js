function openDeleteModal(id, name) {
    document.getElementById('deleteContactId').value = id;
    document.getElementById('deleteMessage').innerHTML =
        'Bạn có chắc muốn xóa liên hệ từ "<b>' + name + '</b>" không?';
    document.getElementById('deleteModal').style.display = 'flex';
}

function closeDeleteModal() {
    document.getElementById('deleteModal').style.display = 'none';
}

function showToast(message, type = 'success', icon = 'fa-circle-check') {
    const container = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = 'toast ' + type;
    toast.innerHTML =
        '<i class="fa-solid ' + icon + '"></i>' +
        '<span>' + message + '</span>' +
        '<button class="toast-close" onclick="dismissToast(this.parentElement)">' +
        '<i class="fa-solid fa-xmark"></i></button>' +
        '<div class="toast-progress"></div>';
    container.appendChild(toast);
    setTimeout(() => dismissToast(toast), 3000);
}

function dismissToast(toast) {
    if (!toast || !toast.parentElement) return;
    toast.style.animation = 'toastOut 0.35s ease forwards';
    setTimeout(() => toast.remove(), 350);
}

(function () {
    const params = new URLSearchParams(window.location.search);
    const success = params.get('success');
    if (success === 'created') {
        showToast('Thêm liên hệ mới thành công!', 'success', 'fa-circle-check');
    }
    if (success) {
        const url = new URL(window.location);
        url.searchParams.delete('success');
        window.history.replaceState({}, '', url);
    }
}());
