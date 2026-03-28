package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.UserService;

import java.io.IOException;

@WebServlet(name = "ResetPasswordController", value = "/reset_pass")
public class ResetPasswordController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/reset_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String otp = request.getParameter("otp");
        String password = request.getParameter("password");

        try {
            userService.resetPassword(email, otp, password);
            response.sendRedirect("login");
        } catch (RuntimeException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/reset_password.jsp").forward(request, response);
        }
    }
}