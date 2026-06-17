package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.Service.InventoryGoogleSheetService;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "InventoryGoogleSheetImportController", value = "/inventory-google-sheet-import")
public class InventoryGoogleSheetImportController extends HttpServlet {
    private InventoryGoogleSheetService googleSheetService;

    @Override
    public void init() throws ServletException {
        googleSheetService = new InventoryGoogleSheetService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            Integer createdBy = getCurrentUserId(request);
            InventoryGoogleSheetService.ImportSyncResult result =
                    googleSheetService.importInventoryFromSheet(getServletContext(), createdBy);

            String message = result.getMessage();
            response.sendRedirect(request.getContextPath()
                    + "/inventory-admin?sheetSuccess=" + encode(message));
        } catch (Exception e) {
            String message = e.getMessage() == null || e.getMessage().isBlank()
                    ? "Không thể đồng bộ nhập kho từ Google Sheet."
                    : e.getMessage();
            response.sendRedirect(request.getContextPath()
                    + "/inventory-admin?sheetError=" + encode(message));
        }
    }

    private Integer getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        User user = (User) session.getAttribute("userlogin");
        if (user == null || user.getId() <= 0) {
            return null;
        }
        return user.getId();
    }

    private String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }
}
