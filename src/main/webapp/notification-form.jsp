<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>
    <c:choose>
      <c:when test="${mode == 'edit'}">Chỉnh sửa thông báo</c:when>
      <c:when test="${mode == 'view'}">Chi tiết thông báo</c:when>
      <c:otherwise>Thêm thông báo</c:otherwise>
    </c:choose>
  </title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user-form.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>

<div class="container">

  <div class="form-header">
    <a href="${pageContext.request.contextPath}/notification-admin" class="btn-back">← Quay lại</a>
    <h2>
      <c:choose>
        <c:when test="${mode == 'edit'}">Chỉnh sửa thông báo</c:when>
        <c:when test="${mode == 'view'}">Chi tiết thông báo</c:when>
        <c:otherwise>Thêm thông báo</c:otherwise>
      </c:choose>
    </h2>
  </div>

  <c:if test="${not empty param.error}">
    <div class="alert alert-error">${param.error}</div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/notification-admin">
    <input type="hidden" name="action" value="${mode == 'edit' ? 'update' : 'create'}">
    <c:if test="${mode == 'edit' || mode == 'view'}">
      <input type="hidden" name="id" value="${notification.id}">
    </c:if>

    <div class="card">
      <h3>Thông tin thông báo</h3>

      <c:if test="${mode != 'add'}">
        <div class="row">
          <div class="col">
            <label>ID</label>
            <input type="text" value="${notification.id}" readonly>
          </div>
        </div>
      </c:if>

      <div class="row">
        <div class="col">
          <label>Tiêu đề <span style="color: red;">*</span></label>
          <input type="text" name="title" value="${notification.title}" ${mode == 'view' ? 'readonly' : ''} required>
        </div>

        <div class="col">
          <label>Loại thông báo <span style="color: red;">*</span></label>
          <select name="type" ${mode == 'view' ? 'disabled' : ''} required>
            <option value="">-- Chọn loại --</option>
            <option value="SYSTEM" ${notification.type == 'SYSTEM' ? 'selected' : ''}>SYSTEM</option>
            <option value="PROMOTION" ${notification.type == 'PROMOTION' ? 'selected' : ''}>PROMOTION</option>
            <option value="NEWS" ${notification.type == 'NEWS' ? 'selected' : ''}>NEWS</option>
            <option value="COUPON" ${notification.type == 'COUPON' ? 'selected' : ''}>COUPON</option>
          </select>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <label>Nội dung <span style="color: red;">*</span></label>
          <textarea name="content" rows="5" ${mode == 'view' ? 'readonly' : ''} required>${notification.content}</textarea>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <label>Đối tượng nhận <span style="color: red;">*</span></label>
          <select name="targetType" id="targetType" ${mode == 'view' ? 'disabled' : ''} required>
            <option value="ALL" ${notification.target_type == 'ALL' ? 'selected' : ''}>Tất cả user</option>
            <option value="USER" ${notification.target_type == 'USER' ? 'selected' : ''}>User cụ thể</option>
          </select>
        </div>

        <div class="col" id="targetUserGroup" style="${notification.target_type == 'USER' ? '' : 'display:none;'}">
          <label>Chọn user</label>
          <select name="targetUserId" ${mode == 'view' ? 'disabled' : ''}>
            <option value="">-- Chọn user --</option>
            <c:forEach items="${users}" var="u">
              <option value="${u.id}" ${notification.target_user_id == u.id ? 'selected' : ''}>${u.id} - ${u.username}</option>
            </c:forEach>
          </select>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <label>Link điều hướng</label>
          <input type="text" name="link" value="${notification.link}" ${mode == 'view' ? 'readonly' : ''} placeholder="/khuyen-mai">
        </div>

        <div class="col">
          <label>Trạng thái</label>
          <select name="isActive" ${mode == 'view' ? 'disabled' : ''}>
            <option value="1" ${notification.is_active == 1 ? 'selected' : ''}>Hiển thị</option>
            <option value="0" ${notification.is_active == 0 ? 'selected' : ''}>Ẩn</option>
          </select>
        </div>
      </div>
    </div>

    <div class="form-footer">
      <c:if test="${mode != 'view'}">
        <button type="submit" class="btn-primary">
          <c:choose>
            <c:when test="${mode == 'edit'}">Cập nhật</c:when>
            <c:otherwise>Thêm thông báo</c:otherwise>
          </c:choose>
        </button>
      </c:if>
      <a href="${pageContext.request.contextPath}/notification-admin" class="btn-secondary">Hủy</a>
    </div>

    <input type="hidden" name="mode" value="${mode}">
  </form>

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

<script>
  const targetType = document.getElementById("targetType");
  const targetUserGroup = document.getElementById("targetUserGroup");

  function toggleTargetUser() {
    if (targetType.value === "USER") {
      targetUserGroup.style.display = "block";
      targetUserGroup.querySelector("select").removeAttribute("disabled");
    } else {
      targetUserGroup.style.display = "none";
      targetUserGroup.querySelector("select").setAttribute("disabled", "disabled");
    }
  }

  targetType.addEventListener("change", toggleTargetUser);
  
  // Initialize on page load
  if (targetType) {
    toggleTargetUser();
  }
</script>

</body>
</html>