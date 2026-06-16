<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Cài đặt admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adminsettings.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="user">
  <aside class="sidebar">
    <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
    <p>ADMIN</p>

    <div class="nav" id="menu">
      <c:if test="${userlogin.permissions.contains('DASHBOARD_VIEW')}">
        <a href="${pageContext.request.contextPath}/dashboard" class="nav-item">
          <i class="fa-solid fa-gauge"></i><span>Thống kê</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('PRODUCT_VIEW')}">
        <a href="${pageContext.request.contextPath}/product-admin" class="nav-item">
          <i class="fa-solid fa-shirt"></i><span>Sản phẩm</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('WAREHOUSE_VIEW')}">
        <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item">
          <i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('RETURN_RECEIPT_VIEW')}">
        <a href="${pageContext.request.contextPath}/return-order-admin" class="nav-item">
          <i class="fa-solid fa-rotate-left"></i><span>Hoàn hàng</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('REPORT_VIEW')}">
        <a href="${pageContext.request.contextPath}/profit-report" class="nav-item">
          <i class="fa-solid fa-chart-line"></i><span>Lợi nhuận</span>
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
          <i class="fa-solid fa-tags"></i><span>Khuyến mãi</span>
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

      <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item">
        <i class="fa-solid fa-user-gear"></i><span>Hồ sơ</span>
      </a>

      <c:if test="${userlogin.permissions.contains('PERMISSION_VIEW')}">
        <a href="${pageContext.request.contextPath}/admin-settings" class="nav-item active">
          <i class="fa-solid fa-gear"></i><span>Cài đặt</span>
        </a>
      </c:if>
    </div>
  </aside>

  <section class="content">
    <header class="topbar">
      <div>
        <h1>Cài đặt</h1>
        <p>Quản lý các tùy chọn quản trị hệ thống.</p>
      </div>
      <div class="actions">
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
      </div>
    </header>

    <main>
      <c:if test="${param.success == 'deleted'}">
        <div class="alert alert-success">Đã xóa tài khoản admin.</div>
      </c:if>
      <c:if test="${param.error == 'selfDelete'}">
        <div class="alert alert-error">Không thể xóa tài khoản đang đăng nhập.</div>
      </c:if>
      <c:if test="${param.error == 'lastAdmin'}">
        <div class="alert alert-error">Không thể xóa admin cuối cùng của hệ thống.</div>
      </c:if>
      <c:if test="${param.error == 'notAdmin'}">
        <div class="alert alert-error">Tài khoản này không phải admin.</div>
      </c:if>
      <c:if test="${param.error == 'deleteFailed' || param.error == 'invalidAdmin' || param.error == 'invalidAction'}">
        <div class="alert alert-error">Không thể xóa tài khoản admin. Vui lòng thử lại.</div>
      </c:if>

      <section class="settings-panel">
        <div class="panel-header">
          <div>
            <h2>Tài khoản admin</h2>
            <p>Chức năng đầu tiên của Cài đặt: xóa tài khoản có vai trò ADMIN.</p>
          </div>
          <span class="count-badge">${adminCount} admin</span>
        </div>

        <div class="admin-table-wrapper">
          <table class="admin-table">
            <thead>
            <tr>
              <th>ID</th>
              <th>Username</th>
              <th>Họ tên</th>
              <th>Email</th>
              <th>Trạng thái</th>
              <th>Hành động</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
              <c:when test="${empty adminUsers}">
                <tr>
                  <td colspan="6" class="empty-row">Chưa có tài khoản admin nào.</td>
                </tr>
              </c:when>
              <c:otherwise>
                <c:forEach items="${adminUsers}" var="admin">
                  <tr>
                    <td>#${admin.id}</td>
                    <td class="admin-username">${admin.username}</td>
                    <td>${admin.fullName}</td>
                    <td>${admin.email}</td>
                    <td>
                      <span class="status ${admin.status == 'ACTIVE' ? 'active' : 'blocked'}">
                        <c:choose>
                          <c:when test="${admin.status == 'ACTIVE'}">Hoạt động</c:when>
                          <c:when test="${admin.status == 'BLOCKED'}">Bị khóa</c:when>
                          <c:otherwise>${admin.status}</c:otherwise>
                        </c:choose>
                      </span>
                    </td>
                    <td>
                      <c:choose>
                        <c:when test="${admin.id == userlogin.id}">
                          <span class="muted-action">Đang đăng nhập</span>
                        </c:when>
                        <c:otherwise>
                          <button type="button"
                                  class="icon-btn delete"
                                  title="Xóa admin"
                                  data-admin-id="${admin.id}"
                                  onclick="openDeleteModal(this)">
                            <i class="fa-solid fa-trash"></i>
                          </button>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
            </tbody>
          </table>
        </div>
      </section>
    </main>
  </section>
</div>

<div id="deleteAdminModal" class="modal-overlay">
  <div class="modal">
    <h3>Xác nhận xóa</h3>
    <p>Bạn có chắc muốn xóa tài khoản admin <strong id="deleteAdminName"></strong> khỏi Cài đặt không?</p>
    <form method="post" action="${pageContext.request.contextPath}/admin-settings">
      <input type="hidden" name="action" value="deleteAdmin">
      <input type="hidden" name="id" id="deleteAdminId">

      <div class="modal-actions">
        <button type="button" class="btn-secondary" onclick="closeDeleteModal()">Hủy</button>
        <button type="submit" class="btn-danger">Xóa</button>
      </div>
    </form>
  </div>
</div>

<script src="${pageContext.request.contextPath}/javaScript/adminsettings.js"></script>
</body>
</html>
