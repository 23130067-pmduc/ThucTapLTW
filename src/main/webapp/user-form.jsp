<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>User Form</title>
  <link rel="stylesheet" href="./css/user-form.css">
</head>
<body>

<div class="container">

  <div class="form-header">
    <a href="user-admin" class="btn-back">← Quay lại</a>
    <h2>
      <c:choose>
        <c:when test="${mode == 'add'}">Thêm người dùng</c:when>
        <c:when test="${mode == 'edit'}">Chỉnh sửa người dùng</c:when>
        <c:otherwise>Xem chi tiết người dùng</c:otherwise>
      </c:choose>
    </h2>
  </div>


  <form method="post" action="user-admin">
    <div class="card">
      <h3>Thông tin tài khoản</h3>

      <div class="row">
        <div class="col">
          <label>ID</label>
          <input type="text" value="${user.id}" readonly>
        </div>

        <div class="col">
          <label>Username</label>
          <input type="text" name="username"
                 value="${user.username}"
                 <c:if test="${mode != 'add'}">readonly</c:if>>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <label>Email</label>
          <input type="email" name="email"
                 value="${user.email}"
                 <c:if test="${mode != 'add'}">readonly</c:if>>
        </div>

        <div class="col">
          <label>Vai trò</label>
          <select name="role">
            <option value="admin" <c:if test="${user.role == 'admin'}">selected</c:if>>Quản trị</option>
            <option value="customer" <c:if test="${user.role == 'customer'}">selected</c:if>>Khách hàng</option>
          </select>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <label>Trạng thái</label>
          <select name="status">
            <option value="ACTIVE" <c:if test="${user.status == 'ACTIVE'}">selected</c:if>>Hoạt động</option>
            <option value="BLOCKED" <c:if test="${user.status == 'BLOCKED'}">selected</c:if>>Bị khóa</option>
          </select>
        </div>
      </div>
    </div>



    <div class="card">
      <h3>Thông tin cá nhân</h3>

      <div class="row">
        <div class="col">
          <label>Họ tên</label>
          <input type="text" name="full_name" value="${user.fullName}">
        </div>

        <div class="col">
          <label>Số điện thoại</label>
          <input type="text" name="phone" value="${user.phone}">
        </div>
      </div>

      <div class="row">
        <div class="col">
          <label>Giới tính</label>
          <select name="gender">
            <option value="">-- Chọn --</option>
            <option value="male" <c:if test="${user.gender == 'male'}">selected</c:if>>Nam</option>
            <option value="female" <c:if test="${user.gender == 'female'}">selected</c:if>>Nữ</option>
          </select>
        </div>
      </div>

    </div>



    <div class="form-footer">
      <c:if test="${mode != 'view'}">
        <button type="submit" name="action"
                value="${mode == 'add' ? 'create' : 'update'}"
                class="btn-primary">
          Lưu
        </button>
      </c:if>
      <a href="user-admin" class="btn-secondary">Hủy</a>
    </div>


    <input type="hidden" name="id" value="${user.id}">
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