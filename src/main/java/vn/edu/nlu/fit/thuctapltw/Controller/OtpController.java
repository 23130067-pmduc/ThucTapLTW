package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.UserService;

@WebServlet(name = "OtpController", value = "/otp")
public class OtpController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/otplogin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("resend".equals(action)) {
            String email = request.getParameter("email");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            try {
                userService.sendOtpResetPassword(email);
                response.getWriter().write("{\"success\":true}");
            } catch (RuntimeException e) {
                response.getWriter().write(
                        "{\"success\":false,\"message\":\"" + e.getMessage() + "\"}"
                );
            }
            return;
        }


        String email = request.getParameter("email");
        String otp = request.getParameter("otp");
        String type = request.getParameter("type");

        System.out.println("EMAIL = " + email);
        System.out.println("OTP = " + otp);
        System.out.println("TYPE = " + type);


        boolean ok;

        if ("reset".equals(type)) {
            ok = userService.checkOtpCuoi(email, otp);
        } else {
            ok = userService.verifyOtp(email, otp);
        }

        if (ok) {
            if ("reset".equals(type)) {
                response.sendRedirect("reset_password.jsp?email=" + email + "&otp=" + otp);
            } else {response.sendRedirect(request.getContextPath() + "/login");
            }
        } else {
            request.setAttribute("error", "OTP sai hoặc đã hết hạn");
            request.getRequestDispatcher("/Verify.jsp").forward(request, response);
        }

    }
}