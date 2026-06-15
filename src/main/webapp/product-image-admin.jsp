<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
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

            <c:if test="${userlogin.permissions.contains('DASHBOARD_VIEW')}">
                <a href="${pageContext.request.contextPath}/dashboard" class="nav-item ">
                    <i class="fa-solid fa-gauge"></i><span>Thống kê</span>
                </a>
            </c:if>

            <c:if test="${userlogin.permissions.contains('PRODUCT_VIEW')}">
                <a href="${pageContext.request.contextPath}/product-admin" class="nav-item active">
                    <i class="fa-solid fa-shirt"></i><span>Sản phẩm</span>
                </a>
            </c:if>

            <c:if test="${userlogin.permissions.contains('WAREHOUSE_VIEW')}">
                <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item">
                    <i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span>
                </a>
            </c:if>

            <c:if test="${userlogin.permissions.contains('RETURN_RECEIPT_VIEW')}">
                <a href="${pageContext.request.contextPath}/return-order-admin" class="nav-item">
                    <i class="fa-solid fa-rotate-left"></i><span>Hoàn hàng</span>
                </a>
            </c:if>

            <c:if test="${userlogin.permissions.contains('REPORT_VIEW')}">
                <a href="${pageContext.request.contextPath}/profit-report" class="nav-item">
                    <i class="fa-solid fa-chart-line"></i><span>Lợi nhuận</span>
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

            <c:if test="${userlogin.permissions.contains('VOUCHER_VIEW')}">
                <a href="${pageContext.request.contextPath}/voucher-admin" class="nav-item">
                    <i class="fa-solid fa-ticket"></i><span>Mã giảm giá</span>
                </a>
            </c:if>

            <c:if test="${userlogin.permissions.contains('PROMOTION_EVENT_VIEW')}">
                <a href="${pageContext.request.contextPath}/promotion-event-admin" class="nav-item">
                    <i class="fa-solid fa-tags"></i>
                    <span>Khuyến mãi</span>
                </a>
            </c:if>

            <c:if test="${userlogin.permissions.contains('BANNER_VIEW')}">
                <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item">
                    <i class="fa-solid fa-image"></i><span>Banner</span>
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
            <c:if test="${param.error == 'invalid_status'}">
                <div class="alert alert-danger">Trạng thái ảnh không hợp lệ!</div>
            </c:if>

            <div class="cards">
                <div class="card">Tổng Ảnh<br><span>${totalImages}</span></div>
                <div class="card">Đang Hiển Thị<br><span>${totalVisibleImages}</span></div>
                <div class="card">Đang Ẩn<br><span>${totalHiddenImages}</span></div>
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
                        <th width="34%">Hình Ảnh</th>
                        <th width="12%">Ảnh Chính</th>
                        <th width="14%">Trạng Thái</th>
                        <th width="20%">Hành Động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty images}">
                            <tr>
                                <td colspan="6" class="text-center">Chưa có ảnh nào</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="img" items="${images}" varStatus="status">
                                <tr class="${img.status == 'Ẩn' ? 'row-hidden' : ''}">
                                    <td>${status.index + 1}</td>
                                    <td>${img.id}</td>
                                    <td>
                                        <c:set var="imageUrl" value="${img.imageUrl}" />
                                        <c:choose>
                                            <c:when test="${fn:startsWith(imageUrl, 'http://') || fn:startsWith(imageUrl, 'https://')}">
                                                <img src="${imageUrl}" alt="Product Image" class="product-thumbnail">
                                            </c:when>
                                            <c:when test="${fn:startsWith(imageUrl, '/')}">
                                                <img src="${pageContext.request.contextPath}${imageUrl}" alt="Product Image" class="product-thumbnail">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/${imageUrl}" alt="Product Image" class="product-thumbnail">
                                            </c:otherwise>
                                        </c:choose>
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
                                        <c:choose>
                                            <c:when test="${img.status == 'Ẩn'}">
                                                <span class="status hidden"><i class="fa fa-eye-slash"></i> Ẩn</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status active"><i class="fa fa-eye"></i> Hiển thị</span>
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
                                            <c:choose>
                                                <c:when test="${img.status == 'Ẩn'}">
                                                    <button type="button"
                                                            class="icon-btn unlock js-toggle-image-status"
                                                            data-image-id="${img.id}"
                                                            data-next-status="Hiển thị"
                                                            data-action-label="hiển thị lại"
                                                            title="Hiển thị lại">
                                                        <i class="fa fa-lock-open"></i>
                                                    </button>
                                                </c:when>
                                                <c:otherwise>
                                                    <button type="button"
                                                            class="icon-btn lock js-toggle-image-status"
                                                            data-image-id="${img.id}"
                                                            data-next-status="Ẩn"
                                                            data-action-label="ẩn"
                                                            title="Ẩn ảnh">
                                                        <i class="fa fa-lock"></i>
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
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

<div id="statusModal" class="modal-overlay" aria-hidden="true">
    <div class="modal" role="dialog" aria-modal="true" aria-labelledby="statusModalTitle">
        <h3 id="statusModalTitle">Xác nhận</h3>
        <p id="statusMessage"></p>

        <form id="statusForm" action="${pageContext.request.contextPath}/product-image-admin" method="post">
            <input type="hidden" name="action" value="updateStatus">
            <input type="hidden" name="productId" value="${productId}">
            <input type="hidden" name="id" id="statusImageId" value="">
            <input type="hidden" name="status" id="nextImageStatus" value="">

            <div class="modal-actions">
                <button type="button" class="btn-secondary js-close-status-modal">Hủy</button>
                <button type="submit" class="btn-danger" id="statusSubmitBtn">Xác nhận</button>
            </div>
        </form>
    </div>
</div>

<script>
    (() => {
        const statusModal = document.getElementById('statusModal');
        const statusMessage = document.getElementById('statusMessage');
        const statusImageId = document.getElementById('statusImageId');
        const nextImageStatus = document.getElementById('nextImageStatus');
        const statusSubmitBtn = document.getElementById('statusSubmitBtn');
        const statusModalTitle = document.getElementById('statusModalTitle');

        const statusButtons = document.querySelectorAll('.js-toggle-image-status');
        const closeButtons = document.querySelectorAll('.js-close-status-modal');

        function openModal() {
            statusModal.classList.add('show');
            statusModal.setAttribute('aria-hidden', 'false');
        }

        function closeModal() {
            statusImageId.value = '';
            nextImageStatus.value = '';
            statusModal.classList.remove('show');
            statusModal.setAttribute('aria-hidden', 'true');
        }

        function openStatusModal(button) {
            const imageId = button.dataset.imageId;
            const status = button.dataset.nextStatus;

            statusImageId.value = imageId;
            nextImageStatus.value = status;

            statusSubmitBtn.classList.remove('btn-danger', 'btn-success');

            if (status === 'Hiển thị') {
                statusModalTitle.innerText = 'Xác nhận hiển thị ảnh';
                statusMessage.innerHTML = 'Bạn có chắc muốn hiển thị lại ảnh này không?';
                statusSubmitBtn.innerText = 'Hiển thị ảnh';
                statusSubmitBtn.classList.add('btn-success');
            } else {
                statusModalTitle.innerText = 'Xác nhận ẩn ảnh';
                statusMessage.innerHTML = 'Bạn có chắc muốn ẩn ảnh này không? Ảnh sẽ không hiển thị ở giao diện khách hàng.';
                statusSubmitBtn.innerText = 'Ẩn ảnh';
                statusSubmitBtn.classList.add('btn-danger');
            }

            openModal();
        }

        statusButtons.forEach((button) => {
            button.addEventListener('click', () => openStatusModal(button));
        });

        closeButtons.forEach((button) => {
            button.addEventListener('click', closeModal);
        });

        statusModal.addEventListener('click', (event) => {
            if (event.target === statusModal) {
                closeModal();
            }
        });

        document.addEventListener('keydown', (event) => {
            if (event.key === 'Escape') {
                closeModal();
            }
        });
    })();
</script>
</body>
</html>
