package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Notification;

import java.util.List;

public class NotificationDao extends BaseDao {

    public List<Notification> getRecentByUser(int userId, int limit) {
        String sql = """
            SELECT 
                n.id,
                n.title,
                n.content,
                n.type,
                n.link,
                n.created_at,
                COALESCE(un.is_read, 0) AS is_read
            FROM notifications n
            LEFT JOIN user_notifications un
                ON n.id = un.notification_id
                AND un.user_id = :userId
            ORDER BY n.created_at DESC
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
            FROM notifications n
            LEFT JOIN user_notifications un
                ON n.id = un.notification_id
                AND un.user_id = :userId
            WHERE COALESCE(un.is_read, 0) = 0
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
            INSERT INTO user_notifications (user_id, notification_id, is_read, read_at)
            VALUES (:userId, :notificationId, 1, NOW())
            ON DUPLICATE KEY UPDATE
                is_read = 1,
                read_at = NOW()
        """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("userId", userId)
                        .bind("notificationId", notificationId)
                        .execute()
        );
    }

    public void insert(Notification notification) {
        String sql = """
            INSERT INTO notifications (title, content, type, link)
            VALUES (:title, :content, :type, :link)
        """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("title", notification.getTitle())
                        .bind("content", notification.getContent())
                        .bind("type", notification.getType())
                        .bind("link", notification.getLink())
                        .execute()
        );
    }
}