<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<%
    request.setAttribute("pageCss", "trangchu.css");
    request.setAttribute("pageTitle" , "Trang chủ");
%>

<%@include file="header.jsp"%>


<section class="banner">

    <div class="slider">
        <div class="img-slides">
            <c:forEach var="b" items="${banners}">
                <div class="slide">
                    <a href="${b.navigateTo}">
                        <img src="${b.imageUrl}" alt="${b.title}">
                    </a>
                </div>
            </c:forEach>


        </div>
        <button class="prev">&#10094;</button>
        <button class="next">&#10095;</button>
    </div>
</section>


<section class="products">
    <h2>Sản phẩm mới nhất</h2>

    <div class="slider-wrapper">
        <div class="product-list" id="new-slider">
            <c:forEach var="p" items="${latestProducts}">
                <div class="product-card">
                    <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="link-cover"></a>
                    <img src="${p.thumbnail}" alt="${p.name}">
                    <h3>${p.name}</h3>

                    <p class="price">
                        Giá: <span class="new-price">
                            <fmt:formatNumber value="${p.sale_price}" type="number"/>đ
                        </span>
                        <span class="old-price">
                            <fmt:formatNumber value="${p.price}" type="number"/>đ
                        </span>
                    </p>

                    <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}&quantity=1" class="btn-add">
                        Thêm vào giỏ
                    </a>
                </div>
            </c:forEach>
        </div>
        <div class="dots-container" id="new-dots"></div>
    </div>

</section>


<section class="categories">
    <h2>Danh mục nổi bật</h2>


    <div class="category-block">
        <div class="category-title">Bé trai 👕</div>

        <div class="slider-wrapper">
            <div class="category-products" id="boy-slider">
                <c:forEach var="p" items="${boyProducts}">
                    <div class="product-mini">
                        <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="link-cover"></a>
                        <img src="${p.thumbnail}" alt="${p.name}">
                        <h3>${p.name}</h3>
                        <p class="price">
                            <span class="new-price"><fmt:formatNumber value="${p.sale_price}" type="number"/>đ</span>
                            <span class="old-price"><fmt:formatNumber value="${p.price}" type="number"/>đ</span>
                        </p>
                        <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}&quantity=1" class="btn-add">Thêm vào giỏ</a>
                    </div>
                </c:forEach>
            </div>

            <div class="dots-container" id="boy-dots"></div>
        </div>

        <div class="load-more">
            <a href="san-pham">Xem thêm</a>
        </div>
    </div>



    <div class="category-block">
        <div class="category-title">Bé gái 👗</div>

        <div class="slider-wrapper">
            <div class="category-products" id="girl-slider">
                <c:forEach var="p" items="${girlProducts}">
                    <div class="product-mini">
                        <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="link-cover"></a>
                        <img src="${p.thumbnail}" alt="${p.name}">
                        <h3>${p.name}</h3>

                        <p class="price">
                        <span class="new-price">
                            <fmt:formatNumber value="${p.sale_price}" type="number"/>đ
                        </span>
                            <span class="old-price">
                            <fmt:formatNumber value="${p.price}" type="number"/>đ
                        </span>
                        </p>

                        <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}&quantity=1" class="btn-add">
                            Thêm vào giỏ
                        </a>
                    </div>
                </c:forEach>
            </div>

            <div class="dots-container" id="girl-dots"></div>
        </div>


        <div class="load-more">
            <a href="san-pham">Xem thêm</a>
        </div>
    </div>



    <div class="category-block">
        <div class="category-title">Phụ kiện 🎒</div>

        <div class="slider-wrapper">
            <div class="category-products" id="acc-slider">
                <c:forEach var="p" items="${accessoryProducts}">
                    <div class="product-mini">
                        <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="link-cover"></a>
                        <img src="${p.thumbnail}" alt="${p.name}">
                        <h3>${p.name}</h3>

                        <p class="price">
                        <span class="new-price">
                            <fmt:formatNumber value="${p.sale_price}" type="number"/>đ
                        </span>
                            <span class="old-price">
                            <fmt:formatNumber value="${p.price}" type="number"/>đ
                        </span>
                        </p>

                        <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}&quantity=1" class="btn-add">
                            Thêm vào giỏ
                        </a>
                    </div>
                </c:forEach>
            </div>

            <div class="dots-container" id="acc-dots"></div>
        </div>


        <div class="load-more">
            <a href="san-pham">Xem thêm</a>
        </div>
    </div>

</section>

<%@include file="footer.jsp"%>