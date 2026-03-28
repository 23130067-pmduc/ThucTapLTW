package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.NewsDao;
import vn.edu.nlu.fit.thuctapltw.model.News;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NewsService {
    private final NewsDao newsDao = new NewsDao();

    public List<News> getNewsPage(int page, int pageSize) {
        if (page < 1) {
            page = 1;
        }
        return newsDao.getNewsPaginated(page, pageSize);
    }

    public int getTotalPages(int pageSize) {
        return newsDao.getTotalPages(pageSize);
    }

    public List<News> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        return newsDao.searchNews(keyword.trim());
    }

    public Optional<News> getBySlug(String slug) {
        return newsDao.getNewsBySlug(slug);
    }

    public List<News> getRelated(int currentId, int limit) {
        return newsDao.getRelatedNews(currentId, limit);
    }

    public List<News> getAdminNews(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return newsDao.getAllNews();
        }
        return newsDao.searchAllNews(keyword.trim());
    }

    public Optional<News> getAdminNewsById(int id) {
        return newsDao.getNewsById(id);
    }

    public Map<String, Integer> getAdminStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total", newsDao.countAllNews());
        stats.put("active", newsDao.countNewsByStatus(1));
        stats.put("hidden", newsDao.countNewsByStatus(0));
        stats.put("week", newsDao.countNewsCreatedInLast7Days());
        return stats;
    }

    public int createNews(News news) {
        validateNews(news);
        news.setSlug(newsDao.generateSlug(news.getTitle()));
        return newsDao.createNews(news);
    }

    public void updateNews(News news) {
        validateNews(news);
        news.setSlug(newsDao.generateSlugForUpdate(news.getTitle(), news.getId()));
        boolean updated = newsDao.updateNews(news);
        if (!updated) {
            throw new RuntimeException("Cập nhật bài viết thất bại");
        }
    }

    public void deleteNews(int id) {
        boolean deleted = newsDao.deleteNews(id);
        if (!deleted) {
            throw new RuntimeException("Xóa bài viết thất bại hoặc bài viết không tồn tại");
        }
    }

    private void validateNews(News news) {
        if (news == null) {
            throw new RuntimeException("Dữ liệu bài viết không hợp lệ");
        }
        if (isBlank(news.getTitle())) {
            throw new RuntimeException("Vui lòng nhập tiêu đề bài viết");
        }
        if (isBlank(news.getShortDescription())) {
            throw new RuntimeException("Vui lòng nhập mô tả ngắn");
        }
        if (isBlank(news.getContent())) {
            throw new RuntimeException("Vui lòng nhập nội dung bài viết");
        }
        if (isBlank(news.getThumbnail())) {
            throw new RuntimeException("Vui lòng chọn ảnh đại diện cho bài viết");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
