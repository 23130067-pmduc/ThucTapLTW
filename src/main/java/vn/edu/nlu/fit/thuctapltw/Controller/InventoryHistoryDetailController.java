package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.InventoryTransactionDetailService;
import vn.edu.nlu.fit.thuctapltw.Service.InventoryTransactionService;
import vn.edu.nlu.fit.thuctapltw.model.InventoryTransaction;
import vn.edu.nlu.fit.thuctapltw.model.InventoryTransactionDetail;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "InventoryHistoryDetailController", value = "/inventory-history-detail")
public class InventoryHistoryDetailController extends HttpServlet {
    private InventoryTransactionService inventoryTransactionService;
    private InventoryTransactionDetailService inventoryTransactionDetailService;

    @Override
    public void init() {
        inventoryTransactionService = new InventoryTransactionService();
        inventoryTransactionDetailService = new InventoryTransactionDetailService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int id = parseId(request.getParameter("id"));
        if (id <= 0) {
            response.sendRedirect(request.getContextPath() + "/inventory-history-admin?error=invalid_id");
            return;
        }

        InventoryTransaction transaction = inventoryTransactionService.getById(id);
        if (transaction == null) {
            response.sendRedirect(request.getContextPath() + "/inventory-history-admin?error=not_found");
            return;
        }

        List<InventoryTransactionDetail> details = inventoryTransactionDetailService.getDetailsByTransactionId(id);

        request.setAttribute("transaction", transaction);
        request.setAttribute("details", details);
        request.getRequestDispatcher("/inventory-history-detail.jsp").forward(request, response);
    }

    private int parseId(String idParam) {
        try {
            return Integer.parseInt(idParam);
        } catch (Exception e) {
            return -1;
        }
    }
}
