<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Mã giảm giá</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pagination.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/voucher-admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="user">
    <aside class="sidebar">
        <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
        <p>ADMIN</p>

        <div class="nav" id="menu">
            <a href="${pageContext.request.contextPath}/dashboard" class="nav-item"><i class="fa-solid fa-gauge"></i><span>Dashboard</span></a>
            <a href="${pageContext.request.contextPath}/product-admin" class="nav-item"><i class="fa-solid fa-shirt"></i><span>Sản phẩm</span></a>
            <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item"><i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span></a>
            <a href="${pageContext.request.contextPath}/profit-report" class="nav-item"><i class="fa-solid fa-chart-line"></i><span>Lợi nhuận</span></a>
            <a href="${pageContext.request.contextPath}/category-admin" class="nav-item"><i class="fa-solid fa-list"></i><span>Danh mục</span></a>
            <a href="${pageContext.request.contextPath}/order-admin" class="nav-item"><i class="fa-solid fa-receipt"></i><span>Đơn hàng</span></a>
            <a href="${pageContext.request.contextPath}/return-order-admin" class="nav-item"><i class="fa-solid fa-rotate-left"></i><span>Hoàn hàng</span></a>
            <a href="${pageContext.request.contextPath}/user-admin" class="nav-item"><i class="fa-solid fa-users"></i><span>Người dùng</span></a>
            <a href="${pageContext.request.contextPath}/voucher-admin" class="nav-item active"><i class="fa-solid fa-ticket"></i><span>Mã giảm giá</span></a>
            <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item"><i class="fa-solid fa-image"></i><span>Banner</span></a>
            <a href="${pageContext.request.contextPath}/news-admin" class="nav-item"><i class="fa-solid fa-newspaper"></i><span>Tin tức</span></a>
            <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item"><i class="fa-solid fa-bell"></i><span>Thông báo</span></a>
            <a href="${pageContext.request.contextPath}/contact-admin" class="nav-item"><i class="fa-solid fa-envelope"></i><span>Liên hệ</span></a>
            <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item"><i class="fa-solid fa-user-gear"></i><span>Hồ sơ</span></a>
        </div>
    </aside>

    <section class="content">
        <header class="topbar">
            <h1>Quản lý mã giảm giá</h1>
            <div class="actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>

        <main>
            <c:if test="${param.success == 'create'}">
                <div class="alert alert-success">
                    <i class="fa-solid fa-circle-check"></i>
                    Thêm mã giảm giá thành công.
                </div>
            </c:if>

            <div class="cards voucher-cards">
                <div class="card">Tổng mã<br><span>${totalAll}</span></div>
                <div class="card">Đang hoạt động<br><span>${totalActive}</span></div>
                <div class="card">Đã khóa<br><span>${totalLocked}</span></div>
                <div class="card">Hết hạn<br><span>${totalExpired}</span></div>
            </div>

            <section class="voucher-panel">
                <div class="voucher-panel-header">
                    <div>
                        <h2>Danh sách mã giảm giá</h2>
                    </div>
                    <div class="voucher-panel-actions">
                        <span class="result-count">${total} kết quả</span>
                        <a href="${pageContext.request.contextPath}/voucher-admin?action=create" class="voucher-add-btn">
                            <i class="fa-solid fa-plus"></i>
                            Thêm mã giảm giá
                        </a>
                    </div>
                </div>
                <div class="voucher-table-wrapper">
                    <table class="voucher-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Mã</th>
                            <th>Tên mã</th>
                            <th>Phạm vi</th>
                            <th>Loại giảm</th>
                            <th>Giá trị giảm</th>
                            <th>Giảm tối đa</th>
                            <th>Đơn tối thiểu</th>
                            <th>Lượt dùng</th>
                            <th>Thời gian</th>
                            <th>Trạng thái</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty vouchers}">
                                <c:forEach items="${vouchers}" var="v">
                                    <tr>
                                        <td>#${v.id}</td>
                                        <td><span class="voucher-code">${v.code}</span></td>
                                        <td>
                                            <div class="voucher-name">${v.name}</div>
                                            <div class="voucher-desc">${v.description}</div>
                                        </td>
                                        <td><span class="scope-badge">${v.scopeLabel}</span></td>
                                        <td>${v.discountTypeLabel}</td>
                                        <td class="money-text">${v.discountText}</td>
                                        <td class="money-text">${v.maxDiscountText}</td>
                                        <td class="money-text">${v.minOrderText}</td>
                                        <td>${v.usageText}</td>
                                        <td class="date-cell">
                                            <div>${v.startDateText}</div>
                                            <div>${v.endDateText}</div>
                                        </td>
                                        <td>
                                            <span class="voucher-status ${v.statusClass}">${v.statusLabel}</span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="11" class="empty-row">
                                        <i class="fa-solid fa-ticket-simple"></i>
                                        <span>Chưa có mã giảm giá nào trong hệ thống.</span>
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
                            <a class="page-btn"
                               href="voucher-admin?page=${currentPage - 1}">Trước</a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <a class="page-btn ${i == currentPage ? 'active' : ''}"
                               href="voucher-admin?page=${i}">${i}</a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a class="page-btn"
                               href="voucher-admin?page=${currentPage + 1}">Sau</a>
                        </c:if>
                    </div>
                </c:if>
            </section>
        </main>
    </section>
</div>
</body>
</html>
