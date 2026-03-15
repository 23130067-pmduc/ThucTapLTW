<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>


<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
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
      </c:choose>
    </h2>
  </div>




  <form method="post" action="banner-admin" enctype="multipart/form-data">

    <div class="card">
      <h3>Thông tin banner</h3>
      <div class="row">
        <div class="col">
          <label>Tiêu đề</label>
          <input type="text"
                 name="title"
                 value="${banner.title}">
        </div>

        <div class="col">
          <label>Liên kết đến</label>
          <input type="text"
                 name="navigateTo"
                 value="${banner.navigateTo}"
                 placeholder="san-pham">
        </div>
      </div>



      <div class="row">
        <div class="col">
          <label>Trạng thái</label>
          <select name="status">
            <option value="1" ${banner.status ? "selected" : ""}>Hoạt động</option>
            <option value="0" ${!banner.status ? "selected" : ""}>Không hoạt động</option>
          </select>
        </div>
      </div>



      <div class="row">
        <div class="col">
          <label>Ảnh banner</label>
          <input type="file"
                 name="imageFile"
                 accept="image/*"
                 <c:if test="${mode == 'add'}">required</c:if>>

          <div class="preview">
            <img id="previewImg"
                 src="${banner.imageUrl}"
                 alt="Preview"
                 style="${mode == 'add' ? 'display:none' : 'display:block'}; max-width:300px;">

          </div>
        </div>
      </div>
    </div>

    <div class="form-footer">
      <button type="submit"
              name="action"
              value="${mode == 'add' ? 'create' : 'update'}"
              class="btn-primary">
        Lưu
      </button>
      <a href="banner-admin" class="btn-secondary">Hủy</a>
    </div>



    <input type="hidden" name="id" value="${banner.id}">
    <input type="hidden" name="mode" value="${mode}">
  </form>


</div>
</body>
</html>