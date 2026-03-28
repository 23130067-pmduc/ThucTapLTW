<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Form bài viết</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/news-form.css">
</head>
<body>
<c:set var="readOnly" value="${mode == 'view'}"/>
<div class="container">
    <div class="form-header">
        <a href="${pageContext.request.contextPath}/news-admin" class="btn-back">← Quay lại</a>
        <h2>
            <c:choose>
                <c:when test="${mode == 'add'}">Thêm bài viết</c:when>
                <c:when test="${mode == 'edit'}">Chỉnh sửa bài viết</c:when>
                <c:otherwise>Xem chi tiết bài viết</c:otherwise>
            </c:choose>
        </h2>
    </div>

    <c:if test="${not empty param.error}">
        <div class="alert alert-error">${param.error}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/news-admin" method="post" enctype="multipart/form-data">
        <div class="card">
            <h3>Thông tin bài viết</h3>
            <div class="row">
                <div class="col col-2">
                    <label for="title">Tiêu đề</label>
                    <input type="text" id="title" name="title" value="${news.title}" ${readOnly ? 'readonly' : ''} required>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <label for="shortDescription">Mô tả ngắn</label>
                    <textarea id="shortDescription" name="shortDescription" rows="4" ${readOnly ? 'readonly' : ''} required>${news.shortDescription}</textarea>
                </div>
            </div>
        </div>

        <div class="card">
            <h3>Nội dung</h3>
            <div class="row">
                <div class="col">
                    <label for="content">Nội dung bài viết</label>
                    <textarea id="content" name="content" rows="12" ${readOnly ? 'readonly' : ''} required>${news.content}</textarea>
                </div>
            </div>
        </div>

        <div class="card">
            <h3>Hiển thị</h3>
            <div class="row">
                <div class="col">
                    <label for="imageFile">Ảnh đại diện</label>
                    <input type="file" id="imageFile" name="imageFile" accept="image/*" ${readOnly ? 'disabled' : ''}>
                    <small class="form-note">Hỗ trợ file jpg, jpeg, png, webp.</small>
                    <c:if test="${not empty news.thumbnail}">
                        <div class="preview">
                            <img src="${pageContext.request.contextPath}/${news.thumbnail}" alt="Thumbnail hiện tại">
                        </div>
                    </c:if>
                </div>
                <div class="col col-status">
                    <label for="status">Trạng thái</label>
                    <select id="status" name="status" ${readOnly ? 'disabled' : ''}>
                        <option value="1" ${news.status == 1 ? 'selected' : ''}>Hiển thị</option>
                        <option value="0" ${news.status == 0 ? 'selected' : ''}>Ẩn</option>
                    </select>
                    <c:if test="${readOnly}">
                        <input type="hidden" name="status" value="${news.status}">
                    </c:if>
                </div>
            </div>
        </div>

        <input type="hidden" name="id" value="${news.id}">
        <input type="hidden" name="action" value="${mode == 'add' ? 'create' : 'update'}">

        <c:if test="${not readOnly}">
            <div class="form-footer">
                <button type="submit" class="btn-primary">Lưu bài viết</button>
                <a href="${pageContext.request.contextPath}/news-admin" class="btn-secondary">Hủy</a>
            </div>
        </c:if>
    </form>
</div>
</body>
</html>
