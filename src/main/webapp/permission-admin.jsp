<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Quản lý phân quyền</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/permission-admin.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>

<div class="user">
  <aside class="sidebar">
    <img src="img/gau.png" alt="Logo">
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
        <a href="${pageContext.request.contextPath}/permission-admin" class="nav-item active">
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

    </div>
  </aside>

  <section class="content">

    <header class="topbar">
      <div>
        <h1 id="pageTitle">Quản lý phân quyền</h1>
      </div>

      <div class="actions">
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
      </div>
    </header>

    <main id="page">
      <section id="permissionPage" class="page active">

        <div class="permission-layout">

          <div class="role-card">
            <div class="card-header">
              <div>
                <h2>Vai trò</h2>
              </div>
              <span class="count-badge">${countRoles} vai trò</span>
            </div>

            <div class="role-list">
              <c:forEach items="${roles}" var="role">
                <a href="${pageContext.request.contextPath}/permission-admin?roleId=${role.id}"
                   class="role-item ${role.id == selectedRoleId ? 'active' : ''}">
                  <div>
                    <strong>${role.description}</strong>
                  </div>
                  <i class="fa-solid fa-chevron-right"></i>
                </a>
              </c:forEach>
            </div>
          </div>

          <div class="permission-card-wrapper">
            <form action="permission-admin" method="post" id="permissionForm">

              <input type="hidden" name="roleId" value="${selectedRoleId}">

              <div class="card-header permission-header">
                <div>
                  <h2>Danh sách quyền</h2>
                  <p>Vai trò đang chọn: <strong id="selectedRoleName">${selectedRoleDescription}</strong></p>
                </div>

                <c:if test="${userlogin.permissions.contains('PERMISSION_UPDATE')}">
                  <button type="submit" class="btn-save">
                    <i class="fa-solid fa-floppy-disk"></i>
                    Cập nhật quyền
                  </button>
                </c:if>
              </div>

              <c:forEach items="${permissionGroups}" var="group">
                <div class="permission-group">
                  <div class="group-title">
                    <span>${group.key}</span>
                  </div>

                  <div class="permission-grid">
                    <c:forEach items="${group.value}" var="permission">
                      <label class="permission-item">
                        <input type="checkbox"
                               name="permissions"
                               value="${permission.id}"
                          ${selectedRolePermissions.contains(permission.name) ? 'checked' : ''}>
                        <div>
                          <strong>${permission.description}</strong>
                        </div>
                      </label>
                    </c:forEach>
                  </div>
                </div>
              </c:forEach>

            </form>
          </div>

        </div>

      </section>
    </main>

  </section>
</div>


</body>
</html>