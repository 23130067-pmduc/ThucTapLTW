<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin</title>
    <link rel="stylesheet" href="css/admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>

<div class="admin">

    <aside class="sidebar">
        <img src="img/gau.png" alt="Logo">
        <p>ADMIN</p>

        <div class="nav">
            <a class="menu-link ${empty page || page == 'dashboard' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/dashboard">
                DashBoard
            </a>
            <a class="menu-link ${page == 'product' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/product-admin">
                Sản phẩm
            </a>
            <a class="menu-link ${page == 'category' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/category-admin">
                Danh mục
            </a>
            <a class="menu-link ${page == 'order' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/order-admin">
                Đơn hàng
            </a>
            <a class="menu-link ${page == 'user' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/user-admin">
                Người dùng
            </a>
            <a class="menu-link ${page == 'banner' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/banner-admin">
                Banner
            </a>
            <a class="menu-link ${page == 'news' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/tin-tuc">
                Tin tức
            </a>
            <a class="menu-link ${page == 'contact' ? 'active' : ''}"
               href="${pageContext.request.contextPath}/contact-admin">
                Liên hệ
            </a>
        </div>
    </aside>

    <section class="content">

        <main>

            <section class="page ${empty page || page == 'dashboard' ? 'active' : ''}">
                <div class="page-header">
                    <h1>Dashboard</h1>
                    <a href="logout" class="logout-btn">Đăng xuất</a>
                </div>
            </section>

            <section id="product" class="page ${page == 'product' ? 'active' : ''}">

                <div class="page-header">
                    <h1>Quản lý sản phẩm</h1>
                    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
                </div>

                <div class="cards">
                    <div class="card">
                        Tổng sản phẩm
                        <span>${totalProducts}</span>
                    </div>
                    <div class="card">
                        Sản phẩm mới / tuần
                        <span>0</span>
                    </div>
                    <div class="card">
                        Đang bán
                        <span>${activeProducts}</span>
                    </div>
                    <div class="card">
                        Ngừng bán
                        <span>${inactiveProducts}</span>
                    </div>
                </div>

                <div class="user-toolbar">
                    <form method="post"
                          action="${pageContext.request.contextPath}/product-admin"
                          class="user-toolbar">
                        <input type="hidden" name="action" value="search">
                        <input type="text" name="keyword"
                               placeholder="Tìm theo tên sản phẩm...">
                        <button type="submit" class="btn-search">
                            <i class="fa fa-search"></i> Tìm
                        </button>
                    </form>

                    <a href="product-admin?mode=add" class="btn-add">
                        <i class="fa fa-plus"></i> Thêm sản phẩm
                    </a>
                </div>

                <div class="user-table-wrapper">
                    <table class="user-table">
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
                        <c:forEach var="p" items="${products}">
                            <tr>
                                <td>${p.id}</td>

                                <td>
                                    <img src="${p.thumbnail}" alt="${p.name}" style="width: 50px; height: 50px; object-fit: cover; border-radius: 5px;">
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${fn:length(p.name) > 50}">
                                            ${fn:substring(p.name, 0, 50)}...
                                        </c:when>
                                        <c:otherwise>
                                            ${p.name}
                                        </c:otherwise>
                                    </c:choose>
                                </td>


                                <td>
                                    <fmt:formatNumber value="${p.price}" type="number"/> đ
                                </td>

                                <td>${p.categoryName}</td>

                                <td>
                                    <span class="status active">
                                        ${p.status}
                                    </span>
                                </td>

                                <td class="action-buttons">

                                    <a href="product-admin?mode=view&id=${p.id}"
                                       class="icon-btn view"
                                       title="Xem chi tiết">
                                        <i class="fa fa-eye"></i>
                                    </a>

                                    <a href="product-admin?mode=edit&id=${p.id}"
                                       class="icon-btn edit"
                                       title="Chỉnh sửa">
                                        <i class="fa fa-pen"></i>
                                    </a>

                                    <a href="product-variant-admin?productId=${p.id}"
                                       class="icon-btn variant"
                                       title="Quản lý biến thể">
                                        <i class="fa fa-layer-group"></i>
                                    </a>

                                    <a href="product-image-admin?productId=${p.id}"
                                       class="icon-btn image"
                                       title="Quản lý ảnh">
                                        <i class="fa fa-images"></i>
                                    </a>

                                    <button class="icon-btn delete"
                                            title="${p.status == 'Đang bán' ? 'Đặt hết hàng' : 'Mở bán lại'}"
                                            onclick="openToggleProductModal(${p.id}, '${p.name}', '${p.status}')">
                                        <i class="fa fa-trash"></i>
                                    </button>

                                </td>

                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <div class="pagination-info">
                            Hiển thị ${(currentPage - 1) * pageSize + 1} - ${currentPage * pageSize > totalProducts ? totalProducts : currentPage * pageSize} của ${totalProducts} sản phẩm
                        </div>
                        <div class="pagination-controls">
                            <c:if test="${currentPage > 1}">
                                <a href="product-admin?page=1" class="page-btn">« Đầu</a>
                                <a href="product-admin?page=${currentPage - 1}" class="page-btn">‹ Trước</a>
                            </c:if>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <c:choose>
                                    <c:when test="${i == currentPage}">
                                        <span class="page-btn active">${i}</span>
                                    </c:when>
                                    <c:when test="${i == 1 || i == totalPages || (i >= currentPage - 2 && i <= currentPage + 2)}">
                                        <a href="product-admin?page=${i}" class="page-btn">${i}</a>
                                    </c:when>
                                    <c:when test="${i == currentPage - 3 || i == currentPage + 3}">
                                        <span class="page-btn dots">...</span>
                                    </c:when>
                                </c:choose>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a href="product-admin?page=${currentPage + 1}" class="page-btn">Sau ›</a>
                                <a href="product-admin?page=${totalPages}" class="page-btn">Cuối »</a>
                            </c:if>
                        </div>
                    </div>
                </c:if>

            </section>

            <section id="category" class="page ${page == 'category' ? 'active' : ''}">

                <div class="page-header">
                    <h1>Quản lý danh mục</h1>
                    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
                </div>

                <div class="cards">
                    <div class="card">
                        Tổng danh mục
                        <span>${totalCategories}</span>
                    </div>
                    <div class="card">
                        Đang dùng
                        <span>${activeCategories}</span>
                    </div>
                    <div class="card">
                        Đã khóa
                        <span>${lockedCategories}</span>
                    </div>
                </div>

                <div class="user-toolbar">
                    <form method="post"
                          action="${pageContext.request.contextPath}/category-admin"
                          class="user-toolbar">
                        <input type="hidden" name="action" value="search">
                        <input type="text" name="keyword"
                               placeholder="Tìm theo tên danh mục...">
                        <button type="submit" class="btn-search">
                            <i class="fa fa-search"></i> Tìm
                        </button>
                    </form>

                    <a href="${pageContext.request.contextPath}/category-admin?mode=add" class="btn-add">
                        <i class="fa fa-plus"></i> Thêm danh mục
                    </a>
                </div>

                <div class="user-table-wrapper">
                    <table class="user-table">
                        <thead>
                        <tr>
                            <th>STT</th>
                            <th>ID</th>
                            <th>Tên danh mục</th>
                            <th>Mô tả</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="c" items="${categories}" varStatus="status">
                            <tr>
                                <td>${status.index + 1}</td>
                                <td>${c.id}</td>
                                <td>${c.name}</td>
                                <td>
                                    <c:if test="${not empty c.description}">
                                        ${c.description}
                                    </c:if>
                                    <c:if test="${empty c.description}">
                                        <span style="color: #999;">Chưa có mô tả</span>
                                    </c:if>
                                </td>
                                <td>
                                    <span class="status ${c.status == 1 ? 'active' : 'inactive'}">
                                        ${c.status == 1 ? 'Đang dùng' : 'Đã khóa'}
                                    </span>
                                </td>
                                <td class="action-buttons">
                                    <a href="${pageContext.request.contextPath}/category-admin?mode=view&id=${c.id}"
                                       class="icon-btn view"
                                       title="Xem chi tiết">
                                        <i class="fa fa-eye"></i>
                                    </a>

                                    <a href="${pageContext.request.contextPath}/category-admin?mode=edit&id=${c.id}"
                                       class="icon-btn edit"
                                       title="Chỉnh sửa">
                                        <i class="fa fa-pen"></i>
                                    </a>

                                    <button class="icon-btn ${c.status == 1 ? 'delete' : 'view'}"
                                            title="${c.status == 1 ? 'Khóa danh mục' : 'Mở khóa'}"
                                            onclick="toggleCategoryStatus(${c.id}, '${c.name}', ${c.status})">
                                        <i class="fa fa-${c.status == 1 ? 'lock' : 'unlock'}"></i>
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
</div>

<div class="modal-overlay" id="toggle-status-modal">
    <div class="modal-content modal-small">
        <div class="modal-header">
            <h2 id="toggle-status-title">Xác nhận</h2>
        </div>
        <div class="modal-body">
            <p id="toggle-status-message"></p>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn-cancel" onclick="closeToggleStatusModal()">Hủy</button>
            <button type="button" class="btn-delete" onclick="confirmToggleStatus()">Xác nhận</button>
        </div>
    </div>
</div>

<div class="modal-overlay" id="product-modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2 id="product-modal-title">Thêm sản phẩm</h2>
            <button class="modal-close" onclick="closeProductModal()">&times;</button>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/product-admin" id="product-form">
            <input type="hidden" name="action" id="product-action" value="add">
            <input type="hidden" name="id" id="product-id">

            <div class="form-section">
                <h3>● Thông tin sản phẩm</h3>
                <div class="form-row">
                    <div class="form-group">
                        <label>Tên sản phẩm</label>
                        <input type="text" name="name" id="product-name" required>
                    </div>
                    <div class="form-group">
                        <label>Giá</label>
                        <input type="number" name="price" id="product-price" required>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Danh mục</label>
                        <select name="category_id" id="product-category">
                            <c:forEach var="c" items="${categories}">
                                <option value="${c.id}">${c.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Trạng thái</label>
                        <select name="status" id="product-status">
                            <option value="Đang bán">Đang bán</option>
                            <option value="Hết hàng">Hết hàng</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label>URL Ảnh</label>
                    <input type="text" name="thumbnail" id="product-thumbnail">
                </div>
            </div>

            <div class="modal-footer">
                <button type="submit" class="btn-save">Lưu</button>
                <button type="button" class="btn-cancel" onclick="closeProductModal()">Hủy</button>
            </div>
        </form>
    </div>
</div>

<div class="modal-overlay" id="view-product-modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2>Xem chi tiết sản phẩm</h2>
            <button class="modal-close" onclick="closeViewProductModal()">&times;</button>
        </div>
        <div class="modal-body">
            <div class="form-section">
                <h3>● Thông tin sản phẩm</h3>
                <div class="info-row">
                    <label>ID:</label>
                    <span id="view-product-id"></span>
                </div>
                <div class="info-row">
                    <label>Tên sản phẩm:</label>
                    <span id="view-product-name"></span>
                </div>
                <div class="info-row">
                    <label>Giá:</label>
                    <span id="view-product-price"></span>
                </div>
                <div class="info-row">
                    <label>Danh mục:</label>
                    <span id="view-product-category"></span>
                </div>
                <div class="info-row">
                    <label>Trạng thái:</label>
                    <span id="view-product-status"></span>
                </div>
                <div class="info-row">
                    <label>Ảnh:</label>
                    <img id="view-product-image" style="max-width: 200px; border-radius: 8px; margin-top: 10px;">
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn-cancel" onclick="closeViewProductModal()">Đóng</button>
        </div>
    </div>
</div>

<div class="modal-overlay" id="toggle-product-modal">
    <div class="modal-content modal-small">
        <div class="modal-header">
            <h2>Xác nhận</h2>
        </div>
        <div class="modal-body">
            <p id="toggle-product-message"></p>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/product-admin" id="toggle-product-form">
            <input type="hidden" name="action" value="toggle-status">
            <input type="hidden" name="id" id="toggle-product-id">
            <div class="modal-footer">
                <button type="button" class="btn-cancel" onclick="closeToggleProductModal()">Hủy</button>
                <button type="submit" class="btn-delete">Xác nhận</button>
            </div>
        </form>
    </div>
</div>

<script src="javaScript/adminProduct.js"></script>
<script src="javaScript/adminCategory.js"></script>

</body>
</html>
