<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix = "c" uri = "jakarta.tags.core" %>
<%@ taglib prefix = "fmt" uri = "jakarta.tags.fmt" %>

<%
    request.setAttribute("pageCss", "giohang.css");
    request.setAttribute("pageTitle" , "Giỏ hàng");
%>

<%@include file="header.jsp"%>

<div class="title">
    <span>GIỎ HÀNG CỦA BẠN</span>
</div>
<section class="card">

    <div class="container">
        <div class="card-content-left">
            <div class="cart-table-wrapper">
                <c:if test="${not empty param.success}">
                    <div class="cart-alert cart-alert-success">${param.success}</div>
                </c:if>
                <c:if test="${not empty param.error}">
                    <div class="cart-alert cart-alert-error">${param.error}</div>
                </c:if>

                <c:choose>
                    <c:when test="${empty cartItems}">
                        <div class="cart-empty-state">
                            <span class="empty-icon">🛒</span>
                            <p>Giỏ hàng của bạn đang trống</p>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <table>
                            <thead>
                            <tr>
                                <th>Sản phẩm</th>
                                <th>Phân loại</th>
                                <th>Số lượng</th>
                                <th>Thành tiền</th>
                                <th>Xóa</th>
                            </tr>
                            </thead>

                            <tbody>
                            <c:forEach var="item" items="${cartItems}">
                                <tr data-price="${item.price}" data-stock="${item.stock}">
                                    <td>
                                        <div class="product-cell">
                                            <img src="${item.product.thumbnail}" alt="${item.product.name}">
                                            <div class="product-cell-info">
                                                <span class="product-cell-name">${item.product.name}</span>
                                            </div>
                                        </div>
                                    </td>

                                    <td>
                                        <span class="variant-label">Size: <b>${item.size}</b></span><br>
                                        <span class="variant-label">Màu: <b>${item.color}</b></span>
                                    </td>

                                    <td>
                                        <form action="update-cart"
                                              method="post"
                                              class="qty-form">

                                            <input type="hidden" name="variantId" value="${item.variantId}">

                                            <button type="button" class="btn-minus">−</button>

                                            <input type="text"
                                                   name="quantity"
                                                   class="qty-display"
                                                   value="${item.quantity}"
                                                   max="${item.stock}"
                                                   readonly
                                                   style="width:40px; text-align:center;">

                                            <button type="button" class="btn-plus">+</button>
                                        </form>
                                    </td>
                                    <td>
                                        <fmt:formatNumber value="${item.price * item.quantity}" type="number"/> ₫
                                    </td>

                                    <td>
                                        <form action="del-item" method="post">
                                            <input type="hidden" name="variantId" value="${item.variantId}">
                                            <button type="submit"> <i class="fa fa-trash"></i></button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>
        <div class="card-content-right">
            <table>
                <tr>
                    <th colspan="2">TỔNG TIỀN GIỎ HÀNG</th>
                </tr>

                <tr>
                    <td>TỔNG SẢN PHẨM</td>
                    <td><span id="totalQuantity">0</span></td>
                </tr>

                <tr>
                    <td>TỔNG TIỀN HÀNG</td>
                    <td>
                        <span id="totalPrice">0</span>₫
                    </td>
                </tr>

                <tr>
                    <td>TẠM TÍNH</td>
                    <td style="font-weight:bold">
                        <span id="totalFinal">0</span>₫
                    </td>
                </tr>
            </table>


            <div class="card-content-right-button">
                <a href="san-pham">
                    <button id="ttms">TIẾP TỤC MUA SẮM</button>
                </a>

                <c:if test="${not empty cartItems}">
                    <form action="${pageContext.request.contextPath}/checkout" method="get" id="checkoutForm" class="checkout-form">

                        <button type="submit" id="tt">
                            THANH TOÁN
                        </button>
                    </form>
                </c:if>
            </div>

        </div>
    </div>
</section>

<%@include file="footer.jsp"%>
<script src="./javaScript/giohang.js"></script>
