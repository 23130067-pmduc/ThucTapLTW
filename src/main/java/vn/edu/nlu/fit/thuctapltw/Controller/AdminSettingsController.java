package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.Service.UserService;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;

@WebServlet(name = "AdminSettingsController", value = "/admin-settings")
public class AdminSettingsController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("adminUsers", userService.getAdminUsers());
        request.setAttribute("adminCount", userService.countAdminUsers());
        request.getRequestDispatcher("/adminsettings.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (!"deleteAdmin".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/admin-settings?error=invalidAction");
            return;
        }

        int adminId;
        try {
            adminId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin-settings?error=invalidAdmin");
            return;
        }

        HttpSession session = request.getSession(false);
        User currentUser = session == null ? null : (User) session.getAttribute("userlogin");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (currentUser.getId() == adminId) {
            response.sendRedirect(request.getContextPath() + "/admin-settings?error=selfDelete");
            return;
        }

        if (!userService.isAdminUser(adminId)) {
            response.sendRedirect(request.getContextPath() + "/admin-settings?error=notAdmin");
            return;
        }

        if (userService.countAdminUsers() <= 1) {
            response.sendRedirect(request.getContextPath() + "/admin-settings?error=lastAdmin");
            return;
        }

        boolean blocked = userService.blockAdminUser(adminId);
        response.sendRedirect(request.getContextPath() + "/admin-settings?" + (blocked ? "success=deleted" : "error=deleteFailed"));
    }
}
