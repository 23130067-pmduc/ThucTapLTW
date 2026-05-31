<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${typeText}</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inventory-transaction-form.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="inventory-transaction-page">
<div class="user">
    <aside class="sidebar">
        <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
        <p>ADMIN</p>

        <div class="nav">
            <a href="${pageContext.request.contextPath}/dashboard" class="nav-item"><i class="fa-solid fa-gauge"></i><span>Dashboard</span></a>
            <a href="${pageContext.request.contextPath}/product-admin" class="nav-item"><i class="fa-solid fa-shirt"></i><span>Sản phẩm</span></a>
            <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item active"><i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span></a>
            <a href="${pageContext.request.contextPath}/category-admin" class="nav-item"><i class="fa-solid fa-list"></i><span>Danh mục</span></a>
            <a href="${pageContext.request.contextPath}/order-admin" class="nav-item"><i class="fa-solid fa-receipt"></i><span>Đơn hàng</span></a>
            <a href="${pageContext.request.contextPath}/user-admin" class="nav-item"><i class="fa-solid fa-users"></i><span>Người dùng</span></a>
            <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item"><i class="fa-solid fa-image"></i><span>Banner</span></a>
            <a href="${pageContext.request.contextPath}/news-admin" class="nav-item"><i class="fa-solid fa-newspaper"></i><span>Tin tức</span></a>
            <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item"><i class="fa-solid fa-bell"></i><span>Thông báo</span></a>
            <a href="${pageContext.request.contextPath}/contact-admin" class="nav-item"><i class="fa-solid fa-envelope"></i><span>Liên hệ</span></a>
            <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item"><i class="fa-solid fa-user-gear"></i><span>Hồ sơ</span></a>
        </div>
    </aside>

    <section class="content transaction-content">
        <header class="topbar">
            <div class="topbar-left">
                <a href="${pageContext.request.contextPath}/inventory-admin" class="back-btn" title="Quay lại quản lý kho">
                    <i class="fa-solid fa-arrow-left"></i>
                </a>
                <h1>${typeText}</h1>
            </div>
            <div class="topbar-actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>

        <c:if test="${param.error == 'empty_items'}">
            <div class="alert-error">Vui lòng thêm ít nhất một sản phẩm vào phiếu.</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/inventory-transaction-form" method="post" id="transactionForm">
            <input type="hidden" name="type" value="${type}">

            <section class="form-card">
                <div class="form-grid">
                    <div class="form-group">
                        <label>Loại phiếu</label>
                        <input type="text" value="${typeText}" readonly>
                    </div>

                    <div class="form-group supplier-group ${type == 'EXPORT' ? 'is-hidden' : ''}">
                        <label>Nhà cung cấp</label>
                        <input type="text" name="supplierName" placeholder="Nhập tên nhà cung cấp">
                    </div>

                    <div class="form-group full-width">
                        <label>Ghi chú</label>
                        <textarea name="note" rows="3" placeholder="Nhập ghi chú cho phiếu nếu có"></textarea>
                    </div>
                </div>
            </section>

            <section class="form-card">
                <div class="section-title">
                    <div>
                        <h2>Chọn sản phẩm</h2>
                        <p>Chọn biến thể sản phẩm, nhập số lượng rồi thêm vào phiếu.</p>
                    </div>
                </div>

                <div class="add-item-row">
                    <select id="variantSelect" class="variant-select">
                        <option value="">-- Chọn sản phẩm / màu / size --</option>
                        <c:forEach items="${inventoryItems}" var="item">
                            <option value="${item.variantId}"
                                    data-name="${item.productName}"
                                    data-color="${item.colorName}"
                                    data-size="${item.sizeName}"
                                    data-stock="${item.stock}">
                                #${item.variantId} - ${item.productName} - ${item.colorName} - ${item.sizeName} - Tồn: ${item.stock}
                            </option>
                        </c:forEach>
                    </select>

                    <input type="number" id="quantityInput" class="quantity-input" min="1" value="1" placeholder="Số lượng">

                    <button type="button" class="btn-add-item" id="addItemBtn">
                        <i class="fa-solid fa-plus"></i> Thêm vào phiếu
                    </button>
                </div>

                <div class="transaction-table-wrapper">
                    <table class="transaction-table">
                        <thead>
                        <tr>
                            <th>ID biến thể</th>
                            <th>Sản phẩm</th>
                            <th>Màu</th>
                            <th>Size</th>
                            <th>Tồn hiện tại</th>
                            <th>Số lượng</th>
                            <th>Hành động</th>
                        </tr>
                        </thead>
                        <tbody id="selectedItemsBody">
                        <tr id="emptyRow">
                            <td colspan="7" class="empty-state">Chưa có sản phẩm nào trong phiếu.</td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/inventory-admin" class="btn-cancel">Hủy</a>
                    <button type="submit" class="btn-save">
                        <i class="fa-solid fa-floppy-disk"></i> Lưu phiếu
                    </button>
                </div>
            </section>
        </form>
    </section>
</div>

<script>
    (() => {
        const variantSelect = document.getElementById('variantSelect');
        const quantityInput = document.getElementById('quantityInput');
        const addItemBtn = document.getElementById('addItemBtn');
        const selectedItemsBody = document.getElementById('selectedItemsBody');
        const emptyRow = document.getElementById('emptyRow');
        const transactionForm = document.getElementById('transactionForm');

        function showEmptyRowIfNeeded() {
            const hasItem = selectedItemsBody.querySelector('.selected-item-row') !== null;
            emptyRow.style.display = hasItem ? 'none' : '';
        }

        function addItem() {
            const option = variantSelect.options[variantSelect.selectedIndex];
            const variantId = variantSelect.value;
            const quantity = Number(quantityInput.value);

            if (!variantId) {
                alert('Vui lòng chọn sản phẩm cần nhập/xuất.');
                return;
            }

            if (!quantity || quantity <= 0) {
                alert('Số lượng phải lớn hơn 0.');
                return;
            }

            const oldRow = selectedItemsBody.querySelector('tr[data-variant-id="' + variantId + '"]');
            if (oldRow) {
                const quantityField = oldRow.querySelector('input[name="quantities"]');
                const quantityText = oldRow.querySelector('.quantity-text');
                const newQuantity = Number(quantityField.value) + quantity;
                quantityField.value = newQuantity;
                quantityText.textContent = newQuantity;
                variantSelect.value = '';
                quantityInput.value = 1;
                return;
            }

            const row = document.createElement('tr');
            row.className = 'selected-item-row';
            row.dataset.variantId = variantId;
            row.innerHTML =
                '<td>#' + variantId + '<input type="hidden" name="variantIds" value="' + variantId + '"></td>' +
                '<td>' + (option.dataset.name || '-') + '</td>' +
                '<td>' + (option.dataset.color || '-') + '</td>' +
                '<td>' + (option.dataset.size || '-') + '</td>' +
                '<td>' + (option.dataset.stock || '0') + '</td>' +
                '<td><span class="quantity-text">' + quantity + '</span><input type="hidden" name="quantities" value="' + quantity + '"></td>' +
                '<td><button type="button" class="btn-remove-item"><i class="fa-solid fa-trash"></i></button></td>';

            selectedItemsBody.appendChild(row);
            variantSelect.value = '';
            quantityInput.value = 1;
            showEmptyRowIfNeeded();
        }

        addItemBtn.addEventListener('click', addItem);

        selectedItemsBody.addEventListener('click', (event) => {
            const removeBtn = event.target.closest('.btn-remove-item');
            if (removeBtn) {
                removeBtn.closest('tr').remove();
                showEmptyRowIfNeeded();
            }
        });

        transactionForm.addEventListener('submit', (event) => {
            if (!selectedItemsBody.querySelector('.selected-item-row')) {
                event.preventDefault();
                alert('Vui lòng thêm ít nhất một sản phẩm vào phiếu.');
            }
        });
    })();
</script>
</body>
</html>
