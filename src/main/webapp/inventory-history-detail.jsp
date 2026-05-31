<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

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
                    <th>Ghi chú</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty details}">
                    <tr>
                        <td colspan="7" class="detail-empty">Phiếu này chưa có sản phẩm chi tiết.</td>
                    </tr>
                </c:if>

                <c:forEach items="${details}" var="detail">
                    <tr>
                        <td>
                            <img src="${pageContext.request.contextPath}/${detail.thumbnail}" alt="${detail.productName}" class="product-thumb">
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
