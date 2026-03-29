<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    request.setAttribute("pageCss", "donmua.css");
    request.setAttribute("pageTitle", "Đơn hàng của tôi");
%>

<%@ include file="header.jsp" %>

<section class="profile-container order-page">
    <div class="profile-sidebar">
        <nav class="profile-menu">
            <ul>
                <li><a href="${pageContext.request.contextPath}/profile"><i class="fas fa-user"></i> Thông tin cá nhân</a></li>
                <li><a href="${pageContext.request.contextPath}/dia-chi"><i class="fas fa-map-marker-alt"></i> Địa chỉ của tôi</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/don-mua"><i class="fas fa-clipboard-list"></i> Đơn hàng của tôi</a></li>
                <li><a href="${pageContext.request.contextPath}/forget"><i class="fas fa-lock"></i> Đổi mật khẩu</a></li>
                <li><a href="${pageContext.request.contextPath}/logout"><i class="fa fa-sign-out"></i> Đăng xuất</a></li>
            </ul>
        </nav>
    </div>

    <div class="profile-content">
        <div class="order-page-header">
            <h2>Đơn hàng của tôi</h2>
        </div>

        <div class="order-tabs">
            <a href="${pageContext.request.contextPath}/don-mua?status=all" class="tab-item ${currentStatus == 'all' ? 'active' : ''}">Tất cả</a>
            <a href="${pageContext.request.contextPath}/don-mua?status=PENDING" class="tab-item ${currentStatus == 'PENDING' ? 'active' : ''}">Chờ xác nhận</a>
            <a href="${pageContext.request.contextPath}/don-mua?status=SHIPPING" class="tab-item ${currentStatus == 'SHIPPING' ? 'active' : ''}">Đang giao</a>
            <a href="${pageContext.request.contextPath}/don-mua?status=COMPLETED" class="tab-item ${currentStatus == 'COMPLETED' ? 'active' : ''}">Đã giao</a>
            <a href="${pageContext.request.contextPath}/don-mua?status=CANCELLED" class="tab-item ${currentStatus == 'CANCELLED' ? 'active' : ''}">Đã huỷ</a>
        </div>

        <c:if test="${not empty success}">
            <div class="order-message success-message">${success}</div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="order-message error-message">${error}</div>
        </c:if>

        <c:choose>
            <c:when test="${empty orders}">
                <div class="empty-order-box">
                    <p>Bạn chưa có đơn hàng nào ở mục này.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="order-list">
                    <c:forEach var="o" items="${orders}">
                        <div class="order-card">
                            <div class="order-card-header">
                                <div>
                                    <h3>Đơn hàng #${o.id}</h3>
                                    <p>${o.createdDateOnly}</p>
                                </div>
                                <span class="order-status-badge ${o.orderStatus}">${o.orderStatusLabel}</span>
                            </div>

                            <div class="order-products-wrapper">
                                <c:forEach var="i" items="${o.items}">
                                    <div class="order-product-item">
                                        <div class="order-product-left">
                                            <div class="order-product-thumb">
                                                <img src="${pageContext.request.contextPath}/${not empty i.thumbnail ? i.thumbnail : 'img/aox.webp'}"
                                                     alt="${i.productName}">
                                            </div>
                                            <div class="order-product-info">
                                                <h4>${i.productName}</h4>
                                                <p>Size: ${i.size} | Màu: ${i.color}</p>
                                                <p>Số lượng: ${i.quantity}</p>
                                            </div>
                                        </div>
                                        <div class="order-product-price">
                                            <fmt:formatNumber value="${i.total}" type="number"/> đ
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                            <div class="order-info-footer">
                                <div class="order-customer-info">
                                    <div class="info-row"><span class="info-label">Người nhận:</span> <span>${o.receiverName}</span></div>
                                    <div class="info-row"><span class="info-label">Địa chỉ giao:</span> <span>${o.shippingAddress}</span></div>
                                    <div class="info-row"><span class="info-label">Số điện thoại:</span> <span>${o.phone}</span></div>
                                    <div class="info-row"><span class="info-label">Hình thức thanh toán:</span> <span>${o.paymentMethodLabel}</span></div>
                                    <div class="info-row"><span class="info-label">Trạng thái thanh toán:</span>
                                        <span class="payment-badge ${o.paymentStatuses}">${o.paymentStatusLabel}</span>
                                    </div>
                                </div>
                                <div class="order-summary-box">
                                    <div class="summary-label">Tổng cộng</div>
                                    <div class="summary-total"><fmt:formatNumber value="${o.finalAmount}" type="number"/> đ</div>
                                </div>
                            </div>

                            <div class="order-actions-row">
                                <c:choose>
                                    <c:when test="${o.orderStatus == 'PENDING'}">
                                        <form action="${pageContext.request.contextPath}/don-mua" method="post">
                                            <input type="hidden" name="action" value="cancel">
                                            <input type="hidden" name="orderId" value="${o.id}">
                                            <input type="hidden" name="currentStatus" value="${currentStatus}">
                                            <button type="submit" class="order-action-btn btn-cancel">Huỷ đơn</button>
                                        </form>
                                    </c:when>
                                    <c:when test="${o.orderStatus == 'SHIPPING'}">
                                        <form action="${pageContext.request.contextPath}/don-mua" method="post">
                                            <input type="hidden" name="action" value="received">
                                            <input type="hidden" name="orderId" value="${o.id}">
                                            <input type="hidden" name="currentStatus" value="${currentStatus}">
                                            <button type="submit" class="order-action-btn btn-received">Đã nhận</button>
                                        </form>
                                    </c:when>
                                    <c:when test="${o.orderStatus == 'COMPLETED'}">
                                        <button type="button" class="order-action-btn btn-warm">Đánh giá</button>
                                        <button type="button" class="order-action-btn btn-warm">Mua lại</button>
                                    </c:when>
                                    <c:when test="${o.orderStatus == 'CANCELLED'}">
                                        <button type="button" class="order-action-btn btn-warm">Mua lại</button>
                                    </c:when>
                                </c:choose>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<%@ include file="footer.jsp" %>
