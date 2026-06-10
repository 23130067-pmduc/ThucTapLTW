<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Chi tiết mã giảm giá</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/voucher-admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="voucher-form-page">
    <header class="voucher-form-header">
        <div class="voucher-form-header-left">
            <a href="${pageContext.request.contextPath}/voucher-admin" class="back-btn">
                <i class="fa-solid fa-arrow-left"></i>
                Quay lại
            </a>
            <h1>Chi tiết mã giảm giá</h1>
        </div>

        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
    </header>

    <main class="voucher-form-content">
        <section class="voucher-detail-card">
            <div class="voucher-detail-head">
                <div>
                    <span class="voucher-code detail-code">${voucher.code}</span>
                    <h2>${voucher.name}</h2>
                    <p>${empty voucher.description ? 'Chưa có mô tả cho mã giảm giá này.' : voucher.description}</p>
                </div>
                <span class="voucher-status ${voucher.statusClass}">${voucher.statusLabel}</span>
            </div>

            <div class="voucher-detail-grid">
                <div class="detail-item">
                    <span>ID</span>
                    <strong>#${voucher.id}</strong>
                </div>

                <div class="detail-item">
                    <span>Phạm vi áp dụng</span>
                    <strong>${voucher.scopeLabel}</strong>
                </div>

                <div class="detail-item">
                    <span>Loại giảm</span>
                    <strong>${voucher.discountTypeLabel}</strong>
                </div>

                <div class="detail-item">
                    <span>Giá trị giảm</span>
                    <strong>${voucher.discountText}</strong>
                </div>

                <div class="detail-item">
                    <span>Giảm tối đa</span>
                    <strong>${voucher.maxDiscountText}</strong>
                </div>

                <div class="detail-item">
                    <span>Đơn tối thiểu</span>
                    <strong>${voucher.minOrderText}</strong>
                </div>

                <div class="detail-item">
                    <span>Lượt sử dụng</span>
                    <strong>${voucher.usageText}</strong>
                    <small>Còn lại ${voucher.remainingQuantity} lượt</small>
                </div>

                <div class="detail-item">
                    <span>Trạng thái</span>
                    <strong>${voucher.statusLabel}</strong>
                </div>

                <div class="detail-item">
                    <span>Thời gian bắt đầu</span>
                    <strong>${voucher.startDateText}</strong>
                </div>

                <div class="detail-item">
                    <span>Thời gian kết thúc</span>
                    <strong>${voucher.endDateText}</strong>
                </div>

                <div class="detail-item">
                    <span>ID sản phẩm áp dụng</span>
                    <strong>${empty voucher.product_id ? 'Áp dụng theo phạm vi chung' : voucher.product_id}</strong>
                </div>

                <div class="detail-item">
                    <span>ID khách hàng áp dụng</span>
                    <strong>${empty voucher.customer_id ? 'Tất cả khách hàng' : voucher.customer_id}</strong>
                </div>

                <div class="detail-item">
                    <span>Thời gian tạo</span>
                    <strong>${voucher.createdAtText}</strong>
                </div>

                <div class="detail-item">
                    <span>Đơn hàng đã áp dụng gần nhất</span>
                    <strong>${empty voucher.order_id ? 'Chưa áp dụng cho đơn hàng' : voucher.order_id}</strong>
                </div>
            </div>
        </section>
    </main>
</div>

<div id="voucherStatusModal" class="voucher-modal-overlay" aria-hidden="true">
    <div class="voucher-modal" role="dialog" aria-modal="true" aria-labelledby="voucherStatusModalTitle">
        <div class="voucher-modal-icon"><i id="voucherStatusModalIcon" class="fa-solid fa-lock"></i></div>
        <h3 id="voucherStatusModalTitle">Xác nhận thay đổi trạng thái</h3>
        <p id="voucherStatusModalMessage"></p>

        <form method="post" action="${pageContext.request.contextPath}/voucher-admin">
            <input type="hidden" name="action" value="toggle-status">
            <input type="hidden" name="id" id="voucherStatusId">
            <input type="hidden" name="status" id="voucherStatusValue">

            <div class="voucher-modal-actions">
                <button type="button" class="btn-secondary js-close-voucher-status-modal">Hủy</button>
                <button type="submit" id="voucherStatusSubmit" class="btn-voucher-confirm">Xác nhận</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/javaScript/voucher-admin.js"></script>
</body>
</html>
