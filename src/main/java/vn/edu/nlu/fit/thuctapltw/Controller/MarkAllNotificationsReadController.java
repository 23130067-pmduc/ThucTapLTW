package vn.edu.nlu.fit.thuctapltw.Controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.DAO.NotificationDao;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "MarkAllNotificationsReadController", value = "/notifications/read-all")
public class MarkAllNotificationsReadController extends HttpServlet {

    private final NotificationDao notificationDao = new NotificationDao();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("updated", 0);

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.getWriter().write(gson.toJson(result));
            return;
        }

        User user = (User) session.getAttribute("userlogin");
        if (user == null) {
            response.getWriter().write(gson.toJson(result));
            return;
        }

        try {
            int updated = notificationDao.markAllAsReadByUser(user.getId());
            result.put("success", true);
            result.put("updated", updated);
        } catch (Exception e) {
            result.put("success", false);
            result.put("updated", 0);
        }

        response.getWriter().write(gson.toJson(result));
    }
}
