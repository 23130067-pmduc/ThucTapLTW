package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.edu.nlu.fit.thuctapltw.Service.NewsService;
import vn.edu.nlu.fit.thuctapltw.model.News;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "NewsAdminServlet", value = "/news-admin")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024, maxRequestSize = 20 * 1024 * 1024)
public class NewsAdminController extends HttpServlet {
    private NewsService newsService;

    @Override
    public void init() {
        newsService = new NewsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mode = safeTrim(request.getParameter("mode"));

        if ("add".equals(mode)) {
            request.setAttribute("mode", "add");
            request.getRequestDispatcher("/news-form.jsp").forward(request, response);
            return;
        }

        if ("edit".equals(mode) || "view".equals(mode)) {
            int id = parseId(request.getParameter("id"));
            Optional<News> optionalNews = newsService.getAdminNewsById(id);
            if (optionalNews.isEmpty()) {
                redirectWithMessage(response, request.getContextPath() + "/news-admin", "error", "Không tìm thấy bài viết");
                return;
            }

            request.setAttribute("news", optionalNews.get());
            request.setAttribute("mode", mode);
            request.getRequestDispatcher("/news-form.jsp").forward(request, response);
            return;
        }

        String keyword = safeTrim(request.getParameter("keyword"));
        List<News> newsList = newsService.getAdminNews(keyword);
        Map<String, Integer> stats = newsService.getAdminStats();

        request.setAttribute("newsList", newsList);
        request.setAttribute("keyword", keyword == null ? "" : keyword);
        request.setAttribute("total", stats.getOrDefault("total", 0));
        request.setAttribute("totalActive", stats.getOrDefault("active", 0));
        request.setAttribute("totalHidden", stats.getOrDefault("hidden", 0));
        request.setAttribute("totalInWeek", stats.getOrDefault("week", 0));
        request.setAttribute("success", request.getParameter("success"));
        request.setAttribute("error", request.getParameter("error"));

        request.getRequestDispatcher("/newsAdmin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = safeTrim(request.getParameter("action"));

        try {
            switch (action == null ? "" : action) {
                case "create" -> handleCreate(request);
                case "update" -> handleUpdate(request);
                case "delete" -> handleDelete(request);
                default -> throw new RuntimeException("Thao tác không hợp lệ");
            }

            redirectWithMessage(response, request.getContextPath() + "/news-admin", "success", successMessage(action));
        } catch (RuntimeException e) {
            String mode = "create".equals(action) ? "add" : "edit";
            String redirectUrl = request.getContextPath() + "/news-admin?mode=" + mode;
            String id = safeTrim(request.getParameter("id"));
            if (id != null && !id.isBlank()) {
                redirectUrl += "&id=" + URLEncoder.encode(id, StandardCharsets.UTF_8);
            }
            redirectWithMessage(response, redirectUrl, "error", e.getMessage());
        }
    }

    private void handleCreate(HttpServletRequest request) throws ServletException, IOException {
        News news = buildNewsFromRequest(request, null);
        news.setAuthorId(resolveAuthorId(request));
        newsService.createNews(news);
    }

    private void handleUpdate(HttpServletRequest request) throws ServletException, IOException {
        int id = parseId(request.getParameter("id"));
        News existingNews = newsService.getAdminNewsById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết cần cập nhật"));

        News news = buildNewsFromRequest(request, existingNews);
        news.setId(id);
        news.setAuthorId(existingNews.getAuthorId());
        newsService.updateNews(news);
    }

    private void handleDelete(HttpServletRequest request) {
        int id = parseId(request.getParameter("id"));
        newsService.deleteNews(id);
    }

    private News buildNewsFromRequest(HttpServletRequest request, News existingNews) throws ServletException, IOException {
        String title = safeTrim(request.getParameter("title"));
        String shortDescription = safeTrim(request.getParameter("shortDescription"));
        String content = safeTrim(request.getParameter("content"));
        int status = parseStatus(request.getParameter("status"));

        News news = new News();
        news.setTitle(title);
        news.setShortDescription(shortDescription);
        news.setContent(content);
        news.setStatus(status);

        String thumbnail = uploadThumbnail(request.getPart("imageFile"));
        if (thumbnail == null || thumbnail.isBlank()) {
            thumbnail = existingNews != null ? existingNews.getThumbnail() : null;
        }
        news.setThumbnail(thumbnail);
        return news;
    }

    private String uploadThumbnail(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        String submittedFileName = filePart.getSubmittedFileName();
        if (submittedFileName == null || submittedFileName.isBlank()) {
            return null;
        }

        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Vui lòng chọn file ảnh hợp lệ");
        }

        String extension = "";
        int lastDotIndex = submittedFileName.lastIndexOf('.');
        if (lastDotIndex >= 0) {
            extension = submittedFileName.substring(lastDotIndex).toLowerCase();
        }

        if (!extension.matches("\\.(jpg|jpeg|png|webp)$")) {
            throw new RuntimeException("Ảnh chỉ hỗ trợ định dạng jpg, jpeg, png hoặc webp");
        }

        String fileName = "news_" + UUID.randomUUID() + extension;
        String uploadPath = getServletContext().getRealPath("") + File.separator + "img" + File.separator + "news";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            throw new RuntimeException("Không thể tạo thư mục lưu ảnh tin tức");
        }

        filePart.write(uploadPath + File.separator + fileName);
        return "img/news/" + fileName;
    }

    private int resolveAuthorId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object loginUser = session.getAttribute("userlogin");
            if (loginUser instanceof User user) {
                return user.getId();
            }
        }
        return 1;
    }

    private int parseId(String rawId) {
        try {
            return Integer.parseInt(rawId);
        } catch (Exception e) {
            throw new RuntimeException("Mã bài viết không hợp lệ");
        }
    }

    private int parseStatus(String rawStatus) {
        if (rawStatus == null || rawStatus.isBlank()) {
            return 0;
        }
        try {
            int status = Integer.parseInt(rawStatus);
            if (status != 0 && status != 1) {
                throw new RuntimeException("Trạng thái không hợp lệ");
            }
            return status;
        } catch (NumberFormatException e) {
            throw new RuntimeException("Trạng thái không hợp lệ");
        }
    }

    private void redirectWithMessage(HttpServletResponse response, String baseUrl, String key, String message) throws IOException {
        String separator = baseUrl.contains("?") ? "&" : "?";
        response.sendRedirect(baseUrl + separator + key + "=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
    }

    private String successMessage(String action) {
        return switch (action) {
            case "create" -> "Thêm bài viết thành công";
            case "update" -> "Cập nhật bài viết thành công";
            case "delete" -> "Xóa bài viết thành công";
            default -> "Thao tác thành công";
        };
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }
}
