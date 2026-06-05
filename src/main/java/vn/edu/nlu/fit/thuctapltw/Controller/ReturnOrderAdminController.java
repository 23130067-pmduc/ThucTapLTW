package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.ReturnOrderService;
import vn.edu.nlu.fit.thuctapltw.model.ReturnOrder;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/return-order-admin")
public class ReturnOrderAdminController extends HttpServlet {
    private ReturnOrderService returnOrderService;

    @Override
    public void init() {
        returnOrderService = new ReturnOrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String mode = request.getParameter("mode");
        String success = request.getParameter("success");
        String error = request.getParameter("error");
        if (success != null && !success.isBlank()) request.setAttribute("success", success);
        if (error != null && !error.isBlank()) request.setAttribute("error", error);

        if ("detail".equalsIgnoreCase(mode)) {
            int id = parseInt(request.getParameter("id"), 0);
            ReturnOrder returnOrder = returnOrderService.findById(id);
            if (returnOrder == null) {
                response.sendRedirect(request.getContextPath() + "/return-order-admin?error="
                        + URLEncoder.encode("Không tìm thấy phiếu hoàn hàng", StandardCharsets.UTF_8));
                return;
            }
            request.setAttribute("returnOrder", returnOrder);
            request.setAttribute("items", returnOrder.getItems());
            request.getRequestDispatcher("/return-order-detail.jsp").forward(request, response);
            return;
        }

        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        var returns = returnOrderService.search(keyword, status);
        long pending = returns.stream().filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count();
        long approved = returns.stream().filter(r -> "APPROVED".equalsIgnoreCase(r.getStatus())).count();
        long rejected = returns.stream().filter(r -> "REJECTED".equalsIgnoreCase(r.getStatus())).count();
        long completed = returns.stream().filter(r -> "COMPLETED".equalsIgnoreCase(r.getStatus())).count();

        request.setAttribute("returnOrders", returns);
        request.setAttribute("total", returns.size());
        request.setAttribute("countPending", pending);
        request.setAttribute("countApproved", approved);
        request.setAttribute("countRejected", rejected);
        request.setAttribute("countCompleted", completed);
        request.getRequestDispatcher("/return-order-admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        int id = parseInt(request.getParameter("id"), 0);
        String action = request.getParameter("action");
        String adminNote = request.getParameter("adminNote");

        Integer adminId = null;
        Object sessionUser = request.getSession().getAttribute("userlogin");
        if (sessionUser instanceof User user) {
            adminId = user.getId();
        }

        String result;
        try {
            result = switch (action == null ? "" : action) {
                case "approve" -> returnOrderService.approve(id, adminId, adminNote);
                case "reject" -> returnOrderService.reject(id, adminId, adminNote);
                case "complete" -> returnOrderService.completeReturn(id, adminId, adminNote);
                default -> "INVALID_STATUS";
            };
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/return-order-admin?mode=detail&id=" + id
                    + "&error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
            return;
        }

        boolean ok = "SUCCESS".equals(result) || "SUCCESS_IMPORTED".equals(result);
        String key = ok ? "success" : "error";
        response.sendRedirect(request.getContextPath() + "/return-order-admin?mode=detail&id=" + id
                + "&" + key + "=" + URLEncoder.encode(returnOrderService.getMessage(result), StandardCharsets.UTF_8));
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
