<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>${mode == 'edit' ? 'Sửa thông báo' : mode == 'view' ? 'Chi tiết thông báo' : 'Thêm thông báo'}</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/news-form.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<div class="form-page">
  <div class="form-card">
    <div class="form-header">
      <h1>
        <c:choose>
          <c:when test="${mode == 'edit'}">Sửa thông báo</c:when>
          <c:when test="${mode == 'view'}">Chi tiết thông báo</c:when>
          <c:otherwise>Thêm thông báo</c:otherwise>
        </c:choose>
      </h1>
      <a href="${pageContext.request.contextPath}/notification-admin" class="back-link">
        <i class="fa fa-arrow-left"></i> Quay lại
      </a>
    </div>

    <c:if test="${not empty param.error}">
      <div class="alert alert-error">${param.error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/notification-admin">
      <input type="hidden" name="action" value="${mode == 'edit' ? 'update' : 'create'}">
      <c:if test="${mode == 'edit'}">
        <input type="hidden" name="id" value="${notification.id}">
      </c:if>

      <div class="form-group">
        <label>Tiêu đề</label>
        <input type="text" name="title" value="${notification.title}" ${mode == 'view' ? 'readonly' : ''} required>
      </div>

      <div class="form-group">
        <label>Nội dung</label>
        <textarea name="content" rows="6" ${mode == 'view' ? 'readonly' : ''} required>${notification.content}</textarea>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>Loại thông báo</label>
          <select name="type" ${mode == 'view' ? 'disabled' : ''}>
            <option value="SYSTEM" ${notification.type == 'SYSTEM' ? 'selected' : ''}>SYSTEM</option>
            <option value="PROMOTION" ${notification.type == 'PROMOTION' ? 'selected' : ''}>PROMOTION</option>
            <option value="NEWS" ${notification.type == 'NEWS' ? 'selected' : ''}>NEWS</option>
            <option value="COUPON" ${notification.type == 'COUPON' ? 'selected' : ''}>COUPON</option>
          </select>
        </div>

        <div class="form-group">
          <label>Đối tượng nhận</label>
          <select name="targetType" id="targetType" ${mode == 'view' ? 'disabled' : ''}>
            <option value="ALL" ${notification.target_type == 'ALL' ? 'selected' : ''}>Tất cả user</option>
            <option value="USER" ${notification.target_type == 'USER' ? 'selected' : ''}>User cụ thể</option>
          </select>
        </div>
      </div>

      <div class="form-group" id="targetUserGroup" style="${notification.target_type == 'USER' ? '' : 'display:none;'}">
        <label>Chọn user</label>
        <select name="targetUserId" ${mode == 'view' ? 'disabled' : ''}>
          <option value="">-- Chọn user --</option>
          <c:forEach items="${users}" var="u">
            <option value="${u.id}" ${notification.target_user_id == u.id ? 'selected' : ''}>${u.id} - ${u.username}</option>
          </c:forEach>
        </select>
      </div>

      <div class="form-group">
        <label>Link điều hướng</label>
        <input type="text" name="link" value="${notification.link}" ${mode == 'view' ? 'readonly' : ''} placeholder="/khuyen-mai">
      </div>

      <div class="form-group">
        <label>Trạng thái</label>
        <select name="isActive" ${mode == 'view' ? 'disabled' : ''}>
          <option value="1" ${notification.is_active == 1 ? 'selected' : ''}>Hiển thị</option>
          <option value="0" ${notification.is_active == 0 ? 'selected' : ''}>Ẩn</option>
        </select>
      </div>

      <c:if test="${mode != 'view'}">
        <div class="form-actions">
          <button type="submit" class="btn-primary">
            <c:choose>
              <c:when test="${mode == 'edit'}">Cập nhật</c:when>
              <c:otherwise>Thêm thông báo</c:otherwise>
            </c:choose>
          </button>
          <a href="${pageContext.request.contextPath}/notification-admin" class="btn-secondary">Hủy</a>
        </div>
      </c:if>
    </form>
  </div>
</div>

<c:if test="${mode != 'view'}">
  <script>
    const targetType = document.getElementById("targetType");
    const targetUserGroup = document.getElementById("targetUserGroup");

    function toggleTargetUser() {
      if (targetType.value === "USER") {
        targetUserGroup.style.display = "block";
      } else {
        targetUserGroup.style.display = "none";
      }
    }

    targetType.addEventListener("change", toggleTargetUser);
    toggleTargetUser();
  </script>
</c:if>
</body>
</html>