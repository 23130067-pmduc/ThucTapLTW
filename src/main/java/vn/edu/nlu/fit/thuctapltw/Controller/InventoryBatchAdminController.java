package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.InventoryBatchService;
import vn.edu.nlu.fit.thuctapltw.model.InventoryBatch;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "InventoryBatchAdminController", value = "/inventory-batch-admin")
public class InventoryBatchAdminController extends HttpServlet {
    private InventoryBatchService inventoryBatchService;

    @Override
    public void init() {
        inventoryBatchService = new InventoryBatchService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String keyword = getValue(request.getParameter("keyword"));
        String batchStatus = normalizeBatchStatus(request.getParameter("batchStatus"));
        int pageSize = 20;
        int currentPage = parsePage(request.getParameter("page"));

        int totalItems = inventoryBatchService.countBatches(keyword, batchStatus);
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        if (totalPages == 0) {
            totalPages = 1;
        }
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int offset = (currentPage - 1) * pageSize;
        List<InventoryBatch> batches = inventoryBatchService.searchBatches(keyword, batchStatus, pageSize, offset);

        request.setAttribute("batches", batches);
        request.setAttribute("keyword", keyword);
        request.setAttribute("batchStatus", batchStatus);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("totalBatches", inventoryBatchService.countAll());
        request.setAttribute("remainingBatches", inventoryBatchService.countRemainingBatches());
        request.setAttribute("remainingQuantity", inventoryBatchService.sumRemainingQuantity());
        request.setAttribute("remainingValue", inventoryBatchService.sumRemainingValue());

        request.getRequestDispatcher("/inventory-batch-admin.jsp").forward(request, response);
    }

    private int parsePage(String pageParam) {
        try {
            return Math.max(Integer.parseInt(pageParam), 1);
        } catch (Exception e) {
            return 1;
        }
    }

    private String getValue(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeBatchStatus(String value) {
        if (value == null) {
            return "";
        }
        String normalized = value.trim().toUpperCase();
        if ("AVAILABLE".equals(normalized) || "PARTIAL".equals(normalized) || "EMPTY".equals(normalized)) {
            return normalized;
        }
        return "";
    }
}
