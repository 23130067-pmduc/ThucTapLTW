<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    request.setAttribute("pageCss", "thanhtoan.css");
    request.setAttribute("pageTitle", "Thanh toán");
%>

<%@include file="header.jsp"%>

<section class="checkout">
    <div class="checkout-container">
        <form action="pay" method="post">

            <div class="checkout-left">
                <h2>Thông tin giao hàng</h2>

                <div class="form-group">
                    <label>Họ và tên</label>
                    <input type="text" name="receiverName" placeholder="Tên người nhận" required>
                </div>

                <div class="form-group">
                    <label>Số điện thoại</label>
                    <input type="text" name="phone" placeholder="Nhập số điện thoại" pattern="[0-9]{9,11}" required>
                </div>

                <div class="form-group">
                    <label>Địa chỉ nhận hàng</label>
                    <input type="text" name="address" placeholder="Số nhà, đường, phường/xã, quận/huyện" required>
                </div>

                <div class="form-group">
                    <label>Ghi chú</label>
                    <textarea name="note" placeholder="Giao giờ hành chính..."></textarea>
                </div>

                <h2>Phương thức thanh toán</h2>

                <div class="payment-method">
                    <label>
                        <input type="radio" name="paymentMethod" value="COD" checked>
                        Thanh toán khi nhận hàng (COD)
                    </label>

                    <label>
                        <input type="radio" name="paymentMethod" value="MOMO">
                        Ví điện tử MOMO
                    </label>

                    <label>
                        <input type="radio" name="paymentMethod" value="ONEPAY">
                        Thẻ ATM / Visa (OnePay)
                    </label>
                </div>
            </div>

            <div class="checkout-right">
                <h3>Đơn hàng của bạn</h3>
                <input type="hidden" name="cartId" value="${sessionScope.cartId}">
                <c:forEach var="item" items="${checkoutItems}">
                    <input type="hidden" name="variantIds" value="${item.variantId}">
                    <input type="hidden" name="quantities" value="${item.quantity}">
                </c:forEach>


                <div class="order-items">
                    <c:set var="total" value="0"/>
                    <c:forEach var="item" items="${checkoutItems}">
                        <div class="order-item">
                            <img src="${item.product.thumbnail}">
                            <div class="info">
                                <p class="name">${item.product.name}</p>
                                <p class="variant">Size ${item.size} · ${item.color}
                                </p>
                                <p class="qty">SL: ${item.quantity}</p>
                            </div>
                            <div class="price">
                                <fmt:formatNumber value="${item.price * item.quantity}" type="number"/>₫
                            </div>
                        </div>

                        <c:set var="total" value="${total + item.price * item.quantity}"/>
                    </c:forEach>
                </div>

                <div class="order-summary">
                    <div>
                        <span>Tạm tính</span>
                        <span>
                        <fmt:formatNumber value="${total}" type="number"/>₫
                    </span>
                    </div>

                    <div>
                        <span>Phí vận chuyển</span>
                        <span>Miễn phí</span>
                    </div>

                    <div class="total">
                        <span>Tổng cộng</span>
                        <span>
                        <fmt:formatNumber value="${total}" type="number"/>₫
                    </span>
                    </div>
                </div>

                <button type="submit" class="btn-checkout">
                    XÁC NHẬN THANH TOÁN
                </button>

            </div>
        </form>
    </div>
</section>

<%@include file="footer.jsp"%>