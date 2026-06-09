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
            </div>

            <div class="voucher-detail-actions">
                <a href="${pageContext.request.contextPath}/voucher-admin" class="btn-secondary">
                    <i class="fa-solid fa-list"></i>
                    Về danh sách
                </a>
                <a href="${pageContext.request.contextPath}/voucher-admin?action=edit&id=${voucher.id}" class="btn-primary">
                    <i class="fa-solid fa-pen"></i>
                    Sửa mã giảm giá
                </a>
            </div>
        </section>
    </main>
</div>
</body>
</html>
