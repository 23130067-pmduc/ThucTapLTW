package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.Service.OrderService;
import vn.edu.nlu.fit.thuctapltw.model.Order;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/don-mua")
public class MyOrderController extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedInUser(request, response);
        if (user == null) {
            return;
        }

        String status = normalizeStatus(request.getParameter("status"));
        List<Order> orders = orderService.getOrdersByUser(user.getId(), status);

        request.setAttribute("orders", orders);
        request.setAttribute("currentStatus", status);
        request.setAttribute("pageCss", "donmua.css");
        request.setAttribute("pageTitle", "Đơn hàng của tôi");

        String success = request.getParameter("success");
        if (success != null && !success.isBlank()) {
            request.setAttribute("success", success);
        }

        String error = request.getParameter("error");
        if (error != null && !error.isBlank()) {
            request.setAttribute("error", error);
        }

        request.getRequestDispatcher("/donmua.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedInUser(request, response);
        if (user == null) {
            return;
        }

        String action = request.getParameter("action");
        String currentStatus = normalizeStatus(request.getParameter("currentStatus"));

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            switch (action == null ? "" : action) {
                case "cancel" -> {
                    orderService.cancelOrder(orderId, user.getId());
                    redirectWithMessage(response, request.getContextPath() + "/don-mua?status=" + currentStatus,
                            "success", "Huỷ đơn hàng thành công");
                }
                case "received" -> {
                    orderService.confirmReceived(orderId, user.getId());
                    redirectWithMessage(response, request.getContextPath() + "/don-mua?status=COMPLETED",
                            "success", "Đã xác nhận nhận hàng");
                }
                default -> redirectWithMessage(response, request.getContextPath() + "/don-mua?status=" + currentStatus,
                        "error", "Thao tác không hợp lệ");
            }
        } catch (NumberFormatException e) {
            redirectWithMessage(response, request.getContextPath() + "/don-mua?status=" + currentStatus,
                    "error", "Mã đơn hàng không hợp lệ");
        } catch (RuntimeException e) {
            redirectWithMessage(response, request.getContextPath() + "/don-mua?status=" + currentStatus,
                    "error", e.getMessage());
        }
    }

    private User getLoggedInUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userlogin") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        return (User) session.getAttribute("userlogin");
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank() || "all".equalsIgnoreCase(status)) {
            return "all";
        }
        return status.trim().toUpperCase();
    }

    private void redirectWithMessage(HttpServletResponse response, String baseUrl, String key, String message)
            throws IOException {
        response.sendRedirect(baseUrl + "&" + key + "=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
    }
}
