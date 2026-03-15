package vn.edu.nlu.fit.thuctapltw.Controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.NewsService;
import vn.edu.nlu.fit.thuctapltw.model.News;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/tin-tuc")
public class NewsController extends HttpServlet {

    private transient NewsService newsService;

    @Override
    public void init() {
        newsService = new NewsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String slug = request.getParameter("slug");
        String search = request.getParameter("search");
        String pageParam = request.getParameter("page");

        //Chi tiết
        if (slug != null && !slug.isEmpty()) {
            showDetail(request, response, slug);
            return;
        }
        //tìm kếm
        if (search != null && !search.trim().isEmpty()) {
            searchNews(request, response, search);
            return;
        }

        showList(request, response, pageParam);
    }

    private void showList(HttpServletRequest request, HttpServletResponse response, String pageParam)
            throws ServletException, IOException {

        int pageSize = 9;
        int currentPage = 1;

        try {
            if (pageParam != null) {
                currentPage = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException ignored) {}

        List<News> newsList = newsService.getNewsPage(currentPage, pageSize);
        int totalPages = newsService.getTotalPages(pageSize);

        request.setAttribute("newsList", newsList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/tintuc.jsp").forward(request, response);
    }

    private void showDetail(HttpServletRequest request, HttpServletResponse response, String slug)
            throws ServletException, IOException {

        Optional<News> opt = newsService.getBySlug(slug);

        if (opt.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/tin-tuc");
            return;
        }

        News news = opt.get();
        List<News> relatedNews = newsService.getRelated(news.getId(), 4);

        request.setAttribute("news", news);
        request.setAttribute("relatedNews", relatedNews);
        request.getRequestDispatcher("/chitiet_tintuc.jsp").forward(request, response);
    }

    private void searchNews(HttpServletRequest request, HttpServletResponse response, String keyword)
            throws ServletException, IOException {

        List<News> result = newsService.search(keyword);

        request.setAttribute("newsList", result);
        request.setAttribute("searchKeyword", keyword);

        request.getRequestDispatcher("/tintuc.jsp").forward(request, response);
    }
}