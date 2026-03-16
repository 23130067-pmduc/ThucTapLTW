package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.NewsDao;
import vn.edu.nlu.fit.thuctapltw.model.News;

import java.util.List;
import java.util.Optional;

public class NewsService {

    private final NewsDao newsDao = new NewsDao();

    public List<News> getNewsPage(int page, int pageSize) {
        if (page < 1) page = 1;
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

    public void deleteNews(int id) {
        newsDao.deleteNews(id);
    }


    public News getNewsById(int id) {
        return newsDao.getNewsById(id);
    }

    public List<News> getAllNews() {
        return newsDao.getAllNews();
    }

    public void createNews(News news) {
        news.setSlug(newsDao.generateSlug(news.getTitle()));
        newsDao.createNews(news);
    }

    public void updateNews(News news) {
        news.setSlug(newsDao.generateSlug(news.getTitle()));
        newsDao.updateNews(news);
    }
}