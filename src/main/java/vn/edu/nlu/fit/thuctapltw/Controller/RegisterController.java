package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.UserService;

import java.io.IOException;

@WebServlet(name = "RegisterController", value = "/register")
public class RegisterController extends HttpServlet {
    private UserService userService;
    @Override
    public void init() {
        userService = new UserService();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");

        if (username == null || email == null ||
                password == null || repassword == null ||
                username.isEmpty() || email.isEmpty() ||
                password.isEmpty() || repassword.isEmpty()) {

            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(repassword)) {
            request.setAttribute("error", "Mật khẩu nhập lại không khớp");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        try {
            userService.registerSendOtp(username, email, password);
            response.sendRedirect(
                    request.getContextPath() + "/otp?email=" + email
            );

        } catch (RuntimeException e) {
            request.setAttribute("Email đã tồn tại", e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }

    }
}