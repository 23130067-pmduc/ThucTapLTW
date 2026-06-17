<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết lô nhập hàng</title>

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
                            <i class="fa-solid fa-gauge"></i><span>Tổng quan</span>
                        </a>
                    </c:if>

                    <c:if test="${userlogin.permissions.contains('REPORT_VIEW')}">
                        <a href="${pageContext.request.contextPath}/profit-report" class="nav-item">
                            <i class="fa-solid fa-chart-line"></i><span>Thống kê</span>
                        </a>
                    </c:if>


                    <c:if test="${userlogin.permissions.contains('PRODUCT_VIEW')}">
                        <a href="${pageContext.request.contextPath}/product-admin" class="nav-item">
                            <i class="fa-solid fa-shirt"></i><span>Sản phẩm</span>
                        </a>
                    </c:if>

                    <c:if test="${userlogin.permissions.contains('WAREHOUSE_VIEW')}">
                        <a href="${pageContext.request.contextPath}/supplier-admin" class="nav-item">
                            <i class="fa-solid fa-truck"></i><span>Nhà cung cấp</span>
                        </a>
                        <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item active">
                            <i class="fa-solid fa-boxes-stacked"></i><span>Tồn kho</span>
                        </a>
                    </c:if>

                    <c:if test="${userlogin.permissions.contains('RETURN_RECEIPT_VIEW')}">
                        <a href="${pageContext.request.contextPath}/return-order-admin" class="nav-item">
                            <i class="fa-solid fa-rotate-left"></i><span>Hoàn hàng</span>
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

    <section class="content batch-content">
        <header class="topbar">
            <div class="topbar-left">
                <a href="${pageContext.request.contextPath}/inventory-batch-admin" class="back-btn" title="Quay lại danh sách lô">
                    <i class="fa-solid fa-arrow-left"></i>
                </a>
                <h1>Chi tiết lô nhập hàng</h1>
            </div>
            <div class="topbar-actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>

        <div class="batch-detail-header">
            <div class="batch-detail-title">
                <span>Mã lô</span>
                <h2>${batch.batchCode}</h2>
                <span class="batch-status ${batch.remainingQuantity == 0 ? 'status-empty' : (batch.remainingQuantity < batch.importQuantity ? 'status-partial' : 'status-available')}">
                    ${batch.batchStatusText}
                </span>
            </div>
            <div class="batch-detail-actions">
                <a href="${pageContext.request.contextPath}/inventory-history-detail?id=${batch.transactionId}" class="btn-history">
                    <i class="fa-solid fa-file-lines"></i> Xem phiếu nhập
                </a>
                <a href="${pageContext.request.contextPath}/inventory-batch-admin" class="btn-reset">
                    <i class="fa-solid fa-list"></i> Danh sách lô
                </a>
            </div>
        </div>

        <div class="batch-detail-grid">
            <div class="batch-detail-card product-card-detail">
                <h3>Thông tin sản phẩm</h3>
                <div class="detail-product-box">
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
                        <p>Mã SP: #${batch.productId} · Biến thể: #${batch.productVariantId}</p>
                        <p>Danh mục: ${empty batch.categoryName ? '-' : batch.categoryName}</p>
                        <p>Phân loại: ${empty batch.colorName ? '-' : batch.colorName} / ${empty batch.sizeName ? '-' : batch.sizeName}</p>
                    </div>
                </div>
            </div>

            <div class="batch-detail-card">
                <h3>Số lượng & giá nhập</h3>
                <div class="detail-info-list">
                    <div><span>Số lượng nhập</span><strong>${batch.importQuantity}</strong></div>
                    <div><span>Số lượng còn</span><strong>${batch.remainingQuantity}</strong></div>
                    <div><span>Giá nhập</span><strong><fmt:formatNumber value="${batch.unitCost}" pattern="#,#00" /> đ</strong></div>
                    <div><span>Giá trị nhập</span><strong><fmt:formatNumber value="${batch.importValue}" pattern="#,#00" /> đ</strong></div>
                    <div><span>Giá trị còn</span><strong><fmt:formatNumber value="${batch.remainingValue}" pattern="#,#00" /> đ</strong></div>
                </div>
            </div>

            <div class="batch-detail-card">
                <h3>Phiếu nhập liên quan</h3>
                <div class="detail-info-list">
                    <div>
                        <span>Mã phiếu</span>
                        <strong>
                            <a href="${pageContext.request.contextPath}/inventory-history-detail?id=${batch.transactionId}" class="transaction-link">
                                ${batch.transactionCode}
                            </a>
                        </strong>
                    </div>
                    <div><span>Nhà cung cấp</span><strong>${empty batch.supplierName ? '-' : batch.supplierName}</strong></div>
                    <div><span>Người tạo phiếu</span><strong>${empty batch.createdByName ? '-' : batch.createdByName}</strong></div>
                    <div><span>Ngày nhập</span><strong>${batch.createdAtText}</strong></div>
                    <div><span>Cập nhật gần nhất</span><strong>${empty batch.updatedAtText ? '-' : batch.updatedAtText}</strong></div>
                </div>
            </div>

            <div class="batch-detail-card note-card-detail">
                <h3>Ghi chú</h3>
                <p>${empty batch.note ? 'Không có ghi chú.' : batch.note}</p>
            </div>
        </div>
    </section>
</div>
</body>
</html>
