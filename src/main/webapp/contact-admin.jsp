<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>



<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liên hệ - Admin</title>
    <link rel="stylesheet" href="css/contact.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
</head>
<body>
<div class="user">
    <div class="toast-container" id="toastContainer"></div>
    <aside class="sidebar">
        <img src="img/gau.png" alt="" Logo>
        <p>ADMIN</p>

        <div class="nav" id="menu">
            <a href="${pageContext.request.contextPath}/dashboard" class="nav-item"><i class="fa-solid fa-gauge"></i><span>Thống kê</span></a>
            <a href="${pageContext.request.contextPath}/product-admin" class="nav-item"><i class="fa-solid fa-shirt"></i><span>Sản phẩm</span></a>
            <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item"><i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span></a>
            <a href="${pageContext.request.contextPath}/profit-report" class="nav-item"><i class="fa-solid fa-chart-line"></i><span>Lợi nhuận</span></a>
            <a href="${pageContext.request.contextPath}/category-admin" class="nav-item"><i class="fa-solid fa-list"></i><span>Danh mục</span></a>
            <a href="${pageContext.request.contextPath}/order-admin" class="nav-item"><i class="fa-solid fa-receipt"></i><span>Đơn hàng</span></a>
            <a href="${pageContext.request.contextPath}/return-order-admin" class="nav-item"><i class="fa-solid fa-rotate-left"></i><span>Hoàn hàng</span></a>
            <a href="${pageContext.request.contextPath}/user-admin" class="nav-item"><i class="fa-solid fa-users"></i><span>Người dùng</span></a>
            <a href="${pageContext.request.contextPath}/voucher-admin" class="nav-item"><i class="fa-solid fa-ticket"></i><span>Mã giảm giá</span></a>
            <a href="${pageContext.request.contextPath}/promotion-event-admin" class="nav-item"><i class="fa-solid fa-tags"></i><span>Khuyến mãi</span></a>
            <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item"><i class="fa-solid fa-image"></i><span>Banner</span></a>
            <a href="${pageContext.request.contextPath}/news-admin" class="nav-item"><i class="fa-solid fa-newspaper"></i><span>Tin tức</span></a>
            <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item"><i class="fa-solid fa-bell"></i><span>Thông báo</span></a>
            <a href="${pageContext.request.contextPath}/contact-admin" class="nav-item active"><i class="fa-solid fa-envelope"></i><span>Liên hệ</span></a>
            <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item"><i class="fa-solid fa-user-gear"></i><span>Hồ sơ</span></a>
        </div>
    </aside>

    <section class="content">
        <!-- PHẦN HEADER -->
        <header class="topbar">
            <h1 id="pageTitle">Liên hệ</h1>
            <div class="actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>



        <main id="page">
            <!-- DASHBROAD -->
            <section id="dashboard" class="page active">
                <div class="cards">
                    <div class="card">Tổng liên hệ<br><span id="dashboard-total-contact">${total}</span></div>
                    <div class="card">Liên hệ mới<br><span id="dashboard-total-contact-new">${totalNew}</span></div>
                    <div class="card">Liên hệ đang xử lý<br><span id="dashboard-total-contact-processing">${totalProcessing}</span></div>
                    <div class="card">Liên hệ đã xử lý<br><span id="dashboard-total-contact-closed">${totalClosed}</span></div>
                </div>

                <div class="contact-toolbar">
                    <a href="contact-admin?mode=add" class="btn-add">
                        <i class="fa fa-plus"></i> Thêm liên hệ
                    </a>
                </div>


                <div class="contact-table-wrapper">
                    <!-- TABLE USER -->
                    <table class="contact-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tên</th>
                            <th>Email</th>
                            <th>Số điện thoại</th>
                            <th>Nội dung</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                        </thead>
                        <tbody id="contactTableBody">
                        <!-- demo data -->
                        <c:forEach items="${contacts}" var="c">
                            <tr>
                                <td>${c.id}</td>
                                <td>${c.name}</td>
                                <td>${c.email}</td>
                                <td>${c.phone}</td>
                                <td class="message-preview">
                                        ${fn:length(c.message) > 50
                                                ? fn:substring(c.message, 0, 50).concat("...")
                                                : c.message}
                                </td>
                                <td>
                                    <form method="post" action="contact-admin" class="status-form">
                                        <input type="hidden" name="action" value="updateStatus">
                                        <input type="hidden" name="id" value="${c.id}">
                                        <select name="status" class="status-select status-${fn:toLowerCase(c.status)}" onchange="updateContactStatus(this)">
                                            <option value="New" ${c.status == 'New' ? 'selected' : ''}>Liên hệ mới</option>
                                            <option value="Processing" ${c.status == 'Processing' ? 'selected' : ''}>Đang xử lý</option>
                                            <option value="Closed" ${c.status == 'Closed' ? 'selected' : ''}>Đã xử lý</option>
                                        </select>
                                    </form>
                                </td>
                                <td class="actions">
                                    <!-- XEM -->
                                    <a href="contact-admin?mode=view&id=${c.id}"
                                       class="icon-btn view" title="Xem chi tiết">
                                        <i class="fa fa-eye"></i>
                                    </a>

                                    <!-- SỬA -->
                                    <a href="contact-admin?mode=edit&id=${c.id}"
                                       class="icon-btn edit" title="Chỉnh sửa">
                                        <i class="fa fa-pen"></i>
                                    </a>

                                    <!-- XÓA MỀM -->
                                    <button type="button"
                                            class="icon-btn delete"
                                            title="Xóa liên hệ"
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
    <!-- MODAL XÓA -->
    <div id="deleteModal" class="modal-overlay">
        <div class="modal">
            <h3>Xác nhận xóa</h3>
            <p id="deleteMessage">Bạn có chắc muốn xóa liên hệ này không?</p>

            <form id="deleteForm" method="post" action="contact-admin">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" id="deleteContactId">

                <div class="modal-actions">
                    <button type="button" class="btn-secondary" onclick="closeDeleteModal()">Hủy</button>
                    <button type="submit" class="btn-danger">Xóa</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="javaScript/contact-admin.js"></script>
</body>

</html>
