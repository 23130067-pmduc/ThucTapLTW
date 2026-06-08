<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin Dashboard</title>
  <link rel="stylesheet" href="css/user.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">


</head>
<body>
<div class="user">
  <aside class="sidebar">
    <img src="img/gau.png" alt="" Logo>
    <p>ADMIN</p>

    <div class="nav">
      <a href="${pageContext.request.contextPath}/dashboard" class="nav-item active"><i class="fa-solid fa-gauge"></i><span>Dashboard</span></a>
      <a href="${pageContext.request.contextPath}/product-admin" class="nav-item"><i class="fa-solid fa-shirt"></i><span>Sản phẩm</span></a>
      <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item"><i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span></a>
      <a href="${pageContext.request.contextPath}/profit-report" class="nav-item"><i class="fa-solid fa-chart-line"></i><span>Lợi nhuận</span></a>
      <a href="${pageContext.request.contextPath}/category-admin" class="nav-item"><i class="fa-solid fa-list"></i><span>Danh mục</span></a>
      <a href="${pageContext.request.contextPath}/order-admin" class="nav-item"><i class="fa-solid fa-receipt"></i><span>Đơn hàng</span></a>
            <a href="${pageContext.request.contextPath}/return-order-admin" class="nav-item"><i class="fa-solid fa-rotate-left"></i><span>Hoàn hàng</span></a>
      <a href="${pageContext.request.contextPath}/user-admin" class="nav-item"><i class="fa-solid fa-users"></i><span>Người dùng</span></a>
      <a href="${pageContext.request.contextPath}/voucher-admin" class="nav-item"><i class="fa-solid fa-ticket"></i><span>Mã giảm giá</span></a>
      <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item"><i class="fa-solid fa-image"></i><span>Banner</span></a>
      <a href="${pageContext.request.contextPath}/news-admin" class="nav-item"><i class="fa-solid fa-newspaper"></i><span>Tin tức</span></a>
      <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item"><i class="fa-solid fa-bell"></i><span>Thông báo</span></a>
      <a href="${pageContext.request.contextPath}/contact-admin" class="nav-item"><i class="fa-solid fa-envelope"></i><span>Liên hệ</span></a>
      <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item"><i class="fa-solid fa-user-gear"></i><span>Hồ sơ</span></a>
    </div>
  </aside>

  <section class="content">
    <!-- PHẦN HEADER -->
    <header class="topbar">
      <h1 id="pageTitle">Dashboard</h1>
      <div class="actions">
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
      </div>
    </header>



    <main id="page">
      <!-- DASHBROAD -->
      <section id="dashboard" class="page active">
        <div class="cards">
          <div class="card">
            Tổng đơn hàng
            <span>${totalOrders}</span>
          </div>

          <div class="card">
            Doanh thu
            <span>
                            <fmt:formatNumber value="${totalRevenue}"/>đ
                        </span>
          </div>

          <div class="card">
            Sản phẩm
            <span>${totalProducts}</span>
          </div>

          <div class="card">
            Người dùng
            <span>${totalUsers}</span>
          </div>
        </div>

        <h2 style="margin-bottom: 12px;">Đơn hàng mới nhất</h2>

        <div class="user-table-wrapper">
          <table class="order-table">
            <tr>
              <th>Mã</th>
              <th>Người nhận</th>
              <th>Tổng tiền</th>
              <th>Trạng thái</th>
            </tr>

            <c:forEach items="${latestOrders}" var="o">
              <tr>
                <td>#${o.id}</td>
                <td>${o.receiverName}</td>
                <td>
                  <fmt:formatNumber value="${o.totalPrice}"/>đ
                </td>
                <td>
                                    <span class="order-status ${o.orderStatus}">
                                        <c:choose>
                                          <c:when test="${o.orderStatus == 'PENDING'}">Chờ xử lý</c:when>
                                          <c:when test="${o.orderStatus == 'SHIPPING'}">Đang giao</c:when>
                                          <c:when test="${o.orderStatus == 'COMPLETED'}">Hoàn thành</c:when>
                                          <c:when test="${o.orderStatus == 'CANCELLED'}">Đã hủy</c:when>
                                          <c:otherwise>${o.orderStatus}</c:otherwise>
                                        </c:choose>
                                    </span>
                </td>
              </tr>
            </c:forEach>
          </table>
        </div>

      </section>

    </main>
  </section>
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
