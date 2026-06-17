<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Người dùng</title>
  <link rel="stylesheet" href="./css/user-admin.css">
  <link rel="stylesheet" href="css/pagination.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">


</head>
<body>
<div class="user">
  <aside class="sidebar">
    <img src="img/gau.png" alt="Logo">
    <p>ADMIN</p>


    <div class="nav" id="menu">

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
        <a href="${pageContext.request.contextPath}/user-admin" class="nav-item active">
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

      <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item">
        <i class="fa-solid fa-user-gear"></i><span>Hồ sơ</span>
      </a>

    </div>
  </aside>

  <section class="content">

    <header class="topbar">
      <h1 id="pageTitle">Người dùng</h1>
      <div class="actions">
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
      </div>
    </header>


    <main id="page">
      <section id="dashboard" class="page active">
        <div class="cards">
          <div class="card">Tổng người dùng<br><span id="dashboard-total-user">${total}</span></div>
          <div class="card">Người dùng mới / tuần<br><span id="dashboard-total-user-in-week">${countInWeek}</span></div>
          <div class="card">Hoạt động<br><span id="dashboard-total-user-active">${countActive}</span></div>
          <div class="card">Bị khóa<br><span id="dashboard-total-user-block">${countBlock}</span></div>
        </div>

        <div class="user-toolbar">
          <form action="user-admin" method="get" class="user-search-form" id="userSearchForm">
            <div class="search-box">
              <input type="text"
                     name="keyword"
                     id="keywordInput"
                     placeholder="Tìm theo username, email, họ tên..."
                     value="${keyword}">
            </div>

            <select name="role" id="roleFilter" class="filter-select">
              <option value="">Tất cả vai trò</option>
              <option value="ADMIN" ${role == 'ADMIN' ? 'selected' : ''}>Quản trị</option>
              <option value="CUSTOMER" ${role == 'CUSTOMER' ? 'selected' : ''}>Khách hàng</option>
              <option value="STAFF_ORDER" ${role == 'STAFF_ORDER' ? 'selected' : ''}>Nhân viên quản lý đơn hàng</option>
              <option value="STAFF_PRODUCT" ${role == 'STAFF_PRODUCT' ? 'selected' : ''}>Nhân viên quản lý sản phẩm</option>
              <option value="STAFF_MARKETING" ${role == 'STAFF_MARKETING' ? 'selected' : ''}>Nhân viên marketing</option>
            </select>

            <select name="status" id="statusFilter" class="filter-select">
              <option value="">Tất cả trạng thái</option>
              <option value="ACTIVE" ${status == 'ACTIVE' ? 'selected' : ''}>Hoạt động</option>
              <option value="BLOCKED" ${status == 'BLOCKED' ? 'selected' : ''}>Bị khóa</option>
            </select>

            <button type="button" class="btn-search" id="searchBtn">
              <i class="fa-solid fa-magnifying-glass"></i>
              <span>Tìm</span>
            </button>
          </form>

          <c:if test="${userlogin.permissions.contains('USER_CREATE')}">
            <a href="user-admin?mode=add" class="btn-add">
              <i class="fa fa-plus"></i> Thêm người dùng
            </a>
          </c:if>
        </div>


        <div class="user-table-wrapper">
          <table class="user-table">
            <thead>
            <tr>
              <th>ID</th>
              <th>Username</th>
              <th>Họ tên</th>
              <th>Email</th>
              <th>Vai trò</th>
              <th>Trạng thái</th>
              <th>Hành động</th>
            </tr>
            </thead>
            <tbody id="userTableBody">

            <c:forEach items="${users}" var="u">
              <tr>
                <td>${u.id}</td>
                <td>${u.username}</td>
                <td>${u.fullName}</td>
                <td>${u.email}</td>
                <td>
                  <c:choose>
                    <c:when test="${u.roleName == 'ADMIN'}">
                      <span class="role-badge role-admin">Quản trị viên</span>
                    </c:when>

                    <c:when test="${u.roleName == 'STAFF_PRODUCT'}">
                      <span class="role-badge role-product">Nhân viên quản lý sản phẩm</span>
                    </c:when>

                    <c:when test="${u.roleName == 'STAFF_ORDER'}">
                      <span class="role-badge role-order">Nhân viên quản lý đơn hàng</span>
                    </c:when>

                    <c:when test="${u.roleName == 'STAFF_MARKETING'}">
                      <span class="role-badge role-marketing">Nhân viên marketing</span>
                    </c:when>

                    <c:otherwise>
                      <span class="role-badge role-customer">Khách hàng</span>
                    </c:otherwise>
                  </c:choose>
                </td>

                <td><span class="status active">
                                    <c:choose>
                                      <c:when test="${u.status == 'ACTIVE'}">
                                        <span class="status active">Hoạt động</span>
                                      </c:when>
                                      <c:otherwise>
                                        <span class="status blocked">Bị khóa</span>
                                      </c:otherwise>
                                    </c:choose>
                                </span></td>
                <td class="actions">
                  <a href="user-admin?mode=view&id=${u.id}"
                     class="icon-btn view" title="Xem chi tiết">
                    <i class="fa fa-eye"></i>
                  </a>


                  <c:if test="${userlogin.permissions.contains('USER_UPDATE')}">
                    <a href="user-admin?mode=edit&id=${u.id}"
                       class="icon-btn edit" title="Chỉnh sửa">
                      <i class="fa fa-pen"></i>
                    </a>
                  </c:if>

                  <c:if test="${userlogin.permissions.contains('USER_LOCK')}">
                    <c:choose>
                      <c:when test="${u.status == 'ACTIVE'}">
                        <button type="button"
                                class="icon-btn delete"
                                title="Khóa người dùng"
                                onclick="openStatusModal(${u.id}, 'BLOCKED', 'khóa')">
                          <i class="fa-solid fa-lock"></i>
                        </button>
                      </c:when>

                      <c:otherwise>
                        <button type="button"
                                class="icon-btn unlock"
                                title="Mở khóa người dùng"
                                onclick="openStatusModal(${u.id}, 'ACTIVE', 'mở khóa')">
                          <i class="fa-solid fa-lock-open"></i>
                        </button>
                      </c:otherwise>
                    </c:choose>
                  </c:if>

                </td>
              </tr>
            </c:forEach>

            </tbody>
          </table>

          <c:set var="startPage" value="${currentPage - 2}" />
          <c:set var="endPage" value="${currentPage + 2}" />

          <c:if test="${startPage < 1}">
            <c:set var="startPage" value="1" />
          </c:if>

          <c:if test="${endPage > totalPages}">
            <c:set var="endPage" value="${totalPages}" />
          </c:if>

          <c:if test="${totalPages > 1}">
            <div class="pagination" id="userPagination">

              <c:if test="${currentPage > 1}">
                <a class="page-btn" href="user-admin?page=${currentPage - 1}">
                  Trước
                </a>
              </c:if>

              <c:forEach begin="${startPage}" end="${endPage}" var="i">
                <a href="user-admin?page=${i}"
                   class="page-btn ${i == currentPage ? 'active' : ''}">
                    ${i}
                </a>
              </c:forEach>

              <c:if test="${currentPage < totalPages}">
                <a class="page-btn" href="user-admin?page=${currentPage + 1}">
                  Sau
                </a>
              </c:if>

            </div>
          </c:if>
        </div>

      </section>

    </main>
  </section>

  <div id="statusModal" class="modal-overlay">
    <div class="modal">
      <h3 id="statusModalTitle">Xác nhận</h3>
      <p id="statusMessage"></p>

      <form id="statusForm" method="post" action="user-admin">
        <input type="hidden" name="action" value="changeStatus">
        <input type="hidden" name="id" id="statusUserId">
        <input type="hidden" name="status" id="statusUserValue">

        <div class="modal-actions">
          <button type="button" class="btn-secondary" onclick="closeStatusModal()">Hủy</button>
          <button type="submit" class="btn-danger" id="statusSubmitBtn">Xác nhận</button>
        </div>
      </form>
    </div>
  </div>

</div>


</body>
<script>
  function openStatusModal(userId, status, actionText) {
    document.getElementById("statusUserId").value = userId;
    document.getElementById("statusUserValue").value = status;

    document.getElementById("statusModalTitle").innerText =
            actionText === "khóa" ? "Xác nhận khóa" : "Xác nhận mở khóa";

    document.getElementById("statusMessage").innerHTML =
            "Bạn có chắc muốn <b>" + actionText + " người dùng</b> này không?";

    document.getElementById("statusSubmitBtn").innerText =
            actionText === "khóa" ? "Khóa" : "Mở khóa";

    document.getElementById("statusModal").style.display = "flex";
  }

  function closeStatusModal() {
    document.getElementById("statusModal").style.display = "none";
  }
</script>
<script>
  window.userPermissions = {
    canUpdateUser: ${userlogin.permissions.contains('USER_UPDATE')},
    canLockUser: ${userlogin.permissions.contains('USER_LOCK')}
  };
</script>
<script src="javaScript/searchUser.js"></script>
</html>
