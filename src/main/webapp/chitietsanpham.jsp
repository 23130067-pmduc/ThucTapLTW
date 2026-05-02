<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%
    request.setAttribute("pageCss", "chitietsanpham.css");
    request.setAttribute("pageTitle" , "Chi tiết sản phẩm");
%>

<%@include file="header.jsp"%>


<main class="product-detail">
    <div class="product-container">

        <div class="product-image">
            <c:forEach var="img" items="${images}">
                <c:if test="${img.main}">
                    <img id="main-image"
                         src="${img.imageUrl}"
                         alt="${product.name}">
                </c:if>
            </c:forEach>

            <div class="image-thumbs">
                <c:forEach var="img" items="${images}">
                    <img class="thumb ${img.main ? 'active' : ''}"
                         src="${img.imageUrl}"
                         alt="${product.name}">
                </c:forEach>
            </div>
        </div>

        <div class="product-info">
            <h1 class="product-name">${product.name}</h1>

            <p class="product-price">Giá:
                <span><fmt:formatNumber value="${product.sale_price}" type="number"/>₫</span>
            </p>
            <div class="product-rating">
                <c:forEach begin="1" end="${displayStar}">⭐
                </c:forEach>

                <c:forEach begin="1" end="${5 - displayStar}">☆
                </c:forEach>

                (${totalReviews} đánh giá)
            </div>


            <div class="product-colors">
                <p><strong>Màu sắc:</strong></p>
                <div class="color-options">
                    <c:forEach var="color" items="${colors}">
                        <div class="color-thumb" data-color-id="${color.id}" style="background-color: ${color.hex};">
                        </div>
                    </c:forEach>
                </div>
            </div>


            <div class="product-sizes">
                <p><strong>Chọn size:</strong></p>
                <div class="size-options">
                    <c:forEach var="s" items="${sizes}">
                        <button class="size-btn"
                                data-size-id="${s.id}">
                                ${s.code}
                        </button>
                    </c:forEach>
                </div>
            </div>

            <div class="product-quantity">
                <p><strong>Số lượng:</strong></p>
                <div class="quantity-control">
                    <button class="btn-decrease">−</button>
                    <input type="number" id="quantity" min="1" value="1">
                    <button class="btn-increase">+</button>
                </div>
            </div>


            <div class="product-actions">
                <button class="btn-add-cart">Thêm vào giỏ hàng</button>
            </div>
        </div>
    </div>

    <section class="product-description">
        <h2>Mô tả chi tiết</h2>
        <div class="product-description">
            ${product.description}
        </div>

    </section>

    <section class="product-review">
        <c:if test="${param.reviewSuccess == '1'}">
            <p class="review-success">Đánh giá của bạn đã được gửi thành công!</p>
        </c:if>

        <c:if test="${param.reviewError == 'no_permission'}">
            <p class="review-error">
                Bạn chưa mua sản phẩm này hoặc đã dùng hết lượt đánh giá.
            </p>
        </c:if>

        <c:choose>
            <c:when test="${canReview}">
                <p class="review-note">
                    Bạn còn ${remainingReviewTimes} lượt đánh giá cho sản phẩm này.
                </p>

                <form action="${pageContext.request.contextPath}/review" method="post" class="review-form">
                    <input type="hidden" name="product_id" value="${product.id}">
                    <input type="hidden" name="rating" id="rating-value" value="5">

                    <div class="star-select">
                        <span class="star" data-value="1">★</span>
                        <span class="star" data-value="2">★</span>
                        <span class="star" data-value="3">★</span>
                        <span class="star" data-value="4">★</span>
                        <span class="star" data-value="5">★</span>
                    </div>

                    <textarea id="review-text"
                              name="comment"
                              required
                              placeholder="Nhập nhận xét của bạn..."></textarea>

                    <button type="submit" class="review-submit-btn">Gửi đánh giá</button>
                </form>
            </c:when>

            <c:otherwise>
                <p class="review-warning">
                    Bạn cần mua sản phẩm này và còn lượt đánh giá thì mới được đánh giá.
                </p>
            </c:otherwise>
        </c:choose>



        <section class="review-list">
            <h3>Đánh giá của khách hàng</h3>

            <c:if test="${empty reviews}">
                <p>Chưa có đánh giá nào.</p>
            </c:if>

            <c:forEach var="r" items="${reviews}">
                <div class="review-item">
                    <strong>${r.userName}</strong>
                    <div>
                        <c:forEach begin="1" end="${r.rating}">⭐</c:forEach>
                    </div>
                    <p>${r.comment}</p>
                    <small>${r.createdAtFormatted}</small>
                </div>
            </c:forEach>
        </section>

    </section>

    <section class="suggested-products">
        <h2>Sản phẩm phù hợp khác</h2>
        <div class="suggested-list">
            <c:forEach var="item" items="${ralatedProducts}">
                <div class="suggested-item">
                    <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${item.id}" class="link-cover">
                        <img src="${item.thumbnail}" alt="${item.name}">
                    </a>
                    <h3 class="name"><a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${item.id}">${item.name}</a></h3>
                    <fmt:setLocale value="vi_VN"/>
                    <p class="price">
                        Giá: <span class="new-price">
                        <fmt:formatNumber value="${item.sale_price}" type="number" groupingUsed="true"/>đ
                    </span>
                    </p>
                    <a href="${pageContext.request.contextPath}/chi-tiet-san-pham?id=${item.id}" class="btn-add">
                        Thêm vào giỏ
                    </a>
                </div>
            </c:forEach>

            <c:if test="${empty ralatedProducts}">
                <p>Không tìm thấy sản phẩm phù hợp khác.</p>
            </c:if>
        </div>
        <a href="san-pham" class="btn-view-more">Xem thêm</a>
    </section>
</main>

<script>
    const variants = [
        <c:forEach var="v" items="${variants}" varStatus="st">
        {
            id: ${v.id},
            colorId: ${v.colorId},
            sizeId: ${v.sizeId},
            stock: ${v.stock}
        }<c:if test="${!st.last}">,</c:if>
        </c:forEach>
    ];
</script>

<%@include file="footer.jsp"%>