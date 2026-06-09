<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Quản lý thông báo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/notification-admin.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="user">
  <aside class="sidebar">
    <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
    <p>ADMIN</p>

    <div class="nav">
      <div class="nav" id="menu">

        <c:if test="${userlogin.permissions.contains('DASHBOARD_VIEW')}">
          <a href="${pageContext.request.contextPath}/dashboard" class="nav-item ">
            <i class="fa-solid fa-gauge"></i><span>Dashboard</span>
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

        <c:if test="${userlogin.permissions.contains('RETURN_RECEIPT_CREATE')}">
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

        <c:if test="${userlogin.permissions.contains('BANNER_VIEW')}">
          <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item">
            <i class="fa-solid fa-image"></i><span>Banner</span>
          </a>
        </c:if>

        <c:if test="${userlogin.permissions.contains('NEWS_VIEW')}">
          <a href="${pageContext.request.contextPath}/news-admin" class="nav-item">
            <i class="fa-solid fa-newspaper"></i><span>Tin tức</span>
          </a>
        </c:if>

        <c:if test="${userlogin.permissions.contains('NOTIFICATION_VIEW')}">
          <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item active">
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
    </div>
  </aside>

  <section class="content">
    <header class="topbar">
      <h1>Quản lý thông báo</h1>
      <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
    </header>

    <c:if test="${not empty success}">
      <div class="alert alert-success">${success}</div>
    </c:if>

    <c:if test="${not empty error}">
      <div class="alert alert-error">${error}</div>
    </c:if>

    <section class="cards">
      <div class="card">
        Tổng thông báo
        <br>
        <span>${total}</span>
      </div>
      <div class="card">
        Đang hiển thị
        <br>
        <span>${totalActive}</span>
      </div>
      <div class="card">
        Gửi tất cả user
        <br>
        <span>${totalAllTarget}</span>
      </div>
      <div class="card">
        Gửi riêng user
        <br>
        <span>${totalUserTarget}</span>
      </div>
    </section>

    <div class="notification-toolbar">
      <form method="get" action="${pageContext.request.contextPath}/notification-admin" class="notification-search-form">
        <input type="text" name="keyword" value="${keyword}" placeholder="Tìm theo tiêu đề, nội dung...">
        <button type="submit" class="btn-search">
          <i class="fa fa-search"></i> Tìm
        </button>
      </form>

      <a href="${pageContext.request.contextPath}/notification-admin?mode=add" class="btn-add">
        <i class="fa fa-plus"></i> Thêm thông báo
      </a>
    </div>

    <div class="notification-table-wrapper">
      <table class="notification-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Tiêu đề</th>
          <th>Loại</th>
          <th>Đối tượng</th>
          <th>Link</th>
          <th>Ngày tạo</th>
          <th>Trạng thái</th>
          <th>Hành động</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
          <c:when test="${empty notificationList}">
            <tr>
              <td colspan="8" class="empty-row">Chưa có thông báo nào.</td>
            </tr>
          </c:when>
          <c:otherwise>
            <c:forEach items="${notificationList}" var="n">
              <tr>
                <td>${n.id}</td>
                <td class="notification-title-cell">${n.title}</td>
                <td>${n.type}</td>
                <td>
                  <c:choose>
                    <c:when test="${n.target_type == 'ALL'}">Tất cả user</c:when>
                    <c:otherwise>User ID: ${n.target_user_id}</c:otherwise>
                  </c:choose>
                </td>
                <td>${n.link}</td>
                <td>
                  <fmt:formatDate value="${n.created_at}" pattern="dd/MM/yyyy"/>
                </td>
                <td>
                  <c:choose>
                    <c:when test="${n.is_active == 1}">
                      <span class="status active">Hiển thị</span>
                    </c:when>
                    <c:otherwise>
                      <span class="status blocked">Ẩn</span>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <div class="actions">
                    <a href="${pageContext.request.contextPath}/notification-admin?mode=view&id=${n.id}"
                       class="icon-btn view"
                       title="Xem">
                      <i class="fa fa-eye"></i>
                    </a>

                    <a href="${pageContext.request.contextPath}/notification-admin?mode=edit&id=${n.id}"
                       class="icon-btn edit"
                       title="Sửa">
                      <i class="fa fa-pen"></i>
                    </a>

                    <button type="button"
                            class="icon-btn delete open-confirm-btn"
                            title="Khóa"
                            data-id="${n.id}">
                      <i class="fa fa-trash"></i>
                    </button>
                  </div>
                </td>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>
        </tbody>
      </table>
    </div>
  </section>
</div>

<div id="confirmModal" class="modal-overlay">
  <div class="modal">
    <h3>Xác nhận khóa thông báo</h3>
    <p>Bạn có chắc muốn khóa thông báo này không?</p>

    <form method="post" action="${pageContext.request.contextPath}/notification-admin">
      <input type="hidden" name="action" value="delete">
      <input type="hidden" name="id" id="confirmNotificationId">

      <div class="modal-actions">
        <button type="button" class="btn-secondary" id="closeConfirmModal">Hủy</button>
        <button type="submit" class="btn-danger">Đồng ý</button>
      </div>
    </form>
  </div>
</div>

<script src="${pageContext.request.contextPath}/javaScript/notification-admin.js"></script>
</body>
</html>
