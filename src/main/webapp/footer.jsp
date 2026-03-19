<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<footer class="footer">
    <section class="s-footer-1">
        <div class="footer-info">
            <h3>SunnyBear Kids Clothing</h3>
            <p class="slogan">Thời trang trẻ em chất lượng, an toàn cho bé yêu</p>
            <p class="fa-phone"> <i class="fa-solid fa-phone"></i> Hotline: 0909 999 999</p>
            <p class="fa-mail"> <i class="fa-solid fa-envelope"></i> Email: contact@sunnybear.vn</p>
        </div>
    </section>
    <section class="s-footer-2">

        <div class="footer-danhmuc">
            <h3>Danh mục</h3>
            <a href="trang-chu">Trang chủ</a>
            <a href="san-pham">Sản Phẩm</a>
            <a href="tin-tuc">Tin Tức</a>
            <a href="khuyen-mai">Khuyến mãi</a>
            <a href="lienhe.jsp">Liên hệ</a>

        </div>

    </section>
    <section class="s-footer-3">
        <div class="footer-contact">
            <h3>Địa chỉ & Thời gian làm việc</h3>
            <p>123 Đường Hạnh Phúc, Quận 5, TP.HCM</p>
            <p>Thời gian làm việc: </p>
            <p>Thứ 2 - Thứ 6: 8h00 - 17h30</p>
            <p>Thứ 7 - Chủ nhật: 9h00 - 17h00</p>
        </div>
    </section>

    <section class="s-footer-4">
        <div class="footer-social">
            <h3>Kết nối với chúng tôi</h3>

            <div class="social-icons">
                <a href="#"><i class="fa-brands fa-facebook-f"></i></a>
                <a href="#."><img src="${pageContext.request.contextPath}/image/zalo.webp" alt="Zalo"></a>
                <a href="#"><i class="fa-brands fa-twitter"></i></a>
                <a href="#"><i class="fa-brands fa-instagram"></i></a>
                <a href="#"><i class="fa-brands fa-tiktok"></i></a>
                <a href="#"><i class="fa-brands fa-youtube"></i></a>
            </div>
        </div>
    </section>
</footer>
<p class="copyright">© 2025 SunnyBear. All rights reserved.</p>

<script>
    window.updateCartBadge = function(count) {
        console.log("[global] updateCartBadge called with count: " + count);

        const cartIcons = document.querySelectorAll(".iconCart");
        console.log("[global] Found " + cartIcons.length + " cart icons");

        if (cartIcons.length === 0) {
            console.warn("[global] No cart icons found");
            return;
        }

        cartIcons.forEach((iconCart, index) => {
            let cartCount = iconCart.querySelector(".cart-count");

            if (!cartCount) {
                cartCount = document.createElement("span");
                cartCount.className = "cart-count";
                iconCart.appendChild(cartCount);
            }

            cartCount.textContent = count;
            cartCount.style.display = count > 0 ? "block" : "none";
            console.log("[global] Updated cart badge #" + (index + 1) + ": " + count);
        });
    };
</script>
<script src="${pageContext.request.contextPath}/javaScript/thongBao.js"></script>
<script src="${pageContext.request.contextPath}/javaScript/search.js"></script>
<script src="${pageContext.request.contextPath}/javaScript/slider.js"></script>
<script src="${pageContext.request.contextPath}/javaScript/themvaogiohang.js"></script>
<script src="${pageContext.request.contextPath}/javaScript/dropdown.js"></script>
<script src="${pageContext.request.contextPath}/javaScript/pageatxl.js"></script>
<script src="${pageContext.request.contextPath}/javaScript/loadMore.js"></script>
<script src="${pageContext.request.contextPath}/javaScript/profile.js"></script>
<script src="${pageContext.request.contextPath}/javaScript/chuyensanpham.js"></script>
<script src="${pageContext.request.contextPath}/javaScript/header.js"></script>
</body>
</html>