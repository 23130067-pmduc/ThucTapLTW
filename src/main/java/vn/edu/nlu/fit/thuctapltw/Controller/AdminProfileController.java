package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.fit.thuctapltw.Service.UserService;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@WebServlet("/admin-profile")
public class AdminProfileController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userlogin") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User sessionUser = (User) session.getAttribute("userlogin");

        if (sessionUser.getPermissions() == null || sessionUser.getPermissions().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User admin = userService.findById(sessionUser.getId());

        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/logout");
            return;
        }

        if (admin.getCreatedAt() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            request.setAttribute("createdAtFormatted", admin.getCreatedAt().format(formatter));
        }

        request.setAttribute("admin", admin);
        request.setAttribute("success", request.getParameter("success"));
        request.setAttribute("error", request.getParameter("error"));

        request.getRequestDispatcher("/admin-profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userlogin") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User sessionUser = (User) session.getAttribute("userlogin");

        if (sessionUser.getRole() == null || !sessionUser.getRole().equalsIgnoreCase("admin")) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String fullName = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");

        if (fullName == null || fullName.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin-profile?error=empty-name");
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin-profile?error=empty-email");
            return;
        }

        User admin = userService.findById(sessionUser.getId());

        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/logout");
            return;
        }

        admin.setFullName(fullName.trim());
        admin.setEmail(email.trim());
        admin.setPhone(phone == null ? "" : phone.trim());
        admin.setGender(gender);

        userService.update(admin);

        User updatedAdmin = userService.findById(sessionUser.getId());
        session.setAttribute("userlogin", updatedAdmin);

        response.sendRedirect(request.getContextPath() + "/admin-profile?success=updated");
    }
}