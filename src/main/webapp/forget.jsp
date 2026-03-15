<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên mật khẩu</title>
    <link rel="stylesheet" href="./css/forget.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="forget-page">

<main class="forgot-container">
    <div class="forget-box">
        <a href="trangchu.jsp">
            <button class="back-btn"><i class="fa-solid fa-arrow-left"></i></button>
        </a>

        <h2 class="quenMatKhau">Quên mật khẩu</h2>
        <p style="color:red; text-align:center;">
            ${error}
        </p>
        <form id="forgetForm" action="forget" method="post">
            <div class="input-group">
                <label for="email">Email đã đăng ký</label>
                <input type="email" id="email" name="email" placeholder="Nhập email" required>
            </div>

            <button type="submit" class="btn-primary">Tiếp theo</button>
        </form>
        <div class="links">
            <a href="login.jsp">Quay lại đăng nhập</a>
        </div>
    </div>
</main>

</body>
</html>