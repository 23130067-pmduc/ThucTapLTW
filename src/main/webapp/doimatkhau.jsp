<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%
    request.setAttribute("pageCss", "doimatkhau.css");
    request.setAttribute("pageTitle" , "Đổi mật khẩu");
%>

<%@include file="header.jsp"%>


<section class="profile-container">
    <div class="profile-sidebar">
        <nav class="profile-menu">
            <ul>
                <li><a href="profile.jsp"><i class="fas fa-user"></i> Thông tin cá nhân</a></li>
                <li><a href="diachi.jsp"><i class="fas fa-map-marker-alt"></i> Địa chỉ của tôi</a></li>
                <li><a href="don-mua"><i class="fas fa-clipboard-list"></i> Đơn hàng của tôi</a></li>
                <li class="active"><a href="doi-mat-khau"><i class="fas fa-lock"></i> Đổi mật khẩu</a></li>
                <li><a href="trang-chu"><i class="fa fa-sign-out"></i> Đăng xuất</a></li>
            </ul>
        </nav>
    </div>

    <div class="profile-content" >
        <h2>Đổi mật khẩu</h2>

        <form class="profile-form" method="post" action="doi-mat-khau">
            <div class="form-row">
                <div class="form-group">
                    <label for="oldpass">Mật khẩu hiện tại</label>
                    <input type="password" id="oldpass" name="oldpass" placeholder="Nhập mật khẩu cũ">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="newpass">Mật khẩu mới</label>
                    <input type="password" id="newpass" name="newpass" placeholder="Nhập mật khẩu mới">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="repass">Nhập lại mật khẩu mới</label>
                    <input type="password" id="repass" name="repass" placeholder="Nhập lại mật khẩu mới">
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-save">Lưu mật khầu</button>
            </div>
        </form>

        <c:if test="${not empty error}">
            <p style="color:red">${error}</p>
        </c:if>

    </div>
</section>

<%@ include file="footer.jsp"%>