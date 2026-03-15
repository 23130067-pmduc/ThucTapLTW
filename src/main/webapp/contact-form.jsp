<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Contact - Form</title>
  <link rel="stylesheet" href="./css/contact-form.css">
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
          <label>Tên</label>
          <input type="text" name="name"
                 value="${contact.name}"
                 <c:if test="${mode == 'view'}">readonly</c:if>>

        </div>
      </div>

      <div class="row">
        <div class="col">
          <label>Email</label>
          <input type="text" name="email"
                 value="${contact.email}"
                 <c:if test="${mode == 'view'}">readonly</c:if>>
        </div>


        <div class="col">
          <label>Số điện thoại</label>
          <input type="text" name="phone"
                 value="${contact.phone}"
                 <c:if test="${mode == 'view'}">readonly</c:if>>
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
          <label>Nội dung</label>
          <textarea name="message"
                    rows="5"
                    <c:if test="${mode != 'add'}">readonly</c:if>
          >${contact.message}</textarea>
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
      <a href="contact-admin" class="btn-secondary">Hủy</a>
    </div>

    <input type="hidden" name="id" value="${contact.id}">
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
</body>
</html>