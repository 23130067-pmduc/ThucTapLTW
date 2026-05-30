const keywordInput = document.getElementById("keywordInput");
const searchBtn = document.getElementById("searchBtn");
const categoryTableBody = document.getElementById("categoryTableBody");
const pagination = document.getElementById("pagination");

let searchTimeout;

if (keywordInput) {
    keywordInput.addEventListener("input", function () {
        clearTimeout(searchTimeout);

        searchTimeout = setTimeout(function () {
            searchCategories(1);
        }, 400);
    });
}

if (searchBtn) {
    searchBtn.addEventListener("click", function () {
        searchCategories(1);
    });
}

function searchCategories(page) {
    const keyword = keywordInput.value.trim();

    fetch("category-admin?ajax=search&keyword=" + encodeURIComponent(keyword) + "&page=" + page)
        .then(response => response.json())
        .then(data => {
            renderCategoryTable(data.categorys);
            renderPagination(data.currentPage, data.totalPages);
        })
        .catch(error => {
            console.error("Lỗi tìm kiếm danh mục:", error);
        });
}

function renderCategoryTable(categorys) {
    categoryTableBody.innerHTML = "";

    if (!categorys || categorys.length === 0) {
        categoryTableBody.innerHTML = `
            <tr>
                <td colspan="8" class="empty-row" style="text-align:center; padding:24px;">
                    Không tìm thấy sản phẩm phù hợp
                </td>
            </tr>
        `;
        return;
    }

    categorys.forEach(c => {
        const statusHtml = c.status == 1
            ? `<span class="status active">Hoạt động</span>`
            : `<span class="status blocked">Bị khóa</span>`;

        const row = `
            <tr>
                <td>${c.id}</td>
                <td>${escapeHtml(c.name)}</td>
                <td>${escapeHtml(c.description || "")}</td>
                <td>${c.countProduct}</td>
                <td>${statusHtml}</td>
                <td class="actions">
                    <a href="category-admin?mode=view&id=${c.id}"
                       class="icon-btn view" title="Xem chi tiết">
                        <i class="fa fa-eye"></i>
                    </a>

                    <a href="category-admin?mode=edit&id=${c.id}"
                       class="icon-btn edit" title="Chỉnh sửa">
                        <i class="fa fa-pen"></i>
                    </a>

                    <button type="button"
                            class="icon-btn delete"
                            title="Xóa danh mục"
                            onclick="openDeleteModal(${c.id}, '${escapeJs(c.name)}')">
                        <i class="fa fa-trash"></i>
                    </button>
                </td>
            </tr>
        `;

        categoryTableBody.insertAdjacentHTML("beforeend", row);
    });
}

function renderPagination(currentPage, totalPages) {
    pagination.innerHTML = "";

    if (totalPages <= 1) {
        return;
    }

    if (currentPage > 1) {
        pagination.insertAdjacentHTML("beforeend", `
            <button type="button" class="page-btn" onclick="searchCategories(${currentPage - 1})">
                Trước
            </button>
        `);
    } else {
        pagination.insertAdjacentHTML("beforeend", `
            <span class="page-btn disabled">Trước</span>
        `);
    }

    for (let i = 1; i <= totalPages; i++) {
        pagination.insertAdjacentHTML("beforeend", `
            <button type="button"
                    class="page-btn ${i === currentPage ? "active" : ""}"
                    onclick="searchCategories(${i})">
                ${i}
            </button>
        `);
    }

    if (currentPage < totalPages) {
        pagination.insertAdjacentHTML("beforeend", `
            <button type="button" class="page-btn" onclick="searchCategories(${currentPage + 1})">
                Sau
            </button>
        `);
    } else {
        pagination.insertAdjacentHTML("beforeend", `
            <span class="page-btn disabled">Sau</span>
        `);
    }
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