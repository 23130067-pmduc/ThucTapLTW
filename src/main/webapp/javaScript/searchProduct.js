const keywordInput = document.getElementById("keywordInput");
const searchBtn = document.getElementById("searchBtn");
const productTableBody = document.getElementById("productTableBody");
const pagination = document.getElementById("productPagination");

const categoryFilter = document.getElementById("categoryFilter");
const statusFilter = document.getElementById("statusFilter");

let searchTimeout;

if (keywordInput){
    keywordInput.addEventListener("input", function (){
        clearTimeout(searchTimeout);

        searchTimeout = setTimeout(function (){
            searchProduct(1);

        }, 400);
    })
}



if (categoryFilter) {
    categoryFilter.addEventListener("change", function () {
        searchProduct(1);
    });
}

if (statusFilter) {
    statusFilter.addEventListener("change", function () {
        searchProduct(1);
    });
}


if (searchBtn){
    searchBtn.addEventListener("click", function (){
        searchProduct(1);
    })
}

function searchProduct(page){
    const keyword = keywordInput.value.trim();
    const categoryId = categoryFilter ? categoryFilter.value : 0;
    const status = statusFilter ? statusFilter.value : "";

    fetch("product-admin?ajax=search&keyword=" + encodeURIComponent(keyword)
        + "&categoryId=" + encodeURIComponent(categoryId)
        + "&status=" + encodeURIComponent(status)
        + "&page=" + page)
        .then(response => response.json())
        .then(data => {
            renderProductTable(data.products);
            renderPagination(data.currentPage, data.totalPages);
        })
}

function renderProductTable(products) {
    productTableBody.innerHTML = "";

    if (!products || products.length === 0) {
        productTableBody.innerHTML = `
            <tr>
                <td colspan="8" class="empty-row">
                    Không tìm thấy sản phẩm phù hợp
                </td>
            </tr>
        `;
        return;
    }

    products.forEach(p => {
        const statusClass = p.status === "Đang bán" ? "active" : "inactive";

        const statusBtn = p.status === "Đang bán"
            ? `
                <button type="button"
                        class="icon-btn delete"
                        title="Ngừng bán sản phẩm"
                        onclick="openStatusModal(${p.id}, '${escapeJs(p.name)}', 'Ngừng bán')">
                    <i class="fa fa-trash"></i>
                </button>
            `
            : `
                <button type="button"
                        class="icon-btn restore"
                        title="Mở bán lại sản phẩm"
                        onclick="openStatusModal(${p.id}, '${escapeJs(p.name)}', 'Đang bán')">
                    <i class="fa fa-rotate-left"></i>
                </button>
            `;

        const row = `
            <tr>
                <td class="col-id">${p.id}</td>

                <td class="col-image">
                    <img src="${escapeHtml(p.thumbnail || '')}"
                         alt="${escapeHtml(p.name)}"
                         class="product-thumb"
                         onerror="handleImageError(this)">
                </td>

                <td class="col-name" title="${escapeHtml(p.name)}">
                    ${escapeHtml(p.name)}
                </td>

                <td class="col-price">
                    ${formatPrice(p.price)} đ
                </td>

                <td class="col-category">
                    ${escapeHtml(p.categoryName || "")}
                </td>

                <td class="col-stock">
                    ${p.totalStock || 0}
                </td>

                <td class="col-status">
                    <span class="status-badge ${statusClass}">
                        ${escapeHtml(p.status)}
                    </span>
                </td>

                <td class="col-actions">
                    <div class="action-list">
                        <a href="product-admin?mode=view&id=${p.id}"
                           class="action-btn view"
                           title="Xem">
                            <i class="fa-solid fa-eye"></i>
                        </a>

                        <a href="product-admin?mode=edit&id=${p.id}"
                           class="action-btn edit"
                           title="Sửa">
                            <i class="fa-solid fa-pen"></i>
                        </a>

                        <a href="product-variant-admin?productId=${p.id}"
                           class="action-btn variant"
                           title="Biến thể">
                            <i class="fa-solid fa-layer-group"></i>
                        </a>

                        <a href="product-image-admin?productId=${p.id}"
                           class="action-btn image"
                           title="Ảnh sản phẩm">
                            <i class="fa-regular fa-image"></i>
                        </a>

                        ${statusBtn}
                    </div>
                </td>
            </tr>
        `;

        productTableBody.insertAdjacentHTML("beforeend", row);
    });
}

function renderPagination(currentPage, totalPages) {
    pagination.innerHTML = "";

    if (totalPages <= 1) {
        return;
    }

    let startPage = currentPage - 2;
    let endPage = currentPage + 2;

    if (startPage < 1) {
        startPage = 1;
    }

    if (endPage > totalPages) {
        endPage = totalPages;
    }

    if (currentPage > 1) {
        pagination.insertAdjacentHTML("beforeend", `
            <button type="button" class="page-btn" onclick="searchProduct(${currentPage - 1})">
                Trước
            </button>
        `);
    }

    for (let i = startPage; i <= endPage; i++) {
        pagination.insertAdjacentHTML("beforeend", `
            <button type="button"
                    class="page-btn ${i === currentPage ? "active" : ""}"
                    onclick="searchProduct(${i})">
                ${i}
            </button>
        `);
    }

    if (currentPage < totalPages) {
        pagination.insertAdjacentHTML("beforeend", `
            <button type="button" class="page-btn" onclick="searchProduct(${currentPage + 1})">
                Sau
            </button>
        `);
    }
}

function formatPrice(value) {
    if (value == null) return "0";
    return Number(value).toLocaleString("vi-VN");
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function escapeJs(value) {
    return String(value)
        .replaceAll("\\", "\\\\")
        .replaceAll("'", "\\'")
        .replaceAll('"', '\\"');
}