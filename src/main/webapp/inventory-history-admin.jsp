<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lịch sử nhập xuất kho</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/inventory-history-admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="inventory-history-page">
<div class="user">
    <aside class="sidebar">
        <img src="${pageContext.request.contextPath}/img/gau.png" alt="Logo">
        <p>ADMIN</p>

        <div class="nav">
            <a href="${pageContext.request.contextPath}/dashboard" class="nav-item"><i class="fa-solid fa-gauge"></i><span>Dashboard</span></a>
            <a href="${pageContext.request.contextPath}/product-admin" class="nav-item"><i class="fa-solid fa-shirt"></i><span>Sản phẩm</span></a>
            <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item active"><i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span></a>
            <a href="${pageContext.request.contextPath}/profit-report" class="nav-item"><i class="fa-solid fa-chart-line"></i><span>Lợi nhuận</span></a>
            <a href="${pageContext.request.contextPath}/category-admin" class="nav-item"><i class="fa-solid fa-list"></i><span>Danh mục</span></a>
            <a href="${pageContext.request.contextPath}/order-admin" class="nav-item"><i class="fa-solid fa-receipt"></i><span>Đơn hàng</span></a>
            <a href="${pageContext.request.contextPath}/user-admin" class="nav-item"><i class="fa-solid fa-users"></i><span>Người dùng</span></a>
            <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item"><i class="fa-solid fa-image"></i><span>Banner</span></a>
            <a href="${pageContext.request.contextPath}/news-admin" class="nav-item"><i class="fa-solid fa-newspaper"></i><span>Tin tức</span></a>
            <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item"><i class="fa-solid fa-bell"></i><span>Thông báo</span></a>
            <a href="${pageContext.request.contextPath}/contact-admin" class="nav-item"><i class="fa-solid fa-envelope"></i><span>Liên hệ</span></a>
            <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item"><i class="fa-solid fa-user-gear"></i><span>Hồ sơ</span></a>
        </div>
    </aside>

    <section class="content inventory-history-content">
        <header class="topbar">
            <div class="topbar-left">
                <a href="${pageContext.request.contextPath}/inventory-admin" class="back-btn" title="Quay lại quản lý kho">
                    <i class="fa-solid fa-arrow-left"></i>
                </a>
                <h1>Lịch sử nhập xuất kho</h1>
            </div>

            <div class="topbar-actions">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
            </div>
        </header>

        <c:if test="${param.success == 'status_updated'}">
            <div class="alert alert-success">Cập nhật trạng thái phiếu và tồn kho thành công.</div>
        </c:if>
        <c:if test="${param.error == 'already_processed'}">
            <div class="alert alert-warning">Phiếu này đã được xử lý nên không thể đổi trạng thái.</div>
        </c:if>
        <c:if test="${param.error == 'insufficient_stock'}">
            <div class="alert alert-danger">Không thể hoàn thành phiếu xuất vì số lượng tồn kho hiện tại không đủ.</div>
        </c:if>
                <c:if test="${param.error == 'missing_unit_cost'}">
            <div class="alert alert-danger">Không thể hoàn thành phiếu nhập vì phiếu chưa có giá nhập hợp lệ.</div>
        </c:if>

<c:if test="${param.error == 'invalid_status' || param.error == 'status_update_failed'}">
            <div class="alert alert-danger">Không thể cập nhật trạng thái phiếu. Vui lòng thử lại.</div>
        </c:if>

        <div class="history-cards">
            <div class="history-card">Tổng phiếu <span>${totalTransactions}</span></div>
            <div class="history-card import-card">Phiếu nhập <span>${totalImport}</span></div>
            <div class="history-card export-card">Phiếu xuất <span>${totalExport}</span></div>
            <div class="history-card pending-card">Đang xử lý <span>${totalPending}</span></div>
        </div>

        <div class="history-toolbar">
            <form action="${pageContext.request.contextPath}/inventory-history-admin" method="get" class="history-search-form">
                <input type="text" name="keyword" class="history-search-input"
                       placeholder="Tìm mã phiếu, nhà cung cấp, ghi chú..."
                       value="${keyword}">

                <select name="type" class="history-filter-select">
                    <option value="" ${type == '' ? 'selected' : ''}>Tất cả loại phiếu</option>
                    <option value="IMPORT" ${type == 'IMPORT' ? 'selected' : ''}>Nhập kho</option>
                    <option value="EXPORT" ${type == 'EXPORT' ? 'selected' : ''}>Xuất kho</option>
                </select>

                <select name="status" class="history-filter-select">
                    <option value="" ${status == '' ? 'selected' : ''}>Tất cả trạng thái</option>
                    <option value="PENDING" ${status == 'PENDING' ? 'selected' : ''}>Đang xử lý</option>
                    <option value="COMPLETED" ${status == 'COMPLETED' ? 'selected' : ''}>Đã hoàn thành</option>
                    <option value="CANCELLED" ${status == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
                </select>

                <button type="submit" class="btn-search">
                    <i class="fa-solid fa-magnifying-glass"></i> Tìm
                </button>

                <a href="${pageContext.request.contextPath}/inventory-history-admin" class="btn-reset">Làm mới</a>
            </form>
        </div>

        <div class="history-result-info">
            Hiển thị ${transactions.size()} / ${totalItems} phiếu nhập xuất kho
        </div>

        <div class="history-table-wrapper">
            <table class="history-table">
                <thead>
                <tr>
                    <th>Mã phiếu</th>
                    <th>Loại</th>
                    <th>Tổng số lượng</th>
                    <th>Nhà cung cấp</th>
                    <th>Trạng thái</th>
                    <th>Người tạo</th>
                    <th>Thời gian</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty transactions}">
                    <tr>
                        <td colspan="8" class="history-empty">Chưa có lịch sử nhập xuất kho</td>
                    </tr>
                </c:if>

                <c:forEach items="${transactions}" var="item">
                    <tr>
                        <td><strong>${item.code}</strong></td>
                        <td>
                            <span class="type-badge ${item.type == 'IMPORT' ? 'type-import' : 'type-export'}">
                                ${item.typeText}
                            </span>
                        </td>
                        <td>${item.totalQuantity}</td>
                        <td>
                            <c:choose>
                                <c:when test="${empty item.supplierName}">-</c:when>
                                <c:otherwise>${item.supplierName}</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <span class="status-badge ${item.status == 'PENDING' ? 'status-pending' : (item.status == 'COMPLETED' ? 'status-completed' : 'status-cancelled')}">
                                ${item.statusText}
                            </span>
                        </td>
                        <td>${item.createdByName}</td>
                        <td>${item.createdAtText}</td>
                        <td>
                            <div class="admin-actions">
                                <a href="${pageContext.request.contextPath}/inventory-history-detail?id=${item.id}"
                                   class="icon-btn view"
                                   title="Xem chi tiết">
                                    <i class="fa-solid fa-eye"></i>
                                </a>

                                <c:if test="${item.status == 'PENDING'}">
                                    <button type="button"
                                            class="history-action-btn complete js-open-status-modal"
                                            title="Hoàn thành phiếu"
                                            data-transaction-id="${item.id}"
                                            data-status="COMPLETED"
                                            data-redirect="/inventory-history-admin"
                                            data-message="Bạn có chắc muốn hoàn thành phiếu ${item.code} không?">
                                        <i class="fa-solid fa-check"></i>
                                    </button>

                                    <button type="button"
                                            class="history-action-btn cancel js-open-status-modal"
                                            title="Hủy phiếu"
                                            data-transaction-id="${item.id}"
                                            data-status="CANCELLED"
                                            data-redirect="/inventory-history-admin"
                                            data-message="Bạn có chắc muốn hủy phiếu ${item.code} không?">
                                        <i class="fa-solid fa-xmark"></i>
                                    </button>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <c:if test="${totalPages > 1}">
                <c:set var="startPage" value="${currentPage - 2}" />
                <c:set var="endPage" value="${currentPage + 2}" />

                <c:if test="${startPage < 1}">
                    <c:set var="startPage" value="1" />
                    <c:set var="endPage" value="5" />
                </c:if>

                <c:if test="${endPage > totalPages}">
                    <c:set var="endPage" value="${totalPages}" />
                    <c:set var="startPage" value="${totalPages - 4}" />
                </c:if>

                <c:if test="${startPage < 1}">
                    <c:set var="startPage" value="1" />
                </c:if>

                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <c:url var="prevUrl" value="/inventory-history-admin">
                            <c:param name="page" value="${currentPage - 1}" />
                            <c:param name="keyword" value="${keyword}" />
                            <c:param name="type" value="${type}" />
                            <c:param name="status" value="${status}" />
                        </c:url>
                        <a href="${prevUrl}" class="page-link">Trước</a>
                    </c:if>

                    <c:forEach begin="${startPage}" end="${endPage}" var="pageNumber">
                        <c:choose>
                            <c:when test="${pageNumber == currentPage}">
                                <span class="page-link active">${pageNumber}</span>
                            </c:when>
                            <c:otherwise>
                                <c:url var="pageUrl" value="/inventory-history-admin">
                                    <c:param name="page" value="${pageNumber}" />
                                    <c:param name="keyword" value="${keyword}" />
                                    <c:param name="type" value="${type}" />
                                    <c:param name="status" value="${status}" />
                                </c:url>
                                <a href="${pageUrl}" class="page-link">${pageNumber}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <c:url var="nextUrl" value="/inventory-history-admin">
                            <c:param name="page" value="${currentPage + 1}" />
                            <c:param name="keyword" value="${keyword}" />
                            <c:param name="type" value="${type}" />
                            <c:param name="status" value="${status}" />
                        </c:url>
                        <a href="${nextUrl}" class="page-link">Sau</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </section>
</div>


<div id="statusModal" class="status-modal-overlay" aria-hidden="true">
    <div class="status-modal-box" role="dialog" aria-modal="true">
        <h3>Xác nhận thao tác</h3>
        <p id="statusModalMessage">Bạn có chắc muốn thực hiện thao tác này không?</p>

        <form method="post" action="${pageContext.request.contextPath}/inventory-transaction-status">
            <input type="hidden" name="transactionId" id="statusTransactionId">
            <input type="hidden" name="status" id="statusValue">
            <input type="hidden" name="redirect" id="statusRedirect" value="/inventory-history-admin">

            <div class="status-modal-actions">
                <button type="button" class="status-btn-cancel js-close-status-modal">Hủy</button>
                <button type="submit" class="status-btn-confirm">Đồng ý</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/javaScript/inventory-status-modal.js"></script>
</body>
</html>
