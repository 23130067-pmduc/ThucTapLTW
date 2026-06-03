package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.InventoryBatchService;
import vn.edu.nlu.fit.thuctapltw.model.InventoryBatch;

import java.io.IOException;

@WebServlet(name = "InventoryBatchDetailController", value = "/inventory-batch-detail")
public class InventoryBatchDetailController extends HttpServlet {
    private InventoryBatchService inventoryBatchService;

    @Override
    public void init() {
        inventoryBatchService = new InventoryBatchService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int id = parseId(request.getParameter("id"));
        if (id <= 0) {
            response.sendRedirect(request.getContextPath() + "/inventory-batch-admin");
            return;
        }

        InventoryBatch batch = inventoryBatchService.getById(id);
        if (batch == null) {
            response.sendRedirect(request.getContextPath() + "/inventory-batch-admin?error=not_found");
            return;
        }

        request.setAttribute("batch", batch);
        request.getRequestDispatcher("/inventory-batch-detail.jsp").forward(request, response);
    }

    private int parseId(String idParam) {
        try {
            return Integer.parseInt(idParam);
        } catch (Exception e) {
            return 0;
        }
    }
}
