<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>


<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Form banner</title>
  <link rel="stylesheet" href="./css/banner-form.css">
</head>
<body>
<div class="container">

  <div class="form-header">
    <a href="banner-admin" class="btn-back">← Quay lại</a>
    <h2>
      <c:choose>
        <c:when test="${mode == 'add'}">Thêm banner</c:when>
        <c:when test="${mode == 'edit'}">Chỉnh sửa banner</c:when>
        <c:otherwise>Xem chi tiết banner</c:otherwise>
      </c:choose>
    </h2>
  </div>

  <form method="post" action="banner-admin" enctype="multipart/form-data">

    <div class="card">
      <h3>Thông tin banner</h3>
      <div class="row">
        <div class="col">
          <label>Tiêu đề</label>
          <input type="text"  name="title"
                 value="${banner.title}"
                 <c:if test="${mode == 'view'}">readonly</c:if>>
        </div>

        <div class="col">
          <label>Liên kết đến</label>
          <select name="navigateTo" <c:if test="${mode == 'view'}">disabled</c:if>>
            <option value="trang-chu" ${banner.navigateTo == 'trang-chu' ? 'selected' : ''}>
              Trang chủ
            </option>

            <option value="san-pham" ${banner.navigateTo == 'san-pham' ? 'selected' : ''}>
              Sản phẩm
            </option>

            <option value="khuyen-mai" ${banner.navigateTo == 'khuyen-mai' ? 'selected' : ''}>
              Khuyến mãi
            </option>

            <option value="tin-tuc" ${banner.navigateTo == 'tin-tuc' ? 'selected' : ''}>
              Tin tức
            </option>

            <option value="lien-he" ${banner.navigateTo == 'lien-he' ? 'selected' : ''}>
              Liên hệ
            </option>
          </select>
        </div>
      </div>



      <div class="row">
        <div class="col">
          <label>Trạng thái</label>
          <select name="status" <c:if test="${mode == 'view'}">disabled</c:if>>
            <option value="1" ${banner.status ? "selected" : ""}>Hoạt động</option>
            <option value="0" ${!banner.status ? "selected" : ""}>Không hoạt động</option>
          </select>
        </div>
      </div>



      <div class="row">
        <div class="col">
          <label>Ảnh banner</label>

          <label class="banner-upload-box ${mode == 'view' ? 'view-only' : ''}" for="${mode == 'view' ? '' : 'imageFile'}">

            <c:if test="${mode != 'view'}">
              <input type="file"
                     id="imageFile"
                     name="imageFile"
                     accept="image/*"
                     <c:if test="${mode == 'add'}">required</c:if>>
            </c:if>

            <img id="previewImg"
                 src="${banner.imageUrl}"
                 alt="Preview"
                 class="banner-upload-preview"
                 style="${mode == 'add' ? 'display:none' : 'display:block'};">

            <div id="uploadPlaceholder"
                 class="banner-upload-placeholder"
                 style="${mode == 'add' ? 'display:flex' : 'display:none'};">
              <span>+ Chọn ảnh banner</span>
              <small>JPG, PNG, WEBP</small>
            </div>

          </label>

        </div>
      </div>
    </div>

    <div class="form-footer">
      <c:if test="${mode != 'view'}">
        <button type="submit"
                name="action"
                value="${mode == 'add' ? 'create' : 'update'}"
                class="btn-primary">
          Lưu
        </button>
      </c:if>

      <a href="banner-admin" class="btn-secondary">Hủy</a>
    </div>



    <input type="hidden" name="id" value="${banner.id}">
    <input type="hidden" name="mode" value="${mode}">
  </form>


</div>

<script>
  const imageFileInput = document.getElementById("imageFile");
  const previewImg = document.getElementById("previewImg");
  const uploadPlaceholder = document.getElementById("uploadPlaceholder");

  if (imageFileInput && previewImg) {
    imageFileInput.addEventListener("change", function () {
      const file = this.files[0];

      if (!file) {
        return;
      }

      previewImg.src = URL.createObjectURL(file);
      previewImg.style.display = "block";

      if (uploadPlaceholder) {
        uploadPlaceholder.style.display = "none";
      }
    });
  }
</script>

</body>
</html>