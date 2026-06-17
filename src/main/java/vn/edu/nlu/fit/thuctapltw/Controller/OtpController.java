package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.fit.thuctapltw.Service.UserService;

import java.io.IOException;

@WebServlet(name = "OtpController", value = "/otp")
public class OtpController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String type = request.getParameter("type");

        if ("reset".equals(type)) {
            request.getRequestDispatcher("/Verify.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/otplogin.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("resend".equals(action)) {
            handleResendOtp(request, response);
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
                response.sendRedirect(request.getContextPath()
                        + "/reset_password.jsp?email=" + email + "&otp=" + otp);

            } else if ("admin-create".equals(type)) {
                response.sendRedirect(request.getContextPath() + "/user-admin");

            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }

        } else {
            request.setAttribute("error", "OTP sai hoặc đã hết hạn");
            request.setAttribute("email", email);
            request.setAttribute("type", type);

            if ("reset".equals(type)) {
                request.getRequestDispatcher("/Verify.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/otplogin.jsp").forward(request, response);
            }
        }
    }

    private void handleResendOtp(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String email = request.getParameter("email");
        String type = request.getParameter("type");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if ("reset".equals(type)) {
                userService.sendOtpResetPassword(email);
            } else {
                userService.resendOtpVerifyEmail(email);
            }

            response.getWriter().write("{\"success\":true}");

        } catch (RuntimeException e) {
            response.getWriter().write(
                    "{\"success\":false,\"message\":\"" + e.getMessage() + "\"}"
            );
        }
    }
}