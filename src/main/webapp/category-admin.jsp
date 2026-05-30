<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin - Danh mục</title>
  <link rel="stylesheet" href="css/category-admin.css">
  <link rel="stylesheet" href="css/pagination.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="user">
  <aside class="sidebar">
    <img src="img/gau.png" alt="" Logo>
    <p>ADMIN</p>

    <div class="nav" id="menu">
      <a href="${pageContext.request.contextPath}/dashboard" class="nav-item"><i class="fa-solid fa-gauge"></i><span>Dashboard</span></a>
      <a href="${pageContext.request.contextPath}/product-admin" class="nav-item"><i class="fa-solid fa-shirt"></i><span>Sản phẩm</span></a>
      <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item"><i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span></a>
      <a href="${pageContext.request.contextPath}/category-admin" class="nav-item active"><i class="fa-solid fa-list"></i><span>Danh mục</span></a>
      <a href="${pageContext.request.contextPath}/order-admin" class="nav-item"><i class="fa-solid fa-receipt"></i><span>Đơn hàng</span></a>
      <a href="${pageContext.request.contextPath}/user-admin" class="nav-item"><i class="fa-solid fa-users"></i><span>Người dùng</span></a>
      <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item"><i class="fa-solid fa-image"></i><span>Banner</span></a>
      <a href="${pageContext.request.contextPath}/news-admin" class="nav-item"><i class="fa-solid fa-newspaper"></i><span>Tin tức</span></a>
      <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item"><i class="fa-solid fa-bell"></i><span>Thông báo</span></a>
      <a href="${pageContext.request.contextPath}/contact-admin" class="nav-item"><i class="fa-solid fa-envelope"></i><span>Liên hệ</span></a>
      <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item"><i class="fa-solid fa-user-gear"></i><span>Hồ sơ</span></a>
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
          <div class="card">Tổng danh mục<br><span id="dashboard-total-category">${totalAllCategory}</span></div>
          <div class="card">Đang hoạt động<br><span id="dashboard-total-category-active">${totalActive}</span></div>
        </div>

        <div class="category-toolbar">

          <form action="category-admin" method="get" class="category-search-form" id="categorySearchForm">
            <div class="search-box">
              <input type="text"
                     name="keyword"
                     id="keywordInput"
                     placeholder="Tìm theo tên danh mục..."
                     value="${keyword}">
            </div>

            <button type="button" class="btn-search" id="searchBtn">
              <i class="fa-solid fa-magnifying-glass"></i>
              <span>Tìm</span>
            </button>
          </form>

          <div class="toolbar-right">
            <a href="category-admin?mode=add" class="btn-add">
              <i class="fa fa-plus"></i> Thêm danh mục
            </a>
          </div>

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

            <c:forEach items="${categorys}" var="c">
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

          <c:if test="${totalPages > 1}">
            <div class="pagination" id="pagination">

              <c:if test="${currentPage > 1}">
                <a class="page-btn" href="category-admin?page=${currentPage - 1}">
                  Trước
                </a>
              </c:if>

              <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="category-admin?page=${i}"
                   class="page-btn ${i == currentPage ? 'active' : ''}">
                    ${i}
                </a>
              </c:forEach>

              <c:if test="${currentPage < totalPages}">
                <a class="page-btn" href="category-admin?page=${currentPage + 1}">
                  Sau
                </a>
              </c:if>

            </div>
          </c:if>

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
          <button type="submit" class="btn-danger">Khóa</button>
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
<script src="javaScript/searchCategory.js"></script>

</html>