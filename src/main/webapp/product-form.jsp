<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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

            <c:if test="${mode eq 'edit'}">
                <div class="row">
                    <div class="col">
                        <label>ID</label>
                        <input type="text" name="id" value="${product.id}" readonly>
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
                    <label>Mô tả</label>
                    <textarea name="description"
                              rows="5"
                              class="form-textarea"
                    ${mode == 'view' ? 'readonly' : ''}>${product.description}</textarea>
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
                        <option value="Ngừng bán" ${product.status == 'Ngừng bán' ? 'selected' : ''}>
                            Ngừng bán
                        </option>
                    </select>
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <label>Ảnh chính</label>
                    <input type="file"
                           name="imageFile"
                           accept="image/*"
                           onchange="previewProductImage(event)"
                    ${mode == 'view' ? 'disabled' : ''}>

                    <small class="form-hint">
                        Chọn 1 ảnh chính cho sản phẩm
                    </small>

                    <div id="product-image-preview-container" class="image-preview-container">
                        <c:if test="${not empty product.thumbnail}">
                            <label>Xem trước ảnh chính</label>
                            <img id="product-image-preview"
                                 src="${product.thumbnail}"
                                 alt="Product image"
                                 class="product-image-preview show">
                        </c:if>

                        <c:if test="${empty product.thumbnail}">
                            <img id="product-image-preview"
                                 src=""
                                 alt="Preview"
                                 class="product-image-preview">
                        </c:if>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <label>Ảnh phụ</label>
                    <input type="file"
                           name="subImageFiles"
                           accept="image/*"
                           multiple
                           onchange="previewSubImages(event)"
                    ${mode == 'view' ? 'disabled' : ''}>

                    <small class="form-hint">
                        Có thể chọn nhiều ảnh phụ cùng lúc
                    </small>

                    <div id="sub-images-preview" class="sub-images-preview"></div>
                </div>
            </div>

        </div>



        <!-- FOOTER -->
        <div class="form-footer">
            <c:if test="${mode != 'view'}">
                <button type="submit" name="action"
                        value="${mode == 'add' ? 'create' : 'update'}"
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
                preview.classList.add('show');
            };

            reader.readAsDataURL(file);
        }
    }

    function previewSubImages(event) {
        const files = event.target.files;
        const previewContainer = document.getElementById('sub-images-preview');

        previewContainer.innerHTML = '';

        Array.from(files).forEach(file => {
            const reader = new FileReader();

            reader.onload = function(e) {
                const img = document.createElement('img');
                img.src = e.target.result;
                img.alt = 'Ảnh phụ';
                img.className = 'sub-image-preview-item';

                previewContainer.appendChild(img);
            };

            reader.readAsDataURL(file);
        });
    }
</script>

</body>
</html>
