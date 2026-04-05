package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.DAO.CategoryDao;
import vn.edu.nlu.fit.thuctapltw.Service.ProductService;
import vn.edu.nlu.fit.thuctapltw.Service.SearchHistoryService;
import vn.edu.nlu.fit.thuctapltw.model.Category;
import vn.edu.nlu.fit.thuctapltw.model.Product;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchController", value = "/SearchController")
public class SearchController extends HttpServlet {
    private final SearchHistoryService searchHistoryService = new SearchHistoryService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String keyword = request.getParameter("keyword");
        if (keyword != null) {
            keyword = keyword.trim();
        }
        if (keyword == null || keyword.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/trang-chu");
            return;
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userlogin");
        searchHistoryService.saveHistory(session, user, keyword);
        ProductService ps = new ProductService();
        List<Product> list = ps.searchProducts(keyword);
        request.setAttribute("list", list);
        CategoryDao cd = new CategoryDao();
        List<Category> danhMuc = cd.findAll();
        request.setAttribute("danhMuc", danhMuc);
        request.getRequestDispatcher("/Search.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}