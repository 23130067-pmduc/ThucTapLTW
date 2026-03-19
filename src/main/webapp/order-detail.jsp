<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết sản phẩm</title>
    <link rel="stylesheet" href="./css/user-form.css">
</head>
<body>

<div class="container">

    <!-- HEADER -->
    <div class="form-header">
        <a href="order-admin" class="btn-back">← Quay lại danh sách</a>
        <h2>Chi tiết đơn hàng #${order.id}</h2>
    </div>

    <div class="card">
        <h3>Thông tin người nhận</h3>
        <p><b>Người nhận:</b> ${order.receiverName}</p>
        <p><b>SĐT:</b> ${order.phone}</p>
        <p><b>Địa chỉ:</b> ${order.shippingAddress}</p>
    </div>

    <div class="card">
        <h3>Trạng thái đơn hàng</h3>

        <form method="post" action="order-admin" class="status-form">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="${order.id}">

            <label>Trạng thái</label>
            <select name="orderStatus">
                <option value="PENDING" ${order.orderStatus=='PENDING'?'selected':''}>Chờ xử lý</option>
                <option value="SHIPPING" ${order.orderStatus=='SHIPPING'?'selected':''}>Đang giao</option>
                <option value="COMPLETED" ${order.orderStatus=='COMPLETED'?'selected':''}>Hoàn thành</option>
                <option value="CANCELLED" ${order.orderStatus=='CANCELLED'?'selected':''}>Đã hủy</option>
            </select>

            <button class="btn-primary">Cập nhật</button>
        </form>
    </div>

    <div class="card">
        <h3>Sản phẩm</h3>

        <table class="order-table">
            <thead>
            <tr>
                <th>Ảnh</th>
                <th>Sản phẩm</th>
                <th>Size</th>
                <th>Màu</th>
                <th>SL</th>
                <th>Giá</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${items}" var="i">
                <tr>
                    <td>
                        <img src="${i.thumbnail}" class="product-thumb"
                             onerror="this.src='img/no-image.png'">
                    </td>
                    <td>${i.productName}</td>
                    <td>${i.size}</td>
                    <td>${i.color}</td>
                    <td>${i.quantity}</td>
                    <td>${i.price} đ</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

</div>


<c:if test="${mode == 'view'}">
    <style>
        input, select, textarea, button {
            pointer-events: none;
            background: #f2f2f2;
        }
        .btn-secondary {
            pointer-events: auto;
        }
    </style>
</c:if>

</body>
</html>

