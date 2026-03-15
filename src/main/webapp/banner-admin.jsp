<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Banner</title>
    <link rel="stylesheet" href="css/banner.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="user">
    <aside class="sidebar">
        <img src="img/gau.png" alt="" Logo>
        <p>ADMIN</p>

        <div class="nav" id="menu">
            <a href="dashboard" class="nav-item">Dashboard</a>
            <a href="product-admin" class="nav-item">Sản phẩm</a>
            <a href="category-admin" class="nav-item">Danh mục</a>
            <a href="order-admin" class="nav-item">Đơn hàng</a>
            <a href="user-admin" class="nav-item">Người dùng</a>
            <a href="banner-admin" class="nav-item active">Banner</a>
            <a href="news-admin" class="nav-item">Tin tức</a>
            <a href="contact-admin" class="nav-item">Liên hệ</a>
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
                    <a href="banner-admin?mode=add" class="btn-add">
                        <i class="fa fa-plus"></i> Thêm banner
                    </a>
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
                                <td class="actions">
                                    <!-- XEM -->
                                    <a href="banner-admin?mode=view&id=${b.id}"
                                       class="icon-btn view" title="Xem chi tiết">
                                        <i class="fa fa-eye"></i>
                                    </a>

                                    <!-- SỬA -->
                                    <a href="banner-admin?mode=edit&id=${b.id}"
                                       class="icon-btn edit" title="Chỉnh sửa">
                                        <i class="fa fa-pen"></i>
                                    </a>

                                    <!-- XÓA MỀM -->
                                    <button type="button"
                                            class="icon-btn delete"
                                            title="Xóa banner"
                                            onclick="openDeleteModal(${b.id}, '${b.title}')">
                                        <i class="fa fa-trash"></i>
                                    </button>
                                </td>

                            </tr>
                        </c:forEach>

                        </tbody>
                    </table>
                </div>
            </section>
        </main>
    </section>

    <div id="deleteModal" class="modal-overlay">
        <div class="modal">
            <h3>Xác nhận xóa</h3>
            <p id="deleteMessage">Bạn có chắc muốn xóa banner này không?</p>

            <form id="deleteForm" method="post" action="banner-admin">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" id="deleteBannerId">

                <div class="modal-actions">
                    <button type="button" class="btn-secondary" onclick="closeDeleteModal()">Hủy</button>
                    <button type="submit" class="btn-danger">Xóa</button>
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
            'Bạn có chắc muốn xóa banner "<b>' + title + '</b>" không?';
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