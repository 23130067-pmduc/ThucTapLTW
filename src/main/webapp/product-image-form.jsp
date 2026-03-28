<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${mode == 'add'}">Thêm Ảnh</c:when>
            <c:when test="${mode == 'edit'}">Sửa Ảnh</c:when>
            <c:otherwise>Xem Ảnh</c:otherwise>
        </c:choose>
    </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product-image-form.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="container">
    <div class="form-header">
        <a href="${pageContext.request.contextPath}/product-image-admin?productId=${productId}" class="btn-back">
            <i class="fa fa-arrow-left"></i> Quay lại
        </a>
        <h2>
            <c:choose>
                <c:when test="${mode == 'add'}">Thêm Ảnh Mới</c:when>
                <c:when test="${mode == 'edit'}">Sửa Ảnh</c:when>
                <c:otherwise>Chi Tiết Ảnh</c:otherwise>
            </c:choose>
        </h2>
    </div>

    <div class="card">
        <form action="${pageContext.request.contextPath}/product-image-admin" method="post" enctype="multipart/form-data">
            <input type="hidden" name="productId" value="${productId}">

            <c:if test="${mode == 'edit'}">
                <input type="hidden" name="id" value="${image.id}">
            </c:if>

            <c:if test="${mode != 'view'}">
                <input type="hidden" name="action" value="${mode == 'add' ? 'create' : 'update'}">
            </c:if>

            <div class="col">
                <label>
                    Hình Ảnh
                    <c:if test="${mode == 'add'}">
                        <span class="required-mark">*</span>
                    </c:if>
                </label>

                <c:if test="${mode != 'add' && image.imageUrl != null}">
                    <div class="image-preview">
                        <img src="${pageContext.request.contextPath}/${image.imageUrl}" alt="Current Image" id="currentImage">
                    </div>
                </c:if>

                <c:if test="${mode != 'view'}">
                    <div class="file-input-wrapper">
                        <input type="file"
                               id="imageFile"
                               name="imageFile"
                               accept="image/*"
                               ${mode == 'add' ? 'required' : ''}>
                        <label for="imageFile" class="file-input-label">
                            <i class="fa fa-upload"></i> Chọn Ảnh
                        </label>
                        <span class="file-name" id="fileName" data-default-text="${mode == 'add' ? 'Chưa chọn file' : 'Chọn để thay đổi'}">
                            ${mode == 'add' ? 'Chưa chọn file' : 'Chọn để thay đổi'}
                        </span>
                    </div>

                    <div class="image-preview image-preview-hidden" id="newImagePreview">
                        <img src="" alt="Preview" id="previewImg">
                    </div>
                </c:if>
            </div>

            <div class="col form-section-spacing">
                <label for="isMain">Đặt làm ảnh chính</label>
                <div class="checkbox-wrapper">
                    <input type="checkbox"
                           id="isMain"
                           name="isMain"
                           value="true"
                           ${mode == 'edit' && image.main ? 'checked' : ''}
                           ${mode == 'view' ? 'disabled' : ''}>
                    <small class="form-text">Ảnh chính sẽ được hiển thị đầu tiên</small>
                </div>
            </div>

            <c:if test="${mode != 'view'}">
                <div class="form-footer form-footer-spacing">
                    <a href="${pageContext.request.contextPath}/product-image-admin?productId=${productId}" class="btn-secondary">
                        <i class="fa fa-times"></i> Hủy
                    </a>
                    <button type="submit" class="btn-primary">
                        <i class="fa fa-save"></i>
                        ${mode == 'add' ? 'Thêm Ảnh' : 'Cập Nhật'}
                    </button>
                </div>
            </c:if>

            <c:if test="${mode == 'view'}">
                <div class="form-footer form-footer-spacing">
                    <a href="${pageContext.request.contextPath}/product-image-admin?productId=${productId}" class="btn-secondary">
                        <i class="fa fa-arrow-left"></i> Quay lại
                    </a>
                </div>
            </c:if>
        </form>
    </div>
</div>

<script>
    (() => {
        const imageInput = document.getElementById('imageFile');
        const fileName = document.getElementById('fileName');
        const newImagePreview = document.getElementById('newImagePreview');
        const previewImage = document.getElementById('previewImg');

        if (!imageInput || !fileName || !newImagePreview || !previewImage) {
            return;
        }

        imageInput.addEventListener('change', (event) => {
            const file = event.target.files[0];

            if (!file) {
                fileName.textContent = fileName.dataset.defaultText;
                newImagePreview.classList.remove('is-visible');
                previewImage.removeAttribute('src');
                return;
            }

            fileName.textContent = file.name;

            const reader = new FileReader();
            reader.onload = (loadEvent) => {
                previewImage.src = loadEvent.target.result;
                newImagePreview.classList.add('is-visible');
            };
            reader.readAsDataURL(file);
        });
    })();
</script>
</body>
</html>
