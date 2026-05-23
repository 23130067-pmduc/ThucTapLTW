<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Sản phẩm</title>

  <link rel="stylesheet" href="css/user.css">
  <link rel="stylesheet" href="css/product-admin.css">
  <link rel="stylesheet" href="css/pagination.css">

  <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="product-admin-page">

<div class="user">
  <aside class="sidebar">
    <img src="img/gau.png" alt="Logo">
    <p>ADMIN</p>

    <div class="nav" id="menu">
      <a href="${pageContext.request.contextPath}/dashboard" class="nav-item">Dashboard</a>
      <a href="${pageContext.request.contextPath}/product-admin" class="nav-item active">Sản phẩm</a>
      <a href="${pageContext.request.contextPath}/category-admin" class="nav-item">Danh mục</a>
      <a href="${pageContext.request.contextPath}/order-admin" class="nav-item">Đơn hàng</a>
      <a href="${pageContext.request.contextPath}/user-admin" class="nav-item">Người dùng</a>
      <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item">Banner</a>
      <a href="${pageContext.request.contextPath}/news-admin" class="nav-item">Tin tức</a>
      <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item">Thông báo</a>
      <a href="${pageContext.request.contextPath}/contact-admin" class="nav-item">Liên hệ</a>
      <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item">Hồ sơ</a>
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
          <div class="card-value">${newProductThisWeek}</div>
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

          <button type="button" class="btn-search" id="searchBtn">
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
            <th>Tồn kho</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
          </thead>

          <tbody id="productTableBody">
          <c:if test="${empty products}">
            <tr>
              <td colspan="8" class="empty-row">Chưa có sản phẩm nào</td>
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

              <td class="col-name" title="${p.name}">${p.name}</td>

              <td class="col-price">
                <fmt:formatNumber value="${p.price}" pattern="#,##0" /> đ
              </td>

              <td class="col-category">${p.categoryName}</td>

              <td class="col-stock">${p.totalStock}</td>

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

                  <c:choose>
                    <c:when test="${p.status eq 'Đang bán'}">
                      <button type="button"
                              class="icon-btn delete"
                              title="Ngừng bán sản phẩm"
                              onclick="openStatusModal(${p.id}, '${p.name}', 'Ngừng bán')">
                        <i class="fa fa-trash"></i>
                      </button>
                    </c:when>

                    <c:otherwise>
                      <button type="button"
                              class="icon-btn restore"
                              title="Mở bán lại sản phẩm"
                              onclick="openStatusModal(${p.id}, '${p.name}', 'Đang bán')">
                        <i class="fa fa-rotate-left"></i>
                      </button>
                    </c:otherwise>
                  </c:choose>
                </div>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>



        <c:set var="startPage" value="${currentPage - 2}" />
        <c:set var="endPage" value="${currentPage + 2}" />

        <c:if test="${startPage < 1}">
          <c:set var="startPage" value="1" />
        </c:if>

        <c:if test="${endPage > totalPages}">
          <c:set var="endPage" value="${totalPages}" />
        </c:if>

        <c:if test="${totalPages > 1}">
          <div class="pagination" id="productPagination">

            <c:if test="${currentPage > 1}">
              <a class="page-btn" href="product-admin?page=${currentPage - 1}">
                Trước
              </a>
            </c:if>

            <c:forEach begin="${startPage}" end="${endPage}" var="i">
              <a href="product-admin?page=${i}"
                 class="page-btn ${i == currentPage ? 'active' : ''}">
                  ${i}
              </a>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
              <a class="page-btn" href="product-admin?page=${currentPage + 1}">
                Sau
              </a>
            </c:if>

          </div>
        </c:if>
      </div>


    </main>
  </section>
</div>
<div id="statusModal" class="modal-overlay">
  <div class="modal">
    <h3 id="statusModalTitle">Xác nhận</h3>
    <p id="statusMessage"></p>

    <form id="statusForm" method="post" action="product-admin">
      <input type="hidden" name="action" value="updateStatus">
      <input type="hidden" name="id" id="statusProductId">
      <input type="hidden" name="status" id="statusProductValue">

      <div class="modal-actions">
        <button type="button" class="btn-secondary" onclick="closeStatusModal()">Hủy</button>
        <button type="submit" class="btn-danger " id="statusSubmitBtn">Khóa</button>
      </div>
    </form>
  </div>
</div>

<script>
  function openStatusModal(id, title, status) {
    document.getElementById("statusProductId").value = id;
    document.getElementById("statusProductValue").value = status;

    document.getElementById("statusModalTitle").innerText =
            status === "Đang bán" ? "Xác nhận mở bán lại" : "Xác nhận ngừng bán";

    document.getElementById("statusMessage").innerHTML =
            status === "Đang bán"
                    ? 'Bạn có chắc muốn mở bán lại sản phẩm "<b>' + title + '</b>" không?'
                    : 'Bạn có chắc muốn ngừng bán sản phẩm "<b>' + title + '</b>" không?';

    document.getElementById("statusSubmitBtn").innerText =
            status === "Đang bán" ? "Mở bán lại" : "Ngừng bán";

    document.getElementById("statusModal").style.display = "flex";
  }

  function closeDeleteModal() {
    document.getElementById("deleteModal").style.display = "none";
  }

  function closeStatusModal() {
    document.getElementById("statusModal").style.display = "none";
  }
</script>

<script src="javaScript/searchProduct.js"></script>

</body>
</html>