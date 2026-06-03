package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.InventoryTransactionService;
import vn.edu.nlu.fit.thuctapltw.model.InventoryTransaction;

import java.io.IOException;

@WebServlet(name = "InventoryTransactionStatusController", value = "/inventory-transaction-status")
public class InventoryTransactionStatusController extends HttpServlet {
    private InventoryTransactionService inventoryTransactionService;

    @Override
    public void init() {
        inventoryTransactionService = new InventoryTransactionService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int transactionId = parseId(request.getParameter("transactionId"));
        String status = normalizeStatus(request.getParameter("status"));
        String redirect = normalizeRedirect(request.getParameter("redirect"));

        if (transactionId <= 0 || status == null) {
            response.sendRedirect(request.getContextPath() + redirect + appendParam(redirect, "error=invalid_status"));
            return;
        }

        InventoryTransaction transaction = inventoryTransactionService.getById(transactionId);
        if (transaction == null) {
            response.sendRedirect(request.getContextPath() + "/inventory-history-admin?error=not_found");
            return;
        }

        if (!"PENDING".equals(transaction.getStatus())) {
            response.sendRedirect(request.getContextPath() + redirect + appendParam(redirect, "error=already_processed"));
            return;
        }

        String result = inventoryTransactionService.updateStatusIfPending(transactionId, status);
        if ("SUCCESS".equals(result)) {
            response.sendRedirect(request.getContextPath() + redirect + appendParam(redirect, "success=status_updated"));
        } else if ("INSUFFICIENT_STOCK".equals(result)) {
            response.sendRedirect(request.getContextPath() + redirect + appendParam(redirect, "error=insufficient_stock"));
        } else if ("MISSING_UNIT_COST".equals(result)) {
            response.sendRedirect(request.getContextPath() + redirect + appendParam(redirect, "error=missing_unit_cost"));
        } else if ("ALREADY_PROCESSED".equals(result)) {
            response.sendRedirect(request.getContextPath() + redirect + appendParam(redirect, "error=already_processed"));
        } else {
            response.sendRedirect(request.getContextPath() + redirect + appendParam(redirect, "error=status_update_failed"));
        }
    }

    private int parseId(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return -1;
        }
    }

    private String normalizeStatus(String status) {
        if (status == null) {
            return null;
        }
        String normalized = status.trim().toUpperCase();
        if ("COMPLETED".equals(normalized) || "CANCELLED".equals(normalized)) {
            return normalized;
        }
        return null;
    }

    private String normalizeRedirect(String redirect) {
        if (redirect == null || redirect.isBlank() || !redirect.startsWith("/")) {
            return "/inventory-history-admin";
        }
        if (redirect.startsWith("//") || redirect.contains("http://") || redirect.contains("https://")) {
            return "/inventory-history-admin";
        }
        return redirect;
    }

    private String appendParam(String url, String param) {
        return url.contains("?") ? "&" + param : "?" + param;
    }
}
