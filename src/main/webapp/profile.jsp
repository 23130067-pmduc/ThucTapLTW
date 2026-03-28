<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>



<%
    request.setAttribute("pageCss", "profile.css");
    request.setAttribute("pageTitle" , "Profile");
%>

<%@include file="header.jsp"%>

<section class="profile-container">
    <div class="profile-sidebar">
        <nav class="profile-menu">
            <ul>
                <li class="active"><a href="profile"><i class="fas fa-user"></i> Thông tin cá nhân</a></li>
                <li><a href="dia-chi"><i class="fas fa-map-marker-alt"></i> Địa chỉ của tôi</a></li>
                <li><a href="don-mua"><i class="fas fa-clipboard-list"></i> Đơn hàng của tôi</a></li>
                <li><a href="forget"><i class="fas fa-lock"></i> Đổi mật khẩu</a></li>
                <li><a href="trang-chu"><i class="fa fa-sign-out"></i> Đăng xuất</a></li>
            </ul>
        </nav>
    </div>

    <div class="profile-content">
        <h2>Thông tin cá nhân</h2>

        <form class="profile-form" method="post" action="profile" onsubmit="syncBirthday()">
            <div class="form-row">
                <div class="form-group">
                    <label for="fullname">Họ và tên</label>
                    <input type="text" id="fullname" name="fullname" value="${user.fullName}" disabled>
                </div>
                <div class="form-group">
                    <label for="phone">Số điện thoại</label>
                    <input type="tel" id="phone" name="phone" value="${user.phone}" disabled>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" value="${user.email}" disabled>
                </div>
                <div class="form-group">
                    <label for="birthday">Ngày sinh</label>
                    <input type="text" id="birthdayDisplay"
                           value="<fmt:formatDate value='${birthdayDate}' pattern='dd-MM-yyyy'/>" disabled>


                    <input type="hidden"
                           id="birthday"
                           name="birthday"
                           value="<fmt:formatDate value='${birthdayDate}' pattern='yyyy-MM-dd'/>">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="address">Địa chỉ</label>
                    <input type="text" id="address" name="address" value="${user.address}" disabled>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label>Giới tính</label>
                    <div class="radio-group">
                        <label>
                            <input type="radio" name="gender" value="male"
                            ${user.gender == 'male' ? 'checked' : ''} disabled>
                            Nam
                        </label>

                        <label>
                            <input type="radio" name="gender" value="female"
                            ${user.gender == 'female' ? 'checked' : ''} disabled>
                            Nữ
                        </label>

                        <label>
                            <input type="radio" name="gender" value="other"
                            ${user.gender == 'other' ? 'checked' : ''} disabled>
                            Khác
                        </label>

                    </div>
                </div>
            </div>

            <div class="form-actions">
                <button type="button" class="btn-edit" id="btn-edit-profile">Sửa</button>
                <button type="button" class="btn-cancel" id="btn-cancel-profile" style="display: none;">Hủy</button>
                <button type="submit" class="btn-save" id="btn-save-profile" style="display: none;">Lưu thay đổi</button>
            </div>
        </form>
    </div>
</section>

<%@include file="footer.jsp"%>