<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<% request.setAttribute("pageCss", "chitietorder.css" );
    request.setAttribute("pageTitle", "Chi tiết đơn hàng" ); %>

<%@ include file="header.jsp" %>

<section class="profile-container order-page">
    <div class="profile-sidebar">
        <nav class="profile-menu">
            <ul>
                <li><a href="${pageContext.request.contextPath}/profile"><i class="fas fa-user"></i>
                    Thông tin cá nhân</a></li>
                <li><a href="${pageContext.request.contextPath}/dia-chi"><i
                        class="fas fa-map-marker-alt"></i> Địa chỉ của tôi</a></li>
                <li class="active"><a href="${pageContext.request.contextPath}/don-mua"><i
                        class="fas fa-clipboard-list"></i> Đơn hàng của tôi</a></li>
                <li><a href="${pageContext.request.contextPath}/doi-mat-khau"><i
                        class="fas fa-lock"></i> Đổi mật khẩu</a></li>
                <li><a href="${pageContext.request.contextPath}/logout"><i
                        class="fa fa-sign-out"></i> Đăng xuất</a></li>
            </ul>
        </nav>
    </div>

    <div class="profile-content">

        <div class="order-page-header">
            <h2><i class="fas fa-receipt"></i> Chi tiết đơn hàng #${order.id}</h2>
            <a href="${pageContext.request.contextPath}/don-mua" class="btn-back">
                <i class="fas fa-arrow-left"></i> Quay lại
            </a>
        </div>

        <div class="order-detail-status-bar">
            <div
                    class="status-step ${order.orderStatus == 'PENDING' || order.orderStatus == 'SHIPPING' || order.orderStatus == 'COMPLETED' ? 'done' : ''} ${order.orderStatus == 'PENDING' ? 'current' : ''}">
                <div class="step-icon"><i class="fas fa-clock"></i></div>
                <div class="step-label">Chờ xác nhận</div>
            </div>
            <div
                    class="step-line ${order.orderStatus == 'SHIPPING' || order.orderStatus == 'COMPLETED' ? 'done' : ''}">
            </div>
            <div
                    class="status-step ${order.orderStatus == 'SHIPPING' || order.orderStatus == 'COMPLETED' ? 'done' : ''} ${order.orderStatus == 'SHIPPING' ? 'current' : ''}">
                <div class="step-icon"><i class="fas fa-truck"></i></div>
                <div class="step-label">Đang giao</div>
            </div>
            <div class="step-line ${order.orderStatus == 'COMPLETED' ? 'done' : ''}"></div>
            <div class="status-step ${order.orderStatus == 'COMPLETED' ? 'done current' : ''}">
                <div class="step-icon"><i class="fas fa-check-circle"></i></div>
                <div class="step-label">Đã giao</div>
            </div>
        </div>

        <c:if test="${order.orderStatus == 'CANCELLED'}">
            <div class="order-cancelled-notice">
                <i class="fas fa-times-circle"></i> Đơn hàng này đã bị huỷ
            </div>
        </c:if>

        <div class="order-detail-grid">
            <div class="order-detail-card">
                <div class="detail-card-title"><i class="fas fa-map-marker-alt"></i> Thông tin giao
                    hàng</div>
                <div class="detail-row">
                    <span class="detail-label">Người nhận</span>
                    <span class="detail-value">${order.receiverName}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Số điện thoại</span>
                    <span class="detail-value">${order.phone}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Địa chỉ giao</span>
                    <span class="detail-value">${order.shippingAddress}</span>
                </div>
                <c:if test="${not empty order.note}">
                    <div class="detail-row">
                        <span class="detail-label">Ghi chú</span>
                        <span class="detail-value">${order.note}</span>
                    </div>
                </c:if>
                <c:if test="${not empty order.estimatedDeliveryDateFormatted}">
                    <div class="detail-row">
                        <span class="detail-label">Dự kiến giao</span>
                        <span class="detail-value">${order.estimatedDeliveryDateFormatted}</span>
                    </div>
                </c:if>
            </div>

            <div class="order-detail-card">
                <div class="detail-card-title"><i class="fas fa-credit-card"></i> Thông tin thanh
                    toán</div>
                <div class="detail-row">
                    <span class="detail-label">Hình thức</span>
                    <span class="detail-value">${order.paymentMethodLabel}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Trạng thái</span>
                    <span class="detail-value">
                        <span class="payment-badge ${order.paymentStatuses}">${order.paymentStatusLabel}</span>
                        </span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Ngày đặt</span>
                    <span class="detail-value">${order.createdAtFormatted}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Trạng thái đơn</span>
                    <span class="detail-value">
                        <span class="order-status-badge ${order.orderStatus}">${order.orderStatusLabel}</span></span>
                </div>
            </div>
        </div>

        <div class="order-detail-products-card">
            <div class="detail-card-title"><i class="fas fa-box"></i> Sản phẩm trong đơn</div>
            <c:set var="imgFallback" value="${pageContext.request.contextPath}/img/gau.png" />
            <div class="order-products-wrapper">
                <c:forEach var="i" items="${items}">
                    <div class="order-product-item">
                        <div class="order-product-left">
                            <div class="order-product-thumb">
                                <img src="${not empty i.thumbnail ? i.thumbnail : imgFallback}"
                                     alt="${i.productName}"
                                     onerror="this.src='${pageContext.request.contextPath}/img/gau.png'">
                            </div>
                            <div class="order-product-info">
                                <h4>${i.productName}</h4>
                                <p>Size: ${i.size} | Màu: ${i.color}</p>
                                <p>Số lượng: ${i.quantity}</p>
                            </div>
                        </div>
                        <div class="order-product-price">
                            <fmt:formatNumber value="${i.total}" type="number" /> đ
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        <div class="order-detail-summary">
            <div class="summary-row">
                <span>Tạm tính</span>
                <span><fmt:formatNumber value="${order.totalPrice}" type="number" /> đ
                </span>
            </div>
            <c:if test="${order.discount > 0}">
                <div class="summary-row discount">
                    <span>Giảm giá</span>
                    <span>-
                        <fmt:formatNumber value="${order.discount}" type="number" /> đ
                    </span>
                </div>
            </c:if>
            <div class="summary-row">
                <span>Phí vận chuyển</span>
                <span>
                    <c:choose>
                        <c:when test="${order.shippingFee == 0}">Miễn phí</c:when>
                        <c:otherwise>
                            <fmt:formatNumber value="${order.shippingFee}" type="number" /> đ
                        </c:otherwise>
                    </c:choose>
                </span>
            </div>
            <div class="summary-row total">
                <span>Tổng cộng</span>
                <span>
                    <fmt:formatNumber value="${order.finalAmount}" type="number" /> đ
                </span>
            </div>
        </div>
    </div>
</section>

<%@ include file="footer.jsp" %>