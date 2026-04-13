package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Notification;

import java.util.List;
import java.util.Optional;

public class NotificationDao extends BaseDao {

    public List<Notification> getRecentByUser(int userId, int limit) {
        String sql = """
            SELECT
                n.id,
                n.title,
                n.content,
                n.type,
                n.target_type,
                n.link,
                n.is_active,
                n.created_at,
                COALESCE(un.is_read, 0) AS is_read
            FROM notifications n
            LEFT JOIN user_notifications un
                ON n.id = un.notification_id
                AND un.user_id = :userId
            WHERE n.is_active = 1
              AND (
                    n.target_type = 'ALL'
                    OR (n.target_type = 'USER' AND un.user_id = :userId)
              )
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
            WHERE n.is_active = 1
              AND (
                    n.target_type = 'ALL'
                    OR (n.target_type = 'USER' AND un.user_id = :userId)
              )
              AND COALESCE(un.is_read, 0) = 0
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

    public List<Notification> getAdminNotifications(String keyword) {
        String sql = """
            SELECT
                n.id,
                n.title,
                n.content,
                n.type,
                n.target_type,
                n.link,
                n.is_active,
                n.created_at,
                MAX(un.user_id) AS target_user_id
            FROM notifications n
            LEFT JOIN user_notifications un
                ON n.id = un.notification_id
            WHERE (:keyword IS NULL OR :keyword = ''
                   OR n.title LIKE CONCAT('%', :keyword, '%')
                   OR n.content LIKE CONCAT('%', :keyword, '%'))
            GROUP BY n.id, n.title, n.content, n.type, n.target_type, n.link, n.is_active, n.created_at
            ORDER BY n.created_at DESC
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("keyword", keyword == null ? "" : keyword.trim())
                        .mapToBean(Notification.class)
                        .list()
        );
    }

    public Optional<Notification> getAdminNotificationById(int id) {
        String sql = """
            SELECT
                n.id,
                n.title,
                n.content,
                n.type,
                n.target_type,
                n.link,
                n.is_active,
                n.created_at,
                MAX(un.user_id) AS target_user_id
            FROM notifications n
            LEFT JOIN user_notifications un
                ON n.id = un.notification_id
            WHERE n.id = :id
            GROUP BY n.id, n.title, n.content, n.type, n.target_type, n.link, n.is_active, n.created_at
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("id", id)
                        .mapToBean(Notification.class)
                        .findOne()
        );
    }

    public int insertNotification(Notification notification) {
        String sql = """
            INSERT INTO notifications (title, content, type, target_type, link, is_active)
            VALUES (:title, :content, :type, :targetType, :link, :isActive)
        """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("title", notification.getTitle())
                        .bind("content", notification.getContent())
                        .bind("type", notification.getType())
                        .bind("targetType", notification.getTarget_type())
                        .bind("link", notification.getLink())
                        .bind("isActive", notification.getIs_active())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public void insertTargetUser(int notificationId, int userId) {
        String sql = """
            INSERT INTO user_notifications (user_id, notification_id, is_read, read_at)
            VALUES (:userId, :notificationId, 0, NULL)
            ON DUPLICATE KEY UPDATE
                user_id = VALUES(user_id)
        """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("userId", userId)
                        .bind("notificationId", notificationId)
                        .execute()
        );
    }

    public void updateNotification(Notification notification) {
        String sql = """
            UPDATE notifications
            SET title = :title,
                content = :content,
                type = :type,
                target_type = :targetType,
                link = :link,
                is_active = :isActive
            WHERE id = :id
        """;

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("id", notification.getId())
                        .bind("title", notification.getTitle())
                        .bind("content", notification.getContent())
                        .bind("type", notification.getType())
                        .bind("targetType", notification.getTarget_type())
                        .bind("link", notification.getLink())
                        .bind("isActive", notification.getIs_active())
                        .execute()
        );
    }

    public void deleteTargetUsersByNotificationId(int notificationId) {
        String sql = "DELETE FROM user_notifications WHERE notification_id = :notificationId";

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("notificationId", notificationId)
                        .execute()
        );
    }

    public void softDeleteNotification(int id) {
        String sql = "UPDATE notifications SET is_active = 0 WHERE id = :id";

        getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("id", id)
                        .execute()
        );
    }

    public int countAllNotifications() {
        String sql = "SELECT COUNT(*) FROM notifications";

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public int countActiveNotifications() {
        String sql = "SELECT COUNT(*) FROM notifications WHERE is_active = 1";

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public int countAllTargetNotifications() {
        String sql = "SELECT COUNT(*) FROM notifications WHERE target_type = 'ALL'";

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public int countUserTargetNotifications() {
        String sql = "SELECT COUNT(*) FROM notifications WHERE target_type = 'USER'";

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapTo(Integer.class)
                        .one()
        );
    }
}