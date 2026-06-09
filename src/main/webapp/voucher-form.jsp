<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<c:set var="isEdit" value="${mode == 'edit'}" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - ${isEdit ? 'Sửa mã giảm giá' : 'Thêm mã giảm giá'}</title>
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
            <h1>${isEdit ? 'Sửa mã giảm giá' : 'Thêm mã giảm giá'}</h1>
        </div>

        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
    </header>

    <main class="voucher-form-content">
        <section class="voucher-form-card">
            <div class="form-title-row">
                <div>
                    <h2>${isEdit ? 'Cập nhật mã giảm giá' : 'Tạo mã giảm giá mới'}</h2>
                    <p>${isEdit ? 'Chỉnh sửa thông tin để mã giảm giá phù hợp với chương trình hiện tại.' : 'Nhập đầy đủ thông tin để mã có thể hiển thị và áp dụng trên hệ thống.'}</p>
                </div>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    ${errorMessage}
                </div>
            </c:if>

            <form class="voucher-form" method="post" action="${pageContext.request.contextPath}/voucher-admin">
                <input type="hidden" name="action" value="${isEdit ? 'update' : 'create'}">
                <c:if test="${isEdit}">
                    <input type="hidden" name="id" value="${voucher.id}">
                    <input type="hidden" name="used_quantity" value="${voucher.used_quantity}">
                </c:if>

                <div class="form-grid">
                    <div class="form-group">
                        <label for="code">Mã giảm giá <span>*</span></label>
                        <input type="text" id="code" name="code" value="${voucher.code}" placeholder="VD: SUNNY10" required>
                    </div>

                    <div class="form-group">
                        <label for="name">Tên mã <span>*</span></label>
                        <input type="text" id="name" name="name" value="${voucher.name}" placeholder="VD: Giảm 10% đơn hàng" required>
                    </div>

                    <div class="form-group">
                        <label for="voucher_scope">Phạm vi áp dụng <span>*</span></label>
                        <select id="voucher_scope" name="voucher_scope" required>
                            <option value="ORDER" ${voucher.voucher_scope == 'ORDER' ? 'selected' : ''}>Đơn hàng</option>
                            <option value="PRODUCT" ${voucher.voucher_scope == 'PRODUCT' ? 'selected' : ''}>Sản phẩm</option>
                            <option value="SHIPPING" ${voucher.voucher_scope == 'SHIPPING' ? 'selected' : ''}>Vận chuyển</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="discount_type">Loại giảm <span>*</span></label>
                        <select id="discount_type" name="discount_type" required>
                            <option value="PERCENT" ${voucher.discount_type == 'PERCENT' ? 'selected' : ''}>Theo phần trăm</option>
                            <option value="FIXED" ${voucher.discount_type == 'FIXED' ? 'selected' : ''}>Số tiền cố định</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="discount_value">Giá trị giảm <span>*</span></label>
                        <input type="number" min="1" step="0.01" id="discount_value" name="discount_value" value="${voucher.discount_value}" placeholder="VD: 10 hoặc 50000" required>
                    </div>

                    <div class="form-group">
                        <label for="max_discount">Giảm tối đa</label>
                        <input type="number" min="0" step="1000" id="max_discount" name="max_discount" value="${voucher.max_discount}" placeholder="Bỏ trống nếu không giới hạn">
                    </div>

                    <div class="form-group">
                        <label for="min_order_value">Đơn tối thiểu</label>
                        <input type="number" min="0" step="1000" id="min_order_value" name="min_order_value" value="${voucher.min_order_value}" placeholder="VD: 300000">
                    </div>

                    <div class="form-group">
                        <label for="quantity">Số lượt sử dụng <span>*</span></label>
                        <input type="number" min="1" id="quantity" name="quantity" value="${voucher.quantity}" placeholder="VD: 100" required>
                        <c:if test="${isEdit}">
                            <small class="form-note">Đã dùng: ${voucher.used_quantity} lượt. Tổng lượt không được nhỏ hơn số lượt đã dùng.</small>
                        </c:if>
                    </div>

                    <div class="form-group">
                        <label for="start_date">Thời gian bắt đầu <span>*</span></label>
                        <input type="datetime-local" id="start_date" name="start_date" value="${voucher.startDateInputValue}" required>
                    </div>

                    <div class="form-group">
                        <label for="end_date">Thời gian kết thúc <span>*</span></label>
                        <input type="datetime-local" id="end_date" name="end_date" value="${voucher.endDateInputValue}" required>
                    </div>

                    <div class="form-group">
                        <label for="product_id">ID sản phẩm áp dụng</label>
                        <input type="number" min="1" id="product_id" name="product_id" value="${voucher.product_id}" placeholder="Chỉ nhập khi phạm vi là Sản phẩm">
                    </div>

                    <div class="form-group">
                        <label for="customer_id">ID khách hàng áp dụng</label>
                        <input type="number" min="1" id="customer_id" name="customer_id" value="${voucher.customer_id}" placeholder="Bỏ trống nếu áp dụng cho mọi khách">
                    </div>

                    <div class="form-group form-group-full">
                        <label for="description">Mô tả</label>
                        <textarea id="description" name="description" rows="4" placeholder="Nhập mô tả ngắn về mã giảm giá">${voucher.description}</textarea>
                    </div>
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/voucher-admin" class="btn-secondary">Hủy</a>
                    <button type="submit" class="btn-primary">
                        <i class="fa-solid ${isEdit ? 'fa-floppy-disk' : 'fa-plus'}"></i>
                        ${isEdit ? 'Lưu thay đổi' : 'Thêm mã giảm giá'}
                    </button>
                </div>
            </form>
        </section>
    </main>
</div>
</body>
</html>
