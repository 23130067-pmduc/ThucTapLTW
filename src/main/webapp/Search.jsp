<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%

    request.setAttribute("pageCss", "search.css");
    request.setAttribute("pageTitle" , "Tìm kiếm");
%>
<%@include file="header.jsp"%>

<section class="products search-page">

    <h2 class="search-title">
        Kết quả tìm kiếm cho:
        <span class="keyword">"${param.keyword}"</span>
    </h2>

    <c:if test="${empty list}">
        <p class="search-empty">
            Không tìm thấy sản phẩm phù hợp.
        </p>
    </c:if>

    <div class="product-list search-results">
        <c:forEach var="p" items="${list}" >


            <div class="product-card">
                <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="link-cover">
                    <img src="${p.thumbnail}" alt="${p.name}">
                </a>
                <h3><a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}">${p.name}</a></h3>

                <fmt:setLocale value="vi_VN"/>
                <p class="price">
                    Giá: <span class="new-price">
                    <fmt:formatNumber value="${p.sale_price}" type="number" groupingUsed="true"/>đ
                </span>
                </p>

                <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="btn-add">
                    Thêm vào giỏ
                </a>
            </div>

        </c:forEach>
    </div>

</section>

<%@include file="footer.jsp"%>
