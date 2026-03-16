package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.NewsService;
import vn.edu.nlu.fit.thuctapltw.model.News;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "NewsAdminController", value = "/news-admin")
@MultipartConfig

public class NewsAdminController extends HttpServlet {
    private NewsService newsService;

    @Override
    public void init() {
        newsService = new NewsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            newsService.deleteNews(id);
            response.sendRedirect("news-admin");
            return;
        }

        String mode = request.getParameter("mode");
        if (mode == null) {
            List<News> allNews = newsService.getAllNews();
            request.setAttribute("newsList", allNews);
            request.setAttribute("total", allNews.size());
            request.setAttribute("totalActive", allNews.stream().filter(n -> n.getStatus() == 1).count());

            request.getRequestDispatcher("/news-admin.jsp").forward(request, response);
            return;
        }

        if ("add".equals(mode)) {
            request.setAttribute("mode", "add");
            request.getRequestDispatcher("/news-form.jsp").forward(request, response);
            return;
        }

        if ("edit".equals(mode) || "view".equals(mode)) {
            int id = Integer.parseInt(request.getParameter("id"));
            News news = newsService.getNewsById(id);

            request.setAttribute("news", news);
            request.setAttribute("mode", mode);
            request.getRequestDispatcher("/news-form.jsp").forward(request, response);
            return;
        }




    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        String title = request.getParameter("title");
        String shortDescription = request.getParameter("shortDescription");
        String content = request.getParameter("content");

        String statusRaw = request.getParameter("status");
        int status = (statusRaw != null) ? Integer.parseInt(statusRaw) : 0;

        if ("create".equals(action)) {
            News news = new News();
            news.setTitle(title);
            news.setShortDescription(shortDescription);
            news.setContent(content);
            news.setStatus(status);
            news.setAuthorId(1);

            newsService.createNews(news);

            response.sendRedirect("news-admin");
            return;
        }

        if ("update".equals(action)) {
            String idRaw = request.getParameter("id");
            if (idRaw != null) {
                int id = Integer.parseInt(idRaw);

                News news = new News();
                news.setId(id);
                news.setTitle(title);
                news.setShortDescription(shortDescription);
                news.setContent(content);
                news.setStatus(status);

                newsService.updateNews(news);

            }
            response.sendRedirect("news-admin");
            return;
        }

    }
}