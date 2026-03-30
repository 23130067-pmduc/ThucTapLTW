<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký</title>
    <link rel="stylesheet" href="./css/register.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
    <script src="https://accounts.google.com/gsi/client" async defer></script>
</head>
<body>

<main class="register-container">
    <%
        String error = (String) request.getAttribute("error");
        if(error==null) error="";
        String username = request.getParameter("username");
        if(username==null) username="";
    %>

    <a href="trang-chu">
        <button type="button" class="close-btn"><i class="fa-solid fa-xmark"></i></button>
    </a>

    <h2 class="dangKy">Đăng ký tài khoản</h2>

    <form class="registerForm" action="register" method="post">
        <div class="input-group">
            <span style="color: red; width:100%; text-align:center; display:block; margin-bottom:5px;"><%=error%></span>
            <label for="username">Tên đăng nhập</label>
            <input type="text" name="username" id="username" placeholder="Nhập tên đăng nhập..." value="<%=username%>">
        </div>

        <div class="input-group">
            <label for="email">Email</label>
            <input type="email" name="email" id="email" placeholder="Nhập email...">
        </div>

        <div class="input-group">
            <label for="password">Mật khẩu</label>
            <input type="password" name="password" id="password" placeholder="Nhập mật khẩu...">
        </div>

        <div class="input-group">
            <label for="confirmPassword">Xác nhận mật khẩu</label>
            <input type="password" name="repassword" id="confirmPassword" placeholder="Nhập lại mật khẩu...">
        </div>

        <button type="submit" class="btn">Đăng ký</button>

        <div class="google-register-wrap">
            <div id="g_id_onload"
                 data-client_id="YOUR_GOOGLE_CLIENT_ID"
                 data-callback="handleCredentialResponse">
            </div>

            <div class="g_id_signin"
                 data-type="standard"
                 data-size="large"
                 data-theme="outline"
                 data-text="signup_with"
                 data-shape="pill"
                 data-logo_alignment="left"
                 data-width="100%">
            </div>
        </div>

        <div class="links">
            <a href="login">Đã có tài khoản? Đăng nhập</a>
        </div>
    </form>
</main>

<script>
    function handleCredentialResponse(response) {
        const idToken = response.credential;

        fetch("google-login", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: "credential=" + encodeURIComponent(idToken)
        })
            .then(res => res.text())
            .then(data => {
                if (data.trim() === "success") {
                    window.location.href = "trang-chu";
                } else {
                    alert("Đăng ký/đăng nhập bằng Google thất bại");
                }
            })
            .catch(error => {
                console.error("Google Sign-In error:", error);
                alert("Có lỗi xảy ra khi đăng ký bằng Google");
            });
    }
</script>

</body>
</html>