package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.DAO.NotificationDao;
import vn.edu.nlu.fit.thuctapltw.DAO.UserDao;
import vn.edu.nlu.fit.thuctapltw.model.Notification;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "NotificationAdminController", value = "/notification-admin")
public class NotificationAdminController extends HttpServlet {

    private final NotificationDao notificationDao = new NotificationDao();
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mode = safeTrim(request.getParameter("mode"));

        if ("add".equals(mode)) {
            request.setAttribute("mode", "add");
            request.setAttribute("users", userDao.findAll());
            request.getRequestDispatcher("/notification-form.jsp").forward(request, response);
            return;
        }

        if ("edit".equals(mode) || "view".equals(mode)) {
            int id = parseId(request.getParameter("id"));
            Optional<Notification> optionalNotification = notificationDao.getAdminNotificationById(id);
            if (optionalNotification.isEmpty()) {
                redirectWithMessage(response, request.getContextPath() + "/notification-admin", "error", "Không tìm thấy thông báo");
                return;
            }

            request.setAttribute("notification", optionalNotification.get());
            request.setAttribute("mode", mode);
            request.setAttribute("users", userDao.findAll());
            request.getRequestDispatcher("/notification-form.jsp").forward(request, response);
            return;
        }

        String keyword = safeTrim(request.getParameter("keyword"));
        List<Notification> notifications = notificationDao.getAdminNotifications(keyword);

        request.setAttribute("notificationList", notifications);
        request.setAttribute("keyword", keyword == null ? "" : keyword);
        request.setAttribute("total", notificationDao.countAllNotifications());
        request.setAttribute("totalActive", notificationDao.countActiveNotifications());
        request.setAttribute("totalAllTarget", notificationDao.countAllTargetNotifications());
        request.setAttribute("totalUserTarget", notificationDao.countUserTargetNotifications());
        request.setAttribute("success", request.getParameter("success"));
        request.setAttribute("error", request.getParameter("error"));

        request.getRequestDispatcher("/notification-admin.jsp").forward(request, response);
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

            redirectWithMessage(response, request.getContextPath() + "/notification-admin", "success", successMessage(action));
        } catch (RuntimeException e) {
            String mode = "create".equals(action) ? "add" : "edit";
            String redirectUrl = request.getContextPath() + "/notification-admin?mode=" + mode;
            String id = safeTrim(request.getParameter("id"));
            if (id != null && !id.isBlank()) {
                redirectUrl += "&id=" + URLEncoder.encode(id, StandardCharsets.UTF_8);
            }
            redirectWithMessage(response, redirectUrl, "error", e.getMessage());
        }
    }

    private void handleCreate(HttpServletRequest request) {
        Notification notification = buildNotificationFromRequest(request);
        int notificationId = notificationDao.insertNotification(notification);

        if ("USER".equals(notification.getTarget_type())) {
            Integer userId = notification.getTarget_user_id();
            if (userId == null || userId <= 0) {
                throw new RuntimeException("Vui lòng chọn người dùng nhận thông báo");
            }
            notificationDao.insertTargetUser(notificationId, userId);
        }
    }

    private void handleUpdate(HttpServletRequest request) {
        int id = parseId(request.getParameter("id"));
        Notification notification = buildNotificationFromRequest(request);
        notification.setId(id);

        notificationDao.updateNotification(notification);
        notificationDao.deleteTargetUsersByNotificationId(id);

        if ("USER".equals(notification.getTarget_type())) {
            Integer userId = notification.getTarget_user_id();
            if (userId == null || userId <= 0) {
                throw new RuntimeException("Vui lòng chọn người dùng nhận thông báo");
            }
            notificationDao.insertTargetUser(id, userId);
        }
    }

    private void handleDelete(HttpServletRequest request) {
        int id = parseId(request.getParameter("id"));
        notificationDao.softDeleteNotification(id);
    }

    private Notification buildNotificationFromRequest(HttpServletRequest request) {
        String title = safeTrim(request.getParameter("title"));
        String content = safeTrim(request.getParameter("content"));
        String type = safeTrim(request.getParameter("type"));
        String targetType = safeTrim(request.getParameter("targetType"));
        String link = safeTrim(request.getParameter("link"));
        int isActive = parseStatus(request.getParameter("isActive"));

        if (title == null || title.isBlank()) {
            throw new RuntimeException("Tiêu đề không được để trống");
        }
        if (content == null || content.isBlank()) {
            throw new RuntimeException("Nội dung không được để trống");
        }
        if (type == null || type.isBlank()) {
            throw new RuntimeException("Loại thông báo không hợp lệ");
        }
        if (!"ALL".equals(targetType) && !"USER".equals(targetType)) {
            throw new RuntimeException("Đối tượng nhận không hợp lệ");
        }

        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setTarget_type(targetType);
        notification.setLink(link);
        notification.setIs_active(isActive);

        String targetUserIdRaw = safeTrim(request.getParameter("targetUserId"));
        if ("USER".equals(targetType) && targetUserIdRaw != null && !targetUserIdRaw.isBlank()) {
            notification.setTarget_user_id(parseId(targetUserIdRaw));
        }

        return notification;
    }

    private int parseId(String rawId) {
        try {
            return Integer.parseInt(rawId);
        } catch (Exception e) {
            throw new RuntimeException("ID không hợp lệ");
        }
    }

    private int parseStatus(String rawStatus) {
        if (rawStatus == null || rawStatus.isBlank()) {
            return 1;
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
            case "create" -> "Thêm thông báo thành công";
            case "update" -> "Cập nhật thông báo thành công";
            case "delete" -> "Ẩn thông báo thành công";
            default -> "Thao tác thành công";
        };
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }
}