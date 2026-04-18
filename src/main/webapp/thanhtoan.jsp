<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    request.setAttribute("pageCss", "thanhtoan.css?v=" + System.currentTimeMillis());
    request.setAttribute("pageTitle", "Thanh toán");
%>

<%@include file="header.jsp"%>

<section class="checkout">
    <div class="checkout-container">
        <c:if test="${param.error == 'out_of_stock'}">
            <div class="checkout-error-msg">
                <strong>Thông báo:</strong> Một hoặc nhiều sản phẩm trong giỏ hàng của bạn đã bán hết hoặc không đủ số lượng tồn kho. Vui lòng giảm số lượng.
            </div>
        </c:if>

        <c:if test="${param.error == 'missing_info'}">
            <div class="checkout-error-msg">
                <strong>Thông báo:</strong> Vui lòng chọn hoặc thêm địa chỉ giao hàng trước khi thanh toán.
            </div>
        </c:if>
        <form action="checkout" method="post">

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

                    <div id="addressSummaryView" onclick="toggleAddress()" class="address-summary-view" style="display: block;">
                        <c:choose>
                            <c:when test="${not empty addresses}">
                                <c:set var="selAddr" value="${addresses[0]}" />
                                <c:forEach var="a" items="${addresses}">
                                    <c:if test="${a.defaultAddress}"><c:set var="selAddr" value="${a}" /></c:if>
                                </c:forEach>
                                <strong id="summaryClientInfo" class="summary-client-info">${selAddr.receiverName}, ${selAddr.phone}</strong>
                                <p id="summaryAddressInfo" class="summary-address-info">${selAddr.detailAddress}, ${selAddr.ward}, ${selAddr.district}, ${selAddr.city}</p>
                            </c:when>
                            <c:otherwise>
                                <p class="empty-address-msg">Bạn chưa có địa chỉ nào!</p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div id="addressDetailView" class="address-detail-view" style="display: none;">
                        <c:choose>
                            <c:when test="${not empty addresses}">
                                <div class="address-selector-content">
                                    <c:forEach var="a" items="${addresses}">
                                        <label class="address-item-radio ${a.defaultAddress ? 'active' : ''}">
                                            <input type="radio" name="selectedAddress" value="${a.id}"
                                                   data-name="${a.receiverName}"
                                                   data-phone="${a.phone}"
                                                   data-address="${a.detailAddress}, ${a.ward}, ${a.district}, ${a.city}"
                                                   onchange="updateHiddenFieldsFromRadio(this)"
                                                ${a.defaultAddress ? 'checked' : ''}>
                                            <div class="address-info">
                                                <strong>${a.receiverName}, ${a.phone}</strong>
                                                <p>${a.detailAddress}, ${a.ward}, ${a.district}, ${a.city}</p>
                                                <c:if test="${a.defaultAddress}">
                                                    <span class="badge-default">Mặc định</span>
                                                </c:if>
                                            </div>
                                        </label>
                                    </c:forEach>
                                </div>
                                <button type="button" class="btn-add-address-checkout" onclick="openCheckoutModal()">
                                    <i class="fas fa-plus"></i> Sử dụng địa chỉ khác
                                </button>
                            </c:when>
                            <c:otherwise>
                                <p class="empty-address-msg-mb">Bạn chưa có địa chỉ nào!</p>
                                <button type="button" class="btn-add-address-checkout" onclick="openCheckoutModal()">
                                    <i class="fas fa-plus"></i> Thêm địa chỉ mới
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <input type="hidden" name="receiverName" id="hiddenName" value="${not empty addresses ? addresses[0].receiverName : ''}">
                    <input type="hidden" name="phone" id="hiddenPhone" value="${not empty addresses ? addresses[0].phone : ''}">
                    <c:set var="defaultAddr" value="" />
                    <c:if test="${not empty addresses}">
                        <c:set var="defaultAddr" value="${addresses[0].detailAddress}, ${addresses[0].ward}, ${addresses[0].district}, ${addresses[0].city}" />
                    </c:if>
                    <input type="hidden" name="address" id="hiddenAddress" value="${defaultAddr}">
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

<div class="checkout-modal-overlay" id="checkoutAddressModal">
    <div class="checkout-modal">
        <div class="checkout-modal-header">
            <span>Thêm địa chỉ</span>
            <button type="button" onclick="closeCheckoutModal()">&times;</button>
        </div>
        <form action="${pageContext.request.contextPath}/dia-chi" method="post" id="addressForm">
            <input type="hidden" name="action" value="add">
            <input type="hidden" name="redirectTo" value="/checkout">

            <div class="checkout-modal-body">
                <div class="modal-group-mb15">
                    <label class="modal-label">Quốc gia/Vùng</label>
                    <select disabled class="modal-select-disabled">
                        <option>Việt Nam</option>
                    </select>
                </div>
                <div class="form-row">
                    <div>
                        <label class="modal-label">Họ và tên</label>
                        <input type="text" name="receiverName" required placeholder="Họ và tên">
                    </div>
                    <div>
                        <label class="modal-label">Điện thoại</label>
                        <input type="text" id="phoneInput" name="phone" required placeholder="09xxxx..." pattern="^(0[3|5|7|8|9])[0-9]{8}$">
                        <small id="phoneError" class="phone-error-msg"></small>
                    </div>
                </div>
                <div class="form-row">
                    <div>
                        <label class="modal-label">Tỉnh Thành</label>
                        <select name="city" id="citySelect" required>
                            <option value="">-- Chọn Tỉnh / Thành phố --</option>
                            <option value="Hồ Chí Minh">Hồ Chí Minh</option>
                            <option value="Hà Nội">Hà Nội</option>
                            <option value="Bình Dương">Bình Dương</option>
                        </select>
                    </div>
                    <div>
                        <label for="districtSelect" class="modal-label">Quận Huyện</label>
                        <select name="district" id="districtSelect" required disabled>
                            <option value="">-- Chọn Quận / Huyện --</option>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <div>
                        <label for="wardSelect" class="modal-label">Phường Xã</label>
                        <select name="ward" id="wardSelect" required disabled>
                            <option value="">-- Chọn Phường / Xã --</option>
                        </select>
                    </div>
                    <div>
                        <label class="modal-label">Địa chỉ cụ thể</label>
                        <input type="text" name="detailAddress" required placeholder="Số nhà, tên đường...">
                    </div>
                </div>
                <div class="modal-group-mt15">
                    <label class="modal-checkbox-label">
                        <input type="checkbox" name="isDefault" value="true">
                        Đây là địa chỉ mặc định của tôi
                    </label>
                </div>
            </div>
            <div class="checkout-modal-footer">
                <button type="button" class="btn-cancel-modal" onclick="closeCheckoutModal()">Hủy</button>
                <button type="submit" class="btn-save-modal">Lưu địa chỉ</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/javaScript/thanhtoan.js?v=<%=System.currentTimeMillis()%>"></script>
<script src="${pageContext.request.contextPath}/javaScript/address.js"></script>
<%@include file="footer.jsp"%>