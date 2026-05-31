package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.InventoryTransactionService;
import vn.edu.nlu.fit.thuctapltw.model.InventoryTransaction;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "InventoryHistoryAdminController", value = "/inventory-history-admin")
public class InventoryHistoryAdminController extends HttpServlet {
    private InventoryTransactionService inventoryTransactionService;

    @Override
    public void init() {
        inventoryTransactionService = new InventoryTransactionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String keyword = getValue(request.getParameter("keyword"));
        String type = getValue(request.getParameter("type"));
        String status = getValue(request.getParameter("status"));

        int pageSize = 20;
        int currentPage = parsePage(request.getParameter("page"));

        int totalItems = inventoryTransactionService.countTransactions(keyword, type, status);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        if (totalPages == 0) {
            totalPages = 1;
        }

        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int offset = (currentPage - 1) * pageSize;
        List<InventoryTransaction> transactions = inventoryTransactionService.searchTransactions(keyword, type, status, pageSize, offset);

        request.setAttribute("transactions", transactions);
        request.setAttribute("keyword", keyword);
        request.setAttribute("type", type);
        request.setAttribute("status", status);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("totalTransactions", inventoryTransactionService.countAll());
        request.setAttribute("totalImport", inventoryTransactionService.countImport());
        request.setAttribute("totalExport", inventoryTransactionService.countExport());
        request.setAttribute("totalPending", inventoryTransactionService.countPending());

        request.getRequestDispatcher("/inventory-history-admin.jsp").forward(request, response);
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
