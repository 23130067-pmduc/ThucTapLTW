<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nhà cung cấp</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/supplier-admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pagination.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="supplier-page">
<div class="user">
    <aside class="sidebar">
        <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
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
                <a href="${pageContext.request.contextPath}/supplier-admin" class="nav-item active">
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

    <section class="content supplier-content">
        <header class="topbar">
            <h1>Nhà cung cấp</h1>
            <div class="topbar-actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>

        <div class="supplier-cards">
            <div class="supplier-card">Tổng nhà cung cấp <span>${total}</span></div>
            <div class="supplier-card">Đang hoạt động <span>${totalActive}</span></div>
            <div class="supplier-card danger-card">Đã khóa <span>${totalLocked}</span></div>
        </div>

        <c:if test="${param.success == 'create'}">
            <div class="supplier-alert success">
                <i class="fa-solid fa-circle-check"></i> Thêm nhà cung cấp thành công.
            </div>
        </c:if>

        <section class="supplier-panel">
            <div class="supplier-panel-header">
                <h2>Danh sách nhà cung cấp</h2>
                <a href="${pageContext.request.contextPath}/supplier-admin?mode=add" class="btn-add">
                    <i class="fa-solid fa-plus"></i> Thêm nhà cung cấp
                </a>
            </div>

            <form action="${pageContext.request.contextPath}/supplier-admin" method="get" class="supplier-toolbar">
                <input type="text" name="keyword" placeholder="Tìm mã, tên, SĐT, email, địa chỉ..." value="${keyword}">

                <select name="status">
                    <option value="" ${empty status ? 'selected' : ''}>Tất cả trạng thái</option>
                    <option value="ACTIVE" ${status == 'ACTIVE' ? 'selected' : ''}>Đang hoạt động</option>
                    <option value="LOCKED" ${status == 'LOCKED' ? 'selected' : ''}>Đã khóa</option>
                </select>

                <button type="submit" class="btn-search">
                    <i class="fa-solid fa-magnifying-glass"></i> Tìm
                </button>

                <a href="${pageContext.request.contextPath}/supplier-admin" class="btn-reset">Làm mới</a>
            </form>

            <div class="supplier-result-info">
                Hiển thị ${suppliers.size()} / ${total} nhà cung cấp
            </div>

            <div class="supplier-table-wrapper">
                <table class="supplier-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Mã NCC</th>
                        <th>Tên nhà cung cấp</th>
                        <th>Số điện thoại</th>
                        <th>Email</th>
                        <th>Địa chỉ</th>
                        <th>Trạng thái</th>
                        <th>Cập nhật</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty suppliers}">
                            <tr>
                                <td colspan="8" class="empty-row">Không có nhà cung cấp phù hợp.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${suppliers}" var="supplier">
                                <tr>
                                    <td>#${supplier.id}</td>
                                    <td>${supplier.code}</td>
                                    <td>
                                        <strong>${supplier.name}</strong>
                                        <c:if test="${not empty supplier.note}">
                                            <small>${supplier.note}</small>
                                        </c:if>
                                    </td>
                                    <td>${supplier.phone}</td>
                                    <td>${supplier.email}</td>
                                    <td>${supplier.address}</td>
                                    <td>
                                        <span class="status-badge ${supplier.status == 'ACTIVE' ? 'active' : 'locked'}">
                                            ${supplier.statusText}
                                        </span>
                                    </td>
                                    <td>${empty supplier.updatedAtText ? '-' : supplier.updatedAtText}</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>

            <c:if test="${totalPages > 1}">
                <div class="pagination supplier-pagination">
                    <c:if test="${currentPage > 1}">
                        <c:url var="prevUrl" value="/supplier-admin">
                            <c:param name="page" value="${currentPage - 1}" />
                            <c:param name="keyword" value="${keyword}" />
                            <c:param name="status" value="${status}" />
                        </c:url>
                        <a class="page-btn" href="${prevUrl}">Trước</a>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <c:url var="pageUrl" value="/supplier-admin">
                            <c:param name="page" value="${i}" />
                            <c:param name="keyword" value="${keyword}" />
                            <c:param name="status" value="${status}" />
                        </c:url>
                        <a class="page-btn ${i == currentPage ? 'active' : ''}" href="${pageUrl}">${i}</a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <c:url var="nextUrl" value="/supplier-admin">
                            <c:param name="page" value="${currentPage + 1}" />
                            <c:param name="keyword" value="${keyword}" />
                            <c:param name="status" value="${status}" />
                        </c:url>
                        <a class="page-btn" href="${nextUrl}">Sau</a>
                    </c:if>
                </div>
            </c:if>
        </section>
    </section>
</div>
</body>
</html>
