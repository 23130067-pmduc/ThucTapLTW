<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý kho hàng</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inventory-admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="inventory-page">
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

    <section class="content inventory-content">
        <header class="topbar">
            <h1>Quản lý kho hàng</h1>
            <div class="topbar-actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>

        <div class="inventory-cards">
            <div class="inventory-card">Tổng biến thể <span>${totalVariants}</span></div>
            <div class="inventory-card">Tổng tồn kho <span>${totalStock}</span></div>
            <div class="inventory-card warning-card">Sắp hết hàng <span>${lowStockCount}</span></div>
            <div class="inventory-card danger-card">Hết hàng <span>${outOfStockCount}</span></div>
        </div>

        <div class="inventory-toolbar">
            <form action="${pageContext.request.contextPath}/inventory-admin" method="get" class="inventory-search-form">
                <input type="text" name="keyword" class="inventory-search-input"
                       placeholder="Tìm tên sản phẩm, danh mục..."
                       value="${keyword}">

                <select name="stockStatus" class="inventory-filter-select">
                    <option value="" ${stockStatus == '' ? 'selected' : ''}>Tất cả tồn kho</option>
                    <option value="LOW" ${stockStatus == 'LOW' ? 'selected' : ''}>Sắp hết hàng</option>
                    <option value="OUT" ${stockStatus == 'OUT' ? 'selected' : ''}>Hết hàng</option>
                    <option value="AVAILABLE" ${stockStatus == 'AVAILABLE' ? 'selected' : ''}>Còn nhiều hàng</option>
                </select>

                <button type="submit" class="btn-search">
                    <i class="fa-solid fa-magnifying-glass"></i> Tìm
                </button>

                <a href="${pageContext.request.contextPath}/inventory-admin" class="btn-reset">Làm mới</a>
            </form>
        </div>

        <div class="inventory-result-info">
            Hiển thị ${inventoryItems.size()} / ${totalItems} biến thể trong kho
        </div>

        <div class="inventory-table-wrapper">
            <table class="inventory-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Sản phẩm</th>
                    <th>Danh mục</th>
                    <th>Màu</th>
                    <th>Size</th>
                    <th>Giá</th>
                    <th>Tồn kho</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty inventoryItems}">
                    <tr>
                        <td colspan="9" class="inventory-empty">Không có dữ liệu kho hàng</td>
                    </tr>
                </c:if>

                <c:forEach items="${inventoryItems}" var="item">
                    <tr class="${item.stock == 0 ? 'out-stock-row' : (item.stock <= 10 ? 'low-stock-row' : '')}">
                        <td>#${item.variantId}</td>
                        <td>
                            <div class="inventory-product-info">
                                <img src="${pageContext.request.contextPath}/${item.thumbnail}" alt="${item.productName}">
                                <div>
                                    <strong>${item.productName}</strong>
                                    <small>Mã SP: #${item.productId}</small>
                                </div>
                            </div>
                        </td>
                        <td>${item.categoryName}</td>
                        <td>${item.colorName}</td>
                        <td>${item.sizeName}</td>
                        <td>
                            <c:choose>
                                <c:when test="${item.salePrice > 0 && item.salePrice < item.price}">
                                    <fmt:formatNumber value="${item.salePrice}" pattern="#,##0" /> đ
                                </c:when>
                                <c:otherwise>
                                    <fmt:formatNumber value="${item.price}" pattern="#,##0" /> đ
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <span class="stock-number ${item.stock == 0 ? 'stock-out' : (item.stock <= 10 ? 'stock-low' : 'stock-ok')}">${item.stock}</span>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${item.stock == 0}">
                                    <span class="inventory-status out">Hết hàng</span>
                                </c:when>
                                <c:when test="${item.stock <= 10}">
                                    <span class="inventory-status low">Sắp hết hàng</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="inventory-status ok">Còn hàng</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="admin-actions">
                                <a href="${pageContext.request.contextPath}/product-variant-admin?productId=${item.productId}"
                                   class="icon-btn view" title="Xem biến thể">
                                    <i class="fa-solid fa-eye"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/product-variant-admin?mode=edit&productId=${item.productId}&id=${item.variantId}"
                                   class="icon-btn edit" title="Cập nhật tồn kho">
                                    <i class="fa-solid fa-pen"></i>
                                </a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <c:url var="prevUrl" value="/inventory-admin">
                            <c:param name="page" value="${currentPage - 1}" />
                            <c:param name="keyword" value="${keyword}" />
                            <c:param name="stockStatus" value="${stockStatus}" />
                        </c:url>
                        <a href="${prevUrl}" class="page-link">Trước</a>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                        <c:choose>
                            <c:when test="${pageNumber == currentPage}">
                                <span class="page-link active">${pageNumber}</span>
                            </c:when>
                            <c:otherwise>
                                <c:url var="pageUrl" value="/inventory-admin">
                                    <c:param name="page" value="${pageNumber}" />
                                    <c:param name="keyword" value="${keyword}" />
                                    <c:param name="stockStatus" value="${stockStatus}" />
                                </c:url>
                                <a href="${pageUrl}" class="page-link">${pageNumber}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <c:url var="nextUrl" value="/inventory-admin">
                            <c:param name="page" value="${currentPage + 1}" />
                            <c:param name="keyword" value="${keyword}" />
                            <c:param name="stockStatus" value="${stockStatus}" />
                        </c:url>
                        <a href="${nextUrl}" class="page-link">Sau</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </section>
</div>
</body>
</html>
