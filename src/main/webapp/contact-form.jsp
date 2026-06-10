<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Contact - Form</title>
  <link rel="stylesheet" href="./css/contact-form.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<div class="container">

  <div class="form-header">
    <a href="contact-admin" class="btn-back">← Quay lại</a>
    <h2>
      <c:choose>
        <c:when test="${mode == 'add'}">Thêm liên hệ</c:when>
        <c:when test="${mode == 'edit'}">Chỉnh sửa liên hệ</c:when>
        <c:otherwise>Xem chi tiết liên hệ</c:otherwise>
      </c:choose>
    </h2>
  </div>


  <form method="post" action="contact-admin">

    <div class="card">
      <h3>Thông tin liên hệ</h3>

      <div class="row">
        <div class="col">
          <label>ID</label>
          <input type="text" value="${contact.id}" readonly>
        </div>

        <div class="col">
            <label>Tên<span class="required-star">*</span></label>
            <input type="text" id="name" name="name" value="${contact.name}"
                   placeholder="Nhập họ và tên..." <c:if test="${mode == 'view'}">readonly</c:if>>
            <span class="error-msg" id="nameError"></span>

        </div>
      </div>

      <div class="row">
        <div class="col">
          <label>Email <span class="required-star">*</span></label>
          <input type="text" id="email" name="email"
                 value="${contact.email}" placeholder="example@email.com"
                 <c:if test="${mode == 'view'}">readonly</c:if>>
            <span class="error-msg" id="emailError"></span>
        </div>


        <div class="col">
          <label>Số điện thoại <span class="required-star">*</span></label>
          <input type="text" id="phone" name="phone" value="${contact.phone}" placeholder="0xxxxxxxxx"
                 <c:if test="${mode == 'view'}">readonly</c:if>>
          <span class="error-msg" id="phoneError"></span>
        </div>

      </div>

      <div class="row">
        <div class="col">
          <label>Địa chỉ</label>
          <input type="text" name="address"
                 value="${contact.address}"
                 <c:if test="${mode == 'view'}">readonly</c:if>>
        </div>
        <div class="col">
          <label>Trạng thái</label>
          <select name="status">
            <option value="New" ${contact.status == 'New' ? "selected" : ""}>Mới</option>
            <option value="Processing" ${contact.status == 'Processing' ? "selected" : ""}>Đang xử lý</option>
            <option value="Closed" ${contact.status == 'Closed' ? "selected" : ""}>Đã xử lý</option>
          </select>
        </div>

      </div>

      <div class="row">
        <div class="col">
          <label>Nội dung <span class="required-star">*</span></label>
          <textarea id="message" name="message" rows="5" placeholder="Nhập nội dung liên hệ..."
                    <c:if test="${mode != 'add'}">readonly</c:if>
          >${contact.message}</textarea>
          <span class="error-msg" id="messageError"></span>
        </div>
      </div>

    </div>


    <!-- FOOTER -->
    <div class="form-footer">
      <c:if test="${mode != 'view'}">
        <button type="submit" id="btnSave" class="btn-primary">
          <i class="fa-solid ${mode == 'add' ? 'fa-plus' : 'fa-floppy-disk'}"></i>
          ${mode == 'add' ? 'Thêm liên hệ' : 'Lưu thay đổi'}
        </button>
      </c:if>
      <a href="contact-admin" class="btn-secondary">Hủy</a>
    </div>

    <input type="hidden" name="action" value="${mode == 'add' ? 'create' : 'update'}">
    <input type="hidden" name="id" value="${contact.id}">
    <input type="hidden" name="mode" value="${mode}">

  </form>

</div>
<script src="javaScript/contact-form.js"></script>
</body>
</html>