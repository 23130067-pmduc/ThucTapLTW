<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn hàng</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>

<body class="order-page">
<div class="user">
    <aside class="sidebar">
        <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
        <p>ADMIN</p>

        <div class="nav">
            <a href="${pageContext.request.contextPath}/dashboard" class="nav-item"><i class="fa-solid fa-gauge"></i><span>Dashboard</span></a>
            <a href="${pageContext.request.contextPath}/product-admin" class="nav-item"><i class="fa-solid fa-shirt"></i><span>Sản phẩm</span></a>
            <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item"><i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span></a>
            <a href="${pageContext.request.contextPath}/profit-report" class="nav-item"><i class="fa-solid fa-chart-line"></i><span>Lợi nhuận</span></a>
            <a href="${pageContext.request.contextPath}/category-admin" class="nav-item"><i class="fa-solid fa-list"></i><span>Danh mục</span></a>
            <a href="${pageContext.request.contextPath}/order-admin" class="nav-item active"><i class="fa-solid fa-receipt"></i><span>Đơn hàng</span></a>
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

    <section class="content order-page">
        <header class="topbar">
            <h1>Đơn hàng</h1>
            <div class="topbar-actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>

        <div class="cards five-cards">
            <div class="card">Tổng đơn <span>${total}</span></div>
            <div class="card">Chờ xử lý <span>${countPending}</span></div>
            <div class="card">Đang giao <span>${countShipping}</span></div>
            <div class="card">Hoàn thành <span>${countCompleted}</span></div>
            <div class="card">Đã hủy <span>${countCancelled}</span></div>
        </div>

        <div class="order-toolbar">
            <form action="${pageContext.request.contextPath}/order-admin" method="get" class="order-search-form">
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

                <a class="btn-reset" href="${pageContext.request.contextPath}/order-admin">Làm mới</a>
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
                        <td colspan="6" class="order-empty">Chưa có đơn hàng</td>
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
                                    <c:when test="${o.orderStatus eq 'PENDING'}">Chờ xử lý</c:when>
                                    <c:when test="${o.orderStatus eq 'SHIPPING'}">Đang giao</c:when>
                                    <c:when test="${o.orderStatus eq 'COMPLETED'}">Hoàn thành</c:when>
                                    <c:when test="${o.orderStatus eq 'CANCELLED'}">Đã hủy</c:when>
                                    <c:otherwise>${o.orderStatus}</c:otherwise>
                                </c:choose>
                            </span>
                        </td>
                        <td class="order-date">
                                ${fn:substring(o.createdAtFormatted, 0, 10)}
                        </td>
                        <td>
                            <div class="admin-actions">
                                <a href="${pageContext.request.contextPath}/order-admin?mode=view&id=${o.id}"
                                   class="icon-btn view" title="Xem chi tiết">
                                    <i class="fa-solid fa-eye"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/order-admin?mode=edit&id=${o.id}"
                                   class="icon-btn edit" title="Cập nhật trạng thái">
                                    <i class="fa-solid fa-pen"></i>
                                </a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>
</div>
</body>
</html>
