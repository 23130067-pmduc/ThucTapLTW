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

<%@include file="footer.jsp"%>