<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết lịch sử nhập xuất kho</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inventory-history-detail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="inventory-detail-page">
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

    <section class="content inventory-detail-content">
        <header class="topbar">
            <div class="topbar-left">
                <a href="${pageContext.request.contextPath}/inventory-history-admin" class="back-btn" title="Quay lại lịch sử nhập xuất">
                    <i class="fa-solid fa-arrow-left"></i>
                </a>
                <h1>Chi tiết lịch sử nhập xuất kho</h1>
            </div>

            <div class="topbar-actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>

        <c:if test="${param.success == 'status_updated'}">
            <div class="alert alert-success">Cập nhật trạng thái phiếu và tồn kho thành công.</div>
        </c:if>
        <c:if test="${param.error == 'already_processed'}">
            <div class="alert alert-warning">Phiếu này đã được xử lý nên không thể đổi trạng thái.</div>
        </c:if>
        <c:if test="${param.error == 'insufficient_stock'}">
            <div class="alert alert-danger">Không thể hoàn thành phiếu xuất vì số lượng tồn kho hiện tại không đủ.</div>
        </c:if>
        <c:if test="${param.error == 'insufficient_batch_stock'}">
            <div class="alert alert-danger">Không thể hoàn thành phiếu xuất vì số lượng trong các lô nhập không đủ để xuất theo FIFO.</div>
        </c:if>
                <c:if test="${param.error == 'missing_unit_cost'}">
            <div class="alert alert-danger">Không thể hoàn thành phiếu nhập vì phiếu chưa có giá nhập hợp lệ.</div>
        </c:if>

<c:if test="${param.error == 'invalid_status' || param.error == 'status_update_failed'}">
            <div class="alert alert-danger">Không thể cập nhật trạng thái phiếu. Vui lòng thử lại.</div>
        </c:if>

        <div class="detail-summary">
            <div class="summary-card">
                <span class="label">Mã phiếu</span>
                <strong>${transaction.code}</strong>
            </div>

            <div class="summary-card">
                <span class="label">Loại phiếu</span>
                <span class="type-badge ${transaction.type == 'IMPORT' ? 'type-import' : 'type-export'}">
                    ${transaction.typeText}
                </span>
            </div>

            <div class="summary-card">
                <span class="label">Trạng thái</span>
                <span class="status-badge ${transaction.status == 'PENDING' ? 'status-pending' : (transaction.status == 'COMPLETED' ? 'status-completed' : 'status-cancelled')}">
                    ${transaction.statusText}
                </span>
            </div>

            <div class="summary-card">
                <span class="label">Tổng số lượng</span>
                <strong>${transaction.totalQuantity}</strong>
            </div>
        </div>

        <c:if test="${transaction.status == 'PENDING'}">
            <div class="status-action-box">
                <div>
                    <h3>Xử lý trạng thái phiếu</h3>
                    <p>Phiếu đang ở trạng thái Đang xử lý. Bạn có thể chuyển sang Hoàn thành hoặc Hủy phiếu.</p>
                </div>

                <div class="status-action-buttons">
                    <button type="button"
                            class="btn-complete js-open-status-modal"
                            data-transaction-id="${transaction.id}"
                            data-status="COMPLETED"
                            data-redirect="/inventory-history-detail?id=${transaction.id}"
                            data-message="Bạn có chắc muốn hoàn thành phiếu ${transaction.code} không?">
                        <i class="fa-solid fa-check"></i> Hoàn thành phiếu
                    </button>

                    <button type="button"
                            class="btn-cancel-status js-open-status-modal"
                            data-transaction-id="${transaction.id}"
                            data-status="CANCELLED"
                            data-redirect="/inventory-history-detail?id=${transaction.id}"
                            data-message="Bạn có chắc muốn hủy phiếu ${transaction.code} không?">
                        <i class="fa-solid fa-xmark"></i> Hủy phiếu
                    </button>
                </div>
            </div>
        </c:if>

        <div class="detail-info-grid">
            <div class="info-box">
                <h3>Thông tin phiếu</h3>
                <div class="info-row">
                    <span>Người tạo</span>
                    <strong>${transaction.createdByName}</strong>
                </div>
                <div class="info-row">
                    <span>Thời gian tạo</span>
                    <strong>${transaction.createdAtText}</strong>
                </div>
                <div class="info-row">
                    <span>Nhà cung cấp</span>
                    <strong>
                        <c:choose>
                            <c:when test="${empty transaction.supplierName}">-</c:when>
                            <c:otherwise>${transaction.supplierName}</c:otherwise>
                        </c:choose>
                    </strong>
                </div>
            </div>

            <div class="info-box">
                <h3>Ghi chú</h3>
                <p class="note-text">
                    <c:choose>
                        <c:when test="${empty transaction.note}">Không có ghi chú.</c:when>
                        <c:otherwise>${transaction.note}</c:otherwise>
                    </c:choose>
                </p>
            </div>
        </div>

        <div class="detail-table-wrapper">
            <div class="table-title">
                <h2>Danh sách sản phẩm trong phiếu</h2>
                <span>${details.size()} sản phẩm</span>
            </div>

            <table class="detail-table">
                <thead>
                <tr>
                    <th>Ảnh</th>
                    <th>Sản phẩm</th>
                    <th>Danh mục</th>
                    <th>Màu</th>
                    <th>Size</th>
                    <th>Số lượng</th>
                    <c:choose>
                        <c:when test="${transaction.type == 'IMPORT'}">
                            <th>Giá nhập</th>
                            <th>Thành tiền nhập</th>
                        </c:when>
                        <c:otherwise>
                            <th>Giá vốn TB</th>
                            <th>Tổng giá vốn</th>
                        </c:otherwise>
                    </c:choose>
                    <th>Ghi chú</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty details}">
                    <tr>
                        <td colspan="9" class="detail-empty">Phiếu này chưa có sản phẩm chi tiết.</td>
                    </tr>
                </c:if>

                <c:forEach items="${details}" var="detail">
                    <tr>
                        <td>
                            <c:set var="thumbUrl" value="${detail.thumbnail}" />
                            <c:choose>
                                <c:when test="${empty thumbUrl}">
                                    <img src="${pageContext.request.contextPath}/img/gau.png" alt="${detail.productName}" class="product-thumb">
                                </c:when>
                                <c:when test="${fn:startsWith(thumbUrl, 'http://') || fn:startsWith(thumbUrl, 'https://')}">
                                    <img src="${thumbUrl}" alt="${detail.productName}" class="product-thumb">
                                </c:when>
                                <c:when test="${fn:startsWith(thumbUrl, '/')}">
                                    <img src="${pageContext.request.contextPath}${thumbUrl}" alt="${detail.productName}" class="product-thumb">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/${thumbUrl}" alt="${detail.productName}" class="product-thumb">
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="product-info">
                                <strong>${detail.productName}</strong>
                                <small>Mã SP: #${detail.productId} - Biến thể: #${detail.productVariantId}</small>
                            </div>
                        </td>
                        <td>${detail.categoryName}</td>
                        <td>${detail.colorName}</td>
                        <td>${detail.sizeName}</td>
                        <td>
                            <span class="quantity-badge ${transaction.type == 'IMPORT' ? 'quantity-import' : 'quantity-export'}">
                                <c:choose>
                                    <c:when test="${transaction.type == 'IMPORT'}">+${detail.quantity}</c:when>
                                    <c:otherwise>-${detail.quantity}</c:otherwise>
                                </c:choose>
                            </span>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${empty detail.unitCost || detail.unitCost <= 0}">-</c:when>
                                <c:otherwise>
                                    <span class="unit-cost-badge">
                                        <fmt:formatNumber value="${detail.unitCost}" pattern="#,#00" /> đ
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${empty detail.unitCost || detail.unitCost <= 0}">-</c:when>
                                <c:otherwise>
                                    <span class="unit-cost-badge">
                                        <fmt:formatNumber value="${detail.totalCost}" pattern="#,#00" /> đ
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${empty detail.note}">-</c:when>
                                <c:otherwise>${detail.note}</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>
</div>

<div id="statusModal" class="status-modal-overlay" aria-hidden="true">
    <div class="status-modal-box" role="dialog" aria-modal="true">
        <h3>Xác nhận thao tác</h3>
        <p id="statusModalMessage">Bạn có chắc muốn thực hiện thao tác này không?</p>

        <form method="post" action="${pageContext.request.contextPath}/inventory-transaction-status">
            <input type="hidden" name="transactionId" id="statusTransactionId">
            <input type="hidden" name="status" id="statusValue">
            <input type="hidden" name="redirect" id="statusRedirect" value="/inventory-history-admin">

            <div class="status-modal-actions">
                <button type="button" class="status-btn-cancel js-close-status-modal">Hủy</button>
                <button type="submit" class="status-btn-confirm">Đồng ý</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/javaScript/inventory-status-modal.js"></script>
</body>
</html>
