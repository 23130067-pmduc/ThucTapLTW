function confirmDelete(productId, productName) {
    const ok = confirm(`Bạn chắc chắn muốn xóa sản phẩm "${productName}" không?`);
    if (ok) {
        window.location.href = `product-admin?action=delete&id=${productId}`;
    }
}

function handleImageError(img) {
    img.onerror = null;
    img.src = 'img/no-image.png';
}

document.addEventListener('DOMContentLoaded', function () {
    const searchForm = document.getElementById('productSearchForm');
    const keywordInput = document.getElementById('keywordInput');

    if (searchForm && keywordInput) {
        searchForm.addEventListener('submit', function () {
            keywordInput.value = keywordInput.value.trim();
        });
    }
});