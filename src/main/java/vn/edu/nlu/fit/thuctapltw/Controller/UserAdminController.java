package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.UserService;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "UserAdminController", value = "/user-admin")
public class UserAdminController extends HttpServlet {
    private UserService userService;

    @Override
    public void init(){
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String mode = request.getParameter("mode");

        if(mode == null){
            int pageSize = 20;
            int currentPage = 1;

            String pageParam = request.getParameter("page");

            if (pageParam != null){
                try {
                    currentPage = Integer.parseInt(pageParam);
                } catch (NumberFormatException e){
                    currentPage = 1;
                }
            }

            if (currentPage < 1){
                currentPage = 1;
            }

            int totalUsers = userService.countAllUsers();

            int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

            if (totalPages == 0){
                totalPages = 1;
            }

            if (currentPage > totalPages){
                currentPage = totalPages;
            }

            int offset = (currentPage - 1) * pageSize;


            List<User> users = userService.getUserByPage(pageSize, offset);

            int countInWeek = userService.getCountInWeek();
            int countActive = userService.getCountActive();
            int countBlock = userService.getCountBlock();


            request.setAttribute("total", totalUsers);
            request.setAttribute("countInWeek", countInWeek);
            request.setAttribute("countActive", countActive);
            request.setAttribute("countBlock", countBlock);
            request.setAttribute("users", users);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.getRequestDispatcher("/user-admin.jsp").forward(request,response);
            return;
        }

        if("edit".equals(mode) || "view".equals(mode)){
            int id = Integer.parseInt(request.getParameter("id"));
            User user = userService.findById(id);

            request.setAttribute("user", user);
            request.setAttribute("mode", mode);

            request.getRequestDispatcher("/user-form.jsp").forward(request,response);
            return;
        }

        if("add".equals(mode)){
            request.setAttribute("mode", "add");
            request.getRequestDispatcher("/user-form.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("create".equals(action)){
            User user = new User();
            user.setUsername(request.getParameter("username"));
            user.setEmail(request.getParameter("email"));
            user.setRole(request.getParameter("role"));
            user.setStatus(request.getParameter("status"));
            user.setFullName(request.getParameter("full_name"));
            user.setPhone(request.getParameter("phone"));
            user.setGender(request.getParameter("gander"));



            userService.createUser(user);

            response.sendRedirect("user-admin");
            return;
        }

        if ("update".equals(action)){
            int id = Integer.parseInt(request.getParameter("id"));

            User user = new User();

            user.setId(id);
            user.setUsername(request.getParameter("username"));
            user.setEmail(request.getParameter("email"));
            user.setRole(request.getParameter("role"));
            user.setStatus(request.getParameter("status"));
            user.setFullName(request.getParameter("full_name"));
            user.setPhone(request.getParameter("phone"));
            user.setGender(request.getParameter("gender"));



            userService.updateUser(user);

            response.sendRedirect("user-admin?mode=view&id=" + id);
        }

        if ("block".equals(action)){

            int id = Integer.parseInt(request.getParameter("id"));

            userService.blockUser(id);

            response.sendRedirect("user-admin");
            return;
        }

    }
}