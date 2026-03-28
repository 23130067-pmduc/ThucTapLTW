<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Tin tức</title>
    <link rel="stylesheet" href="css/news.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="user">
    <aside class="sidebar">
        <img src="img/gau.png" alt=""Logo>
        <p>ADMIN</p>

        <div class="nav" id="menu">
            <a href="dashboard" class="nav-item">Dashboard</a>
            <a href="product-admin" class="nav-item">Sản phẩm</a>
            <a href="category-admin" class="nav-item">Danh mục</a>
            <a href="order-admin" class="nav-item">Đơn hàng</a>
            <a href="user-admin" class="nav-item">Người dùng</a>
            <a href="banner-admin" class="nav-item">Banner</a>
            <a href="news-admin" class="nav-item active">Tin tức</a>
            <a href="contact-admin" class="nav-item">Liên hệ</a>
        </div>
    </aside>

    <section class="content">

        <header class="topbar">
            <h1 id="pageTitle">Tin tức</h1>
            <div class="actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>



        <main id="page">

            <section id="dashboard" class="page active">
                <div class="cards">
                    <div class="cards cards-2">
                        <div class="card">Tổng tin<br><span>${total}</span></div>
                        <div class="card">Đang hiển thị<br><span>${totalActive}</span></div>
                    </div>
                </div>

                <div class="news-toolbar">
                    <a href="news-admin?mode=add" class="btn-add">
                        <i class="fa fa-plus"></i> Thêm tin
                    </a>
                </div>


                <div class="news-table-wrapper">

                    <table class="news-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Ảnh</th>
                            <th>Tiêu đề</th>
                            <th>Ngày tạo</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${newsList}" var="n">
                            <tr>
                                <td>${n.id}</td>
                                <td>
                                    <img src="${n.thumbnail}" class="news-thumb">
                                </td>
                                <td>${n.title}</td>
                                <td>${n.createdAt}</td>
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
                                <td class="actions">
                                    <a href="news-admin?mode=view&id=${n.id}"
                                       class="icon-btn view" title="Xem chi tiết">
                                        <i class="fa fa-eye"></i>
                                    </a>



                                    <a href="news-admin?mode=edit&id=${n.id}"
                                       class="icon-btn edit" title="Chỉnh sửa">
                                        <i class="fa fa-pen"></i>
                                    </a>



                                    <button type="button"
                                            class="icon-btn delete"
                                            title="Xóa tin tức"
                                            onclick="openDeleteModal(${n.id}, '${n.title}')">
                                        <i class="fa fa-trash"></i>
                                    </button>
                                </td>
                                </a>
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
            <p id="deleteMessage">Bạn có chắc muốn xóa tin tức này không?</p>

            <form id="deleteForm" method="post" action="news-admin">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" id="deleteNewsId">

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
        document.getElementById("deleteNewsId").value = id;
        document.getElementById("deleteMessage").innerHTML =
            'Bạn có chắc muốn xóa tin tức "<b>' + title + '</b>" không?';
        document.getElementById("deleteModal").style.display = "flex";
    }

    function closeDeleteModal() {
        document.getElementById("deleteModal").style.display = "none";
    }
</script>
<script>
    function openConfirmModal(userId) {
        document.getElementById("confirmUserId").value = userId;
        document.getElementById("confirmModal").style.display = "flex";
    }

    function closeModal() {
        document.getElementById("confirmModal").style.display = "none";
    }
</script>
</html>