package vn.edu.nlu.fit.thuctapltw.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.UserService;
import vn.edu.nlu.fit.thuctapltw.model.User;

import javax.management.relation.Role;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String ajax = request.getParameter("ajax");

        if ("search".equalsIgnoreCase(ajax)){
            response.setContentType("application/json;charset=UTF-8");

            String keyword = request.getParameter("keyword");
            String role = request.getParameter("role");
            String status = request.getParameter("status");

            if (keyword == null) {
                keyword = "";
            }

            if (role == null) {
                role = "";
            }

            if (status == null) {
                status = "";
            }

            keyword = keyword.trim();
            role = role.trim();
            status = status.trim();

            int pageSize = 20;
            int currentPage = 1;

            String pageParam = request.getParameter("page");

            if (pageParam != null) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }

            if (currentPage < 1) {
                currentPage = 1;
            }

            int total = userService.countUserByFilter(keyword, role, status);

            int totalPages = (int) Math.ceil((double) total / pageSize);

            if (totalPages == 0) {
                totalPages = 1;
            }

            if (currentPage > totalPages) {
                currentPage = totalPages;
            }

            int offset = (currentPage - 1) * pageSize;

            List<User> users = userService.searchUserByFilter(keyword, role, status, pageSize, offset);

            List<Map<String, Object>> safeUsers = new ArrayList<>();

            for (User user : users) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", user.getId());
                item.put("username", user.getUsername());
                item.put("fullName", user.getFullName());
                item.put("email", user.getEmail());
                item.put("roleName", user.getRoleName());
                item.put("status", user.getStatus());
                safeUsers.add(item);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("users", safeUsers);
            result.put("currentPage", currentPage);
            result.put("totalPages", totalPages);
            result.put("keyword", keyword);
            result.put("role", role);
            result.put("status", status);

            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(result);

            response.getWriter().write(json);
            return;
        }

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

            String keyword = request.getParameter("keyword");
            String role = request.getParameter("role");
            String status = request.getParameter("status");

            if (keyword == null) {
                keyword = "";
            }

            if (role == null) {
                role = "";
            }

            if (status == null) {
                status = "";
            }

            keyword = keyword.trim();
            role = role.trim();
            status = status.trim();



            int filteredTotal = userService.countUserByFilter(keyword, role, status);
            int totalPages = (int) Math.ceil((double) filteredTotal / pageSize);

            if (totalPages == 0){
                totalPages = 1;
            }

            if (currentPage > totalPages){
                currentPage = totalPages;
            }

            int offset = (currentPage - 1) * pageSize;


            List<User> users = userService.searchUserByFilter(keyword, role, status, pageSize, offset);


            int totalUsers = userService.countAllUsers();
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

            request.setAttribute("keyword", keyword);
            request.setAttribute("role", role);
            request.setAttribute("status", status);
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
            return;
        }

        if ("block".equals(action)){

            int id = Integer.parseInt(request.getParameter("id"));

            userService.blockUser(id);

            response.sendRedirect("user-admin");
            return;
        }

    }
}