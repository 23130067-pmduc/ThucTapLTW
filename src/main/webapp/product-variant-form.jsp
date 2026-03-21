<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${mode == 'add'}">Thêm biến thể</c:when>
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
                <c:when test="${mode == 'add'}">Thêm biến thể</c:when>
                <c:otherwise>Chỉnh sửa biến thể</c:otherwise>
            </c:choose>
        </h2>
    </div>

    <form method="post" action="product-variant-admin" id="variantForm">
        <section class="card">
            <h3>Thông tin biến thể</h3>

            <c:if test="${mode == 'edit'}">
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
                    <select id="sizeId" name="sizeId" required <c:if test="${mode == 'edit'}">disabled</c:if>>
                        <c:forEach items="${sizes}" var="size">
                            <option value="${size.id}" ${mode == 'edit' && size.id == variant.sizeId ? 'selected' : ''}>
                                    ${size.code}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="col">
                    <label for="colorId">Màu sắc</label>
                    <select id="colorId" name="colorId" required <c:if test="${mode == 'edit'}">disabled</c:if>>
                        <c:forEach items="${colors}" var="color">
                            <option value="${color.id}" ${mode == 'edit' && color.id == variant.colorId ? 'selected' : ''}>
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
                           value="${mode == 'edit' ? variant.price : 0}">
                </div>

                <div class="col">
                    <label for="salePrice">Giá sale</label>
                    <input id="salePrice" type="number" name="salePrice" step="0.01" min="0" required
                           value="${mode == 'edit' ? variant.salePrice : 0}">
                </div>
            </div>

            <div class="row single-row">
                <div class="col">
                    <label for="stock">Tồn kho</label>
                    <input id="stock" type="number" name="stock" min="0" required
                           value="${mode == 'edit' ? variant.stock : 0}">
                </div>
            </div>
        </section>

        <div class="form-footer">
            <button type="submit"
                    name="action"
                    value="${mode == 'add' ? 'create' : 'update'}"
                    class="btn-primary">
                Lưu
            </button>

            <a href="product-variant-admin?productId=${productId}" class="btn-secondary">Hủy</a>
        </div>

        <input type="hidden" name="productId" value="${productId}">
        <c:if test="${mode == 'edit'}">
            <input type="hidden" name="id" value="${variant.id}">
            <input type="hidden" name="sizeId" value="${variant.sizeId}">
            <input type="hidden" name="colorId" value="${variant.colorId}">
        </c:if>
    </form>
</div>

<script src="js/product-variant-form.js"></script>
</body>
</html>
