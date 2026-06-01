<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%
    request.setAttribute("pageTitle", "Tin tức - SunnyBear");
    request.setAttribute("pageCss", "tintuc.css");
    request.setAttribute("activePage", "tintuc");
%>
<body>

<%@include file="header.jsp"%>

<div class="title">
    <span>TIN TỨC</span>
</div>

<main class="container">
    <div class="newsContainer">

        <c:choose>
            <c:when test="${not empty newsList}">
                <c:forEach var="news" items="${newsList}">
                    <div class="newsItem">
                        <a href="${pageContext.request.contextPath}/tin-tuc?slug=${news.slug}">
                            <c:set var="thumbUrl" value="${news.thumbnail}" />
                                    <c:choose>
                                        <c:when test="${empty thumbUrl}">
                                            <img src="${pageContext.request.contextPath}/img/gau.png" alt="${news.title}">
                                        </c:when>
                                        <c:when test="${fn:startsWith(thumbUrl, 'http://') || fn:startsWith(thumbUrl, 'https://')}">
                                            <img src="${thumbUrl}" alt="${news.title}">
                                        </c:when>
                                        <c:when test="${fn:startsWith(thumbUrl, '/')}">
                                            <img src="${pageContext.request.contextPath}${thumbUrl}" alt="${news.title}">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/${thumbUrl}" alt="${news.title}">
                                        </c:otherwise>
                                    </c:choose>
                        </a>

                        <div class="news-content">
                            <h3>
                                <a href="${pageContext.request.contextPath}/tin-tuc?slug=${news.slug}">
                                        ${news.title}
                                </a>
                            </h3>

                            <p>${news.shortDescription}</p>

                            <div class="news-meta">
                                <span class="news-date">
                                    <i class="fa-regular fa-calendar"></i>
                                    <fmt:formatDate value="${news.createdAt}" pattern="dd/MM/yyyy"/>
                                </span>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>

            <c:otherwise>
                <p style="text-align:center">Hiện chưa có tin tức nào.</p>
            </c:otherwise>
        </c:choose>

    </div>

    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <c:forEach begin="1" end="${totalPages}" var="pageNum">
                <a href="${pageContext.request.contextPath}/tin-tuc?page=${pageNum}"
                   class="${pageNum == currentPage ? 'active' : ''}">
                        ${pageNum}
                </a>
            </c:forEach>
        </div>
    </c:if>

</main>

<jsp:include page="footer.jsp"/>

<script src="${pageContext.request.contextPath}/javaScript/header.js"></script>
<script src="${pageContext.request.contextPath}/javaScript/search.js"></script>

</body>
</html>