package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.News;

import java.util.List;
import java.util.Optional;

public class NewsDao extends BaseDao {

    public List<News> getAllActiveNews() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM news WHERE status = 1 ORDER BY created_at DESC")
                        .mapToBean(News.class)
                        .list()
        );
    }

    public Optional<News> getNewsById(int id) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM news WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(News.class)
                        .findFirst()
        );
    }

    public Optional<News> getNewsBySlug(String slug) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM news WHERE slug = :slug AND status = 1")
                        .bind("slug", slug)
                        .mapToBean(News.class)
                        .findFirst()
        );
    }

    public List<News> getLatestNews(int limit) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM news WHERE status = 1 ORDER BY created_at DESC LIMIT :limit")
                        .bind("limit", limit)
                        .mapToBean(News.class)
                        .list()
        );
    }

    public List<News> getNewsPaginated(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM news WHERE status = 1 ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
                        .bind("limit", pageSize)
                        .bind("offset", offset)
                        .mapToBean(News.class)
                        .list()
        );
    }

    public int getTotalNewsCount() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM news WHERE status = 1")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public List<News> searchNews(String keyword) {
        String pattern = "%" + keyword + "%";
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM news WHERE status = 1 AND (title LIKE :keyword OR short_description LIKE :keyword) ORDER BY created_at DESC")
                        .bind("keyword", pattern)
                        .mapToBean(News.class)
                        .list()
        );
    }

    public List<News> searchAllNews(String keyword) {
        String pattern = "%" + keyword + "%";
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM news WHERE title LIKE :keyword OR short_description LIKE :keyword ORDER BY created_at DESC")
                        .bind("keyword", pattern)
                        .mapToBean(News.class)
                        .list()
        );
    }

    public List<News> getNewsByAuthor(int authorId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM news WHERE author_id = :authorId AND status = 1 ORDER BY created_at DESC")
                        .bind("authorId", authorId)
                        .mapToBean(News.class)
                        .list()
        );
    }

    public List<News> getRelatedNews(int currentId, int limit) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM news WHERE id != :currentId AND status = 1 ORDER BY RAND() LIMIT :limit")
                        .bind("currentId", currentId)
                        .bind("limit", limit)
                        .mapToBean(News.class)
                        .list()
        );
    }

    public int createNews(News news) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("INSERT INTO news (title, slug, thumbnail, short_description, content, author_id, status, created_at, updated_at) VALUES (:title, :slug, :thumbnail, :shortDescription, :content, :authorId, :status, NOW(), NOW())")
                        .bind("title", news.getTitle())
                        .bind("slug", news.getSlug())
                        .bind("thumbnail", news.getThumbnail())
                        .bind("shortDescription", news.getShortDescription())
                        .bind("content", news.getContent())
                        .bind("authorId", news.getAuthorId())
                        .bind("status", news.getStatus())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public boolean updateNews(News news) {
        int rows = getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE news SET title = :title, slug = :slug, thumbnail = :thumbnail, short_description = :shortDescription, content = :content, status = :status, updated_at = NOW() WHERE id = :id")
                        .bind("id", news.getId())
                        .bind("title", news.getTitle())
                        .bind("slug", news.getSlug())
                        .bind("thumbnail", news.getThumbnail())
                        .bind("shortDescription", news.getShortDescription())
                        .bind("content", news.getContent())
                        .bind("status", news.getStatus())
                        .execute()
        );
        return rows > 0;
    }

    public boolean hideNews(int id) {
        int rows = getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE news SET status = 0, updated_at = NOW() WHERE id = :id")
                        .bind("id", id)
                        .execute()
        );
        return rows > 0;
    }

    public boolean showNews(int id) {
        int rows = getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE news SET status = 1, updated_at = NOW() WHERE id = :id")
                        .bind("id", id)
                        .execute()
        );
        return rows > 0;
    }

    public boolean deleteNews(int id) {
        int rows = getJdbi().withHandle(handle ->
                handle.createUpdate("DELETE FROM news WHERE id = :id")
                        .bind("id", id)
                        .execute()
        );
        return rows > 0;
    }

    public boolean isSlugExists(String slug) {
        Integer count = getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM news WHERE slug = :slug")
                        .bind("slug", slug)
                        .mapTo(Integer.class)
                        .one()
        );
        return count > 0;
    }

    public boolean isSlugExistsExceptId(String slug, int id) {
        Integer count = getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM news WHERE slug = :slug AND id <> :id")
                        .bind("slug", slug)
                        .bind("id", id)
                        .mapTo(Integer.class)
                        .one()
        );
        return count > 0;
    }

    public String generateSlug(String title) {
        String slug = normalizeSlug(title);
        int counter = 1;
        String finalSlug = slug;
        while (isSlugExists(finalSlug)) {
            finalSlug = slug + "-" + counter;
            counter++;
        }
        return finalSlug;
    }

    public String generateSlugForUpdate(String title, int id) {
        String slug = normalizeSlug(title);
        int counter = 1;
        String finalSlug = slug;
        while (isSlugExistsExceptId(finalSlug, id)) {
            finalSlug = slug + "-" + counter;
            counter++;
        }
        return finalSlug;
    }

    public int getTotalPages(int pageSize) {
        int total = getTotalNewsCount();
        return (int) Math.ceil((double) total / pageSize);
    }

    public List<News> getAllNews() {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT * FROM news ORDER BY created_at DESC").mapToBean(News.class).list()
        );
    }

    public int countAllNews() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM news")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public int countNewsByStatus(int status) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM news WHERE status = :status")
                        .bind("status", status)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public int countNewsCreatedInLast7Days() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM news WHERE created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    private String normalizeSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
                .replaceAll("[èéẹẻẽêềếệểễ]", "e")
                .replaceAll("[ìíịỉĩ]", "i")
                .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
                .replaceAll("[ùúụủũưừứựửữ]", "u")
                .replaceAll("[ỳýỵỷỹ]", "y")
                .replaceAll("[đ]", "d")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}
