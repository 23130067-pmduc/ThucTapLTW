<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết phiếu hoàn hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/return-order.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="return-detail-page">
<div class="return-detail-wrapper">
    <div class="return-detail-top">
        <a href="${pageContext.request.contextPath}/return-order-admin" class="btn-back-green">← Quay lại</a>
        <h1>Chi tiết phiếu hoàn hàng</h1>
    </div>

    <c:if test="${not empty success}"><div class="return-alert success">${success}</div></c:if>
    <c:if test="${not empty error}"><div class="return-alert error">${error}</div></c:if>

    <div class="return-form-card">
        <h3>• Thông tin phiếu hoàn</h3>
        <div class="return-form-grid">
            <div class="form-group"><label>Mã phiếu</label><input type="text" value="${returnOrder.code}" readonly></div>
            <div class="form-group"><label>Mã đơn hàng</label><input type="text" value="#${returnOrder.orderId}" readonly></div>
            <div class="form-group"><label>Khách hàng</label><input type="text" value="${returnOrder.receiverName}" readonly></div>
            <div class="form-group"><label>Số điện thoại</label><input type="text" value="${returnOrder.phone}" readonly></div>
            <div class="form-group"><label>Ngày tạo</label><input type="text" value="${returnOrder.createdAtText}" readonly></div>
            <div class="form-group"><label>Trạng thái</label><input type="text" value="${returnOrder.statusText}" readonly></div>
            <div class="form-group form-group-full"><label>Lý do hoàn hàng</label><textarea readonly>${returnOrder.reason}</textarea></div>
            <c:if test="${not empty returnOrder.adminNote}">
                <div class="form-group form-group-full"><label>Ghi chú xử lý</label><textarea readonly>${returnOrder.adminNote}</textarea></div>
            </c:if>
        </div>
    </div>

    <c:if test="${not empty returnOrder.inventoryTransactionId}">
        <div class="return-form-card return-import-card">
            <h3>• Phiếu nhập kho hoàn hàng</h3>
            <p>Phiếu hoàn này đã nhập lại kho bằng phiếu: <strong>${returnOrder.inventoryTransactionCode}</strong></p>
            <a href="${pageContext.request.contextPath}/inventory-history-detail?id=${returnOrder.inventoryTransactionId}" class="btn-return-search">
                <i class="fa-solid fa-box-open"></i> Xem chi tiết phiếu nhập
            </a>
        </div>
    </c:if>

    <div class="return-form-card">
        <h3>• Sản phẩm hoàn hàng</h3>
        <table class="return-detail-table">
            <thead>
            <tr>
                <th>Ảnh</th>
                <th>Sản phẩm</th>
                <th>Size</th>
                <th>Màu</th>
                <th>SL hoàn</th>
                <th>Giá</th>
                <th>Thành tiền</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${items}" var="i">
                <tr>
                    <td>
                        <c:set var="thumbUrl" value="${i.thumbnail}" />
                        <c:choose>
                            <c:when test="${empty thumbUrl}">
                                <img src="${pageContext.request.contextPath}/img/gau.png" class="return-thumb" onerror="this.src='${pageContext.request.contextPath}/img/gau.png'">
                            </c:when>
                            <c:when test="${fn:startsWith(thumbUrl, 'http://') || fn:startsWith(thumbUrl, 'https://')}">
                                <img src="${thumbUrl}" class="return-thumb" onerror="this.src='${pageContext.request.contextPath}/img/gau.png'">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/${thumbUrl}" class="return-thumb" onerror="this.src='${pageContext.request.contextPath}/img/gau.png'">
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${i.productName}</td>
                    <td>${i.size}</td>
                    <td>${i.color}</td>
                    <td>${i.quantity}</td>
                    <td><fmt:formatNumber value="${i.price}" pattern="#,##0" /> đ</td>
                    <td><fmt:formatNumber value="${i.total}" pattern="#,##0" /> đ</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="return-form-card">
        <h3>• Xử lý phiếu hoàn</h3>
        <c:choose>
            <c:when test="${returnOrder.status == 'PENDING'}">
                <div class="return-action-grid">
                    <form action="${pageContext.request.contextPath}/return-order-admin" method="post" class="return-admin-form">
                        <input type="hidden" name="id" value="${returnOrder.id}">
                        <input type="hidden" name="action" value="approve">
                        <textarea name="adminNote" placeholder="Ghi chú duyệt hoàn hàng..."></textarea>
                        <button type="submit" class="btn-approve"><i class="fa-solid fa-check"></i> Duyệt hoàn hàng</button>
                    </form>
                    <form action="${pageContext.request.contextPath}/return-order-admin" method="post" class="return-admin-form">
                        <input type="hidden" name="id" value="${returnOrder.id}">
                        <input type="hidden" name="action" value="reject">
                        <textarea name="adminNote" placeholder="Lý do từ chối..."></textarea>
                        <button type="submit" class="btn-reject"><i class="fa-solid fa-xmark"></i> Từ chối</button>
                    </form>
                </div>
            </c:when>
            <c:when test="${returnOrder.status == 'APPROVED'}">
                <form action="${pageContext.request.contextPath}/return-order-admin" method="post" class="return-admin-form single">
                    <input type="hidden" name="id" value="${returnOrder.id}">
                    <input type="hidden" name="action" value="complete">
                    <textarea name="adminNote" placeholder="Ghi chú nhập lại kho..."></textarea>
                    <button type="submit" class="btn-complete"><i class="fa-solid fa-boxes-packing"></i> Nhập lại kho</button>
                </form>
            </c:when>
            <c:otherwise>
                <div class="return-finished-box">Phiếu hoàn hàng đã ở trạng thái <strong>${returnOrder.statusText}</strong>, không còn thao tác xử lý.</div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
