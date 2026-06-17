<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thống kê bán hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profit-report.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>
</head>
<body class="profit-report-page">
<div class="user">
    <aside class="sidebar">
        <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
        <p>ADMIN</p>

        <div class="nav" id="menu">
            <c:if test="${userlogin.permissions.contains('DASHBOARD_VIEW')}">
                <a href="${pageContext.request.contextPath}/dashboard" class="nav-item">
                    <i class="fa-solid fa-gauge"></i><span>Tổng quan</span>
                </a>
            </c:if>
            <c:if test="${userlogin.permissions.contains('REPORT_VIEW')}">
                <a href="${pageContext.request.contextPath}/profit-report" class="nav-item active">
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
                <a href="${pageContext.request.contextPath}/return-order-admin" class="nav-item">
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

    <section class="content profit-content">
        <header class="topbar">
            <div>
                <h1>Thống kê bán hàng</h1>
                <p class="page-subtitle">Theo dõi doanh thu, chi phí nhập hàng, giá vốn, lợi nhuận và sản phẩm bán được.</p>
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
            <a href="${pageContext.request.contextPath}/profit-report-export-excel?fromDate=${fromDate}&toDate=${toDate}" class="btn-export-profit" role="button">
                <i class="fa-solid fa-file-excel"></i> Xuất Excel
            </a>
        </form>

        <div class="profit-cards profit-summary-cards">
            <div class="profit-card revenue-card">
                <div class="card-label">Tổng doanh thu</div>
                <div class="card-value"><fmt:formatNumber value="${summary.grossRevenue}"/>đ</div>
                <div class="card-note">Tổng tiền đơn hoàn thành</div>
            </div>
            <div class="profit-card import-card">
                <div class="card-label">Tổng chi phí nhập hàng</div>
                <div class="card-value"><fmt:formatNumber value="${summary.importCost}"/>đ</div>
                <div class="card-note">Tổng giá trị nhập kho trong kỳ</div>
            </div>
            <div class="profit-card cost-card">
                <div class="card-label">Giá vốn hàng bán</div>
                <div class="card-value"><fmt:formatNumber value="${summary.costOfGoods}"/>đ</div>
                <div class="card-note">Chi phí hàng đã xuất/bán</div>
            </div>
            <div class="profit-card profit-card-main ${summary.negativeProfit ? 'is-negative' : ''}">
                <div class="card-label">Lợi nhuận</div>
                <div class="card-value"><fmt:formatNumber value="${summary.grossProfit}"/>đ</div>
                <div class="card-note">Doanh thu thuần - giá vốn</div>
            </div>
            <div class="profit-card margin-card">
                <div class="card-label">Tỉ suất lợi nhuận</div>
                <div class="card-value"><fmt:formatNumber value="${summary.profitMargin}" maxFractionDigits="2"/>%</div>
                <div class="card-note">${summary.completedOrders} đơn hoàn thành / ${summary.soldQuantity} sản phẩm bán</div>
            </div>
        </div>

        <div class="chart-grid">
            <section class="profit-panel chart-panel">
                <div class="panel-header">
                    <h2>Biểu đồ doanh thu theo ngày</h2>
                    <p>Doanh thu thuần trong khoảng thời gian đã lọc.</p>
                </div>
                <div class="chart-box"><canvas id="profitRevenueChart"></canvas></div>
            </section>

            <section class="profit-panel chart-panel">
                <div class="panel-header">
                    <h2>Biểu đồ chi phí nhập hàng theo ngày</h2>
                    <p>Chi phí nhập kho phát sinh theo thời gian.</p>
                </div>
                <div class="chart-box"><canvas id="profitImportCostChart"></canvas></div>
            </section>
        </div>

        <section class="profit-panel">
            <div class="panel-header panel-header-with-form">
                <div>
                    <h2>Thống kê lợi nhuận theo từng sản phẩm</h2>
                    <p>Đối chiếu số lượng nhập, bán, doanh thu, giá vốn và lợi nhuận.</p>
                </div>
                <form action="${pageContext.request.contextPath}/profit-report" method="get" class="product-stat-filter">
                    <input type="hidden" name="fromDate" value="${fromDate}">
                    <input type="hidden" name="toDate" value="${toDate}">
                    <label for="productId">Chọn sản phẩm</label>
                    <select id="productId" name="productId">
                        <option value="0">-- Xem tất cả sản phẩm --</option>
                        <c:forEach items="${productOptions}" var="product">
                            <option value="${product.productId}" ${selectedProductId == product.productId ? 'selected' : ''}>#${product.productId} - ${product.productName}</option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn-small-primary">Xem thống kê</button>
                    <a href="${pageContext.request.contextPath}/profit-report?fromDate=${fromDate}&toDate=${toDate}" class="btn-small-secondary">Xóa chọn</a>
                </form>
            </div>
            <div class="profit-table-wrapper">
                <table id="profitProductTable" class="profit-table product-profit-table js-paginated-table" data-page-size="10">
                    <thead>
                    <tr>
                        <th>STT</th>
                        <th>Mã sản phẩm</th>
                        <th>Tên sản phẩm</th>
                        <th>SL nhập</th>
                        <th>Tổng chi phí nhập</th>
                        <th>SL bán</th>
                        <th>Doanh thu</th>
                        <th>Giá vốn</th>
                        <th>Lợi nhuận</th>
                        <th>Tỉ suất LN</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty productReports}">
                            <tr><td colspan="10" class="empty-row">Chưa có sản phẩm phát sinh trong khoảng thời gian này.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${productReports}" var="row" varStatus="st">
                                <tr>
                                    <td>${st.index + 1}</td>
                                    <td>#${row.productId}</td>
                                    <td class="product-name-cell">${row.productName}</td>
                                    <td>${row.importedQuantity}</td>
                                    <td><fmt:formatNumber value="${row.importCost}"/>đ</td>
                                    <td>${row.soldQuantity}</td>
                                    <td><fmt:formatNumber value="${row.revenue}"/>đ</td>
                                    <td><fmt:formatNumber value="${row.costOfGoods}"/>đ</td>
                                    <td class="profit-number ${row.negativeProfit ? 'negative-profit' : ''}"><fmt:formatNumber value="${row.profit}"/>đ</td>
                                    <td><fmt:formatNumber value="${row.profitMargin}" maxFractionDigits="2"/>%</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
            <div class="table-pagination" data-pagination-for="profitProductTable"></div>
        </section>

        <section class="profit-panel">
            <div class="panel-header panel-header-inline">
                <div>
                    <h2>Thống kê sản phẩm bán được</h2>
                    <p>Danh sách sản phẩm bán ra trong khoảng thời gian đã chọn.</p>
                </div>
            </div>
            <div class="profit-table-wrapper">
                <table id="soldProductTable" class="profit-table sold-product-table js-paginated-table" data-page-size="10">
                    <thead>
                    <tr>
                        <th>STT</th>
                        <th>Mã sản phẩm</th>
                        <th>Tên sản phẩm</th>
                        <th>Danh mục</th>
                        <th>Giá bán</th>
                        <th>Ngày bán gần nhất</th>
                        <th>Số lượng bán</th>
                        <th>Doanh thu</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty soldProductReports}">
                            <tr><td colspan="8" class="empty-row">Chưa có sản phẩm bán được trong khoảng thời gian này.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${soldProductReports}" var="row" varStatus="st">
                                <tr>
                                    <td>${st.index + 1}</td>
                                    <td>#${row.productId}</td>
                                    <td class="product-name-cell">${row.productName}</td>
                                    <td>${row.categoryName}</td>
                                    <td><fmt:formatNumber value="${row.price}"/>đ</td>
                                    <td>${empty row.lastSoldDate ? '-' : row.lastSoldDate}</td>
                                    <td>${row.soldQuantity}</td>
                                    <td><fmt:formatNumber value="${row.revenue}"/>đ</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
            <div class="table-pagination" data-pagination-for="soldProductTable"></div>
        </section>

        <section class="profit-panel chart-panel wide-chart-panel">
            <div class="panel-header">
                <h2>Biểu đồ sản phẩm bán được</h2>
                <p>Top sản phẩm bán nhiều nhất trong khoảng thời gian đã lọc.</p>
            </div>
            <div class="chart-box chart-box-large"><canvas id="profitSoldProductChart"></canvas></div>
        </section>

        <section class="profit-panel">
            <div class="panel-header">
                <h2>Thống kê sản phẩm không bán được</h2>
                <p>Sản phẩm chưa phát sinh đơn hoàn thành trong khoảng thời gian đã lọc.</p>
            </div>
            <div class="profit-table-wrapper">
                <table id="unsoldProductTable" class="profit-table unsold-product-table js-paginated-table" data-page-size="10">
                    <thead>
                    <tr>
                        <th>STT</th>
                        <th>Mã sản phẩm</th>
                        <th>Tên sản phẩm</th>
                        <th>Danh mục</th>
                        <th>Giá bán</th>
                        <th>Tồn hiện tại</th>
                        <th>Ngày tạo</th>
                        <th>Lần bán gần nhất</th>
                        <th>Đã bán</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty unsoldProductReports}">
                            <tr><td colspan="9" class="empty-row">Tất cả sản phẩm đều có phát sinh bán trong khoảng thời gian này.</td></tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${unsoldProductReports}" var="row" varStatus="st">
                                <tr>
                                    <td>${st.index + 1}</td>
                                    <td>#${row.productId}</td>
                                    <td class="product-name-cell">${row.productName}</td>
                                    <td>${row.categoryName}</td>
                                    <td><fmt:formatNumber value="${row.price}"/>đ</td>
                                    <td>${row.currentStock}</td>
                                    <td>${empty row.createdDate ? '-' : row.createdDate}</td>
                                    <td>${empty row.lastSoldDate ? 'Chưa từng bán' : row.lastSoldDate}</td>
                                    <td>0</td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
            <div class="table-pagination" data-pagination-for="unsoldProductTable"></div>
        </section>
    </section>
</div>

<script>
    window.profitReportChartData = {
        dailyLabels: ${dailyLabelsJson},
        dailyRevenue: ${dailyRevenueJson},
        dailyImportCost: ${dailyImportCostJson},
        dailyCost: ${dailyCostJson},
        topSoldLabels: ${topSoldLabelsJson},
        topSoldValues: ${topSoldValuesJson}
    };
</script>
<script src="${pageContext.request.contextPath}/javaScript/profit-report.js"></script>
</body>
</html>
