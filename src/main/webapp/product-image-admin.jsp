<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Ảnh Sản Phẩm</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product-image-admin.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="user">
    <aside class="sidebar">
        <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
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
        </div>
    </aside>

    <section class="image-content">
        <header class="topbar">
            <div class="topbar-left">
                <a href="${pageContext.request.contextPath}/product-admin" class="back-to-product-btn" title="Quay về quản lý sản phẩm">
                    <i class="fa fa-arrow-left"></i>
                </a>
                <h1>Quản Lý Ảnh Sản Phẩm</h1>
            </div>
            <div class="actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>

        <main id="page">
            <c:if test="${param.error == 'true'}">
                <div class="alert alert-danger">Có lỗi xảy ra khi xử lý yêu cầu!</div>
            </c:if>
            <c:if test="${param.error == 'no_image'}">
                <div class="alert alert-danger">Vui lòng chọn ảnh để tải lên!</div>
            </c:if>
            <c:if test="${param.error == 'invalid_id'}">
                <div class="alert alert-danger">Dữ liệu ảnh không hợp lệ!</div>
            </c:if>
            <c:if test="${param.error == 'not_found'}">
                <div class="alert alert-danger">Không tìm thấy ảnh sản phẩm!</div>
            </c:if>

            <div class="cards">
                <div class="card">Tổng Ảnh<br><span>${totalImages}</span></div>
            </div>

            <div class="images-toolbar">
                <a href="${pageContext.request.contextPath}/product-image-admin?productId=${productId}&mode=add" class="btn-add">
                    <i class="fa fa-plus"></i> Thêm Ảnh
                </a>
            </div>

            <div class="image-table-wrapper">
                <table class="image-table">
                    <thead>
                    <tr>
                        <th width="5%">STT</th>
                        <th width="8%">ID</th>
                        <th width="40%">Hình Ảnh</th>
                        <th width="12%">Ảnh Chính</th>
                        <th width="20%">Hành Động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty images}">
                            <tr>
                                <td colspan="5" class="text-center">Chưa có ảnh nào</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="img" items="${images}" varStatus="status">
                                <tr>
                                    <td>${status.index + 1}</td>
                                    <td>${img.id}</td>
                                    <td>
                                        <img src="${pageContext.request.contextPath}/${img.imageUrl}" alt="Product Image" class="product-thumbnail">
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${img.main}">
                                                <span class="status active"><i class="fa fa-check-circle"></i> Có</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status inactive"><i class="fa fa-times-circle"></i> Không</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="actions">
                                            <a href="${pageContext.request.contextPath}/product-image-admin?productId=${productId}&mode=view&id=${img.id}" class="icon-btn view" title="Xem">
                                                <i class="fa fa-eye"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/product-image-admin?productId=${productId}&mode=edit&id=${img.id}" class="icon-btn edit" title="Sửa">
                                                <i class="fa fa-pen"></i>
                                            </a>
                                            <button type="button" class="icon-btn delete js-delete-image" data-image-id="${img.id}" title="Xóa">
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
            </div>
        </main>
    </section>
</div>

<div id="delete-modal" class="modal-overlay" aria-hidden="true">
    <div class="modal-content modal-small">
        <div class="modal-header">
            <h3>Xác Nhận Xóa</h3>
            <button type="button" class="modal-close js-close-delete-modal">&times;</button>
        </div>
        <div class="modal-body">
            <p id="delete-message">Bạn có chắc chắn muốn xóa ảnh này?</p>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn-cancel js-close-delete-modal">Hủy</button>
            <button type="button" class="btn-delete" id="confirm-delete-btn">Xóa</button>
        </div>
    </div>
</div>

<form id="delete-form" action="${pageContext.request.contextPath}/product-image-admin" method="post">
    <input type="hidden" name="action" value="delete">
    <input type="hidden" name="productId" value="${productId}">
    <input type="hidden" name="id" id="delete-image-id" value="">
</form>

<script>
    (() => {
        const modal = document.getElementById('delete-modal');
        const deleteForm = document.getElementById('delete-form');
        const deleteImageIdInput = document.getElementById('delete-image-id');
        const confirmDeleteButton = document.getElementById('confirm-delete-btn');
        const deleteButtons = document.querySelectorAll('.js-delete-image');
        const closeButtons = document.querySelectorAll('.js-close-delete-modal');

        function openDeleteModal(imageId) {
            deleteImageIdInput.value = imageId;
            modal.classList.add('is-open');
            modal.setAttribute('aria-hidden', 'false');
        }

        function closeDeleteModal() {
            deleteImageIdInput.value = '';
            modal.classList.remove('is-open');
            modal.setAttribute('aria-hidden', 'true');
        }

        deleteButtons.forEach((button) => {
            button.addEventListener('click', () => openDeleteModal(button.dataset.imageId));
        });

        closeButtons.forEach((button) => {
            button.addEventListener('click', closeDeleteModal);
        });

        modal.addEventListener('click', (event) => {
            if (event.target === modal) {
                closeDeleteModal();
            }
        });

        confirmDeleteButton.addEventListener('click', () => {
            if (deleteImageIdInput.value) {
                deleteForm.submit();
            }
        });
    })();
</script>
</body>
</html>
