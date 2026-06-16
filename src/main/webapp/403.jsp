<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>403 - Không có quyền truy cập</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/403.css">
</head>
<body>

<div class="error-page">
    <div class="error-card">
        <div class="error-icon">🔒</div>

        <h1>403</h1>
        <h2>Không có quyền truy cập</h2>

        <p>
            Tài khoản của bạn chưa được cấp quyền để truy cập chức năng này.
        </p>

        <div class="error-actions">
            <button onclick="goBack()" class="btn btn-secondary">Quay lại</button>
        </div>

        <script>
            function goBack() {
                if (document.referrer) {
                    history.back();
                } else {
                    window.location.href = '${pageContext.request.contextPath}/trang-chu';
                }
            }
        </script>
    </div>
</div>

</body>
</html>