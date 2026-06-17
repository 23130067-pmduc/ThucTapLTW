<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Banner</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/banner.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pagination.css">
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
                    <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item active">
                        <i class="fa-solid fa-image"></i><span>Banner</span>
                    </a>
                </c:if>

                <c:if test="${userlogin.permissions.contains('VOUCHER_VIEW')}">
                    <a href="${pageContext.request.contextPath}/voucher-admin" class="nav-item">
                        <i class="fa-solid fa-ticket"></i>
                        <span>Mã giảm giá</span>
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
        </div>
    </aside>

    <section class="content">

        <header class="topbar">
            <h1 id="pageTitle">Banner</h1>
            <div class="actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>



        <main id="page">
            <section id="dashboard" class="page active">
                <div class="cards">
                    <div class="card">Tổng banner<br><span id="dashboard-total-banner">${total}</span></div>
                    <div class="card">Đang hoạt động<br><span id="dashboard-total-banner-active">${totalActive}</span></div>
                </div>

                <div class="banner-toolbar">
                    <c:if test="${userlogin.permissions.contains('BANNER_CREATE')}">
                        <a href="${pageContext.request.contextPath}/banner-admin?mode=add" class="btn-add">
                            <i class="fa fa-plus"></i> Thêm banner
                        </a>
                    </c:if>
                </div>


                <div class="banner-table-wrapper">
                    <table class="banner-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Ảnh</th>
                            <th>Liên kết đến</th>
                            <th>Tiêu đề</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                        </thead>
                        <tbody id="bannerTableBody">

                        <c:forEach items="${banners}" var="b">
                            <tr>
                                <td>${b.id}</td>
                                <td><img src="${b.imageUrl}" alt="" class="banner-thumb"></td>
                                <td>${b.navigateTo}</td>
                                <td>${b.title}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${b.status}">
                                            <span class="status active">Hoạt động</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="status blocked">Bị khóa</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="banner-actions">
                                    <a href="banner-admin?mode=view&id=${b.id}"
                                       class="icon-btn view" title="Xem chi tiết">
                                        <i class="fa fa-eye"></i>
                                    </a>

                                    <c:if test="${userlogin.permissions.contains('BANNER_UPDATE')}">
                                        <a href="banner-admin?mode=edit&id=${b.id}"
                                           class="icon-btn edit" title="Chỉnh sửa">
                                            <i class="fa fa-pen"></i>
                                        </a>
                                    </c:if>

                                    <c:if test="${userlogin.permissions.contains('BANNER_DELETE')}">
                                        <button type="button"
                                                class="icon-btn delete"
                                                title="Xóa banner"
                                                onclick="openDeleteModal(${b.id}, '${b.title}')">
                                            <i class="fa fa-trash"></i>
                                        </button>
                                    </c:if>
                                </td>

                            </tr>
                        </c:forEach>

                        </tbody>
                    </table>
                </div>


                <c:if test="${totalPages > 1}">
                    <div class="pagination">

                        <c:if test="${currentPage > 1}">
                            <a class="page-btn" href="banner-admin?page=${currentPage - 1}">
                                Trước
                            </a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <a href="banner-admin?page=${i}"
                               class="page-btn ${i == currentPage ? 'active' : ''}">
                                    ${i}
                            </a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a class="page-btn" href="banner-admin?page=${currentPage + 1}">
                                Sau
                            </a>
                        </c:if>

                    </div>
                </c:if>

            </section>
        </main>
    </section>

    <div id="deleteModal" class="modal-overlay">
        <div class="modal">
            <h3>Xác nhận xóa</h3>
            <p id="deleteMessage">Bạn có chắc muốn xóa banner này không?</p>

            <form id="deleteForm" method="post" action="banner-admin">
                <input type="hidden" name="action" value="block">
                <input type="hidden" name="id" id="deleteBannerId">

                <div class="modal-actions">
                    <button type="button" class="btn-secondary" onclick="closeDeleteModal()">Hủy</button>
                    <button type="submit" class="btn-danger">Ẩn</button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
<script>
    function openDeleteModal(id, title) {
        document.getElementById("deleteBannerId").value = id;
        document.getElementById("deleteMessage").innerHTML =
            'Bạn có chắc muốn ẩn banner "<b>' + title + '</b>" không?';
        document.getElementById("deleteModal").style.display = "flex";
    }

    function closeDeleteModal() {
        document.getElementById("deleteModal").style.display = "none";
    }

    function closeModal() {
        document.getElementById("confirmModal").style.display = "none";
    }
</script>
</html>
