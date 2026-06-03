package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.InventoryService;
import vn.edu.nlu.fit.thuctapltw.model.InventoryItem;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "InventoryAdminController", value = "/inventory-admin")
public class InventoryAdminController extends HttpServlet {
    private InventoryService inventoryService;

    @Override
    public void init() {
        inventoryService = new InventoryService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String keyword = getValue(request.getParameter("keyword"));
        String stockStatus = getValue(request.getParameter("stockStatus"));
        String sortField = getValue(request.getParameter("sortField"));
        String sortDir = getValue(request.getParameter("sortDir"));

        if (!"id".equals(sortField) && !"productName".equals(sortField)) {
            sortField = "";
        }

        if (!"asc".equalsIgnoreCase(sortDir) && !"desc".equalsIgnoreCase(sortDir)) {
            sortDir = "";
        } else {
            sortDir = sortDir.toLowerCase();
        }

        int pageSize = 20;
        int currentPage = parsePage(request.getParameter("page"));

        int totalItems = inventoryService.countInventoryByFilter(keyword, stockStatus);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        if (totalPages == 0) {
            totalPages = 1;
        }

        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int offset = (currentPage - 1) * pageSize;
        List<InventoryItem> inventoryItems = inventoryService.searchInventory(keyword, stockStatus, sortField, sortDir, pageSize, offset);

        request.setAttribute("inventoryItems", inventoryItems);
        request.setAttribute("keyword", keyword);
        request.setAttribute("stockStatus", stockStatus);
        request.setAttribute("sortField", sortField);
        request.setAttribute("sortDir", sortDir);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("totalVariants", inventoryService.countTotalVariants());
        request.setAttribute("lowStockCount", inventoryService.countLowStock());
        request.setAttribute("outOfStockCount", inventoryService.countOutOfStock());
        request.setAttribute("totalStock", inventoryService.sumStock());

        request.getRequestDispatcher("/inventory-admin.jsp").forward(request, response);
    }

    private int parsePage(String pageParam) {
        int page = 1;

        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        return Math.max(page, 1);
    }

    private String getValue(String value) {
        return value == null ? "" : value.trim();
    }
}
