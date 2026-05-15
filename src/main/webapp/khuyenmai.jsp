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
            <p>Săn mã giảm giá, freeship và các chương trình sale theo sự kiện đang diễn ra tại SunnyBear.</p>
            <div class="hero-actions">
                <a href="#coupon-section" class="btn-primary-promo">Lấy mã ngay</a>
                <a href="${pageContext.request.contextPath}/san-pham" class="btn-outline-promo">Mua sắm ngay</a>
            </div>
        </div>
        <div class="hero-card">
            <div class="hero-card-top">Deal nổi bật</div>
            <strong>Giảm đến 50%</strong>
            <span>Flash sale sản phẩm chọn lọc</span>
            <a href="#flash-sale-section">Xem sản phẩm sale</a>
        </div>
    </section>

    <section id="coupon-section" class="promo-section coupon-section">
        <div class="section-heading">
            <div>
                <span>Mã giảm giá</span>
                <h2>Voucher nổi bật</h2>
            </div>
            <p>Lưu mã trước, nhập ở trang thanh toán để áp dụng khi đủ điều kiện.</p>
        </div>

        <div class="coupon-grid">
            <c:forEach items="${couponSamples}" var="coupon">
                <div class="coupon-card">
                    <div class="coupon-left">
                        <span>${coupon.type}</span>
                        <strong>${coupon.code}</strong>
                    </div>
                    <div class="coupon-right">
                        <h3>${coupon.title}</h3>
                        <p>${coupon.condition}</p>
                        <small>${coupon.expire}</small>
                        <button type="button" class="btn-copy-coupon" data-code="${coupon.code}">
                            Sao chép mã
                        </button>
                    </div>
                </div>
            </c:forEach>
        </div>
    </section>

    <section class="promo-section event-section">
        <div class="section-heading">
            <div>
                <span>Sự kiện</span>
                <h2>Khuyến mãi theo mùa</h2>
            </div>
            <p>Các nhóm sự kiện này sau này có thể nối với bảng promotion_events và sản phẩm thuộc sự kiện.</p>
        </div>

        <div class="event-grid">
            <c:forEach items="${promotionEvents}" var="event">
                <div class="event-card">
                    <div class="event-icon">
                        <i class="fa-solid ${event.icon}"></i>
                    </div>
                    <span>${event.tag}</span>
                    <h3>${event.title}</h3>
                    <p>${event.description}</p>
                    <a href="${pageContext.request.contextPath}/san-pham">Xem sản phẩm</a>
                </div>
            </c:forEach>
        </div>
    </section>

    <section id="flash-sale-section" class="promo-section flash-sale">
        <div class="section-heading flash-heading">
            <div>
                <span>Flash sale</span>
                <h2><i class="fas fa-fire"></i> Deal sốc hôm nay</h2>
            </div>
            <p>Sản phẩm đang giảm sâu, số lượng có hạn.</p>
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
<script src="${pageContext.request.contextPath}/javaScript/khuyenmai.js?v=2.2"></script>

<jsp:include page="/quick-add-modal.jsp" />
<%@ include file="footer.jsp" %>
