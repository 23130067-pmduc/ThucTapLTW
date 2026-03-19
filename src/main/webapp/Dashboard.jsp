<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
      <a href="dashboard" class="nav-item active">Dashboard</a>
      <a href="product-admin" class="nav-item">Sản phẩm</a>
      <a href="category-admin" class="nav-item">Danh mục</a>
      <a href="order-admin" class="nav-item">Đơn hàng</a>
      <a href="user-admin" class="nav-item">Người dùng</a>
      <a href="banner-admin" class="nav-item">Banner</a>
      <a href="tin-tuc" class="nav-item">Tin tức</a>
      <a href="contact-admin" class="nav-item">Liên hệ</a>
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
