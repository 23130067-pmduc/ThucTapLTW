<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Mã xác minh</title>
    <link rel="stylesheet" href="css/Verify.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>

<main class="main">
    <div class="card">

        <a href="forget">
            <button class="back-btn"><i class="fa-solid fa-arrow-left"></i></button>
        </a>
        <h2>Nhập mã xác nhận</h2>

        <p style="color:red; text-align:center;">
            ${error}
        </p>

        <p class="desc">
            Mã xác minh đã được gửi đến Email<br>
            <strong>${param.email}</strong>
        </p>

        <form action="otp" method="post" onsubmit=" return joinOtp()">
            <input type="hidden" name="email" value="${param.email}">
            <input type="hidden" name="otp" id="otp">
            <input type="hidden" name="type" value="${param.type}">

            <div class="otp-line">
                <input type="text" maxlength="1" class="o" inputmode="numeric">
                <input type="text" maxlength="1" class="o" inputmode="numeric">
                <input type="text" maxlength="1" class="o" inputmode="numeric">
                <input type="text" maxlength="1" class="o" inputmode="numeric">
                <input type="text" maxlength="1" class="o" inputmode="numeric">
                <input type="text" maxlength="1" class="o" inputmode="numeric">
            </div>

            <div class="resend">
                <span id="resendText">
                    Bạn vẫn chưa nhận được?
                    <a href="#" id="resendLink">Gửi lại</a>
                </span>
                <span id="countdown" style="display:none; color:#999999;">
                    Vui lòng chờ <b id="time">60</b> giây để gửi lại
                </span>
            </div>

            <button type="submit" class="btn-primary" id="submitBtn">TIẾP THEO</button>
        </form>

    </div>
</main>

</body>
<script src="./javaScript/otp.js"></script>
</html>
