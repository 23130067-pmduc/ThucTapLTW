<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hoàn hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/return-order.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="return-admin-page">
<div class="user">
    <aside class="sidebar">
        <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
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
                <a href="${pageContext.request.contextPath}/return-order-admin" class="nav-item active">
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

    <section class="content return-page-content">
        <header class="topbar">
            <h1>Hoàn hàng</h1>
            <div class="topbar-actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>

        <c:if test="${not empty success}"><div class="return-alert success">${success}</div></c:if>
        <c:if test="${not empty error}"><div class="return-alert error">${error}</div></c:if>

        <div class="cards five-cards">
            <div class="card">Tổng phiếu <span>${total}</span></div>
            <div class="card">Chờ duyệt <span>${countPending}</span></div>
            <div class="card">Đã duyệt <span>${countApproved}</span></div>
            <div class="card">Đã từ chối <span>${countRejected}</span></div>
            <div class="card">Đã nhập kho <span>${countCompleted}</span></div>
        </div>

        <div class="return-toolbar">
            <form action="${pageContext.request.contextPath}/return-order-admin" method="get" class="return-search-form">
                <input type="text" name="keyword" class="return-search-input"
                       placeholder="Tìm mã phiếu, mã đơn, khách hàng, số điện thoại..." value="${param.keyword}">
                <select name="status" class="return-filter-select">
                    <option value="">Tất cả trạng thái</option>
                    <option value="PENDING" ${param.status == 'PENDING' ? 'selected' : ''}>Chờ duyệt</option>
                    <option value="APPROVED" ${param.status == 'APPROVED' ? 'selected' : ''}>Đã duyệt</option>
                    <option value="REJECTED" ${param.status == 'REJECTED' ? 'selected' : ''}>Đã từ chối</option>
                    <option value="COMPLETED" ${param.status == 'COMPLETED' ? 'selected' : ''}>Đã nhập kho</option>
                </select>
                <button type="submit" class="btn-return-search"><i class="fa fa-search"></i> Tìm</button>
                <a class="btn-return-reset" href="${pageContext.request.contextPath}/return-order-admin">Làm mới</a>
            </form>
        </div>

        <div class="return-table-wrapper">
            <table class="return-table">
                <thead>
                <tr>
                    <th>Mã phiếu</th>
                    <th>Mã đơn</th>
                    <th>Khách hàng</th>
                    <th>SL hoàn</th>
                    <th>Lý do</th>
                    <th>Trạng thái</th>
                    <th>Ngày tạo</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty returnOrders}">
                    <tr><td colspan="8" class="return-empty">Chưa có phiếu hoàn hàng</td></tr>
                </c:if>
                <c:forEach items="${returnOrders}" var="r">
                    <tr>
                        <td><strong>${r.code}</strong></td>
                        <td>#${r.orderId}</td>
                        <td>
                            <div class="return-customer">${r.receiverName}</div>
                            <small>${r.phone}</small>
                        </td>
                        <td>${r.totalQuantity}</td>
                        <td class="return-reason">${r.reason}</td>
                        <td><span class="return-status ${r.status}">${r.statusText}</span></td>
                        <td>${r.createdAtText}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/return-order-admin?mode=detail&id=${r.id}" class="return-action view" title="Xem chi tiết">
                                <i class="fa-solid fa-eye"></i>
                            </a>
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
