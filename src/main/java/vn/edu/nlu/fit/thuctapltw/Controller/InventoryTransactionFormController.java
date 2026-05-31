package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.Service.InventoryService;
import vn.edu.nlu.fit.thuctapltw.Service.InventoryTransactionService;
import vn.edu.nlu.fit.thuctapltw.model.InventoryItem;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "InventoryTransactionFormController", value = "/inventory-transaction-form")
public class InventoryTransactionFormController extends HttpServlet {
    private InventoryService inventoryService;
    private InventoryTransactionService inventoryTransactionService;

    @Override
    public void init() {
        inventoryService = new InventoryService();
        inventoryTransactionService = new InventoryTransactionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String type = normalizeType(request.getParameter("type"));
        List<InventoryItem> inventoryItems = inventoryService.searchInventory("", "", 2000, 0);

        request.setAttribute("type", type);
        request.setAttribute("typeText", "IMPORT".equals(type) ? "Nhập kho" : "Xuất kho");
        request.setAttribute("inventoryItems", inventoryItems);
        request.getRequestDispatcher("/inventory-transaction-form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String type = normalizeType(request.getParameter("type"));
        String supplierName = getValue(request.getParameter("supplierName"));
        String note = getValue(request.getParameter("note"));
        String[] variantIdParams = request.getParameterValues("variantIds");
        String[] quantityParams = request.getParameterValues("quantities");

        List<Integer> variantIds = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();

        if (variantIdParams != null && quantityParams != null) {
            int length = Math.min(variantIdParams.length, quantityParams.length);
            for (int i = 0; i < length; i++) {
                int variantId = parsePositiveInt(variantIdParams[i]);
                int quantity = parsePositiveInt(quantityParams[i]);

                if (variantId > 0 && quantity > 0) {
                    variantIds.add(variantId);
                    quantities.add(quantity);
                }
            }
        }

        if (variantIds.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/inventory-transaction-form?type=" + type + "&error=empty_items");
            return;
        }

        Integer createdBy = getCurrentUserId(request);
        int transactionId = inventoryTransactionService.createTransaction(type, supplierName, note, createdBy, variantIds, quantities);

        response.sendRedirect(request.getContextPath() + "/inventory-history-detail?id=" + transactionId + "&created=true");
    }

    private String normalizeType(String type) {
        if ("EXPORT".equalsIgnoreCase(type)) {
            return "EXPORT";
        }
        return "IMPORT";
    }

    private String getValue(String value) {
        return value == null ? "" : value.trim();
    }

    private int parsePositiveInt(String value) {
        try {
            int number = Integer.parseInt(value);
            return Math.max(number, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    private Integer getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userlogin") == null) {
            return null;
        }

        User user = (User) session.getAttribute("userlogin");
        return user.getId();
    }
}
