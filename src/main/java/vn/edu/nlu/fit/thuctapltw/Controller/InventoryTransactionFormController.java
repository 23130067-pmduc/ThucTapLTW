package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.Service.InventoryService;
import vn.edu.nlu.fit.thuctapltw.Service.InventoryTransactionService;
import vn.edu.nlu.fit.thuctapltw.Service.SupplierService;
import vn.edu.nlu.fit.thuctapltw.model.InventoryItem;
import vn.edu.nlu.fit.thuctapltw.model.Supplier;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "InventoryTransactionFormController", value = "/inventory-transaction-form")
public class InventoryTransactionFormController extends HttpServlet {
    private InventoryService inventoryService;
    private InventoryTransactionService inventoryTransactionService;
    private SupplierService supplierService;

    @Override
    public void init() {
        inventoryService = new InventoryService();
        inventoryTransactionService = new InventoryTransactionService();
        supplierService = new SupplierService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String type = normalizeType(request.getParameter("type"));
        prepareFormData(request, type);
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
        String[] unitCostParams = request.getParameterValues("unitCosts");

        if ("IMPORT".equals(type) && !isValidActiveSupplierValue(supplierName)) {
            prepareFormData(request, type);
            request.setAttribute("supplierName", supplierName);
            request.setAttribute("note", note);
            request.setAttribute("supplierError", "Vui lòng chọn nhà cung cấp đang hoạt động trong danh sách.");
            request.getRequestDispatcher("/inventory-transaction-form.jsp").forward(request, response);
            return;
        }

        List<Integer> variantIds = new ArrayList<>();
        List<Integer> quantities = new ArrayList<>();
        List<BigDecimal> unitCosts = new ArrayList<>();

        if (variantIdParams != null && quantityParams != null) {
            int length = Math.min(variantIdParams.length, quantityParams.length);
            for (int i = 0; i < length; i++) {
                int variantId = parsePositiveInt(variantIdParams[i]);
                int quantity = parsePositiveInt(quantityParams[i]);

                if (variantId > 0 && quantity > 0) {
                    variantIds.add(variantId);
                    quantities.add(quantity);

                    if ("IMPORT".equals(type)) {
                        String costValue = unitCostParams != null && i < unitCostParams.length ? unitCostParams[i] : "0";
                        unitCosts.add(parsePositiveDecimal(costValue));
                    }
                }
            }
        }

        if (variantIds.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/inventory-transaction-form?type=" + type + "&error=empty_items");
            return;
        }

        if ("IMPORT".equals(type)) {
            Map<Integer, String> costErrors = inventoryTransactionService.validateImportCost(variantIds, unitCosts);
            if (!costErrors.isEmpty()) {
                prepareFormData(request, type);
                request.setAttribute("costErrors", costErrors.values());
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("note", note);
                request.getRequestDispatcher("/inventory-transaction-form.jsp").forward(request, response);
                return;
            }
        }

        if ("EXPORT".equals(type)) {
            Map<Integer, String> stockErrors = inventoryTransactionService.validateExportStock(variantIds, quantities);
            if (!stockErrors.isEmpty()) {
                prepareFormData(request, type);
                request.setAttribute("stockErrors", stockErrors.values());
                request.setAttribute("supplierName", supplierName);
                request.setAttribute("note", note);
                request.getRequestDispatcher("/inventory-transaction-form.jsp").forward(request, response);
                return;
            }
        }

        Integer createdBy = getCurrentUserId(request);
        int transactionId = inventoryTransactionService.createTransaction(type, supplierName, note, createdBy, variantIds, quantities, unitCosts);

        response.sendRedirect(request.getContextPath() + "/inventory-history-detail?id=" + transactionId + "&created=true");
    }

    private void prepareFormData(HttpServletRequest request, String type) {
        List<InventoryItem> inventoryItems = inventoryService.getInventoryItemsForTransaction();
        List<Supplier> activeSuppliers = "IMPORT".equals(type)
                ? supplierService.getActiveSuppliersForSelect()
                : List.of();

        request.setAttribute("type", type);
        request.setAttribute("typeText", "IMPORT".equals(type) ? "Nhập kho" : "Xuất kho");
        request.setAttribute("inventoryItems", inventoryItems);
        request.setAttribute("activeSuppliers", activeSuppliers);
    }

    private boolean isValidActiveSupplierValue(String supplierValue) {
        String normalizedValue = getValue(supplierValue);
        if (normalizedValue.isEmpty()) {
            return false;
        }

        List<Supplier> activeSuppliers = supplierService.getActiveSuppliersForSelect();
        for (Supplier supplier : activeSuppliers) {
            if (normalizedValue.equals(buildSupplierDisplayName(supplier))) {
                return true;
            }
        }
        return false;
    }

    private String buildSupplierDisplayName(Supplier supplier) {
        if (supplier == null) {
            return "";
        }

        String code = getValue(supplier.getCode());
        String name = getValue(supplier.getName());

        if (code.isEmpty()) {
            return name;
        }
        if (name.isEmpty()) {
            return code;
        }
        return code + " - " + name;
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

    private BigDecimal parsePositiveDecimal(String value) {
        try {
            if (value == null || value.isBlank()) {
                return BigDecimal.ZERO;
            }
            BigDecimal number = new BigDecimal(value.trim().replace(",", ""));
            return number.compareTo(BigDecimal.ZERO) > 0 ? number : BigDecimal.ZERO;
        } catch (Exception e) {
            return BigDecimal.ZERO;
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
