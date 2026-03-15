<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đặt lại mật khẩu</title>
    <link rel="stylesheet" href="css/reset_password.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>

<main class="main">
    <div class="forget-box">

        <a href="Verify.jsp">
            <button class="back-btn"><i class="fa-solid fa-arrow-left"></i></button>
        </a>

        <h2 class="quenMatKhau">Thiết Lập Mật Khẩu</h2>
        <p class="desc">Tạo mật khẩu mới</p>

        <form action="reset_pass" method="post">
            <input type="hidden" name="email" value="${param.email}">
            <input type="hidden" name="otp" value="${param.otp}">

            <div class="input-group password-group">
                <input type="password" id="password" name="password" placeholder="Mật khẩu mới">
            </div>

            <ul class="rules">
                <li id="rule-lower">Ít nhất một kí tự viết thường.</li>
                <li id="rule-upper">Ít nhất một kí tự viết hoa.</li>
                <li id="rule-length">8–16 kí tự.</li>
                <li id="rule-digit">Có số.</li>
                <li id="rule-special">Ký tự đặc biệt.</li>
            </ul>

            <button type="submit" class="btn-primary" id="submitBtn" disabled>TIẾP THEO</button>
        </form>

    </div>
</main>

</body>
<script src="./javaScript/reset_pass.js"></script>
</html>
