<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    request.setAttribute("pageCss", "thanhtoanthanhcong.css");
    request.setAttribute("pageTitle", "Đã đặt hàng");
%>

<%@include file="header.jsp"%>

<div class="order-success-wrapper">
    <div class="order-left">
        <h2>✅ Cảm ơn <b>${order.receiverName}</b>!</h2>
        <p class="order-code">Mã xác nhận <b>#  ${order.id}</b></p>

        <div class="map-box">
            <p>Địa chỉ nhận hàng</p>
            <p><b>${order.shippingAddress}</b></p>
        </div>

        <div class="order-info">
            <h4>Đơn hàng của bạn đã được xác nhận</h4>
            <ul>
                <li>✔ Thanh toán khi nhận hàng (COD)</li>
                <li>✔ Nhân viên giao hàng sẽ liên hệ trước</li>
                <li>✔ Giao hàng tiêu chuẩn (3–5 ngày)</li>
            </ul>
        </div>

        <div class="customer-info">
            <p><b>Người nhận:</b> ${order.receiverName}</p>
            <p><b>SĐT:</b> ${order.phone}</p>
            <c:if test="${not empty order.note}">
                <p><b>Ghi chú:</b> ${order.note}</p>
            </c:if>

        </div>

        <a href="trang-chu" class="btn-main">Tiếp tục mua sắm</a>
    </div>

    <div class="order-right">
        <h3>Tóm tắt đơn hàng</h3>
        <c:forEach var="item" items="${orderItems}">
            <div class="product-item">

                <div class="product-info">
                    <b>${item.productName}</b><br>
                    Size: ${item.size} | Màu: ${item.color}<br>
                    SL: ${item.quantity}
                </div>

                <div class="product-price">
                    <fmt:formatNumber value="${item.total}" type="number"/>₫
                </div>
            </div>
        </c:forEach>
        <div class="summary-row">
            <span>Tạm tính</span>
            <span>
                <fmt:formatNumber value="${order.totalPrice}" type="number"/>₫
            </span>
        </div>

        <div class="summary-row">
            <span>Vận chuyển</span>
            <span>MIỄN PHÍ</span>
        </div>

        <hr>

        <div class="summary-total">
            <span>Tổng</span>
            <span>
                <fmt:formatNumber value="${order.finalAmount}" type="number"/>₫
            </span>
        </div>
    </div>
</div>

<%@include file="footer.jsp"%>