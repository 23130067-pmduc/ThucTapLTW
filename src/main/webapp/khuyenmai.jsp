<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%
    request.setAttribute("pageCss", "khuyenmai.css");
    request.setAttribute("pageTitle", "Khuyến mãi");
    request.setAttribute("activePage", "khuyenmai");
%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/quick-add-modal.css">
<%@ include file="header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/product-card.css">

<main class="promotion-page">
    <section class="promotion-hero">
        <div class="hero-info">
            <span class="hero-label">SunnyBear Deals</span>
            <h1>Khuyến mãi cho bé yêu</h1>
            <div class="hero-actions">
                <a href="#coupon-section" class="btn-primary-promo">Lấy mã ngay</a>
                <a href="${pageContext.request.contextPath}/san-pham" class="btn-outline-promo">Mua sắm ngay</a>
            </div>
        </div>
        <div class="hero-card">
            <div class="hero-card-top">Deal nổi bật</div>
            <strong>Giảm đến 50%</strong>
            <a href="#flash-sale-section">Xem sản phẩm sale</a>
        </div>
    </section>

    <section id="coupon-section" class="promo-section coupon-section">
        <div class="section-heading">
            <div>
                <span>Mã giảm giá</span>
                <h2>Voucher đơn hàng và sản phẩm</h2>
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty vouchers}">
                <div class="coupon-grid">
                    <c:forEach items="${vouchers}" var="voucher">
                        <div class="coupon-card">
                            <div class="coupon-left">
                                <span>${voucher.typeLabel}</span>
                                <strong>${voucher.code}</strong>
                            </div>
                            <div class="coupon-right">
                                <h3>${voucher.name}</h3>
                                <p>${voucher.description}</p>
                                <small>${voucher.conditionText}</small>
                                <small>${voucher.expireText}</small>
                                <button type="button" class="btn-copy-coupon" data-code="${voucher.code}">
                                    Sao chép mã
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-promotion">Hiện chưa có mã giảm giá đơn hàng hoặc sản phẩm khả dụng.</div>
            </c:otherwise>
        </c:choose>
    </section>

    <section id="shipping-coupon-section" class="promo-section coupon-section">
        <div class="section-heading">
            <div>
                <span>Freeship</span>
                <h2>Voucher phí vận chuyển</h2>
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty shippingVouchers}">
                <div class="coupon-grid">
                    <c:forEach items="${shippingVouchers}" var="voucher">
                        <div class="coupon-card">
                            <div class="coupon-left shipping-coupon-left">
                                <span>${voucher.typeLabel}</span>
                                <strong>${voucher.code}</strong>
                            </div>
                            <div class="coupon-right">
                                <h3>${voucher.name}</h3>
                                <p>${voucher.description}</p>
                                <small>${voucher.conditionText}</small>
                                <small>${voucher.expireText}</small>
                                <button type="button" class="btn-copy-coupon" data-code="${voucher.code}">
                                    Sao chép mã
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-promotion">Hiện chưa có mã giảm phí vận chuyển khả dụng.</div>
            </c:otherwise>
        </c:choose>
    </section>


    <section class="promo-section event-section">
        <div class="section-heading">
            <div>
                <span>Sự kiện</span>
                <h2>Khuyến mãi đang diễn ra</h2>
            </div>
            <p>Các chương trình sale có thời gian bắt đầu và kết thúc rõ ràng.</p>
        </div>

        <c:choose>
            <c:when test="${not empty promotionEvents}">
                <div class="event-grid">
                    <c:forEach items="${promotionEvents}" var="event">
                        <div class="event-card">
                            <div class="event-icon">
                                <i class="fa-solid ${event.icon}"></i>
                            </div>
                            <span>${event.discountLabel}</span>
                            <h3>${event.title}</h3>
                            <p>${event.description}</p>
                            <small class="event-date"><i class="fa-regular fa-clock"></i> ${event.dateRangeText}</small>
                            <a href="#event-products-${event.id}">Xem sản phẩm</a>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-promotion">Hiện chưa có sự kiện khuyến mãi nào đang diễn ra.</div>
            </c:otherwise>
        </c:choose>
    </section>

    <c:if test="${not empty promotionEvents}">
        <c:forEach items="${promotionEvents}" var="event">
            <section id="event-products-${event.id}" class="promo-section event-product-section">
                <div class="section-heading flash-heading">
                    <div>
                        <span>${event.discountLabel}</span>
                        <h2><i class="fa-solid ${event.icon}"></i> ${event.title}</h2>
                    </div>
                    <p>${event.dateRangeText}</p>
                </div>

                <c:choose>
                    <c:when test="${not empty event.products}">
                        <div class="flash-products">
                            <c:forEach items="${event.products}" var="p">
                                <div class="product-card">
                                    <span class="badge event-badge">EVENT -${p.discountPercent}%</span>
                                    <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="link-cover"></a>
                                    <img src="${p.thumbnail}" alt="${p.name}">
                                    <h3>${p.name}</h3>
                                    <p class="price">
                                        <span class="new-price"><fmt:formatNumber value="${p.sale_price}" type="number" groupingUsed="true"/>đ</span>
                                        <span class="old-price"><fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/>đ</span>
                                    </p>
                                    <div class="button">
                                        <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="btn-detail">Xem chi tiết</a>
                                        <button class="btn-add" data-product-id="${p.id}">Thêm vào giỏ hàng</button>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-promotion">Sự kiện này hiện chưa có sản phẩm khuyến mãi.</div>
                    </c:otherwise>
                </c:choose>
            </section>
        </c:forEach>
    </c:if>

    <section id="flash-sale-section" class="promo-section flash-sale">
        <div class="section-heading flash-heading">
            <div>
                <span>Flash sale</span>
                <h2><i class="fas fa-fire"></i> Deal sốc hôm nay</h2>
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty flashSaleProducts}">
                <div class="flash-products">
                    <c:forEach items="${flashSaleProducts}" var="p">
                        <div class="product-card">
                            <span class="badge flash">SALE -${p.discountPercent}%</span>
                            <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="link-cover"></a>
                            <img src="${p.thumbnail}" alt="${p.name}">
                            <h3>${p.name}</h3>
                            <p class="price">
                                <span class="new-price"><fmt:formatNumber value="${p.sale_price}" type="number" groupingUsed="true"/>đ</span>
                                <span class="old-price"><fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/>đ</span>
                            </p>
                            <div class="button">
                                <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="btn-detail">Xem chi tiết</a>
                                <button class="btn-add" data-product-id="${p.id}">Thêm vào giỏ hàng</button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-promotion">Hiện chưa có sản phẩm flash sale.</div>
            </c:otherwise>
        </c:choose>
    </section>

    <section class="promo-section products">
        <div class="section-heading centered-heading">
            <div>
                <span>Sale products</span>
                <h2>Tất cả sản phẩm giảm giá</h2>
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty discountProducts}">
                <div class="product-grid discount-grid">
                    <c:forEach items="${discountProducts}" var="p">
                        <div class="product-card">
                            <span class="badge flash">SALE -${p.discountPercent}%</span>
                            <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="link-cover"></a>
                            <img src="${p.thumbnail}" alt="${p.name}">
                            <h3>${p.name}</h3>
                            <p class="price">
                                <span class="new-price"><fmt:formatNumber value="${p.sale_price}" type="number" groupingUsed="true"/>đ</span>
                                <span class="old-price"><fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/>đ</span>
                            </p>
                            <div class="button">
                                <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="btn-detail">Xem chi tiết</a>
                                <button class="btn-add" data-product-id="${p.id}">Thêm vào giỏ hàng</button>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="load-more-wrapper">
                    <button id="load-more" class="btn-load-more">Xem thêm</button>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-promotion">Hiện chưa có sản phẩm giảm giá.</div>
            </c:otherwise>
        </c:choose>
    </section>
</main>

<div id="toast"></div>

<script>
    const CONTEXT_PATH = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/javaScript/khuyenmai.js?v=2.3"></script>

<jsp:include page="/quick-add-modal.jsp" />
<%@ include file="footer.jsp" %>
