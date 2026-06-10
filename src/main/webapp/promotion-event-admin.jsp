<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Khuyến mãi</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pagination.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/promotion-event-admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="user">
    <aside class="sidebar">
        <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
        <p>ADMIN</p>

        <div class="nav">
            <a href="${pageContext.request.contextPath}/dashboard" class="nav-item"><i class="fa-solid fa-gauge"></i><span>Thống kê</span></a>
            <a href="${pageContext.request.contextPath}/product-admin" class="nav-item"><i class="fa-solid fa-shirt"></i><span>Sản phẩm</span></a>
            <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item"><i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span></a>
            <a href="${pageContext.request.contextPath}/profit-report" class="nav-item"><i class="fa-solid fa-chart-line"></i><span>Lợi nhuận</span></a>
            <a href="${pageContext.request.contextPath}/category-admin" class="nav-item"><i class="fa-solid fa-list"></i><span>Danh mục</span></a>
            <a href="${pageContext.request.contextPath}/order-admin" class="nav-item"><i class="fa-solid fa-receipt"></i><span>Đơn hàng</span></a>
            <a href="${pageContext.request.contextPath}/return-order-admin" class="nav-item"><i class="fa-solid fa-rotate-left"></i><span>Hoàn hàng</span></a>
            <a href="${pageContext.request.contextPath}/user-admin" class="nav-item"><i class="fa-solid fa-users"></i><span>Người dùng</span></a>
            <a href="${pageContext.request.contextPath}/voucher-admin" class="nav-item"><i class="fa-solid fa-ticket"></i><span>Mã giảm giá</span></a>
            <a href="${pageContext.request.contextPath}/promotion-event-admin" class="nav-item active"><i class="fa-solid fa-tags"></i><span>Khuyến mãi</span></a>
            <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item"><i class="fa-solid fa-image"></i><span>Banner</span></a>
            <a href="${pageContext.request.contextPath}/news-admin" class="nav-item"><i class="fa-solid fa-newspaper"></i><span>Tin tức</span></a>
            <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item"><i class="fa-solid fa-bell"></i><span>Thông báo</span></a>
            <a href="${pageContext.request.contextPath}/contact-admin" class="nav-item"><i class="fa-solid fa-envelope"></i><span>Liên hệ</span></a>
            <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item"><i class="fa-solid fa-user-gear"></i><span>Hồ sơ</span></a>
        </div>
    </aside>

    <section class="content">
        <header class="topbar">
            <h1>Khuyến mãi</h1>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
        </header>

        <main>
            <c:if test="${param.success == 'create'}">
                <div class="alert alert-success">
                    <i class="fa-solid fa-circle-check"></i>
                    Đã thêm chương trình khuyến mãi thành công.
                </div>
            </c:if>

            <div class="cards promotion-summary">
                <div class="card">Tổng chương trình<br><span>${totalAll}</span></div>
                <div class="card">Đang diễn ra<br><span>${totalActive}</span></div>
                <div class="card">Sắp diễn ra<br><span>${totalUpcoming}</span></div>
                <div class="card">Đã kết thúc<br><span>${totalEnded}</span></div>
            </div>

            <div class="promotion-toolbar">
                <form method="get" action="${pageContext.request.contextPath}/promotion-event-admin" class="promotion-search-form">
                    <input type="text" name="keyword" value="${keyword}" placeholder="Tìm theo ID, tên, nhãn hoặc mô tả...">
                    <button type="submit" class="btn-search">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        Tìm
                    </button>
                    <c:if test="${not empty keyword}">
                        <a href="${pageContext.request.contextPath}/promotion-event-admin" class="btn-reset">Đặt lại</a>
                    </c:if>
                </form>
                <a href="${pageContext.request.contextPath}/promotion-event-admin?action=create" class="btn-add">
                    <i class="fa-solid fa-plus"></i>
                    Thêm chương trình
                </a>
            </div>

            <div class="promotion-table-wrapper">
                    <table class="promotion-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Chương trình</th>
                            <th>Ưu đãi</th>
                            <th>Phạm vi áp dụng</th>
                            <th>Sản phẩm</th>
                            <th>Thời gian áp dụng</th>
                            <th>Trạng thái</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty promotionEvents}">
                                <c:forEach items="${promotionEvents}" var="event">
                                    <tr>
                                        <td class="event-id">#${event.id}</td>
                                        <td>
                                            <div class="event-info">
                                                <span class="event-icon"><i class="fa-solid ${event.icon}"></i></span>
                                                <div>
                                                    <strong>${event.title}</strong>
                                                    <small>${event.description}</small>
                                                </div>
                                            </div>
                                        </td>
                                        <td class="promotion-discount-cell">
                                            <span class="discount-badge">${event.discountLabel}</span>
                                            <small class="event-tag">${event.tag}</small>
                                        </td>
                                        <td><span class="scope-badge">${event.scopeLabel}</span></td>
                                        <td><strong>${event.productCount}</strong> sản phẩm</td>
                                        <td class="date-cell">
                                            <strong>${event.startDateText}</strong>
                                            <span>${event.endDateText}</span>
                                        </td>
                                        <td>
                                            <span class="event-status ${event.statusClass}">${event.statusLabel}</span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="7" class="empty-row">
                                        <i class="fa-solid fa-tags"></i>
                                        Chưa có chương trình khuyến mãi nào.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>

                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <c:if test="${currentPage > 1}">
                            <c:url var="previousPageUrl" value="/promotion-event-admin">
                                <c:param name="page" value="${currentPage - 1}" />
                                <c:param name="keyword" value="${keyword}" />
                            </c:url>
                            <a class="page-btn" href="${previousPageUrl}">Trước</a>
                        </c:if>
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:url var="pageUrl" value="/promotion-event-admin">
                                <c:param name="page" value="${i}" />
                                <c:param name="keyword" value="${keyword}" />
                            </c:url>
                            <a class="page-btn ${i == currentPage ? 'active' : ''}" href="${pageUrl}">${i}</a>
                        </c:forEach>
                        <c:if test="${currentPage < totalPages}">
                            <c:url var="nextPageUrl" value="/promotion-event-admin">
                                <c:param name="page" value="${currentPage + 1}" />
                                <c:param name="keyword" value="${keyword}" />
                            </c:url>
                            <a class="page-btn" href="${nextPageUrl}">Sau</a>
                        </c:if>
                    </div>
                </c:if>
        </main>
    </section>
</div>
</body>
</html>
