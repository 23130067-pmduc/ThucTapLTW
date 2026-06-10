<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
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
            <div class="nav">
                <div class="nav" id="menu">

                    <c:if test="${userlogin.permissions.contains('DASHBOARD_VIEW')}">
                        <a href="${pageContext.request.contextPath}/dashboard" class="nav-item ">
                            <i class="fa-solid fa-gauge"></i><span>Thống kê</span>
                        </a>
                    </c:if>

                    <c:if test="${userlogin.permissions.contains('PRODUCT_VIEW')}">
                        <a href="${pageContext.request.contextPath}/product-admin" class="nav-item">
                            <i class="fa-solid fa-shirt"></i><span>Sản phẩm</span>
                        </a>
                    </c:if>

                    <c:if test="${userlogin.permissions.contains('WAREHOUSE_VIEW')}">
                        <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item active">
                            <i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span>
                        </a>
                    </c:if>

                    <c:if test="${userlogin.permissions.contains('RETURN_RECEIPT_VIEW')}">
                        <a href="${pageContext.request.contextPath}/return-order-admin" class="nav-item">
                            <i class="fa-solid fa-rotate-left"></i><span>Hoàn hàng</span>
                        </a>
                    </c:if>

                    <c:if test="${userlogin.permissions.contains('REPORT_VIEW')}">
                        <a href="${pageContext.request.contextPath}/profit-report" class="nav-item">
                            <i class="fa-solid fa-chart-line"></i><span>Lợi nhuận</span>
                        </a>
                    </c:if>

                    <c:if test="${userlogin.permissions.contains('CATEGORY_VIEW')}">
                        <a href="${pageContext.request.contextPath}/category-admin" class="nav-item">
                            <i class="fa-solid fa-list"></i><span>Danh mục</span>
                        </a>
                    </c:if>

                    <c:if test="${userlogin.permissions.contains('ORDER_VIEW')}">
                        <a href="${pageContext.request.contextPath}/order-admin" class="nav-item">
                            <i class="fa-solid fa-receipt"></i><span>Đơn hàng</span>
                        </a>
                    </c:if>

                    <c:if test="${userlogin.permissions.contains('USER_VIEW')}">
                        <a href="${pageContext.request.contextPath}/user-admin" class="nav-item">
                            <i class="fa-solid fa-users"></i><span>Người dùng</span>
                        </a>
                    </c:if>

                    <c:if test="${userlogin.permissions.contains('BANNER_VIEW')}">
                        <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item">
                            <i class="fa-solid fa-image"></i><span>Banner</span>
                        </a>
                    </c:if>

                    <c:if test="${userlogin.permissions.contains('VOUCHER_VIEW')}">
                        <a href="${pageContext.request.contextPath}/voucher-admin" class="nav-item">
                            <i class="fa-solid fa-ticket"></i><span>Mã giảm giá</span>
                        </a>
                    </c:if>

                    <a href="${pageContext.request.contextPath}/promotion-event-admin" class="nav-item">
                        <i class="fa-solid fa-tags"></i><span>Khuyến mãi</span>
                    </a>

                    <c:if test="${userlogin.permissions.contains('NEWS_VIEW')}">
                        <a href="${pageContext.request.contextPath}/news-admin" class="nav-item">
                            <i class="fa-solid fa-newspaper"></i><span>Tin tức</span>
                        </a>
                    </c:if>

                    <c:if test="${userlogin.permissions.contains('NOTIFICATION_VIEW')}">
                        <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item">
                            <i class="fa-solid fa-bell"></i><span>Thông báo</span>
                        </a>
                    </c:if>

                    <c:if test="${userlogin.permissions.contains('CONTACT_VIEW')}">
                        <a href="${pageContext.request.contextPath}/contact-admin" class="nav-item">
                            <i class="fa-solid fa-envelope"></i><span>Liên hệ</span>
                        </a>
                    </c:if>

                    <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item">
                        <i class="fa-solid fa-user-gear"></i><span>Hồ sơ</span>
                    </a>

                </div>
            </div>
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
                       placeholder="Tìm ID biến thể, ID sản phẩm, tên, danh mục..."
                       value="${keyword}">

                <select name="stockStatus" class="inventory-filter-select">
                    <option value="" ${stockStatus == '' ? 'selected' : ''}>Tất cả tồn kho</option>
                    <option value="LOW" ${stockStatus == 'LOW' ? 'selected' : ''}>Sắp hết hàng</option>
                    <option value="OUT" ${stockStatus == 'OUT' ? 'selected' : ''}>Hết hàng</option>
                    <option value="AVAILABLE" ${stockStatus == 'AVAILABLE' ? 'selected' : ''}>Còn nhiều hàng</option>
                </select>


                <input type="hidden" name="sortField" value="${sortField}">
                <input type="hidden" name="sortDir" value="${sortDir}">

                <button type="submit" class="btn-search">
                    <i class="fa-solid fa-magnifying-glass"></i> Tìm
                </button>

                <a href="${pageContext.request.contextPath}/inventory-admin" class="btn-reset">Làm mới</a>
            </form>

            <div class="inventory-toolbar-actions">
                <c:if test="${userlogin.permissions.contains('IMPORT_RECEIPT_CREATE')}">
                    <a href="${pageContext.request.contextPath}/inventory-transaction-form?type=IMPORT"
                       class="btn-import">
                        <i class="fa-solid fa-circle-plus"></i> Nhập kho
                    </a>
                </c:if>

                <c:if test="${userlogin.permissions.contains('EXPORT_RECEIPT_CREATE')}">
                    <a href="${pageContext.request.contextPath}/inventory-transaction-form?type=EXPORT"
                       class="btn-export">
                        <i class="fa-solid fa-circle-minus"></i> Xuất kho
                    </a>
                </c:if>

                <a href="${pageContext.request.contextPath}/inventory-batch-admin" class="btn-batch">
                    <i class="fa-solid fa-layer-group"></i> Lô nhập hàng
                </a>

                <a href="${pageContext.request.contextPath}/inventory-history-admin" class="btn-history">
                    <i class="fa-solid fa-clock-rotate-left"></i> Lịch sử nhập xuất
                </a>
            </div>
        </div>

        <div class="inventory-result-info">
            Hiển thị ${inventoryItems.size()} / ${totalItems} biến thể trong kho
        </div>

        <div class="inventory-table-wrapper">
            <table class="inventory-table">
                <thead>
                <tr>
                    <th>
                        <c:choose>
                            <c:when test="${sortField == 'id' && sortDir == 'asc'}">
                                <c:url var="idSortUrl" value="/inventory-admin">
                                    <c:param name="page" value="1" />
                                    <c:param name="keyword" value="${keyword}" />
                                    <c:param name="stockStatus" value="${stockStatus}" />
                                    <c:param name="sortField" value="id" />
                                    <c:param name="sortDir" value="desc" />
                                </c:url>
                            </c:when>
                            <c:when test="${sortField == 'id' && sortDir == 'desc'}">
                                <c:url var="idSortUrl" value="/inventory-admin">
                                    <c:param name="page" value="1" />
                                    <c:param name="keyword" value="${keyword}" />
                                    <c:param name="stockStatus" value="${stockStatus}" />
                                </c:url>
                            </c:when>
                            <c:otherwise>
                                <c:url var="idSortUrl" value="/inventory-admin">
                                    <c:param name="page" value="1" />
                                    <c:param name="keyword" value="${keyword}" />
                                    <c:param name="stockStatus" value="${stockStatus}" />
                                    <c:param name="sortField" value="id" />
                                    <c:param name="sortDir" value="asc" />
                                </c:url>
                            </c:otherwise>
                        </c:choose>

                        <a href="${idSortUrl}" class="sort-header">
                            <span>ID</span>
                            <i class="fa-solid ${sortField == 'id' ? (sortDir == 'asc' ? 'fa-arrow-up-short-wide' : 'fa-arrow-down-wide-short') : 'fa-sort'}"></i>
                        </a>
                    </th>
                    <th>
                        <c:choose>
                            <c:when test="${sortField == 'productName' && sortDir == 'asc'}">
                                <c:url var="nameSortUrl" value="/inventory-admin">
                                    <c:param name="page" value="1" />
                                    <c:param name="keyword" value="${keyword}" />
                                    <c:param name="stockStatus" value="${stockStatus}" />
                                    <c:param name="sortField" value="productName" />
                                    <c:param name="sortDir" value="desc" />
                                </c:url>
                            </c:when>
                            <c:when test="${sortField == 'productName' && sortDir == 'desc'}">
                                <c:url var="nameSortUrl" value="/inventory-admin">
                                    <c:param name="page" value="1" />
                                    <c:param name="keyword" value="${keyword}" />
                                    <c:param name="stockStatus" value="${stockStatus}" />
                                </c:url>
                            </c:when>
                            <c:otherwise>
                                <c:url var="nameSortUrl" value="/inventory-admin">
                                    <c:param name="page" value="1" />
                                    <c:param name="keyword" value="${keyword}" />
                                    <c:param name="stockStatus" value="${stockStatus}" />
                                    <c:param name="sortField" value="productName" />
                                    <c:param name="sortDir" value="asc" />
                                </c:url>
                            </c:otherwise>
                        </c:choose>

                        <a href="${nameSortUrl}" class="sort-header">
                            <span>Sản phẩm</span>
                            <i class="fa-solid ${sortField == 'productName' ? (sortDir == 'asc' ? 'fa-arrow-up-a-z' : 'fa-arrow-down-z-a') : 'fa-sort'}"></i>
                        </a>
                    </th>
                    <th>Danh mục</th>
                    <th>Màu</th>
                    <th>Size</th>
                    <th>Giá bán</th>
                    <th>Giá nhập gần nhất</th>
                    <th>Tồn kho</th>
                    <th>Tồn theo lô</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty inventoryItems}">
                    <tr>
                        <td colspan="11" class="inventory-empty">Không có dữ liệu kho hàng</td>
                    </tr>
                </c:if>

                <c:forEach items="${inventoryItems}" var="item">
                    <tr class="${item.stock == 0 ? 'out-stock-row' : (item.stock <= 10 ? 'low-stock-row' : '')}">
                        <td>#${item.variantId}</td>
                        <td>
                            <div class="inventory-product-info">
                                <c:set var="thumbUrl" value="${item.thumbnail}" />
                                <c:choose>
                                    <c:when test="${empty thumbUrl}">
                                        <img src="${pageContext.request.contextPath}/img/gau.png" alt="${item.productName}">
                                    </c:when>
                                    <c:when test="${fn:startsWith(thumbUrl, 'http://') || fn:startsWith(thumbUrl, 'https://')}">
                                        <img src="${thumbUrl}" alt="${item.productName}">
                                    </c:when>
                                    <c:when test="${fn:startsWith(thumbUrl, '/')}">
                                        <img src="${pageContext.request.contextPath}${thumbUrl}" alt="${item.productName}">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/${thumbUrl}" alt="${item.productName}">
                                    </c:otherwise>
                                </c:choose>
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
                        <td class="latest-cost-cell">
                            <c:choose>
                                <c:when test="${empty item.latestUnitCost}">-</c:when>
                                <c:otherwise>
                                    <strong><fmt:formatNumber value="${item.latestUnitCost}" pattern="#,#00" /> đ</strong>
                                    <small>
                                        <c:if test="${not empty item.latestImportDateText}">${item.latestImportDateText}</c:if>
                                        <c:if test="${not empty item.latestBatchCode}"> - ${item.latestBatchCode}</c:if>
                                    </small>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <span class="stock-number ${item.stock == 0 ? 'stock-out' : (item.stock <= 10 ? 'stock-low' : 'stock-ok')}">${item.stock}</span>
                        </td>
                        <td>
                            <span class="batch-stock-number ${item.remainingBatchQuantity == item.stock ? 'batch-stock-ok' : 'batch-stock-warning'}">${item.remainingBatchQuantity}</span>
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
                                <c:if test="${userlogin.permissions.contains('STOCK_UPDATE')}">
                                    <a href="${pageContext.request.contextPath}/product-variant-admin?mode=edit&productId=${item.productId}&id=${item.variantId}"
                                       class="icon-btn edit" title="Cập nhật tồn kho">
                                        <i class="fa-solid fa-pen"></i>
                                    </a>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <c:if test="${totalPages > 1}">
                <c:set var="startPage" value="${currentPage - 2}" />
                <c:set var="endPage" value="${currentPage + 2}" />

                <c:if test="${startPage < 1}">
                    <c:set var="startPage" value="1" />
                    <c:set var="endPage" value="5" />
                </c:if>

                <c:if test="${endPage > totalPages}">
                    <c:set var="endPage" value="${totalPages}" />
                    <c:set var="startPage" value="${totalPages - 4}" />
                </c:if>

                <c:if test="${startPage < 1}">
                    <c:set var="startPage" value="1" />
                </c:if>

                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <c:url var="prevUrl" value="/inventory-admin">
                            <c:param name="page" value="${currentPage - 1}" />
                            <c:param name="keyword" value="${keyword}" />
                            <c:param name="stockStatus" value="${stockStatus}" />
                            <c:param name="sortField" value="${sortField}" />
                            <c:param name="sortDir" value="${sortDir}" />
                        </c:url>
                        <a class="page-btn" href="${prevUrl}">Trước</a>
                    </c:if>

                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <c:url var="pageUrl" value="/inventory-admin">
                            <c:param name="page" value="${i}" />
                            <c:param name="keyword" value="${keyword}" />
                            <c:param name="stockStatus" value="${stockStatus}" />
                            <c:param name="sortField" value="${sortField}" />
                            <c:param name="sortDir" value="${sortDir}" />
                        </c:url>

                        <a class="page-btn ${i == currentPage ? 'active' : ''}" href="${pageUrl}">
                                ${i}
                        </a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <c:url var="nextUrl" value="/inventory-admin">
                            <c:param name="page" value="${currentPage + 1}" />
                            <c:param name="keyword" value="${keyword}" />
                            <c:param name="stockStatus" value="${stockStatus}" />
                            <c:param name="sortField" value="${sortField}" />
                            <c:param name="sortDir" value="${sortDir}" />
                        </c:url>
                        <a class="page-btn" href="${nextUrl}">Sau</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </section>
</div>
</body>
</html>
