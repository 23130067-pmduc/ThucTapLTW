package vn.edu.nlu.fit.thuctapltw.Controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.EmailService;
import vn.edu.nlu.fit.thuctapltw.Service.OrderService;

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

        if (mode == null) {
            String keyword = req.getParameter("keyword");
            String status = req.getParameter("status");

            if (keyword != null) keyword = keyword.trim();
            if (status != null) status = status.trim();

            var orders = ((keyword != null && !keyword.isEmpty()) ||
                    (status != null && !status.isEmpty()))
                    ? orderService.searchOrders(keyword, status)
                    : orderService.getAllOrders();

            long pending = orders.stream().filter(o -> "PENDING".equals(o.getOrderStatus())).count();
            long shipping = orders.stream().filter(o -> "SHIPPING".equals(o.getOrderStatus())).count();
            long completed = orders.stream().filter(o -> "COMPLETED".equals(o.getOrderStatus())).count();
            long cancelled = orders.stream().filter(o -> "CANCELLED".equals(o.getOrderStatus())).count();

            req.setAttribute("orders", orders);
            req.setAttribute("total", orders.size());
            req.setAttribute("countPending", pending);
            req.setAttribute("countShipping", shipping);
            req.setAttribute("countCompleted", completed);
            req.setAttribute("countCancelled", cancelled);

            req.getRequestDispatcher("/orderAdmin.jsp").forward(req, resp);
            return;
        }

        if ("view".equals(mode)) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("order", orderService.findById(id));
            req.setAttribute("items", orderService.getOrderItems(id));
            req.getRequestDispatcher("/order-detail.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String action = req.getParameter("action");
        if (!"update".equals(action)) return;

        int id = Integer.parseInt(req.getParameter("id"));
        String newStatus = req.getParameter("orderStatus");

        var order = orderService.findById(id);
        if (order == null) {
            resp.sendRedirect(req.getContextPath() + "/order-admin");
            return;
        }

        String currentStatus = order.getOrderStatus();

        if ("COMPLETED".equals(currentStatus) || "CANCELLED".equals(currentStatus)) {
            resp.sendRedirect(req.getContextPath() + "/order-admin?mode=view&id=" + id);
            return;
        }

        if ("PENDING".equals(currentStatus) && "COMPLETED".equals(newStatus)) {
            resp.sendRedirect(req.getContextPath() + "/order-admin?mode=view&id=" + id);
            return;
        }

        orderService.updateStatus(id, newStatus);

        String userEmail = orderService.getUserEmailByOrderId(id);

        String statusInVietnamese = switch (newStatus) {
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

        resp.sendRedirect(req.getContextPath() + "/order-admin?mode=view&id=" + id);
    }
}
