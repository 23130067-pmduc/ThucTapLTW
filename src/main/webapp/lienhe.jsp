<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<%
    request.setAttribute("pageCss", "lienhe.css");
    request.setAttribute("pageTitle" , "Liên hệ");
%>

<%@include file="header.jsp"%>


<div class="title">
    <span>THÔNG TIN LIÊN HỆ</span>
</div>
<main class="container">

    <div class="contactFrom">
        <h2>Gửi thông tin liên hệ với chúng tôi</h2>

        <c:if test="${not empty successMessage}">
            <p style="color: green; font-weight: bold;">${successMessage}</p>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/lien-he">
            <label>Địa chỉ email:</label>
            <input type="email" name="email" placeholder="Nhập địa chỉ email">

            <label>Họ và tên:</label>
            <input type="text" name="name" placeholder="Nhập họ tên của bạn">

            <label>Điện thoại của bạn:</label>
            <input type="text" name="phone" placeholder="Mời nhập điện thoại">

            <label>Địa chỉ đầy đủ:</label>
            <input type="text" name="address" placeholder="Nhập đầy đủ thông tin địa chỉ">

            <label>Nội dung:</label>
            <textarea rows="5" name="message" placeholder="Nhập nội dung yêu cầu"></textarea>

            <button type="submit">Gửi tin</button>
        </form>
    </div>

    <div class="contactInfo">
        <h3>Shop SunnyBear Kids Clothing</h3>

        <p><strong>DC:</strong> 123 Đường Hạnh Phúc, Quận 5, TP.HCM</p>
        <p><strong>Mobile:</strong> 0909 999 999</p>
        <p><b>Thời gian làm việc:</b><br>
            Thứ 2 - Thứ 6: 8h00 - 17h30 <br>
            Thứ 7 - Chủ nhật: 9h00 - 17h00
        </p>
    </div>
</main>

<section class="contact-map">
    <h3>Vị trí cửa hàng</h3>
    <iframe src="https://www.google.com/maps/embed?pb=!1m14!1m12!1m3!1d233.10303900362814!2d106.773323162255!3d10.855091005177272!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!5e1!3m2!1svi!2s!4v1775900766281!5m2!1svi!2s"
            width="600"
            height="450"
            style="border:0;"
            allowfullscreen=""
            loading="lazy"
            referrerpolicy="no-referrer-when-downgrade">
    </iframe>

</section>

<%@include file="footer.jsp"%>