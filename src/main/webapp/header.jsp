<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>${pageTitle != null ? pageTitle : "SunnyBear"}</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <c:if test="${not empty pageCss}">
        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/${pageCss}">
    </c:if>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>
<header class="header" id="header">

    <nav class="topbar">
        <p id="hotline">Hotline: <b> 0909 999 999</b> (8h30 - 12h) Tất cả các ngày trong tuần | </p>
        <p id="thongBao">
            <i class="fa-regular fa-bell"></i>
            Thông báo của tôi
        </p>

        <div id="notification-box">
            <ul>
                <li>Hiện không có thông báo nào.</li>
                <li>Đăng nhập để được nhận thêm nhiều ưu đãi.</li>

            </ul>

        </div>
    </nav>



    <nav class="navbar">
        <div class="logo">
            <a href="trang-chu">
                <img src="${pageContext.request.contextPath}/img/gau.png" alt="SunnyBear Logo">
            </a>
        </div>

        <div class="menu">
            <ul>
                <li><a href="${pageContext.request.contextPath}/trang-chu">Trang chủ</a></li>
                <li ><a href="san-pham">Sản phẩm ▾</a>
                    <ul class="sub">
                        <li class="subItem"> <a href="san-pham?group=betrai">Quần áo bé trai</a> </li>
                        <li class="subItem"> <a href="san-pham?group=begai">Quần áo bé gái</a> </li>
                        <li class="subItem"> <a href="san-pham?group=phukien">Phụ kiện</a> </li>
                    </ul>
                </li>
                <li><a href="tin-tuc">Tin tức</a></li>
                <li><a href="khuyen-mai">Khuyến mãi</a></li>
                <li><a href="lien-he">Liên hệ</a></li>
            </ul>
        </div>

        <div class="actions">
            <a href="#" class="iconSearch"><i class="fa-solid fa-magnifying-glass"></i></a>
            <c:choose>
                <c:when test="${not empty sessionScope.userlogin}">
                    <div class="user-menu">
                        <a href="#" class="iconUser">
                            <i class="fa-regular fa-user"></i>
                                ${sessionScope.userlogin.username}
                        </a>
                        <ul class="user-dropdown">
                            <li><a href="profile">Thông tin cá nhân</a></li>
                            <li><a href="don-mua">Đơn hàng của tôi</a></li>
                            <li><a href="logout">Đăng xuất</a></li>
                        </ul>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="user-menu">
                        <a href="#" class="iconUser">
                            <i class="fa-regular fa-user"></i>
                        </a>
                        <ul class="user-dropdown">
                            <li><a href="login">Đăng nhập</a></li>
                            <li><a href="register">Đăng ký</a></li>
                        </ul>
                    </div>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${not empty sessionScope.userlogin}">
                    <a href="my-cart" class="iconCart">
                        <i class="fa-solid fa-cart-shopping"></i>
                        <c:if test="${sessionScope.cartSize != null && sessionScope.cartSize > 0}">
                            <span class="cart-count">${sessionScope.cartSize}</span>
                        </c:if>
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="login" class="iconCart">
                        <i class="fa-solid fa-cart-shopping"></i>
                    </a>
                </c:otherwise>
            </c:choose>

        </div>
    </nav>


</header>

<div class="search-overlay" id="searchOverlay">
    <img class="logo" src="img/gau.png" alt="Logo">

    <div class="boxSearch">
        <form action="SearchController" method="get">
            <input type="text" name="keyword"  value="${param.keyword}" placeholder="Tìm kiếm sản phẩm..." required />
            <button type="submit">
                <i class="fa-solid fa-magnifying-glass"></i>
            </button>
        </form>
    </div>


    <span class="closeSearch" id="closeSearch">&times; </span>
</div>