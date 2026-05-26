package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.CategoryService;
import vn.edu.nlu.fit.thuctapltw.model.Category;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryAdminController", value = "/category-admin")
public class CategoryAdminController extends HttpServlet {

    private CategoryService categoryService;

    @Override
    public void init() {
        categoryService = new CategoryService();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mode = request.getParameter("mode");

        if(mode == null){
            List<Category> categories = categoryService.findAllWithCountProduct();

            int total = categories.size();
            int totalActive = categoryService.getCategoryActive();

            request.setAttribute("categories", categories);
            request.setAttribute("total", total);
            request.setAttribute("totalActive", totalActive);

            request.getRequestDispatcher("category-admin.jsp").forward(request,response);
            return;
        }

        if ("edit".equalsIgnoreCase(mode) || "view".equals(mode)) {

            int id = Integer.parseInt(request.getParameter("id"));
            Category category = categoryService.findById(id);

            request.setAttribute("mode", mode);
            request.setAttribute("category", category);

            request.getRequestDispatcher("category-form.jsp").forward(request,response);
        }

        if ("add".equalsIgnoreCase(mode)) {
            request.setAttribute("mode", mode);

            request.getRequestDispatcher("category-form.jsp").forward(request,response);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");

        String action = request.getParameter("action");

        if ("create".equals(action)){
            Category category = new Category();

            category.setName(request.getParameter("name"));
            category.setDescription(request.getParameter("description"));
            category.setStatus(request.getParameter("status"));

            categoryService.create(category);

            response.sendRedirect("category-admin");
            return;
        }

        if ("update".equals(action)){
            int id = Integer.parseInt(request.getParameter("id"));

            Category category = new Category();

            category.setId(id);
            category.setName(request.getParameter("name"));
            category.setDescription(request.getParameter("description"));
            category.setStatus(request.getParameter("status"));

            categoryService.updateCategory(category);

            response.sendRedirect("category-admin");
            return;
        }

        if ("delete".equals(action)){
            int id = Integer.parseInt(request.getParameter("id"));

            categoryService.deleteCategory(id);

            response.sendRedirect("category-admin");
            return;
        }
    }
}