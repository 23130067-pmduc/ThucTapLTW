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

<div class="title"></div>

<section class="hero-banner">
    <div class="hero-content">
        <h1><i class="fas fa-fire"></i> KHUYẾN MÃI ĐẶC BIỆT</h1>
        <p>Giảm giá lên đến 50% - Thời trang trẻ em chất lượng cao</p>
        <a href="${pageContext.request.contextPath}/san-pham" class="btn-hero">Mua ngay</a>
    </div>
</section>

<section class="flash-sale">
    <div class="flash-header">
        <h2><i class="fas fa-fire"></i> FLASH SALE - SỐC HÔM NAY</h2>
    </div>

    <div class="flash-products">
        <c:forEach items="${flashSaleProducts}" var="p">
            <div class="product-card">
                <span class="badge flash">SALE -${p.discountPercent}%</span>

                <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="link-cover"></a>

                <img src="${p.thumbnail}" alt="${p.name}">

                <h3>${p.name}</h3>

                <p class="price">
        <span class="new-price">
            <fmt:formatNumber value="${p.sale_price}" type="number" groupingUsed="true"/>đ
        </span>
                    <span class="old-price">
            <fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/>đ
        </span>
                </p>

                <div class="button">
                    <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="btn-detail">
                        Xem chi tiết
                    </a>

                    <button class="btn-add"
                            data-product-id="${p.id}">
                        Thêm vào giỏ hàng
                    </button>
                </div>
            </div>
        </c:forEach>
    </div>
</section>

<section class="products">
    <h2>TẤT CẢ SẢN PHẨM GIẢM GIÁ</h2>

    <div class="product-grid discount-grid">
        <c:forEach items="${discountProducts}" var="p">
            <div class="product-card">
                <span class="badge flash">SALE -${p.discountPercent}%</span>

                <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="link-cover"></a>

                <img src="${p.thumbnail}" alt="${p.name}">

                <h3>${p.name}</h3>

                <p class="price">
        <span class="new-price">
            <fmt:formatNumber value="${p.sale_price}" type="number" groupingUsed="true"/>đ
        </span>
                    <span class="old-price">
            <fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/>đ
        </span>
                </p>

                <div class="button">
                    <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="btn-detail">
                        Xem chi tiết
                    </a>

                    <button class="btn-add"
                            data-product-id="${p.id}">
                        Thêm vào giỏ hàng
                    </button>
                </div>
            </div>
        </c:forEach>
    </div>

    <div class="load-more-wrapper">
        <button id="load-more" class="btn-load-more">Xem thêm</button>
    </div>
</section>

<div id="toast"></div>

<script>
    const CONTEXT_PATH = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/javaScript/khuyenmai.js?v=2.1"></script>

<%@ include file="quick-add-modal.jsp" %>
<%@ include file="footer.jsp" %>