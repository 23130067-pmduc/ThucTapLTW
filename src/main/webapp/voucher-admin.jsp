<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Mã giảm giá</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pagination.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/voucher-admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="user">
    <aside class="sidebar">
        <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
        <p>ADMIN</p>

        <div class="nav" id="menu">
            <a href="${pageContext.request.contextPath}/dashboard" class="nav-item"><i class="fa-solid fa-gauge"></i><span>Dashboard</span></a>
            <a href="${pageContext.request.contextPath}/product-admin" class="nav-item"><i class="fa-solid fa-shirt"></i><span>Sản phẩm</span></a>
            <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item"><i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span></a>
            <a href="${pageContext.request.contextPath}/profit-report" class="nav-item"><i class="fa-solid fa-chart-line"></i><span>Lợi nhuận</span></a>
            <a href="${pageContext.request.contextPath}/category-admin" class="nav-item"><i class="fa-solid fa-list"></i><span>Danh mục</span></a>
            <a href="${pageContext.request.contextPath}/order-admin" class="nav-item"><i class="fa-solid fa-receipt"></i><span>Đơn hàng</span></a>
            <a href="${pageContext.request.contextPath}/return-order-admin" class="nav-item"><i class="fa-solid fa-rotate-left"></i><span>Hoàn hàng</span></a>
            <a href="${pageContext.request.contextPath}/user-admin" class="nav-item"><i class="fa-solid fa-users"></i><span>Người dùng</span></a>
            <a href="${pageContext.request.contextPath}/voucher-admin" class="nav-item active"><i class="fa-solid fa-ticket"></i><span>Mã giảm giá</span></a>
            <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item"><i class="fa-solid fa-image"></i><span>Banner</span></a>
            <a href="${pageContext.request.contextPath}/news-admin" class="nav-item"><i class="fa-solid fa-newspaper"></i><span>Tin tức</span></a>
            <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item"><i class="fa-solid fa-bell"></i><span>Thông báo</span></a>
            <a href="${pageContext.request.contextPath}/contact-admin" class="nav-item"><i class="fa-solid fa-envelope"></i><span>Liên hệ</span></a>
            <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item"><i class="fa-solid fa-user-gear"></i><span>Hồ sơ</span></a>
        </div>
    </aside>

    <section class="content">
        <header class="topbar">
            <h1>Quản lý mã giảm giá</h1>
            <div class="actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>

        <main>
            <c:if test="${param.success == 'create'}">
                <div class="alert alert-success"><i class="fa-solid fa-circle-check"></i> Thêm mã giảm giá thành công.</div>
            </c:if>
            <c:if test="${param.success == 'update'}">
                <div class="alert alert-success"><i class="fa-solid fa-circle-check"></i> Cập nhật mã giảm giá thành công.</div>
            </c:if>
            <c:if test="${param.success == 'lock'}">
                <div class="alert alert-success"><i class="fa-solid fa-lock"></i> Khóa mã giảm giá thành công.</div>
            </c:if>
            <c:if test="${param.success == 'unlock'}">
                <div class="alert alert-success"><i class="fa-solid fa-lock-open"></i> Mở mã giảm giá thành công.</div>
            </c:if>
            <c:if test="${not empty param.error}">
                <div class="alert alert-error"><i class="fa-solid fa-circle-exclamation"></i> ${param.error}</div>
            </c:if>

            <div class="cards voucher-cards">
                <div class="card">Tổng mã<br><span>${totalAll}</span></div>
                <div class="card">Đang hoạt động<br><span>${totalActive}</span></div>
                <div class="card">Đã khóa<br><span>${totalLocked}</span></div>
                <div class="card">Hết hạn<br><span>${totalExpired}</span></div>
            </div>

            <section class="voucher-panel">
                <div class="voucher-panel-header">
                    <div>
                        <h2>Danh sách mã giảm giá</h2>
                        <p>Tìm thấy ${total} mã giảm giá phù hợp.</p>
                    </div>
                    <div class="voucher-panel-actions">
                        <a href="${pageContext.request.contextPath}/voucher-admin?action=create" class="voucher-add-btn">
                            <i class="fa-solid fa-plus"></i>
                            Thêm mã giảm giá
                        </a>
                    </div>
                </div>

                <form method="get" action="${pageContext.request.contextPath}/voucher-admin" class="voucher-filter">
                    <div class="filter-group search-box">
                        <label for="keyword">Tìm kiếm</label>
                        <input type="text"
                               id="keyword"
                               name="keyword"
                               value="${keyword}"
                               placeholder="Nhập ID, mã, tên hoặc mô tả...">
                    </div>

                    <div class="filter-group">
                        <label for="scope">Điều kiện áp dụng</label>
                        <select id="scope" name="scope">
                            <option value="ALL" ${scope == 'ALL' ? 'selected' : ''}>Tất cả phạm vi</option>
                            <option value="ORDER" ${scope == 'ORDER' ? 'selected' : ''}>Đơn hàng</option>
                            <option value="PRODUCT" ${scope == 'PRODUCT' ? 'selected' : ''}>Sản phẩm</option>
                            <option value="SHIPPING" ${scope == 'SHIPPING' ? 'selected' : ''}>Vận chuyển</option>
                        </select>
                    </div>

                    <div class="filter-group">
                        <label for="discountType">Loại giảm giá</label>
                        <select id="discountType" name="discountType">
                            <option value="ALL" ${discountType == 'ALL' ? 'selected' : ''}>Tất cả loại giảm</option>
                            <option value="PERCENT" ${discountType == 'PERCENT' ? 'selected' : ''}>Theo phần trăm</option>
                            <option value="FIXED" ${discountType == 'FIXED' ? 'selected' : ''}>Số tiền cố định</option>
                        </select>
                    </div>

                    <div class="filter-group">
                        <label for="voucherStatus">Trạng thái</label>
                        <select id="voucherStatus" name="voucherStatus">
                            <option value="ALL" ${voucherStatus == 'ALL' ? 'selected' : ''}>Tất cả trạng thái</option>
                            <option value="ACTIVE" ${voucherStatus == 'ACTIVE' ? 'selected' : ''}>Đang hoạt động</option>
                            <option value="LOCKED" ${voucherStatus == 'LOCKED' ? 'selected' : ''}>Đã khóa</option>
                            <option value="EXPIRED" ${voucherStatus == 'EXPIRED' ? 'selected' : ''}>Hết hạn</option>
                            <option value="SOLD_OUT" ${voucherStatus == 'SOLD_OUT' ? 'selected' : ''}>Hết lượt</option>
                        </select>
                    </div>

                    <div class="filter-actions">
                        <button type="submit" class="btn-search">
                            <i class="fa-solid fa-magnifying-glass"></i>
                            Tìm kiếm
                        </button>
                        <a href="${pageContext.request.contextPath}/voucher-admin" class="btn-reset">
                            <i class="fa-solid fa-rotate-left"></i>
                            Đặt lại
                        </a>
                    </div>
                </form>

                <div class="voucher-table-wrapper">
                    <table class="voucher-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Mã giảm giá</th>
                            <th>Thông tin</th>
                            <th>Mức giảm</th>
                            <th>Điều kiện</th>
                            <th>Lượt dùng</th>
                            <th>Thời gian</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty vouchers}">
                                <c:forEach items="${vouchers}" var="v">
                                    <tr>
                                        <td class="voucher-id-cell">#${v.id}</td>
                                        <td>
                                            <span class="voucher-code">${v.code}</span>
                                        </td>
                                        <td class="voucher-info-cell">
                                            <div class="voucher-name">${v.name}</div>
                                            <div class="voucher-desc">${v.description}</div>
                                            <span class="scope-badge">${v.scopeLabel}</span>
                                        </td>
                                        <td class="voucher-value-cell">
                                            <strong>${v.discountText}</strong>
                                            <span>${v.discountTypeLabel}</span>
                                            <small>Tối đa: ${v.maxDiscountText}</small>
                                        </td>
                                        <td class="voucher-condition-cell">
                                            <span>Đơn tối thiểu</span>
                                            <strong>${v.minOrderText}</strong>
                                        </td>
                                        <td class="usage-cell">
                                            <strong>${v.usageText}</strong>
                                            <small>Còn ${v.remainingQuantity} lượt</small>
                                        </td>
                                        <td class="date-cell">
                                            <div>${v.startDateText}</div>
                                            <div>${v.endDateText}</div>
                                        </td>
                                        <td>
                                            <span class="voucher-status ${v.statusClass}">${v.statusLabel}</span>
                                        </td>
                                        <td>
                                            <div class="voucher-actions-cell">
                                                <a class="voucher-action-btn view" href="${pageContext.request.contextPath}/voucher-admin?action=detail&id=${v.id}" title="Xem chi tiết">
                                                    <i class="fa-solid fa-eye"></i>
                                                </a>
                                                <a class="voucher-action-btn edit" href="${pageContext.request.contextPath}/voucher-admin?action=edit&id=${v.id}" title="Sửa mã">
                                                    <i class="fa-solid fa-pen"></i>
                                                </a>
                                                <button type="button"
                                                        class="voucher-action-btn ${v.status == 1 ? 'lock' : 'unlock'} js-open-voucher-status-modal"
                                                        title="${v.status == 1 ? 'Khóa mã' : 'Mở mã'}"
                                                        data-id="${v.id}"
                                                        data-code="${v.code}"
                                                        data-status="${v.status == 1 ? 0 : 1}"
                                                        data-action-label="${v.status == 1 ? 'Khóa' : 'Mở'}">
                                                    <i class="fa-solid ${v.status == 1 ? 'fa-lock' : 'fa-lock-open'}"></i>
                                                </button>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="9" class="empty-row">
                                        <i class="fa-solid fa-ticket-simple"></i>
                                        <span>Chưa có mã giảm giá nào trong hệ thống.</span>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>

                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <c:if test="${currentPage > 1}">
                            <c:url var="previousPageUrl" value="/voucher-admin">
                                <c:param name="page" value="${currentPage - 1}" />
                                <c:param name="keyword" value="${keyword}" />
                                <c:param name="scope" value="${scope}" />
                                <c:param name="discountType" value="${discountType}" />
                                <c:param name="voucherStatus" value="${voucherStatus}" />
                            </c:url>
                            <a class="page-btn" href="${previousPageUrl}">Trước</a>
                        </c:if>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <c:url var="pageUrl" value="/voucher-admin">
                                <c:param name="page" value="${i}" />
                                <c:param name="keyword" value="${keyword}" />
                                <c:param name="scope" value="${scope}" />
                                <c:param name="discountType" value="${discountType}" />
                                <c:param name="voucherStatus" value="${voucherStatus}" />
                            </c:url>
                            <a class="page-btn ${i == currentPage ? 'active' : ''}" href="${pageUrl}">${i}</a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <c:url var="nextPageUrl" value="/voucher-admin">
                                <c:param name="page" value="${currentPage + 1}" />
                                <c:param name="keyword" value="${keyword}" />
                                <c:param name="scope" value="${scope}" />
                                <c:param name="discountType" value="${discountType}" />
                                <c:param name="voucherStatus" value="${voucherStatus}" />
                            </c:url>
                            <a class="page-btn" href="${nextPageUrl}">Sau</a>
                        </c:if>
                    </div>
                </c:if>
            </section>
        </main>
    </section>
</div>

<div id="voucherStatusModal" class="voucher-modal-overlay" aria-hidden="true">
    <div class="voucher-modal" role="dialog" aria-modal="true" aria-labelledby="voucherStatusModalTitle">
        <div class="voucher-modal-icon"><i id="voucherStatusModalIcon" class="fa-solid fa-lock"></i></div>
        <h3 id="voucherStatusModalTitle">Xác nhận thay đổi trạng thái</h3>
        <p id="voucherStatusModalMessage"></p>

        <form method="post" action="${pageContext.request.contextPath}/voucher-admin">
            <input type="hidden" name="action" value="toggle-status">
            <input type="hidden" name="id" id="voucherStatusId">
            <input type="hidden" name="status" id="voucherStatusValue">

            <div class="voucher-modal-actions">
                <button type="button" class="btn-secondary js-close-voucher-status-modal">Hủy</button>
                <button type="submit" id="voucherStatusSubmit" class="btn-voucher-confirm">Xác nhận</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/javaScript/voucher-admin.js"></script>
</body>
</html>
