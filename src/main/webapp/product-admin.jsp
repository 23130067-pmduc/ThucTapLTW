<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Sản phẩm</title>

  <link rel="stylesheet" href="css/user.css">
  <link rel="stylesheet" href="css/product-admin.css">

  <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="product-admin-page">

<div class="user">
  <aside class="sidebar">
    <img src="img/gau.png" alt="Logo">
    <p>ADMIN</p>

    <div class="nav" id="menu">
      <a href="dashboard" class="nav-item">Dashboard</a>
      <a href="product-admin" class="nav-item active">Sản phẩm</a>
      <a href="category-admin" class="nav-item">Danh mục</a>
      <a href="order-admin" class="nav-item">Đơn hàng</a>
      <a href="user-admin" class="nav-item">Người dùng</a>
      <a href="banner-admin" class="nav-item">Banner</a>
      <a href="news-admin" class="nav-item">Tin tức</a>
      <a href="contact-admin" class="nav-item">Liên hệ</a>
    </div>
  </aside>

  <section class="content product-admin-content">
    <header class="topbar">
      <h1>Quản lý sản phẩm</h1>
      <div class="actions">
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
      </div>
    </header>

    <main class="product-admin-main">

      <div class="product-cards">
        <div class="product-card">
          <div class="card-title">Tổng sản phẩm</div>
          <div class="card-value">${totalProducts}</div>
        </div>

        <div class="product-card">
          <div class="card-title">Sản phẩm mới / tuần</div>
          <div class="card-value">${newProductsThisWeek}</div>
        </div>

        <div class="product-card">
          <div class="card-title">Đang bán</div>
          <div class="card-value">${activeProducts}</div>
        </div>

        <div class="product-card">
          <div class="card-title">Ngừng bán</div>
          <div class="card-value">${inactiveProducts}</div>
        </div>
      </div>

      <div class="product-toolbar">
        <form action="product-admin" method="get" class="product-search-form" id="productSearchForm">
          <div class="search-box">
            <input type="text"
                   name="keyword"
                   id="keywordInput"
                   placeholder="Tìm theo tên sản phẩm..."
                   value="${keyword}">
          </div>

          <button type="submit" class="btn-search">
            <i class="fa-solid fa-magnifying-glass"></i>
            <span>Tìm</span>
          </button>
        </form>

        <div class="toolbar-right">
          <a href="product-admin?mode=add" class="btn-add-product">
            <i class="fa-solid fa-plus"></i>
            <span>Thêm sản phẩm</span>
          </a>
        </div>
      </div>

      <div class="product-table-wrapper">
        <table class="product-table">
          <thead>
          <tr>
            <th>ID</th>
            <th>Ảnh</th>
            <th>Tên sản phẩm</th>
            <th>Giá</th>
            <th>Danh mục</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
          </thead>

          <tbody>
          <c:if test="${empty products}">
            <tr>
              <td colspan="7" class="empty-row">Chưa có sản phẩm nào</td>
            </tr>
          </c:if>

          <c:forEach items="${products}" var="p">
            <tr>
              <td class="col-id">${p.id}</td>

              <td class="col-image">
                <img src="${p.thumbnail}"
                     alt="${p.name}"
                     class="product-thumb"
                     onerror="handleImageError(this)">
              </td>

              <td class="col-name">${p.name}</td>

              <td class="col-price">
                <fmt:formatNumber value="${p.price}" pattern="#,##0" /> đ
              </td>

              <td class="col-category">${p.categoryName}</td>

              <td class="col-status">
                                <span class="status-badge ${p.status == 'Đang bán' ? 'active' : 'inactive'}">
                                    ${p.status}
                                </span>
              </td>

              <td class="col-actions">
                <div class="action-list">
                  <a href="product-admin?mode=view&id=${p.id}"
                     class="action-btn view"
                     title="Xem">
                    <i class="fa-solid fa-eye"></i>
                  </a>

                  <a href="product-admin?mode=edit&id=${p.id}"
                     class="action-btn edit"
                     title="Sửa">
                    <i class="fa-solid fa-pen"></i>
                  </a>

                  <a href="product-variant-admin?productId=${p.id}"
                     class="action-btn variant"
                     title="Biến thể">
                    <i class="fa-solid fa-layer-group"></i>
                  </a>

                  <a href="product-image-admin?productId=${p.id}"
                     class="action-btn image"
                     title="Ảnh sản phẩm">
                    <i class="fa-regular fa-image"></i>
                  </a>

                  <a href="javascript:void(0)"
                     class="action-btn delete"
                     title="Xóa"
                     onclick="confirmDelete(${p.id}, '${p.name}')">
                    <i class="fa-solid fa-trash"></i>
                  </a>
                </div>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>

      <c:if test="${not empty totalPages and totalPages > 1}">
        <div class="pagination">
          <c:forEach begin="1" end="${totalPages}" var="i">
            <a href="product-admin?page=${i}&keyword=${keyword}"
               class="page-link ${currentPage == i ? 'active' : ''}">
                ${i}
            </a>
          </c:forEach>
        </div>
      </c:if>

    </main>
  </section>
</div>

<script src="javaScript/product-admin.js"></script>
</body>
</html>