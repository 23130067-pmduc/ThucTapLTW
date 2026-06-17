package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.SupplierService;
import vn.edu.nlu.fit.thuctapltw.model.Supplier;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SupplierAdminController", value = "/supplier-admin")
public class SupplierAdminController extends HttpServlet {
    private SupplierService supplierService;

    @Override
    public void init() {
        supplierService = new SupplierService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        int pageSize = 10;
        int currentPage = parseInt(request.getParameter("page"), 1);
        if (currentPage < 1) {
            currentPage = 1;
        }

        String keyword = normalize(request.getParameter("keyword"));
        String status = normalize(request.getParameter("status"));

        int total = supplierService.countSuppliers(keyword, status);
        int totalPages = (int) Math.ceil((double) total / pageSize);
        if (totalPages == 0) {
            totalPages = 1;
        }
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int offset = (currentPage - 1) * pageSize;
        List<Supplier> suppliers = supplierService.getSuppliersByPage(keyword, status, pageSize, offset);

        request.setAttribute("suppliers", suppliers);
        request.setAttribute("keyword", keyword);
        request.setAttribute("status", status);
        request.setAttribute("total", total);
        request.setAttribute("totalActive", supplierService.countActive());
        request.setAttribute("totalLocked", supplierService.countLocked());
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);

        request.getRequestDispatcher("supplier-admin.jsp").forward(request, response);
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return value == null ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
