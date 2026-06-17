<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${mode == 'edit' ? 'Sửa nhà cung cấp' : 'Thêm nhà cung cấp'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-form.css">
</head>
<body>
<div class="container">
    <div class="form-header">
        <a href="${pageContext.request.contextPath}/supplier-admin" class="btn-back">← Quay lại</a>
        <h2>${mode == 'edit' ? 'Sửa nhà cung cấp' : 'Thêm nhà cung cấp'}</h2>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-error">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/supplier-admin">
        <input type="hidden" name="action" value="${mode == 'edit' ? 'update' : 'create'}">
        <input type="hidden" name="mode" value="${mode}">
        <c:if test="${mode == 'edit'}">
            <input type="hidden" name="id" value="${supplier.id}">
        </c:if>

        <div class="card">
            <h3>Thông tin nhà cung cấp</h3>

            <div class="row">
                <div class="col">
                    <label>Mã nhà cung cấp <span style="color:#e03131">*</span></label>
                    <input type="text"
                           name="code"
                           value="${supplier.code}"
                           placeholder="Ví dụ: NCC001"
                           maxlength="30"
                           required>
                </div>

                <div class="col">
                    <label>Tên nhà cung cấp <span style="color:#e03131">*</span></label>
                    <input type="text"
                           name="name"
                           value="${supplier.name}"
                           placeholder="Nhập tên nhà cung cấp"
                           maxlength="150"
                           required>
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <label>Số điện thoại</label>
                    <input type="text"
                           name="phone"
                           value="${supplier.phone}"
                           placeholder="Nhập số điện thoại"
                           maxlength="30">
                </div>

                <div class="col">
                    <label>Email</label>
                    <input type="email"
                           name="email"
                           value="${supplier.email}"
                           placeholder="Nhập email"
                           maxlength="150">
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <label>Địa chỉ</label>
                    <input type="text"
                           name="address"
                           value="${supplier.address}"
                           placeholder="Nhập địa chỉ nhà cung cấp"
                           maxlength="255">
                </div>

                <div class="col">
                    <label>Trạng thái</label>
                    <select name="status">
                        <option value="ACTIVE" ${supplier.status == 'ACTIVE' || empty supplier.status ? 'selected' : ''}>Đang hoạt động</option>
                        <option value="LOCKED" ${supplier.status == 'LOCKED' ? 'selected' : ''}>Đã khóa</option>
                    </select>
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <label>Ghi chú</label>
                    <textarea name="note"
                              rows="4"
                              placeholder="Nhập ghi chú nếu có...">${supplier.note}</textarea>
                </div>
            </div>
        </div>

        <div class="form-footer">
            <button type="submit" class="btn-primary">
                ${mode == 'edit' ? 'Cập nhật nhà cung cấp' : 'Lưu nhà cung cấp'}
            </button>
            <a href="${pageContext.request.contextPath}/supplier-admin" class="btn-secondary">Hủy</a>
        </div>
    </form>
</div>
</body>
</html>
