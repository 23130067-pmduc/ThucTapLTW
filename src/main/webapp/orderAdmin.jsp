<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn hàng</title>

    <link rel="stylesheet" href="css/user.css">
    <link rel="stylesheet" href="css/order.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>

<body class="order-page">
<div class="user">
    <aside class="sidebar">
        <img src="img/gau.png" alt="Logo">
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

    <section class="content order-page">
        <header class="topbar">
            <h1>Đơn hàng</h1>
            <div class="actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>

        <main id="page">
            <section id="dashboard" class="page active">

                <div class="cards">
                    <div class="card">
                        Tổng đơn
                        <span>${total}</span>
                    </div>
                    <div class="card">
                        Chờ xử lý
                        <span>${countPending}</span>
                    </div>
                    <div class="card">
                        Đang giao
                        <span>${countShipping}</span>
                    </div>
                    <div class="card">
                        Hoàn thành
                        <span>${countCompleted}</span>
                    </div>
                    <div class="card">
                        Đã hủy
                        <span>${countCancelled}</span>
                    </div>
                </div>

                <div class="order-toolbar">
                    <form action="order-admin" method="get" class="order-search-form">
                        <input type="text" name="keyword" class="order-search-input"
                               placeholder="Tìm mã đơn, tên khách hàng..."
                               value="${param.keyword}">

                        <select name="status" class="order-filter-select">
                            <option value="">Tất cả trạng thái</option>
                            <option value="PENDING" ${param.status == 'PENDING' ? 'selected' : ''}>Chờ xử lý</option>
                            <option value="SHIPPING" ${param.status == 'SHIPPING' ? 'selected' : ''}>Đang giao</option>
                            <option value="COMPLETED" ${param.status == 'COMPLETED' ? 'selected' : ''}>Hoàn thành</option>
                            <option value="CANCELLED" ${param.status == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
                        </select>

                        <button type="submit" class="btn-search">
                            <i class="fa fa-search"></i> Tìm
                        </button>

                        <button type="button" class="btn-reset"
                                onclick="window.location.href='order-admin'">
                            Làm mới
                        </button>
                    </form>
                </div>

                <div class="order-table-wrapper">
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
                                <td colspan="6" style="text-align: center;">Chưa có đơn hàng</td>
                            </tr>
                        </c:if>

                        <c:forEach items="${orders}" var="o">
                            <tr>
                                <td>#${o.id}</td>
                                <td>${o.receiverName}</td>
                                <td><fmt:formatNumber value="${o.finalAmount}" pattern="#,##0" /> đ</td>
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
                                <td>${o.createdAtFormatted}</td>
                                <td>
                                    <div class="order-actions">
                                        <a href="order-admin?mode=view&id=${o.id}" class="icon-btn view">
                                            <i class="fa fa-eye"></i>
                                        </a>
                                    </div>
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
</html>