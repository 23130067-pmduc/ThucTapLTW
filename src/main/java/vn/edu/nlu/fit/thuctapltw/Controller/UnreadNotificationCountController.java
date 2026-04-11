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

@WebServlet(name = "UnreadNotificationCountController", value = "/notifications/unread-count")
public class UnreadNotificationCountController extends HttpServlet {

    private final NotificationDao notificationDao = new NotificationDao();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Integer> result = new HashMap<>();
        result.put("count", 0);

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
            result.put("count", notificationDao.countUnreadByUser(user.getId()));
        } catch (Exception e) {
            result.put("count", 0);
        }

        response.getWriter().write(gson.toJson(result));
    }
}