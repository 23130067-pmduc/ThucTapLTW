package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Notification;

import java.util.List;

public class NotificationDao extends BaseDao {

    public List<Notification> getRecentByUser(int userId, int limit) {
        String sql = """
            SELECT *
            FROM notifications
            WHERE user_id = :userId
            ORDER BY created_at DESC
            LIMIT :limit
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .bind("limit", limit)
                        .mapToBean(Notification.class)
                        .list()
        );
    }

    public int countUnreadByUser(int userId) {
        String sql = """
            SELECT COUNT(*)
            FROM notifications
            WHERE user_id = :userId AND is_read = 0
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public void markAsRead(int notificationId, int userId) {
        String sql = """
            UPDATE notifications
            SET is_read = 1
            WHERE id = :notificationId AND user_id = :userId
        """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("notificationId", notificationId)
                        .bind("userId", userId)
                        .execute()
        );
    }

    public void insert(Notification notification) {
        String sql = """
            INSERT INTO notifications (user_id, title, content, type, is_read, link)
            VALUES (:userId, :title, :content, :type, :isRead, :link)
        """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("userId", notification.getUser_id())
                        .bind("title", notification.getTitle())
                        .bind("content", notification.getContent())
                        .bind("type", notification.getType())
                        .bind("isRead", notification.getIs_read())
                        .bind("link", notification.getLink())
                        .execute()
        );
    }
}