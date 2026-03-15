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
import java.time.format.DateTimeFormatter;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {

    private UserService userService;

    @Override
    public void init(){
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("userlogin") == null){
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("userlogin");

        User fullUser = userService.findById(user.getId());

        request.setAttribute("user", fullUser);

        // Để chuyển ngày thành dạng bth VN.
        if (fullUser.getBirthday() != null) {
            request.setAttribute(
                    "birthdayDate",
                    java.sql.Date.valueOf(fullUser.getBirthday())
            );
        }

        // format ngày tạo tài khoản
        if (fullUser.getCreatedAt() != null) {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");

            request.setAttribute(
                    "createdAtFormatted",
                    fullUser.getCreatedAt().format(formatter)
            );
        }


        request.getRequestDispatcher("profile.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userlogin") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Lấy user đang đăng nhập
        User userSession = (User) session.getAttribute("userlogin");

        // Lấy dữ liệu từ form (name phải khớp JSP)
        String fullName = request.getParameter("fullname");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String birthdayStr = request.getParameter("birthday"); // yyyy-MM-dd
        String address = request.getParameter("address");

        String gender = request.getParameter("gender");

        // Lấy user đầy đủ từ DB
        User user = userService.findById(userSession.getId());

        // Set lại thông tin
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setEmail(email);
        user.setAddress(address);


        user.setGender(gender);

        // Parse ngày sinh
        if (birthdayStr != null && !birthdayStr.isEmpty()) {
            user.setBirthday(java.time.LocalDate.parse(birthdayStr));
        }

        // Update DB
        userService.update(user);

        // Update lại session (QUAN TRỌNG)
        session.setAttribute("userlogin", user);

        // Quay lại trang profile
        response.sendRedirect("profile");
    }

}
