<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${news.title} - SunnyBear</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chitiet_tintuc.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>

<!-- HEADER -->
<jsp:include page="header.jsp"/>

<!-- BREADCRUMB -->
<div class="breadcrumb">
    <a href="${pageContext.request.contextPath}/trang-chu">Trang chủ</a>
    <span>&gt;</span>
    <a href="${pageContext.request.contextPath}/tin-tuc">Tin tức</a>
    <span>&gt;</span>
    <span>${news.title}</span>
</div>

<!-- MAIN CONTENT -->
<main class="detail-container">
    <!-- BAI VIET CHINH -->
    <article class="main-article">
        <a href="${pageContext.request.contextPath}/tin-tuc" class="btn-back">
            <i class="fa-solid fa-arrow-left"></i> Quay lại trang tin tức
        </a>

        <h1 class="article-title">${news.title}</h1>

        <p class="article-meta">
            <i class="fa-regular fa-calendar"></i>
            <fmt:formatDate value="${news.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
        </p>

        <div class="article-thumbnail">
            <img src="${pageContext.request.contextPath}/${news.thumbnail}" alt="${news.title}">
        </div>

        <p class="article-lead">${news.shortDescription}</p>

        <div class="article-content">
            ${news.content}
        </div>
    </article>

    <!-- BAI VIET LIEN QUAN -->
    <aside class="related-sidebar">
        <h3 class="sidebar-title">Tin tức liên quan</h3>

        <c:choose>
            <c:when test="${not empty relatedNews}">
                <c:forEach var="related" items="${relatedNews}" varStatus="status">
                    <c:if test="${status.index < 2}">
                        <div class="related-item">
                            <a href="${pageContext.request.contextPath}/tin-tuc?slug=${related.slug}">
                                <img src="${pageContext.request.contextPath}/${related.thumbnail}" alt="${related.title}">
                                <div class="related-info">
                                    <h4>${related.title}</h4>
                                    <p class="related-date">
                                        <i class="fa-regular fa-calendar"></i>
                                        <fmt:formatDate value="${related.createdAt}" pattern="dd/MM/yyyy"/>
                                    </p>
                                </div>
                            </a>
                        </div>
                    </c:if>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p class="no-related">Không có tin tức liên quan</p>
            </c:otherwise>
        </c:choose>
    </aside>
</main>

<!-- FOOTER -->
<jsp:include page="footer.jsp"/>

<script src="${pageContext.request.contextPath}/javaScript/header.js"></script>

</body>
</html>