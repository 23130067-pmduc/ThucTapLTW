<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Xem chi tiết đơn hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="order-form-page">

<div class="order-form-wrapper">
    <div class="order-form-top">
        <a href="${pageContext.request.contextPath}/order-admin" class="btn-back-green">← Quay lại</a>
        <h1>Xem chi tiết đơn hàng</h1>
    </div>

    <c:if test="${not empty success}">
        <div class="order-alert success">${success}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="order-alert error">${error}</div>
    </c:if>

    <div class="order-form-card">
        <h3>• Thông tin đơn hàng</h3>

        <div class="order-form-grid">
            <div class="form-group">
                <label>ID đơn hàng</label>
                <input type="text" value="${order.id}" readonly>
            </div>

            <div class="form-group">
                <label>Người nhận</label>
                <input type="text" value="${order.receiverName}" readonly>
            </div>

            <div class="form-group">
                <label>Số điện thoại</label>
                <input type="text" value="${order.phone}" readonly>
            </div>

            <div class="form-group">
                <label>Ngày tạo</label>
                <input type="text" value="${fn:substring(order.createdAtFormatted, 0, 10)}" readonly>
            </div>

            <div class="form-group form-group-full">
                <label>Địa chỉ giao</label>
                <textarea readonly>${order.shippingAddress}</textarea>
            </div>

            <div class="form-group">
                <label>Trạng thái</label>
                <input type="text" value="<c:choose><c:when test='${order.orderStatus eq "PENDING"}'>Chờ xử lý</c:when><c:when test='${order.orderStatus eq "SHIPPING"}'>Đang giao</c:when><c:when test='${order.orderStatus eq "COMPLETED"}'>Hoàn thành</c:when><c:when test='${order.orderStatus eq "CANCELLED"}'>Đã hủy</c:when><c:otherwise>${order.orderStatus}</c:otherwise></c:choose>" readonly>
            </div>

            <div class="form-group">
                <label>Tổng tiền</label>
                <input type="text" value="<fmt:formatNumber value='${order.finalAmount}' pattern='#,##0' /> đ" readonly>
            </div>
        </div>
    </div>

    <div class="order-form-card">
        <h3>• Sản phẩm trong đơn</h3>

        <table class="order-detail-table">
            <thead>
            <tr>
                <th>Ảnh</th>
                <th>Sản phẩm</th>
                <th>Size</th>
                <th>Màu</th>
                <th>SL</th>
                <th>Giá</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${items}" var="i">
                <tr>
                    <td>
                        <img src="${pageContext.request.contextPath}/${i.thumbnail}" class="order-detail-thumb"
                             onerror="this.src='${pageContext.request.contextPath}/img/no-image.png'">
                    </td>
                    <td>${i.productName}</td>
                    <td>${i.size}</td>
                    <td>${i.color}</td>
                    <td>${i.quantity}</td>
                    <td><fmt:formatNumber value="${i.price}" pattern="#,##0" /> đ</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>