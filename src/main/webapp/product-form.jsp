<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Product Form</title>
    <link rel="stylesheet" href="css/user-form.css">
</head>
<body>

<div class="container">

    <!-- HEADER -->
    <div class="form-header">
        <a href="product-admin" class="btn-back">← Quay lại</a>
        <h2>
            <c:choose>
                <c:when test="${mode == 'add'}">Thêm sản phẩm</c:when>
                <c:when test="${mode == 'edit'}">Chỉnh sửa sản phẩm</c:when>
                <c:otherwise>Xem chi tiết sản phẩm</c:otherwise>
            </c:choose>
        </h2>
    </div>


    <form method="post" action="product-admin" enctype="multipart/form-data">


        <div class="card">
            <h3>Thông tin sản phẩm</h3>

            <c:if test="${mode != 'add'}">
                <div class="row">
                    <div class="col">
                        <label>ID</label>
                        <input type="text" name="id" value="${product.id}">
                    </div>
                </div>
            </c:if>

            <div class="row">
                <div class="col">
                    <label>Tên sản phẩm</label>
                    <input type="text" name="name" value="${product.name}" required>
                </div>

                <div class="col">
                    <label>Giá</label>
                    <input type="number" name="price" value="${product.price}" required>
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <label>Danh mục</label>
                    <select name="category_id" required>
                        <option value="">-- Chọn danh mục --</option>
                        <c:forEach var="c" items="${categories}">
                            <option value="${c.id}" 
                                    ${product.category_id == c.id ? 'selected' : ''}>
                                ${c.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="col">
                    <label>Trạng thái</label>
                    <select name="status" required>
                        <option value="Đang bán" ${product.status == 'Đang bán' ? 'selected' : ''}>
                            Đang bán
                        </option>
                        <option value="Hết hàng" ${product.status == 'Hết hàng' ? 'selected' : ''}>
                            Hết hàng
                        </option>
                    </select>
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <label>Hình ảnh sản phẩm</label>
                    <input type="file" name="imageFile" accept="image/*" 
                           onchange="previewProductImage(event)"
                           ${mode == 'view' ? 'disabled' : ''}>
                    <small style="color: #666; display: block; margin-top: 5px;">
                        Chọn file ảnh (JPG, PNG, GIF - tối đa 10MB)
                    </small>
                </div>
            </div>

            <!-- Preview ảnh hiện tại hoặc ảnh mới chọn -->
            <div class="row">
                <div class="col">
                    <div id="product-image-preview-container" style="margin-top: 10px;">
                        <c:if test="${not empty product.thumbnail}">
                            <label>Xem trước ảnh</label>
                            <img id="product-image-preview" src="${product.thumbnail}" 
                                 alt="Product image"
                                 style="max-width: 300px; max-height: 300px; border-radius: 8px; border: 1px solid #ddd; display: block; margin-top: 10px;">
                        </c:if>
                        <c:if test="${empty product.thumbnail}">
                            <img id="product-image-preview" src="" alt="Preview" 
                                 style="display: none; max-width: 300px; max-height: 300px; border-radius: 8px; border: 1px solid #ddd; margin-top: 10px;">
                        </c:if>
                    </div>
                </div>
            </div>
        </div>



        <!-- FOOTER -->
        <div class="form-footer">
            <c:if test="${mode != 'view'}">
                <button type="submit" name="action"
                        value="${mode == 'add' ? 'add' : 'update'}"
                        class="btn-primary">
                    Lưu
                </button>
            </c:if>
            <a href="product-admin" class="btn-secondary">Hủy</a>
        </div>

        <!-- HIDDEN -->
        <input type="hidden" name="id" value="${product.id}">
        <input type="hidden" name="mode" value="${mode}">

    </form>

</div>


<c:if test="${mode == 'view'}">
    <style>
        input, select, textarea, button {
            pointer-events: none;
            background: #f2f2f2;
        }
        .btn-secondary {
            pointer-events: auto;
        }
    </style>
</c:if>

<script>
function previewProductImage(event) {
    const file = event.target.files[0];
    const preview = document.getElementById('product-image-preview');
    
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
        }
        reader.readAsDataURL(file);
    }
}
</script>

</body>
</html>
