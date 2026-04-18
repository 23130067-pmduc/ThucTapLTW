<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>User</title>
  <link rel="stylesheet" href="./css/user-admin.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">


</head>
<body>
<div class="user">
  <aside class="sidebar">
    <img src="img/gau.png" alt="" Logo>
    <p>ADMIN</p>

    <div class="nav">
      <a href="${pageContext.request.contextPath}/dashboard" class="nav-item">Dashboard</a>
      <a href="${pageContext.request.contextPath}/product-admin" class="nav-item">Sản phẩm</a>
      <a href="${pageContext.request.contextPath}/category-admin" class="nav-item">Danh mục</a>
      <a href="${pageContext.request.contextPath}/order-admin" class="nav-item">Đơn hàng</a>
      <a href="${pageContext.request.contextPath}/user-admin" class="nav-item active">Người dùng</a>
      <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item">Banner</a>
      <a href="${pageContext.request.contextPath}/news-admin" class="nav-item">Tin tức</a>
      <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item">Thông báo</a>
      <a href="${pageContext.request.contextPath}/contact-admin" class="nav-item">Liên hệ</a>
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
          <form method="get" action="user-admin" class="user-toolbar">
            <input
                    type="text"
                    name="keyword"
                    value="${param.keyword}"
                    placeholder="Tìm theo username, email..."
            >

            <button type="submit" class="btn-search">
              <i class="fa fa-search"></i> Tìm
            </button>
          </form>

          <a href="user-admin?mode=add" class="btn-add">
            <i class="fa fa-plus"></i> Thêm người dùng
          </a>
        </div>


        <div class="user-table-wrapper">
          <!-- TABLE USER -->
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
                    <c:when test="${u.role == 'admin'}">
                      Quản trị
                    </c:when>
                    <c:otherwise>
                      Khách hàng
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

                  <a href="user-admin?mode=edit&id=${u.id}"
                     class="icon-btn edit" title="Chỉnh sửa">
                    <i class="fa fa-pen"></i>
                  </a>


                  <c:if test="${u.status == 'ACTIVE'}">
                    <button type="button"
                            class="icon-btn delete"
                            title="Khóa người dùng"
                            onclick="openConfirmModal(${u.id})">
                      <i class="fa fa-trash"></i>
                    </button>
                  </c:if>

                </td>
              </tr>
            </c:forEach>

            </tbody>
          </table>
        </div>

      </section>

    </main>
  </section>

  <div id="confirmModal" class="modal-overlay">
    <div class="modal">
      <h3>Xác nhận</h3>
      <p>Bạn có chắc muốn <b>khóa người dùng</b> này không?</p>

      <form id="confirmForm" method="post" action="user-admin">
        <input type="hidden" name="action" value="block">
        <input type="hidden" name="id" id="confirmUserId">

        <div class="modal-actions">
          <button type="button" class="btn-secondary" onclick="closeModal()">Hủy</button>
          <button type="submit" class="btn-danger">Khóa</button>
        </div>
      </form>
    </div>
  </div>

</div>


</body>
<script>
  function openConfirmModal(userId) {
    document.getElementById("confirmUserId").value = userId;
    document.getElementById("confirmModal").style.display = "flex";
  }

  function closeModal() {
    document.getElementById("confirmModal").style.display = "none";
  }
</script>

</html>