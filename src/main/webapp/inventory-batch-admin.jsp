<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý lô nhập hàng</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inventory-batch-admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="inventory-batch-page">
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

                    <c:if test="${userlogin.permissions.contains('PERMISSION_VIEW')}">
                        <a href="${pageContext.request.contextPath}/permission-admin" class="nav-item">
                            <i class="fa-solid fa-user-shield"></i><span>Phân quyền</span>
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

                    <c:if test="${userlogin.permissions.contains('PROMOTION_EVENT_VIEW')}">
                        <a href="${pageContext.request.contextPath}/promotion-event-admin" class="nav-item">
                            <i class="fa-solid fa-tags"></i>
                            <span>Khuyến mãi</span>
                        </a>
                    </c:if>

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

    <section class="content batch-content">
        <header class="topbar">
            <div class="topbar-left">
                <a href="${pageContext.request.contextPath}/inventory-admin" class="back-btn" title="Quay lại quản lý kho">
                    <i class="fa-solid fa-arrow-left"></i>
                </a>
                <h1>Quản lý lô nhập hàng</h1>
            </div>
            <div class="topbar-actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>

        <div class="batch-cards">
            <div class="batch-card">Tổng lô nhập <span>${totalBatches}</span></div>
            <div class="batch-card active-card">Lô còn hàng <span>${remainingBatches}</span></div>
            <div class="batch-card">SL còn theo lô <span>${remainingQuantity}</span></div>
            <div class="batch-card value-card">Giá trị tồn theo giá nhập <span><fmt:formatNumber value="${remainingValue}" pattern="#,#00" /> đ</span></div>
        </div>

        <div class="batch-toolbar">
            <form action="${pageContext.request.contextPath}/inventory-batch-admin" method="get" class="batch-search-form">
                <input type="text" name="keyword" class="batch-search-input"
                       placeholder="Tìm mã lô, mã phiếu, ID biến thể, tên sản phẩm..."
                       value="${keyword}">

                <select name="batchStatus" class="batch-filter-select">
                    <option value="" ${batchStatus == '' ? 'selected' : ''}>Tất cả trạng thái lô</option>
                    <option value="AVAILABLE" ${batchStatus == 'AVAILABLE' ? 'selected' : ''}>Còn nguyên lô</option>
                    <option value="PARTIAL" ${batchStatus == 'PARTIAL' ? 'selected' : ''}>Đã xuất một phần</option>
                    <option value="EMPTY" ${batchStatus == 'EMPTY' ? 'selected' : ''}>Đã hết lô</option>
                </select>

                <button type="submit" class="btn-search">
                    <i class="fa-solid fa-magnifying-glass"></i> Tìm
                </button>

                <a href="${pageContext.request.contextPath}/inventory-batch-admin" class="btn-reset">Làm mới</a>
            </form>

            <div class="batch-toolbar-actions">
                <a href="${pageContext.request.contextPath}/inventory-transaction-form?type=IMPORT" class="btn-import">
                    <i class="fa-solid fa-circle-plus"></i> Tạo phiếu nhập
                </a>
                <a href="${pageContext.request.contextPath}/inventory-history-admin?type=IMPORT" class="btn-history">
                    <i class="fa-solid fa-clock-rotate-left"></i> Phiếu nhập
                </a>
            </div>
        </div>

        <div class="batch-result-info">
            Hiển thị ${batches.size()} / ${totalItems} lô nhập hàng
        </div>

        <div class="batch-table-wrapper">
            <table class="batch-table">
                <thead>
                <tr>
                    <th>Mã lô</th>
                    <th>Sản phẩm</th>
                    <th>Phân loại</th>
                    <th>Mã phiếu</th>
                    <th>SL nhập</th>
                    <th>SL còn</th>
                    <th>Giá nhập</th>
                    <th>Giá trị còn</th>
                    <th>Trạng thái</th>
                    <th>Ngày nhập</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty batches}">
                    <tr>
                        <td colspan="10" class="batch-empty">Chưa có lô nhập hàng nào.</td>
                    </tr>
                </c:if>

                <c:forEach items="${batches}" var="batch">
                    <tr class="${batch.remainingQuantity == 0 ? 'empty-batch-row' : (batch.remainingQuantity < batch.importQuantity ? 'partial-batch-row' : '')}">
                        <td>
                            <a href="${pageContext.request.contextPath}/inventory-batch-detail?id=${batch.id}" class="batch-code-link" title="Xem chi tiết lô">
                                ${batch.batchCode}
                            </a>
                        </td>
                        <td>
                            <div class="batch-product-info">
                                <c:set var="thumbUrl" value="${batch.thumbnail}" />
                                <c:choose>
                                    <c:when test="${empty thumbUrl}">
                                        <img src="${pageContext.request.contextPath}/img/gau.png" alt="${batch.productName}">
                                    </c:when>
                                    <c:when test="${fn:startsWith(thumbUrl, 'http://') || fn:startsWith(thumbUrl, 'https://')}">
                                        <img src="${thumbUrl}" alt="${batch.productName}">
                                    </c:when>
                                    <c:when test="${fn:startsWith(thumbUrl, '/')}">
                                        <img src="${pageContext.request.contextPath}${thumbUrl}" alt="${batch.productName}">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/${thumbUrl}" alt="${batch.productName}">
                                    </c:otherwise>
                                </c:choose>
                                <div>
                                    <strong>${batch.productName}</strong>
                                    <small>Mã SP: #${batch.productId} - Biến thể: #${batch.productVariantId}</small>
                                </div>
                            </div>
                        </td>
                        <td>${batch.colorName} / ${batch.sizeName}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/inventory-history-detail?id=${batch.transactionId}" class="transaction-link">
                                ${batch.transactionCode}
                            </a>
                        </td>
                        <td>${batch.importQuantity}</td>
                        <td><span class="quantity-badge">${batch.remainingQuantity}</span></td>
                        <td><fmt:formatNumber value="${batch.unitCost}" pattern="#,#00" /> đ</td>
                        <td><fmt:formatNumber value="${batch.remainingValue}" pattern="#,#00" /> đ</td>
                        <td>
                            <span class="batch-status ${batch.remainingQuantity == 0 ? 'status-empty' : (batch.remainingQuantity < batch.importQuantity ? 'status-partial' : 'status-available')}">
                                ${batch.batchStatusText}
                            </span>
                        </td>
                        <td>${batch.createdAtText}</td>
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
                        <c:url var="prevUrl" value="/inventory-batch-admin">
                            <c:param name="page" value="${currentPage - 1}" />
                            <c:param name="keyword" value="${keyword}" />
                            <c:param name="batchStatus" value="${batchStatus}" />
                        </c:url>
                        <a class="page-btn" href="${prevUrl}">Trước</a>
                    </c:if>

                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <c:url var="pageUrl" value="/inventory-batch-admin">
                            <c:param name="page" value="${i}" />
                            <c:param name="keyword" value="${keyword}" />
                            <c:param name="batchStatus" value="${batchStatus}" />
                        </c:url>
                        <a class="page-btn ${i == currentPage ? 'active' : ''}" href="${pageUrl}">${i}</a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <c:url var="nextUrl" value="/inventory-batch-admin">
                            <c:param name="page" value="${currentPage + 1}" />
                            <c:param name="keyword" value="${keyword}" />
                            <c:param name="batchStatus" value="${batchStatus}" />
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
