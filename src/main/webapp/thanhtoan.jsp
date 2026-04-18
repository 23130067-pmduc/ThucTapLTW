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

                <div class="contact-email">
                    <div class="avatar">${(not empty sessionScope.userlogin.fullName) ? sessionScope.userlogin.fullName.substring(0,1).toUpperCase() : 'U'}</div>
                    <div class="contact-email-text">${sessionScope.userlogin.email}</div>
                </div>

                <div class="address-selector-wrapper">
                    <h2 onclick="toggleAddress()" class="checkout-section-header">
                        Vận chuyển đến
                        <i id="addressToggleIcon" class="fa-solid fa-chevron-down" class="address-toggle-icon"></i>
                    </h2>

                    <div class="form-group" class="modal-group-mt15">
                        <input type="text" name="note" placeholder="Nhập ghi chú (nếu có)..." class="checkout-note-input">
                    </div>
                </div>

                <div class="shipping-method-wrapper">
                    <h2>Phương thức vận chuyển</h2>
                    <p>Giao hàng tiêu chuẩn (3 đến 7 ngày) · <strong>MIỄN PHÍ</strong></p>
                </div>

                <div class="payment-method-wrapper">
                    <h2>Thanh toán</h2>
                    <p class="payment-security-msg">Toàn bộ các giao dịch được bảo mật và mã hóa.</p>
                    <div class="payment-method">
                        <label class="active">
                            <input type="radio" name="paymentMethod" value="VNPAY" checked onchange="updatePaymentUI(this)">
                            <span>Thanh toán online qua cổng thanh toán VNPay</span>
                        </label>
                        <div id="vnpay-message" class="vnpay-message-box">
                            Bạn sẽ được chuyển hướng đến hệ thống thanh toán VNPay để hoàn tất mua hàng.
                        </div>
                        <label class="cod-label">
                            <input type="radio" name="paymentMethod" value="COD" onchange="updatePaymentUI(this)">
                            <span>Thanh toán khi nhận hàng (COD)</span>
                        </label>
                    </div>
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