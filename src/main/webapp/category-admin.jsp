<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin - Danh mục</title>
  <link rel="stylesheet" href="css/category-admin.css">
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
      <a href="category-admin" class="nav-item active">Danh mục</a>
      <a href="order-admin" class="nav-item">Đơn hàng</a>
      <a href="user-admin" class="nav-item">Người dùng</a>
      <a href="banner-admin" class="nav-item ">Banner</a>
      <a href="news-admin" class="nav-item">Tin tức</a>
      <a href="contact-admin" class="nav-item">Liên hệ</a>
    </div>
  </aside>

  <section class="content">

    <header class="topbar">
      <h1 id="pageTitle">Danh mục</h1>
      <div class="actions">
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
      </div>
    </header>


    <main id="page">
      <section id="dashboard" class="page active">
        <div class="cards">
          <div class="card">Tổng danh mục<br><span id="dashboard-total-category">${total}</span></div>
          <div class="card">Đang hoạt động<br><span id="dashboard-total-category-active">${totalActive}</span></div>
        </div>

        <div class="category-toolbar">
          <a href="category-admin?mode=add" class="btn-add">
            <i class="fa fa-plus"></i> Thêm danh mục
          </a>
        </div>


        <div class="category-table-wrapper">
          <table class="category-table">
            <thead>
            <tr>
              <th>ID</th>
              <th>Tên danh mục</th>
              <th>Mô tả</th>
              <th>Số sản phẩm</th>
              <th>Trạng thái</th>
              <th>Hành động</th>
            </tr>
            </thead>
            <tbody id="categoryTableBody">

            <c:forEach items="${categories}" var="c">
              <tr>
                <td>${c.id}</td>
                <td>${c.name}</td>
                <td>${c.description}</td>
                <td>${c.countProduct}</td>
                <td>
                  <c:choose>
                    <c:when test="${c.status == 1}">
                      <span class="status active">Hoạt động</span>
                    </c:when>
                    <c:otherwise>
                      <span class="status blocked">Bị khóa</span>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td class="actions">


                  <a href="category-admin?mode=view&id=${c.id}"
                     class="icon-btn view" title="Xem chi tiết">
                    <i class="fa fa-eye"></i>
                  </a>



                  <a href="category-admin?mode=edit&id=${c.id}"
                     class="icon-btn edit" title="Chỉnh sửa">
                    <i class="fa fa-pen"></i>
                  </a>



                  <button type="button"
                          class="icon-btn delete"
                          title="Xóa danh mục"
                          onclick="openDeleteModal(${c.id}, '${c.name}')">
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
      <p id="deleteMessage">Bạn có chắc muốn khóa danh mục này không?</p>

      <form id="deleteForm" method="post" action="category-admin">
        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="id" id="deleteCategoryId">

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
    document.getElementById("deleteCategoryId").value = id;
    document.getElementById("deleteMessage").innerHTML =
            'Bạn có chắc muốn khóa danh mục "<b>' + title + '</b>" không?';
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