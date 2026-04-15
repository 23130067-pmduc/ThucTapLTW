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
        <p id="hotline">Hotline: <b> 0983561258 </b> (8h30 - 12h) Tất cả các ngày trong tuần | </p>
        <div class="thong-bao-wrapper">
            <p id="thongBao" class="thong-bao-trigger">
                <i class="fa-regular fa-bell"></i>
                <span class="thong-bao-text">Thông báo của tôi</span>
                <span id="notification-badge" class="notification-badge" style="display:none;">0</span>
            </p>

            <div id="notification-box" class="notification-box">
                <div class="notification-header">
                    <span>Thông báo</span>
                </div>
                <ul id="notification-list" class="notification-list">
                    <li class="notification-empty">Đang tải thông báo...</li>
                </ul>
            </div>
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

    <div class="boxSearch" style="position: relative;">
        <form action="${pageContext.request.contextPath}/SearchController" method="get">
            <input type="text" id="ajaxSearchInput" data-context="${pageContext.request.contextPath}" name="keyword"  value="${param.keyword}" placeholder="Tìm kiếm sản phẩm..." autocomplete="off" required />
            <button type="submit">
                <i class="fa-solid fa-magnifying-glass"></i>
            </button>
        </form>
        <div id="ajaxSearchResults" class="ajax-search-results" style="display:none;"></div>

        <c:if test="${not empty sessionScope.searchHistory}">
            <div class="search-history" id="searchHistoryBox">
                <div class="history-header">
                    <p class="history-title">Tìm kiếm gần đây</p>
                    <a class="clear-history" href="${pageContext.request.contextPath}/clear-search-history">
                        Xóa tất cả
                    </a>
                </div>

                <ul class="history-list">
                    <c:forEach var="item" items="${sessionScope.searchHistory}">
                        <li>
                            <a href="${pageContext.request.contextPath}/SearchController?keyword=${item}">
                                <i class="fa-solid fa-clock-rotate-left"></i>
                                <span>${item}</span>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
    </div>


    <span class="closeSearch" id="closeSearch">&times; </span>
</div>
<script>
    window.ctxPath = '${pageContext.request.contextPath}';
</script>