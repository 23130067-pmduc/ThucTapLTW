<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý tin tức</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/news-admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="user">
    <aside class="sidebar">
        <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
        <p>ADMIN</p>

        <div class="nav">
            <div class="nav" id="menu">

                <c:if test="${userlogin.permissions.contains('DASHBOARD_VIEW')}">
                    <a href="${pageContext.request.contextPath}/dashboard" class="nav-item ">
                        <i class="fa-solid fa-gauge"></i><span>Dashboard</span>
                    </a>
                </c:if>

                <c:if test="${userlogin.permissions.contains('PRODUCT_VIEW')}">
                    <a href="${pageContext.request.contextPath}/product-admin" class="nav-item">
                        <i class="fa-solid fa-shirt"></i><span>Sản phẩm</span>
                    </a>
                </c:if>

                <c:if test="${userlogin.permissions.contains('WAREHOUSE_VIEW')}">
                    <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item">
                        <i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span>
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

                <a href="${pageContext.request.contextPath}/promotion-event-admin" class="nav-item">
                    <i class="fa-solid fa-tags"></i><span>Khuyến mãi</span>
                </a>

                <c:if test="${userlogin.permissions.contains('NEWS_VIEW')}">
                    <a href="${pageContext.request.contextPath}/news-admin" class="nav-item active">
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

        </div>
    </aside>

    <section class="content">
        <header class="topbar">
            <h1>Quản lý bài viết</h1>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
        </header>

        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <section class="cards">
            <div class="card">Tổng bài viết<br><span>${total}</span></div>
            <div class="card">Bài viết mới / tuần<br><span>${totalInWeek}</span></div>
            <div class="card">Đang hiển thị<br><span>${totalActive}</span></div>
            <div class="card">Đã ẩn<br><span>${totalHidden}</span></div>
        </section>

        <div class="news-toolbar">
            <form method="get" action="${pageContext.request.contextPath}/news-admin" class="news-search-form">
                <input type="text" name="keyword" value="${keyword}" placeholder="Tìm theo tiêu đề, mô tả ngắn...">
                <button type="submit" class="btn-search">
                    <i class="fa fa-search"></i> Tìm
                </button>
            </form>

            <a href="${pageContext.request.contextPath}/news-admin?mode=add" class="btn-add">
                <i class="fa fa-plus"></i> Thêm bài viết
            </a>
        </div>

        <div class="news-result-info">
            Hiển thị ${newsList.size()} / ${totalItems} bài viết
        </div>

        <div class="news-table-wrapper">
            <table class="news-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Ảnh</th>
                    <th>Tiêu đề</th>
                    <th>Mô tả ngắn</th>
                    <th>Ngày tạo</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty newsList}">
                        <tr>
                            <td colspan="7" class="empty-row">Chưa có bài viết nào.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${newsList}" var="n">
                            <tr>
                                <td>${n.id}</td>
                                <td>
                                    <img src="${pageContext.request.contextPath}/${n.thumbnail}" alt="Ảnh bài viết" class="news-thumb">
                                </td>
                                <td class="news-title-cell">${n.title}</td>
                                <td class="news-short-desc">${n.shortDescription}</td>
                                <td>
                                    <fmt:formatDate value="${n.createdAt}" pattern="dd/MM/yyyy"/>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${n.status == 1}">
                                            <span class="status active">Hiển thị</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status blocked">Ẩn</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div class="actions">
                                        <a href="${pageContext.request.contextPath}/news-admin?mode=view&id=${n.id}" class="icon-btn view" title="Xem">
                                            <i class="fa fa-eye"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/news-admin?mode=edit&id=${n.id}" class="icon-btn edit" title="Sửa">
                                            <i class="fa fa-pen"></i>
                                        </a>
                                        <button type="button" class="icon-btn delete js-open-delete"
                                                data-id="${n.id}"
                                                data-title="${n.title}"
                                                title="Xóa">
                                            <i class="fa fa-trash"></i>
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>

            <c:if test="${totalPages > 1}">
                <c:set var="startPage" value="${currentPage - 2}" />
                <c:set var="endPage" value="${currentPage + 2}" />

                <c:if test="${startPage < 1}">
                    <c:set var="startPage" value="1" />
                    <c:set var="endPage" value="5" />
                </c:if>

                <c:if test="${endPage > totalPages}">
                    <c:set var="endPage" value="${totalPages}" />
                    <c:set var="startPage" value="${totalPages - 4}" />
                </c:if>

                <c:if test="${startPage < 1}">
                    <c:set var="startPage" value="1" />
                </c:if>

                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <c:url var="prevUrl" value="/news-admin">
                            <c:param name="page" value="${currentPage - 1}" />
                            <c:param name="keyword" value="${keyword}" />
                        </c:url>
                        <a href="${prevUrl}" class="page-link">Trước</a>
                    </c:if>

                    <c:forEach begin="${startPage}" end="${endPage}" var="pageNumber">
                        <c:choose>
                            <c:when test="${pageNumber == currentPage}">
                                <span class="page-link active">${pageNumber}</span>
                            </c:when>
                            <c:otherwise>
                                <c:url var="pageUrl" value="/news-admin">
                                    <c:param name="page" value="${pageNumber}" />
                                    <c:param name="keyword" value="${keyword}" />
                                </c:url>
                                <a href="${pageUrl}" class="page-link">${pageNumber}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <c:url var="nextUrl" value="/news-admin">
                            <c:param name="page" value="${currentPage + 1}" />
                            <c:param name="keyword" value="${keyword}" />
                        </c:url>
                        <a href="${nextUrl}" class="page-link">Sau</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </section>
</div>

<div id="deleteModal" class="modal-overlay">
    <div class="modal">
        <div class="modal-header">
            <h3>Xác nhận xóa</h3>
            <button type="button" class="modal-close" id="btnCloseDeleteModal">&times;</button>
        </div>
        <div class="modal-body">
            <p id="deleteMessage">Bạn có chắc muốn xóa bài viết này không?</p>
        </div>
        <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/news-admin">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="id" id="deleteNewsId">
            <div class="modal-footer">
                <button type="button" class="btn-secondary" id="btnCancelDelete">Hủy</button>
                <button type="submit" class="btn-danger">Xóa</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/javaScript/news-admin.js"></script>
</body>
</html>
