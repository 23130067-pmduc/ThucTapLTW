<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập</title>
    <link rel="stylesheet" href="./css/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
    <script src="https://accounts.google.com/gsi/client" async defer></script>
</head>
<body class="login-page">

<section class="login-container">
    <%
        String error = (String) request.getAttribute("error");
        String errorParam = request.getParameter("error");
        if(error == null && errorParam != null) {
            if("require_login".equals(errorParam)) {
                error = "Vui lòng đăng nhập bằng tài khoản Admin để truy cập trang quản trị!";
            } else if("access_denied".equals(errorParam)) {
                error = "Truy cập bị từ chối!";
            }
        }
        if(error == null) error = "";
        String username = request.getParameter("username");
        if(username == null) username = "";
    %>
    <div class="login-box">
        <a href="trangchu.jsp">
            <button class="close-btn"><i class="fa-solid fa-xmark"></i></button>
        </a>
        <h2 class="dangNhap" >Đăng nhập</h2>
        <form id="loginForm" action="login" method="post">
            <div class="input-group">
                <span style="   color: red; width:100%; text-align:center; display:block; margin-bottom:5px;"><%=error%></span>
                <label for="username">Email/Tên đăng nhập</label>
                <input type="text" id="username" name="username" placeholder="Nhập email/Tên tài khoản" required value="<%=username%>">
            </div>

            <div class="input-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" placeholder="Nhập mật khẩu" required>
            </div>


            <div class="remember-forgot">
                <a href="./forget.jsp">Quên mật khẩu?</a>
            </div>


            <button type="submit" class="btn-primary">Đăng nhập</button>


            <div class="google-login-wrap">
                <div id="g_id_onload"
                     data-client_id="1001120059484-ffncp4n4pvstlq3v1q4gtlu0hprkcedd.apps.googleusercontent.com"
                     data-callback="handleCredentialResponse">
                </div>

                <div class="g_id_signin"
                     data-type="standard"
                     data-size="large"
                     data-theme="outline"
                     data-text="signin_with"
                     data-shape="rectangular"
                     data-logo_alignment="left"
                     data-width="100%">
                </div>
            </div>

            <div class="form-links">

                <p class="notAccount">Chưa có tài khoản? <a href="./register.jsp">Đăng ký ngay</a></p>
            </div>
        </form>


        <form id="googleLoginForm" action="login" method="post">
            <input type="hidden" name="credential" id="googleCredential">
        </form>

    </div>
</section>


<footer class="footer">
    <p>© 2025 SunnyBear. All rights reserved.</p>
</footer>
<script>
    function handleCredentialResponse(response) {
        document.getElementById("googleCredential").value = response.credential;
        document.getElementById("googleLoginForm").submit();
    }
</script>
</body>
</html>
