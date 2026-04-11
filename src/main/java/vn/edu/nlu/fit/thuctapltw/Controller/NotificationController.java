package vn.edu.nlu.fit.thuctapltw.Controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.DAO.NotificationDao;
import vn.edu.nlu.fit.thuctapltw.model.Notification;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "NotificationController", value = "/notifications")
public class NotificationController extends HttpServlet {

    private final NotificationDao notificationDao = new NotificationDao();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.getWriter().write("[]");
            return;
        }

        User user = (User) session.getAttribute("userlogin");
        if (user == null) {
            response.getWriter().write("[]");
            return;
        }

        List<Notification> notifications;
        try {
            notifications = notificationDao.getRecentByUser(user.getId(), 5);
        } catch (Exception e) {
            notifications = Collections.emptyList();
        }

        response.getWriter().write(gson.toJson(notifications));
    }
}