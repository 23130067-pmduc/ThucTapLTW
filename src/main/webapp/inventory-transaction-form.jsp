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
            <a href="${pageContext.request.contextPath}/profit-report" class="nav-item"><i class="fa-solid fa-chart-line"></i><span>Lợi nhuận</span></a>
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

        <c:if test="${not empty stockErrors}">
            <div class="alert-error">
                <strong>Không thể tạo phiếu xuất kho:</strong>
                <ul>
                    <c:forEach items="${stockErrors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <c:if test="${not empty costErrors}">
            <div class="alert-error">
                <strong>Không thể tạo phiếu nhập kho:</strong>
                <ul>
                    <c:forEach items="${costErrors}" var="error">
                        <li>${error}</li>
                    </c:forEach>
                </ul>
            </div>
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
                        <input type="text" name="supplierName" placeholder="Nhập tên nhà cung cấp" value="${supplierName}">
                    </div>

                    <div class="form-group full-width">
                        <label>Ghi chú</label>
                        <textarea name="note" rows="3" placeholder="Nhập ghi chú cho phiếu nếu có">${note}</textarea>
                    </div>
                </div>
            </section>

            <section class="form-card">
                <div class="section-title">
                    <div>
                        <h2>Chọn sản phẩm</h2>
                        <p>
                            <c:choose>
                                <c:when test="${type == 'IMPORT'}">Chọn biến thể, nhập số lượng và giá nhập để tạo lô hàng.</c:when>
                                <c:otherwise>Chọn biến thể sản phẩm, nhập số lượng rồi thêm vào phiếu.</c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </div>

                <div class="variant-picker">
                    <input type="text"
                           id="variantSearchInput"
                           class="variant-search-input"
                           placeholder="Tìm nhanh theo ID biến thể, tên sản phẩm, màu hoặc size...">
                </div>

                <div class="add-item-row ${type == 'IMPORT' ? 'has-cost' : ''}">
                    <select id="variantSelect" class="variant-select">
                        <option value="">-- Chọn sản phẩm / màu / size --</option>
                        <c:forEach items="${inventoryItems}" var="item">
                            <option value="${item.variantId}"
                                    data-name="${item.productName}"
                                    data-color="${item.colorName}"
                                    data-size="${item.sizeName}"
                                    data-category="${item.categoryName}"
                                    data-stock="${item.stock}">
                                #${item.variantId} - ${item.productName} - ${item.colorName} - ${item.sizeName} - Tồn: ${item.stock}
                            </option>
                        </c:forEach>
                    </select>

                    <input type="number" id="quantityInput" class="quantity-input" min="1" value="1" placeholder="Số lượng">

                    <c:if test="${type == 'IMPORT'}">
                        <input type="number" id="unitCostInput" class="cost-price-input" min="1" step="1000" placeholder="Giá nhập">
                    </c:if>

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
                            <c:if test="${type == 'IMPORT'}">
                                <th>Giá nhập</th>
                            </c:if>
                            <th>Hành động</th>
                        </tr>
                        </thead>
                        <tbody id="selectedItemsBody">
                        <tr id="emptyRow">
                            <td colspan="${type == 'IMPORT' ? 8 : 7}" class="empty-state">Chưa có sản phẩm nào trong phiếu.</td>
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

<div id="notifyBox" class="notify-box">
    <div class="notify-content">
        <i class="fa-solid fa-circle-exclamation"></i>
        <span id="notifyMessage"></span>
    </div>
</div>

<script>
    window.transactionType = '${type}';
</script>
<script src="${pageContext.request.contextPath}/javaScript/inventory-transaction-form.js"></script>
</body>
</html>
