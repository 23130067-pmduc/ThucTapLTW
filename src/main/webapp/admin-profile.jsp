<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Hồ sơ admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-profile.css?v=20260612">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="user">
  <aside class="sidebar">
    <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
    <p>ADMIN</p>

    <div class="nav">
      <c:if test="${userlogin.permissions.contains('DASHBOARD_VIEW')}">
        <a href="${pageContext.request.contextPath}/dashboard" class="nav-item ">
          <i class="fa-solid fa-gauge"></i><span>Tổng quan</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('REPORT_VIEW')}">
        <a href="${pageContext.request.contextPath}/profit-report" class="nav-item">
          <i class="fa-solid fa-chart-line"></i><span>Thống kê</span>
        </a>
      </c:if>


      <c:if test="${userlogin.permissions.contains('PRODUCT_VIEW')}">
        <a href="${pageContext.request.contextPath}/product-admin" class="nav-item">
          <i class="fa-solid fa-shirt"></i><span>Sản phẩm</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('WAREHOUSE_VIEW')}">
        <a href="${pageContext.request.contextPath}/supplier-admin" class="nav-item">
            <i class="fa-solid fa-truck"></i><span>Nhà cung cấp</span>
        </a>
        <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item">
          <i class="fa-solid fa-boxes-stacked"></i><span>Tồn kho</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('RETURN_RECEIPT_VIEW')}">
        <a href="${pageContext.request.contextPath}/return-order-admin" class="nav-item">
          <i class="fa-solid fa-rotate-left"></i><span>Hoàn hàng</span>
        </a>
      </c:if>
      <c:if test="${userlogin.permissions.contains('CATEGORY_VIEW')}">
        <a href="${pageContext.request.contextPath}/category-admin" class="nav-item">
          <i class="fa-solid fa-list"></i><span>Danh mục</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('ORDER_VIEW')}">
        <a href="${pageContext.request.contextPath}/order-admin" class="nav-item">
          <i class="fa-solid fa-receipt"></i><span>Đơn hàng</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('USER_VIEW')}">
        <a href="${pageContext.request.contextPath}/user-admin" class="nav-item">
          <i class="fa-solid fa-users"></i><span>Người dùng</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('PERMISSION_VIEW')}">
        <a href="${pageContext.request.contextPath}/permission-admin" class="nav-item">
          <i class="fa-solid fa-user-shield"></i><span>Phân quyền</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('BANNER_VIEW')}">
        <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item">
          <i class="fa-solid fa-image"></i><span>Banner</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('VOUCHER_VIEW')}">
        <a href="${pageContext.request.contextPath}/voucher-admin" class="nav-item">
          <i class="fa-solid fa-ticket"></i><span>Mã giảm giá</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('PROMOTION_EVENT_VIEW')}">
        <a href="${pageContext.request.contextPath}/promotion-event-admin" class="nav-item">
          <i class="fa-solid fa-tags"></i>
          <span>Khuyến mãi</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('NEWS_VIEW')}">
        <a href="${pageContext.request.contextPath}/news-admin" class="nav-item">
          <i class="fa-solid fa-newspaper"></i><span>Tin tức</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('NOTIFICATION_VIEW')}">
        <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item">
          <i class="fa-solid fa-bell"></i><span>Thông báo</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('CONTACT_VIEW')}">
        <a href="${pageContext.request.contextPath}/contact-admin" class="nav-item">
          <i class="fa-solid fa-envelope"></i><span>Liên hệ</span>
        </a>
      </c:if>

      <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item active">
        <i class="fa-solid fa-user-gear"></i><span>Hồ sơ</span>
      </a>
    </div>
  </aside>

  <section class="content">
    <header class="topbar">
      <div>
        <h1>Hồ sơ admin</h1>
        <p>Quản lý thông tin cá nhân của tài khoản quản trị</p>
      </div>

      <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
    </header>

    <c:if test="${success == 'updated'}">
      <div class="alert alert-success">Cập nhật thông tin admin thành công.</div>
    </c:if>

    <c:if test="${error == 'empty-name'}">
      <div class="alert alert-error">Họ tên không được để trống.</div>
    </c:if>

    <c:if test="${error == 'empty-email'}">
      <div class="alert alert-error">Email không được để trống.</div>
    </c:if>

    <div class="profile-layout">
      <div class="profile-card">
        <div class="avatar-box">
          <div class="avatar-circle">
            <i class="fa fa-user-shield"></i>
          </div>

          <h2>
            <c:choose>
              <c:when test="${not empty admin.fullName}">
                ${admin.fullName}
              </c:when>
              <c:otherwise>
                ${admin.username}
              </c:otherwise>
            </c:choose>
          </h2>

          <p>${admin.email}</p>

          <div class="badge-row">
            <span class="role-badge">Quản trị</span>

            <c:choose>
              <c:when test="${admin.status == 'ACTIVE'}">
                <span class="status-badge active">Hoạt động</span>
              </c:when>
              <c:otherwise>
                <span class="status-badge blocked">Bị khóa</span>
              </c:otherwise>
            </c:choose>
          </div>
        </div>

        <div class="profile-info-list">
          <div class="info-item">
            <span>ID tài khoản</span>
            <strong>#${admin.id}</strong>
          </div>

          <div class="info-item">
            <span>Username</span>
            <strong>${admin.username}</strong>
          </div>

          <div class="info-item">
            <span>Ngày tạo</span>
            <strong>${createdAtFormatted}</strong>
          </div>
        </div>

        <a href="${pageContext.request.contextPath}/doi-mat-khau" class="change-pass-btn">
          <i class="fa fa-lock"></i>
          Đổi mật khẩu
        </a>
      </div>

      <div class="form-card">
        <div class="form-card-header">
          <div>
            <h2>Thông tin cá nhân</h2>
            <p>Cập nhật thông tin liên hệ và hồ sơ quản trị viên</p>
          </div>

          <button type="button" id="btn-edit-profile" class="btn-edit">
            <i class="fa fa-pen"></i>
            Chỉnh sửa
          </button>
        </div>

        <form method="post" action="${pageContext.request.contextPath}/admin-profile" id="adminProfileForm">
          <div class="form-grid">
            <div class="form-group">
              <label for="fullname">Họ và tên</label>
              <input type="text" id="fullname" name="fullname" value="${admin.fullName}" disabled>
            </div>

            <div class="form-group">
              <label for="username">Username</label>
              <input type="text" id="username" value="${admin.username}" disabled>
            </div>

            <div class="form-group">
              <label for="email">Email</label>
              <input type="email" id="email" name="email" value="${admin.email}" disabled>
            </div>

            <div class="form-group">
              <label for="phone">Số điện thoại</label>
              <input type="text" id="phone" name="phone" value="${admin.phone}" disabled>
            </div>

            <div class="form-group">
              <label>Giới tính</label>

              <div class="gender-group">
                <label>
                  <input type="radio" name="gender" value="Nam" ${admin.gender == 'Nam' ? 'checked' : ''} disabled>
                  Nam
                </label>

                <label>
                  <input type="radio" name="gender" value="Nữ" ${admin.gender == 'Nữ' ? 'checked' : ''} disabled>
                  Nữ
                </label>
              </div>
            </div>

            <div class="form-group">
              <label>Vai trò</label>
              <input type="text" value="Quản trị viên" disabled>
            </div>

            <div class="form-group">
              <label>Trạng thái</label>

              <c:choose>
                <c:when test="${admin.status == 'ACTIVE'}">
                  <input type="text" value="Hoạt động" disabled>
                </c:when>
                <c:otherwise>
                  <input type="text" value="Bị khóa" disabled>
                </c:otherwise>
              </c:choose>
            </div>

            <div class="form-group">
              <label>Ngày tạo tài khoản</label>
              <input type="text" value="${createdAtFormatted}" disabled>
            </div>
          </div>

          <div class="form-actions">
            <button type="button" id="btn-cancel-profile" class="btn-cancel">Hủy</button>
            <button type="submit" id="btn-save-profile" class="btn-save">Lưu thay đổi</button>
          </div>
        </form>
      </div>
    </div>
  </section>
</div>

<script src="${pageContext.request.contextPath}/javaScript/admin-profile.js"></script>
</body>
</html>
