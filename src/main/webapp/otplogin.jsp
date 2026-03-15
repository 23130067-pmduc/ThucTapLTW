<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nhập mã OTP</title>
    <link rel="stylesheet" href="./css/otplogin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="forget-page">

<main class="forgot-container">
    <div class="forget-box" >
        <a href="trangchu.jsp" class="close-btn">
            <i class="fa-solid fa-arrow-left"></i>
        </a>
        <h2 class="quenMatKhau">Nhập mã OTP</h2>
        <form id="forgetForm" action="otp" method="post">
            <div class="input-group">
                <input type="hidden" name="email" value="${param.email}">
                <input type="text" name="otp" placeholder="Nhập OTP 6 số" maxlength="6" pattern="[0-9]{6}" required>
                <p style="color:red">${error}</p>
            </div>
            <button type="submit" class="btn-primary">Xác nhận</button>
        </form>
        <div class="links">
            <a href="login.jsp">Quay lại đăng nhập</a>
        </div>
    </div>
</main>
</body>
</html>