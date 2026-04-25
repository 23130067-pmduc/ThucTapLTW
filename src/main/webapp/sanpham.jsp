<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%
    request.setAttribute("pageCss", "sanpham.css");
    request.setAttribute("pageTitle" , "Trang chủ");
%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/quick-add-modal.css">
<%@include file="header.jsp"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/product-card.css">



<section class="products">
    <h2>SẢN PHẨM</h2>

    <div class="filter-bar">

        <div class="filter-sort">
            <div class="sort-buttons">

                <a href="san-pham?group=${param.group}&category=${param.category}&sort=new">
                    <button class="${param.sort eq 'new' || empty param.sort ? 'active' : ''}">
                        Mới nhất
                    </button>
                </a>

                <a href="san-pham?group=${param.group}&category=${param.category}&sort=best">
                    <button class="${param.sort eq 'best' ? 'active' : ''}">
                        Bán chạy
                    </button>
                </a>

                <a href="san-pham?group=${param.group}&category=${param.category}&sort=sale">
                    <button class="${param.sort eq 'sale' ? 'active' : ''}">
                        Khuyến mãi
                    </button>
                </a>



                <div class="dropdown">
                    <button class="dropbtn
                ${param.sort eq 'price_asc' || param.sort eq 'price_desc' ? 'active' : ''}">
                        Giá <i class="fa-solid fa-caret-down"></i>
                    </button>

                    <div class="dropdown-content">
                        <a class="${param.sort eq 'price_asc' ? 'active' : ''}"
                           href="san-pham?group=${param.group}&category=${param.category}&sort=price_asc">
                            Giá thấp đến cao
                        </a>

                        <a class="${param.sort eq 'price_desc' ? 'active' : ''}"
                           href="san-pham?group=${param.group}&category=${param.category}&sort=price_desc">
                            Giá cao đến thấp
                        </a>
                    </div>
                </div>

            </div>
        </div>


        <div class="filter-category">


            <a href="san-pham?sort=${param.sort}">
                <button class="${empty param.group && empty param.category ? 'active' : ''}">
                    Tất cả
                </button>
            </a>



            <div class="dropdown">
                <button class="dropbtn ${param.group eq 'betrai' ? 'active' : ''}">
                    Bé trai <i class="fa-solid fa-caret-down"></i>
                </button>
                <div class="dropdown-content">
                    <a class="${param.group eq 'betrai' && empty param.category ? 'active' : ''}"
                       href="san-pham?group=betrai&sort=${param.sort}">
                        Tất cả
                    </a>

                    <a class="${param.group eq 'betrai' && param.category eq '1' ? 'active' : ''}"
                       href="san-pham?group=betrai&category=1&sort=${param.sort}">
                        Áo
                    </a>
                    <a class="${param.group eq 'betrai' && param.category eq '2' ? 'active' : ''}"
                       href="san-pham?group=betrai&category=2&sort=${param.sort}">
                        Quần
                    </a>
                    <a class="${param.group eq 'betrai' && param.category eq '3' ? 'active' : ''}"
                       href="san-pham?group=betrai&category=3&sort=${param.sort}">
                        Đồ bộ
                    </a>
                    <a class="${param.group eq 'betrai' && param.category eq '9' ? 'active' : ''}"
                       href="san-pham?group=betrai&category=9&sort=${param.sort}">
                        Giày dép
                    </a>
                </div>
            </div>




            <div class="dropdown">
                <button class="dropbtn ${param.group eq 'begai' ? 'active' : ''}">
                    Bé gái <i class="fa-solid fa-caret-down"></i>
                </button>
                <div class="dropdown-content">
                    <a class="${param.group eq 'begai' && empty param.category ? 'active' : ''}"
                       href="san-pham?group=begai&sort=${param.sort}">
                        Tất cả
                    </a>


                    <a class="${param.group eq 'begai' && param.category eq '4' ? 'active' : ''}"
                       href="san-pham?group=begai&category=4&sort=${param.sort}">
                        Áo
                    </a>
                    <a class="${param.group eq 'begai' && param.category eq '5' ? 'active' : ''}"
                       href="san-pham?group=begai&category=5&sort=${param.sort}">
                        Quần
                    </a>
                    <a class="${param.group eq 'begai' && param.category eq '6' ? 'active' : ''}"
                       href="san-pham?group=begai&category=6&sort=${param.sort}">
                        Váy / Đầm
                    </a>
                    <a class="${param.group eq 'begai' && param.category eq '7' ? 'active' : ''}"
                       href="san-pham?group=begai&category=7&sort=${param.sort}">
                        Đồ bộ
                    </a>
                    <a class="${param.group eq 'begai' && param.category eq '9' ? 'active' : ''}"
                       href="san-pham?group=begai&category=9&sort=${param.sort}">
                        Giày dép
                    </a>
                </div>
            </div>




            <div class="dropdown">
                <button class="dropbtn ${param.group eq 'phukien' ? 'active' : ''}">
                    Phụ kiện <i class="fa-solid fa-caret-down"></i>
                </button>
                <div class="dropdown-content">
                    <a class="${param.group eq 'phukien' && empty param.category ? 'active' : ''}"
                       href="san-pham?group=phukien&sort=${param.sort}">
                        Tất cả
                    </a>


                    <a class="${param.group eq 'phukien' && param.category eq '8' ? 'active' : ''}"
                       href="san-pham?group=phukien&category=8&sort=${param.sort}">
                        Phụ kiện thời trang
                    </a>
                    <a class="${param.group eq 'phukien' && param.category eq '10' ? 'active' : ''}"
                       href="san-pham?group=phukien&category=10&sort=${param.sort}">
                        Phụ kiện tiện ích
                    </a>
                </div>
            </div>

        </div>


    </div>


    <div class="product-list">

        <c:forEach var="p" items="${list}" >

            <div class="product-card">
                <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${p.id}" class="link-cover"></a>

                <img src="${p.thumbnail}" alt="${p.name}">

                <h3>${p.name}</h3>

                <fmt:setLocale value="vi_VN"/>
                <p class="price">
                    Giá:

                    <c:choose>
                        <c:when test="${p.price ne p.sale_price}">
                            <span class="new-price">
                                <fmt:formatNumber value="${p.sale_price}" type="number" groupingUsed="true"/>đ
                            </span>
                                            <span class="old-price">
                                <fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/>đ
                            </span>
                        </c:when>

                        <c:otherwise>
                            <span class="new-price">
                                <fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/>đ
                            </span>
                        </c:otherwise>
                    </c:choose>
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
    <div class="load-more-container">
        <button id="load-more">Xem thêm</button>
    </div>

    <button id="scrollTopBtn" title="Lên đầu trang">
        <i class="fa-solid fa-arrow-up"></i>
    </button>



</section>
<%@ include file="quick-add-modal.jsp" %>

<%@ include file="footer.jsp"%>