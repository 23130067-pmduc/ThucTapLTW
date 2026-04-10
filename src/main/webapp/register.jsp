<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<%
    request.setAttribute("pageCss", "register.css");
    request.setAttribute("pageTitle" , "Đăng ký");
%>

<%@include file="header.jsp"%>

<main class="register-container">
    <%
        String error = (String) request.getAttribute("error");
        if(error==null) error="";
        String username = request.getParameter("username");
        if(username==null) username="";
    %>

    <div class="register-box">
        <a href="trang-chu">
            <button class="close-btn"><i class="fa-solid fa-xmark"></i></button>
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

            <div class="links">
                <a href="login">Đã có tài khoản? Đăng nhập</a>
            </div>
        </form>
    </div>


</main>

<%@include file="footer.jsp"%>
