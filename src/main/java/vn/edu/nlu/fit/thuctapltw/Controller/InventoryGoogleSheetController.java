package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.InventoryGoogleSheetService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "InventoryGoogleSheetController", value = "/inventory-google-sheet")
public class InventoryGoogleSheetController extends HttpServlet {
    private InventoryGoogleSheetService googleSheetService;

    @Override
    public void init() {
        googleSheetService = new InventoryGoogleSheetService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            int updatedRows = googleSheetService.updateInventoryReport(getServletContext());
            String message = "Cập nhật Google Sheet thành công. Đã ghi " + updatedRows + " biến thể tồn kho.";
            response.sendRedirect(request.getContextPath() + "/inventory-admin?sheetSuccess=" + encode(message));
        } catch (Exception e) {
            String message = e.getMessage() == null ? "Cập nhật Google Sheet thất bại." : e.getMessage();
            response.sendRedirect(request.getContextPath() + "/inventory-admin?sheetError=" + encode(message));
        }
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
