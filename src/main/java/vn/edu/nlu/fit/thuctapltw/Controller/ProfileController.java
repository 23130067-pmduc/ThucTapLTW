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

        User userSession = (User) session.getAttribute("userlogin");

        String fullName = request.getParameter("fullname");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        String gender = request.getParameter("gender");

        User user = userService.findById(userSession.getId());

        user.setFullName(fullName);
        user.setPhone(phone);
        user.setEmail(email);
        user.setGender(gender);


        userService.update(user);

        session.setAttribute("userlogin", user);

        response.sendRedirect("profile");
    }

}
