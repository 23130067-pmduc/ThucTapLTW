<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý biến thể sản phẩm</title>
    <link rel="stylesheet" href="css/product-variant-admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="admin-layout">
    <aside class="sidebar">
        <img src="img/quanao/gau.png" alt="Logo quản trị">
        <p>ADMIN</p>

        <nav class="nav">
            <a href="dashboard" class="nav-item">Dashboard</a>
            <a href="product-admin" class="nav-item active">Sản phẩm</a>
            <a href="category-admin" class="nav-item">Danh mục</a>
            <a href="order-admin" class="nav-item">Đơn hàng</a>
            <a href="user-admin" class="nav-item">Người dùng</a>
            <a href="banner-admin" class="nav-item">Banner</a>
            <a href="news-admin" class="nav-item">Tin tức</a>
            <a href="contact-admin" class="nav-item">Liên hệ</a>
        </nav>
    </aside>

    <section class="variant-page">
        <header class="topbar">
            <div class="topbar-left">
                <a href="product-admin" class="back-to-product-btn" title="Quay lại trang sản phẩm">
                    <i class="fa fa-arrow-left"></i>
                </a>
                <h1>Sản phẩm biến thể</h1>
            </div>

            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
        </header>

        <main>
            <section class="stats-grid">
                <article class="stat-card">
                    <span class="stat-label">Tổng biến thể</span>
                    <span class="stat-value">${total}</span>
                </article>
                <article class="stat-card">
                    <span class="stat-label">Tổng tồn kho</span>
                    <span class="stat-value">${totalStock}</span>
                </article>
                <article class="stat-card">
                    <span class="stat-label">Số size</span>
                    <span class="stat-value">${totalSize}</span>
                </article>
                <article class="stat-card">
                    <span class="stat-label">Số màu</span>
                    <span class="stat-value">${totalColor}</span>
                </article>
            </section>

            <section class="variants-toolbar">
                <a href="product-variant-admin?mode=add&amp;productId=${productId}" class="btn-add">
                    <i class="fa fa-plus"></i>
                    <span>Thêm biến thể</span>
                </a>
            </section>

            <section class="variant-table-wrapper">
                <table class="variant-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Size</th>
                        <th>Màu</th>
                        <th>Giá</th>
                        <th>Giá sale</th>
                        <th>Tồn kho</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty variants}">
                            <c:forEach items="${variants}" var="variant">
                                <tr>
                                    <td>${variant.id}</td>
                                    <td>${variant.sizeName}</td>
                                    <td>${variant.colorName}</td>
                                    <td><fmt:formatNumber value="${variant.price}" type="number"/> ₫</td>
                                    <td><fmt:formatNumber value="${variant.salePrice}" type="number"/> ₫</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${variant.stock == 0}">
                                                <span class="status status-out">Hết hàng</span>
                                            </c:when>
                                            <c:when test="${variant.stock < 10}">
                                                <span class="status status-low">Sắp hết (${variant.stock})</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status status-instock">${variant.stock}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="table-actions">
                                            <a href="product-variant-admin?mode=view&amp;id=${variant.id}&amp;productId=${productId}"
                                               class="icon-btn view"
                                               title="Xem chi tiết">
                                                <i class="fa-solid fa-eye"></i>
                                            </a>

                                            <a href="product-variant-admin?mode=edit&amp;id=${variant.id}&amp;productId=${productId}"
                                               class="icon-btn edit"
                                               title="Chỉnh sửa">
                                                <i class="fa-solid fa-pen"></i>
                                            </a>

                                            <button type="button"
                                                    class="icon-btn delete js-open-delete-modal"
                                                    title="Xóa"
                                                    data-id="${variant.id}"
                                                    data-name="${variant.sizeName} - ${variant.colorName}">
                                                <i class="fa fa-trash"></i>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="7" class="empty-state">Chưa có biến thể nào cho sản phẩm này.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </section>
        </main>
    </section>
</div>

<div id="deleteModal" class="modal-overlay" aria-hidden="true">
    <div class="modal" role="dialog" aria-modal="true" aria-labelledby="deleteModalTitle">
        <h3 id="deleteModalTitle">Xác nhận xóa</h3>
        <p id="deleteMessage">Bạn có chắc muốn xóa biến thể này không?</p>

        <form method="post" action="product-variant-admin">
            <input type="hidden" name="action" value="delete">
            <input type="hidden" name="id" id="deleteVariantId">
            <input type="hidden" name="productId" value="${productId}">

            <div class="modal-actions">
                <button type="button" class="btn-secondary js-close-delete-modal">Hủy</button>
                <button type="submit" class="btn-danger">Xóa</button>
            </div>
        </form>
    </div>
</div>

<script src="javaScript/product-variant-admin.js"></script>
</body>
</html>
