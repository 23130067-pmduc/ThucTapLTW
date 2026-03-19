<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn hàng</title>
    <link rel="stylesheet" href="css/user.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">


</head>
<body>
<div class="user">
    <aside class="sidebar">
        <img src="img/quanao/gau.png" alt="" Logo>
        <p>ADMIN</p>

        <div class="nav" id="menu">
            <a href="dashboard" class="nav-item">Dashboard</a>
            <a href="product-admin" class="nav-item">Sản phẩm</a>
            <a href="category-admin" class="nav-item">Danh mục</a>
            <a href="order-admin" class="nav-item active">Đơn hàng</a>
            <a href="user-admin" class="nav-item">Người dùng</a>
            <a href="banner-admin" class="nav-item">Banner</a>
            <a href="tin-tuc" class="nav-item">Tin tức</a>
            <a href="contact-admin" class="nav-item">Liên hệ</a>
        </div>
    </aside>

    <section class="content">
        <header class="topbar">
            <h1 id="pageTitle">Đơn hàng</h1>
            <div class="actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>



        <main id="page">
            <section id="dashboard" class="page active">
                <div class="cards">
                    <div class="card">Tổng đơn<br>${total}</div>
                    <div class="card">Đang xử lý<br>${countPending}</div>
                    <div class="card">Hoàn thành<br>${countCompleted}</div>
                </div>

                <div class="user-table-wrapper">
                    <table class="order-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Khách hàng</th>
                            <th>Tổng tiền</th>
                            <th>Trạng thái</th>
                            <th>Ngày tạo</th>
                            <th>Hành động</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${empty orders}">
                            <tr>
                                <td colspan="6" style="text-align:center">
                                    Chưa có đơn hàng
                                </td>
                            </tr>
                        </c:if>

                        <c:forEach items="${orders}" var="o">
                            <tr>
                                <td>#${o.id}</td>
                                <td>${o.receiverName}</td>
                                <td>${o.finalAmount} đ</td>
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

                                <td>
                                        ${o.createdAtFormatted}
                                </td>
                                <td>
                                    <a href="order-admin?mode=view&id=${o.id}" class="icon-btn view">
                                        <i class="fa fa-eye"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
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
