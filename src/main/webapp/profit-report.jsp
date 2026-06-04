<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thống kê lợi nhuận</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profit-report.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="profit-report-page">
<div class="user">
    <aside class="sidebar">
        <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
        <p>ADMIN</p>

        <div class="nav">
            <a href="${pageContext.request.contextPath}/dashboard" class="nav-item"><i class="fa-solid fa-gauge"></i><span>Dashboard</span></a>
            <a href="${pageContext.request.contextPath}/product-admin" class="nav-item"><i class="fa-solid fa-shirt"></i><span>Sản phẩm</span></a>
            <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item"><i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span></a>
            <a href="${pageContext.request.contextPath}/profit-report" class="nav-item active"><i class="fa-solid fa-chart-line"></i><span>Lợi nhuận</span></a>
            <a href="${pageContext.request.contextPath}/category-admin" class="nav-item"><i class="fa-solid fa-list"></i><span>Danh mục</span></a>
            <a href="${pageContext.request.contextPath}/order-admin" class="nav-item"><i class="fa-solid fa-receipt"></i><span>Đơn hàng</span></a>
            <a href="${pageContext.request.contextPath}/user-admin" class="nav-item"><i class="fa-solid fa-users"></i><span>Người dùng</span></a>
            <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item"><i class="fa-solid fa-image"></i><span>Banner</span></a>
            <a href="${pageContext.request.contextPath}/news-admin" class="nav-item"><i class="fa-solid fa-newspaper"></i><span>Tin tức</span></a>
            <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item"><i class="fa-solid fa-bell"></i><span>Thông báo</span></a>
            <a href="${pageContext.request.contextPath}/contact-admin" class="nav-item"><i class="fa-solid fa-envelope"></i><span>Liên hệ</span></a>
            <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item"><i class="fa-solid fa-user-gear"></i><span>Hồ sơ</span></a>
        </div>
    </aside>

    <section class="content profit-content">
        <header class="topbar">
            <div>
                <h1>Thống kê lợi nhuận</h1>
                <p class="page-subtitle">Doanh thu lấy từ đơn hoàn thành, giá vốn lấy từ phiếu xuất kho FIFO.</p>
            </div>
            <div class="actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>

        <form action="${pageContext.request.contextPath}/profit-report" method="get" class="profit-filter">
            <div class="filter-group">
                <label for="fromDate">Từ ngày</label>
                <input type="date" id="fromDate" name="fromDate" value="${fromDate}">
            </div>

            <div class="filter-group">
                <label for="toDate">Đến ngày</label>
                <input type="date" id="toDate" name="toDate" value="${toDate}">
            </div>

            <button type="submit" class="btn-filter"><i class="fa-solid fa-filter"></i> Lọc</button>
            <a href="${pageContext.request.contextPath}/profit-report" class="btn-reset-profit">30 ngày gần nhất</a>
        </form>

        <div class="profit-cards">
            <div class="profit-card">
                <div class="card-label">Doanh thu sản phẩm</div>
                <div class="card-value"><fmt:formatNumber value="${summary.grossRevenue}"/>đ</div>
                <div class="card-note">Tổng tiền hàng trước giảm giá</div>
            </div>

            <div class="profit-card discount-card">
                <div class="card-label">Giảm giá</div>
                <div class="card-value"><fmt:formatNumber value="${summary.discount}"/>đ</div>
                <div class="card-note">Không tính phí vận chuyển</div>
            </div>

            <div class="profit-card revenue-card">
                <div class="card-label">Doanh thu thuần</div>
                <div class="card-value"><fmt:formatNumber value="${summary.netRevenue}"/>đ</div>
                <div class="card-note">Doanh thu sản phẩm - giảm giá</div>
            </div>

            <div class="profit-card cost-card">
                <div class="card-label">Giá vốn</div>
                <div class="card-value"><fmt:formatNumber value="${summary.costOfGoods}"/>đ</div>
                <div class="card-note">Tính theo phiếu xuất FIFO</div>
            </div>

            <div class="profit-card profit-card-main">
                <div class="card-label">Lợi nhuận gộp</div>
                <div class="card-value"><fmt:formatNumber value="${summary.grossProfit}"/>đ</div>
                <div class="card-note">Biên lợi nhuận: <fmt:formatNumber value="${summary.profitMargin}" maxFractionDigits="2"/>%</div>
            </div>

            <div class="profit-card quantity-card">
                <div class="card-label">Đơn hoàn thành / SL xuất</div>
                <div class="card-value">${summary.completedOrders} / ${summary.exportedQuantity}</div>
                <div class="card-note">Dùng để đối chiếu đơn và kho</div>
            </div>
        </div>

        <section class="profit-panel">
            <div class="panel-header">
                <h2>Thống kê theo ngày</h2>
            </div>
            <div class="profit-table-wrapper">
                <table class="profit-table">
                    <thead>
                    <tr>
                        <th>Ngày</th>
                        <th>Đơn hoàn thành</th>
                        <th>SL xuất</th>
                        <th>Doanh thu thuần</th>
                        <th>Giá vốn</th>
                        <th>Lợi nhuận gộp</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty dailyReports}">
                            <tr><td colspan="6" class="empty-row">Chưa có dữ liệu trong khoảng thời gian này.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${dailyReports}" var="row">
                                <tr>
                                    <td>${row.reportDate}</td>
                                    <td>${row.completedOrders}</td>
                                    <td>${row.exportedQuantity}</td>
                                    <td><fmt:formatNumber value="${row.revenue}"/>đ</td>
                                    <td><fmt:formatNumber value="${row.costOfGoods}"/>đ</td>
                                    <td class="profit-number ${row.negativeProfit ? 'negative-profit' : ''}"><fmt:formatNumber value="${row.profit}"/>đ</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </section>

        <section class="profit-panel">
            <div class="panel-header">
                <h2>Top sản phẩm theo lợi nhuận</h2>
                <p>Doanh thu theo đơn hoàn thành, giá vốn theo phiếu xuất kho FIFO.</p>
            </div>
            <div class="profit-table-wrapper">
                <table class="profit-table product-profit-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Sản phẩm</th>
                        <th>SL bán</th>
                        <th>SL xuất</th>
                        <th>Doanh thu</th>
                        <th>Giá vốn</th>
                        <th>Lợi nhuận</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty productReports}">
                            <tr><td colspan="7" class="empty-row">Chưa có sản phẩm phát sinh doanh thu hoặc giá vốn.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${productReports}" var="row">
                                <tr>
                                    <td>#${row.productId}</td>
                                    <td class="product-name-cell">${row.productName}</td>
                                    <td>${row.soldQuantity}</td>
                                    <td>${row.exportedQuantity}</td>
                                    <td><fmt:formatNumber value="${row.revenue}"/>đ</td>
                                    <td><fmt:formatNumber value="${row.costOfGoods}"/>đ</td>
                                    <td class="profit-number ${row.negativeProfit ? 'negative-profit' : ''}"><fmt:formatNumber value="${row.profit}"/>đ</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </section>
    </section>
</div>
</body>
</html>
