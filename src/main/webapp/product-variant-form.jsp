<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="isAdd" value="${mode == 'add'}" />
<c:set var="isEdit" value="${mode == 'edit'}" />
<c:set var="isView" value="${mode == 'view'}" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${isAdd}">Thêm biến thể</c:when>
            <c:when test="${isView}">Xem chi tiết biến thể</c:when>
            <c:otherwise>Chỉnh sửa biến thể</c:otherwise>
        </c:choose>
    </title>
    <link rel="stylesheet" href="css/product-variant-form.css">
</head>
<body>
<div class="container">
    <div class="form-header">
        <a href="product-variant-admin?productId=${productId}" class="btn-back">← Quay lại</a>
        <h2>
            <c:choose>
                <c:when test="${isAdd}">Thêm biến thể</c:when>
                <c:when test="${isView}">Xem chi tiết biến thể</c:when>
                <c:otherwise>Chỉnh sửa biến thể</c:otherwise>
            </c:choose>
        </h2>
    </div>

    <c:choose>
        <c:when test="${isView}">
            <section class="card detail-card">
                <h3 class="card-title"><span class="card-title-dot"></span>Thông tin biến thể</h3>

                <div class="row single-row">
                    <div class="col">
                        <label>ID</label>
                        <input type="text" value="${variant.id}" readonly>
                    </div>
                </div>

                <div class="row">
                    <div class="col">
                        <label>Size</label>
                        <input type="text" value="${variant.sizeName}" readonly>
                    </div>

                    <div class="col">
                        <label>Màu sắc</label>
                        <input type="text" value="${variant.colorName}" readonly>
                    </div>
                </div>

                <div class="row">
                    <div class="col">
                        <label>Giá gốc</label>
                        <input type="text" value="<fmt:formatNumber value='${variant.price}' type='number'/> ₫" readonly>
                    </div>

                    <div class="col">
                        <label>Giá sale</label>
                        <input type="text" value="<fmt:formatNumber value='${variant.salePrice}' type='number'/> ₫" readonly>
                    </div>
                </div>

                <div class="row">
                    <div class="col">
                        <label>Tồn kho</label>
                        <input type="text" value="${variant.stock}" readonly>
                    </div>

                    <div class="col">
                        <label>Trạng thái tồn kho</label>
                        <c:choose>
                            <c:when test="${variant.stock == 0}">
                                <input type="text" value="Hết hàng" readonly>
                            </c:when>
                            <c:when test="${variant.stock < 10}">
                                <input type="text" value="Sắp hết hàng" readonly>
                            </c:when>
                            <c:otherwise>
                                <input type="text" value="Còn hàng" readonly>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </section>

            <div class="form-footer view-footer">
                <a href="product-variant-admin?productId=${productId}" class="btn-secondary">Đóng</a>
            </div>
        </c:when>

        <c:otherwise>
            <form method="post" action="product-variant-admin" id="variantForm">
                <section class="card detail-card">
                    <h3 class="card-title"><span class="card-title-dot"></span>Thông tin biến thể</h3>

                    <c:if test="${isEdit}">
                        <div class="row single-row">
                            <div class="col">
                                <label>ID</label>
                                <input type="text" value="${variant.id}" readonly>
                            </div>
                        </div>
                    </c:if>

                    <div class="row">
                        <div class="col">
                            <label for="sizeId">Size</label>
                            <select id="sizeId" name="sizeId" required>
                                <option value="">Chọn size</option>
                                <c:forEach items="${sizes}" var="size">
                                    <option value="${size.id}" ${variant != null && size.id == variant.sizeId ? 'selected="selected"' : ''}>
                                            ${size.code}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col">
                            <label for="colorId">Màu sắc</label>
                            <select id="colorId" name="colorId" required>
                                <option value="">Chọn màu</option>
                                <c:forEach items="${colors}" var="color">
                                    <option value="${color.id}" ${variant != null && color.id == variant.colorId ? 'selected="selected"' : ''}>
                                            ${color.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col">
                            <label for="price">Giá</label>
                            <input id="price" type="number" name="price" step="0.01" min="0" required
                                   value="${variant != null ? variant.price : 0}">
                        </div>

                        <div class="col">
                            <label for="salePrice">Giá sale</label>
                            <input id="salePrice" type="number" name="salePrice" step="0.01" min="0" required
                                   value="${variant != null ? variant.salePrice : 0}">
                        </div>
                    </div>

                    <div class="row single-row">
                        <div class="col">
                            <label for="stock">Tồn kho</label>
                            <input id="stock" type="number" name="stock" min="0" required
                                   value="${variant != null ? variant.stock : 0}">
                        </div>
                    </div>
                </section>

                <div class="form-footer">
                    <button type="submit" name="action" value="${isAdd ? 'create' : 'update'}" class="btn-primary">
                        Lưu
                    </button>
                    <a href="product-variant-admin?productId=${productId}" class="btn-secondary">Hủy</a>
                </div>

                <input type="hidden" name="productId" value="${productId}">
                <c:if test="${isEdit}">
                    <input type="hidden" name="id" value="${variant.id}">
                </c:if>
            </form>
        </c:otherwise>
    </c:choose>
</div>

<script src="javaScript/product-variant-form.js"></script>
</body>
</html>
