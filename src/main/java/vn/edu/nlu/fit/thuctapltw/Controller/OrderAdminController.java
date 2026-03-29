package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.EmailService;
import vn.edu.nlu.fit.thuctapltw.Service.OrderService;

import java.io.IOException;

@WebServlet(name = "OrderAdminController", value = "/order-admin")
public class OrderAdminController extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String mode = req.getParameter("mode");

        if (mode == null || mode.isBlank()) {
            String keyword = trim(req.getParameter("keyword"));
            String status = trim(req.getParameter("status"));

            var orders = ((!isBlank(keyword)) || (!isBlank(status)))
                    ? orderService.searchOrders(keyword, status)
                    : orderService.getAllOrders();

            long pending = orders.stream().filter(o -> "PENDING".equalsIgnoreCase(o.getOrderStatus())).count();
            long shipping = orders.stream().filter(o -> "SHIPPING".equalsIgnoreCase(o.getOrderStatus())).count();
            long completed = orders.stream().filter(o -> "COMPLETED".equalsIgnoreCase(o.getOrderStatus())).count();
            long cancelled = orders.stream().filter(o -> "CANCELLED".equalsIgnoreCase(o.getOrderStatus())).count();

            req.setAttribute("orders", orders);
            req.setAttribute("total", orders.size());
            req.setAttribute("countPending", pending);
            req.setAttribute("countShipping", shipping);
            req.setAttribute("countCompleted", completed);
            req.setAttribute("countCancelled", cancelled);

            req.getRequestDispatcher("/orderAdmin.jsp").forward(req, resp);
            return;
        }

        int id = Integer.parseInt(req.getParameter("id"));
        var order = orderService.findById(id);
        var items = orderService.getOrderItems(id);

        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/order-admin");
            return;
        }

        req.setAttribute("order", order);
        req.setAttribute("items", items);
        req.setAttribute("success", req.getParameter("success"));
        req.setAttribute("error", req.getParameter("error"));

        if ("view".equals(mode)) {
            req.getRequestDispatcher("/order-detail.jsp").forward(req, resp);
            return;
        }

        if ("edit".equals(mode)) {
            req.getRequestDispatcher("/order-edit.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/order-admin");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        if (!"update".equals(action)) {
            resp.sendRedirect(req.getContextPath() + "/order-admin");
            return;
        }

        int id = Integer.parseInt(req.getParameter("id"));
        String newStatus = req.getParameter("orderStatus");

        var order = orderService.findById(id);
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/order-admin");
            return;
        }

        String currentStatus = order.getOrderStatus();

        if ("COMPLETED".equalsIgnoreCase(currentStatus) || "CANCELLED".equalsIgnoreCase(currentStatus)) {
            resp.sendRedirect(req.getContextPath() + "/order-admin?mode=view&id=" + id + "&error=Đơn hàng này không thể cập nhật nữa");
            return;
        }

        if ("PENDING".equalsIgnoreCase(currentStatus) && "COMPLETED".equalsIgnoreCase(newStatus)) {
            resp.sendRedirect(req.getContextPath() + "/order-admin?mode=edit&id=" + id + "&error=Không thể chuyển trực tiếp từ chờ xử lý sang hoàn thành");
            return;
        }

        orderService.updateStatus(id, newStatus);

        String userEmail = orderService.getUserEmailByOrderId(id);

        String statusInVietnamese = switch (newStatus.toUpperCase()) {
            case "PENDING" -> "Chờ xử lý";
            case "SHIPPING" -> "Đang giao";
            case "COMPLETED" -> "Hoàn thành";
            case "CANCELLED" -> "Đã hủy";
            default -> newStatus;
        };

        String subject = "Cập nhật trạng thái đơn hàng #" + id;
        String html = """
                <div style="font-family:Arial,sans-serif;font-size:14px;color:#333">
                    <p>Xin chào,</p>
                    <p>Đơn hàng <strong>#%d</strong> của bạn vừa được cập nhật trạng thái thành:
                    <strong style="color:#0b74de;">%s</strong>.</p>
                    <p>Cảm ơn bạn đã mua sắm tại SunnyBear!</p>
                </div>
                """.formatted(id, statusInVietnamese);

        if (userEmail != null && !userEmail.isBlank()) {
            EmailService.sendEmail(userEmail, subject, html);
        }

        resp.sendRedirect(req.getContextPath() + "/order-admin?mode=view&id=" + id + "&success=Cập nhật trạng thái thành công");
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}