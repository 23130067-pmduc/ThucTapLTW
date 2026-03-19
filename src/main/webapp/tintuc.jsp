<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

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
                            <img src="${pageContext.request.contextPath}/${news.thumbnail}" alt="${news.title}">
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