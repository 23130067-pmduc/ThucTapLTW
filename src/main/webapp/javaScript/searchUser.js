const keywordInput = document.getElementById("keywordInput");
const searchBtn = document.getElementById("searchBtn");
const userTableBody = document.getElementById("userTableBody");
const pagination = document.getElementById("userPagination");

const roleFilter = document.getElementById("roleFilter");
const statusFilter = document.getElementById("statusFilter");

let searchTimeout;

if (keywordInput) {
    keywordInput.addEventListener("input", function () {
        clearTimeout(searchTimeout);

        searchTimeout = setTimeout(function () {
            searchUser(1);
        }, 400);
    });
}

if (roleFilter) {
    roleFilter.addEventListener("change", function () {
        searchUser(1);
    });
}

if (statusFilter) {
    statusFilter.addEventListener("change", function () {
        searchUser(1);
    });
}

if (searchBtn) {
    searchBtn.addEventListener("click", function () {
        searchUser(1);
    });
}

function searchUser(page) {
    const keyword = keywordInput ? keywordInput.value.trim() : "";
    const role = roleFilter ? roleFilter.value : "";
    const status = statusFilter ? statusFilter.value : "";

    fetch("user-admin?ajax=search&keyword=" + encodeURIComponent(keyword)
        + "&role=" + encodeURIComponent(role)
        + "&status=" + encodeURIComponent(status)
        + "&page=" + page)
        .then(response => response.json())
        .then(data => {
            renderUserTable(data.users);
            renderPagination(data.currentPage, data.totalPages);
        });
}

function renderUserTable(users) {
    userTableBody.innerHTML = "";

    if (!users || users.length === 0) {
        userTableBody.innerHTML = `
            <tr>
                <td colspan="7" class="empty-row">
                    Không tìm thấy người dùng phù hợp
                </td>
            </tr>
        `;
        return;
    }

    users.forEach(u => {
        const statusHtml = u.status === "ACTIVE"
            ? `<span class="status active">Hoạt động</span>`
            : `<span class="status blocked">Bị khóa</span>`;

        const editBtn = window.userPermissions && window.userPermissions.canUpdateUser
            ? `
                <a href="user-admin?mode=edit&id=${u.id}"
                   class="icon-btn edit"
                   title="Chỉnh sửa">
                    <i class="fa fa-pen"></i>
                </a>
            `
            : "";

        const lockBtn = window.userPermissions
        && window.userPermissions.canLockUser
        && u.status === "ACTIVE"
            ? `
                <button type="button"
                        class="icon-btn delete"
                        title="Khóa người dùng"
                        onclick="openConfirmModal(${u.id})">
                    <i class="fa fa-trash"></i>
                </button>
            `
            : "";

        const row = `
            <tr>
                <td>${u.id}</td>
                <td>${escapeHtml(u.username || "")}</td>
                <td>${escapeHtml(u.fullName || "")}</td>
                <td>${escapeHtml(u.email || "")}</td>
                <td>${getRoleText(u.roleName)}</td>
                <td>${statusHtml}</td>
                <td class="actions">
                    <a href="user-admin?mode=view&id=${u.id}"
                       class="icon-btn view"
                       title="Xem chi tiết">
                        <i class="fa fa-eye"></i>
                    </a>

                    ${editBtn}
                    ${lockBtn}
                </td>
            </tr>
        `;

        userTableBody.insertAdjacentHTML("beforeend", row);
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
            <button type="button" class="page-btn" onclick="searchUser(${currentPage - 1})">
                Trước
            </button>
        `);
    }

    for (let i = startPage; i <= endPage; i++) {
        pagination.insertAdjacentHTML("beforeend", `
            <button type="button"
                    class="page-btn ${i === currentPage ? "active" : ""}"
                    onclick="searchUser(${i})">
                ${i}
            </button>
        `);
    }

    if (currentPage < totalPages) {
        pagination.insertAdjacentHTML("beforeend", `
            <button type="button" class="page-btn" onclick="searchUser(${currentPage + 1})">
                Sau
            </button>
        `);
    }
}

function getRoleText(roleName) {
    switch (roleName) {
        case "ADMIN":
            return "Quản trị";
        case "CUSTOMER":
            return "Khách hàng";
        case "STAFF_ORDER":
            return "Nhân viên quản lý đơn hàng";
        case "STAFF_PRODUCT":
            return "Nhân viên quản lý sản phẩm";
        case "STAFF_MARKETING":
            return "Nhân viên marketing";
        default:
            return escapeHtml(roleName || "Khách hàng");
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