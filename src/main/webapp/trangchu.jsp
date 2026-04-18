<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<%
    request.setAttribute("pageCss", "trangchu.css");
    request.setAttribute("pageTitle" , "Trang chủ");
%>

<%@include file="header.jsp"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/product-card.css">


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
                        Giá:
                        <c:choose>
                            <c:when test="${p.price ne p.sale_price}">
                                <span class="new-price">
                                    <fmt:formatNumber value="${p.sale_price}" type="number"/>đ
                                </span>
                                <span class="old-price">
                                    <fmt:formatNumber value="${p.price}" type="number"/>đ
                                </span>
                            </c:when>

                            <c:otherwise>
                                <span class="new-price">
                                    <fmt:formatNumber value="${p.price}" type="number"/>đ
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </p>

                    <div class="button">
                        <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}&quantity=1" class="btn-detail">
                            Xem chi tiết
                        </a>

                        <button class="btn-add"
                                data-name="${p.name}"
                                data-image="${pageContext.request.contextPath}/${p.thumbnail}"
                                data-colors="Đỏ,Xanh,Vàng">
                            Thêm vào giỏ hàng
                        </button>
                    </div>
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
                    <div class="product-card">
                        <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="link-cover"></a>
                        <img src="${p.thumbnail}" alt="${p.name}">
                        <h3>${p.name}</h3>
                        <p class="price">
                            <c:choose>
                                <c:when test="${p.price ne p.sale_price}">
                                    <span class="new-price">
                                        <fmt:formatNumber value="${p.sale_price}" type="number"/>đ
                                    </span>
                                                            <span class="old-price">
                                        <fmt:formatNumber value="${p.price}" type="number"/>đ
                                    </span>
                                </c:when>

                                <c:otherwise>
                                    <span class="new-price">
                                        <fmt:formatNumber value="${p.price}" type="number"/>đ
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <div class="button">
                            <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}&quantity=1" class="btn-detail">
                                Xem chi tiết
                            </a>

                            <button class="btn-add">
                                Thêm vào giỏ hàng
                            </button>
                        </div>
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
                    <div class="product-card">
                        <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="link-cover"></a>
                        <img src="${p.thumbnail}" alt="${p.name}">
                        <h3>${p.name}</h3>

                        <p class="price">
                            <c:choose>
                                <c:when test="${p.price ne p.sale_price}">
                                    <span class="new-price">
                                        <fmt:formatNumber value="${p.sale_price}" type="number"/>đ
                                    </span>
                                                            <span class="old-price">
                                        <fmt:formatNumber value="${p.price}" type="number"/>đ
                                    </span>
                                </c:when>

                                <c:otherwise>
                                    <span class="new-price">
                                        <fmt:formatNumber value="${p.price}" type="number"/>đ
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </p>

                        <div class="button">
                            <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}&quantity=1" class="btn-detail">
                                Xem chi tiết
                            </a>

                            <button class="btn-add">
                                Thêm vào giỏ hàng
                            </button>
                        </div>
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
                    <div class="product-card">
                        <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="link-cover"></a>
                        <img src="${p.thumbnail}" alt="${p.name}">
                        <h3>${p.name}</h3>

                        <p class="price">
                            <c:choose>
                                <c:when test="${p.price ne p.sale_price}">
                                    <span class="new-price">
                                        <fmt:formatNumber value="${p.sale_price}" type="number"/>đ
                                    </span>
                                                            <span class="old-price">
                                        <fmt:formatNumber value="${p.price}" type="number"/>đ
                                    </span>
                                </c:when>

                                <c:otherwise>
                                    <span class="new-price">
                                        <fmt:formatNumber value="${p.price}" type="number"/>đ
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </p>

                        <div class="button">
                            <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}&quantity=1" class="btn-detail">
                                Xem chi tiết
                            </a>

                            <button class="btn-add">
                                Thêm vào giỏ hàng
                            </button>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="dots-container" id="acc-dots"></div>
        </div>


        <div class="load-more">
            <a href="san-pham">Xem thêm</a>
        </div>
    </div>




    <div id="quickAddModal" class="quick-add-modal">
        <div class="quick-add-content">
            <button type="button" class="quick-add-close" id="closeQuickAdd">&times;</button>

            <div class="quick-add-left">
                <img id="quickImage" src="" alt="Ảnh sản phẩm">
            </div>


            <div class="quick-add-right">
                <h3 id="quickAddName">Tên sản phẩm</h3>

                <div class="product-rating">
                    ⭐⭐⭐⭐☆
                    (12 đánh giá)
                </div>

                <div class="quick-add-group">
                    <div class="quick-product-colors">
                        <p><strong>Màu sắc:</strong></p>
                        <div class="quick-color-options" id="quickAddColors">
                            <button type="button" class="quick-color-thumb" data-color-id="1" style="background-color: red;"></button>
                            <button type="button" class="quick-color-thumb" data-color-id="2" style="background-color: blue;"></button>
                        </div>
                    </div>

                    <div class="quick-product-sizes">
                        <p><strong>Chọn size:</strong></p>
                        <div class="quick-size-options" id="quickAddSizes">
                            <button type="button" class="quick-size-btn" data-size-id="1">S</button>
                            <button type="button" class="quick-size-btn" data-size-id="2">M</button>
                            <button type="button" class="quick-size-btn" data-size-id="3">L</button>
                        </div>
                    </div>

                    <div class="quick-product-quantity">
                        <p><strong>Số lượng:</strong></p>
                        <div class="quick-quantity-control">
                            <button type="button" class="quick-btn-decrease">−</button>
                            <input type="number" id="quickQuantity" min="1" value="1">
                            <button type="button" class="quick-btn-increase">+</button>
                        </div>
                    </div>
                </div>

                <button type="button" class="quick-add-confirm">
                    Thêm vào giỏ hàng
                </button>

            </div>
        </div>
    </div>

</section>

<%@include file="footer.jsp"%>