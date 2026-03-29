package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.Service.OrderService;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/mua-lai-don")
public class RebuyOrderController extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = new OrderService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userlogin") == null || session.getAttribute("cartId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("userlogin");
        int cartId = (Integer) session.getAttribute("cartId");
        String currentStatus = normalizeStatus(request.getParameter("currentStatus"));

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String message = orderService.rebuyOrder(orderId, user.getId(), cartId);
            session.setAttribute("cartSize", orderService.getCartSize(cartId));

            response.sendRedirect(request.getContextPath() + "/my-cart?success="
                    + URLEncoder.encode(message, StandardCharsets.UTF_8));
        } catch (NumberFormatException e) {
            redirectBack(response, request.getContextPath(), currentStatus, "Mã đơn hàng không hợp lệ");
        } catch (RuntimeException e) {
            redirectBack(response, request.getContextPath(), currentStatus, e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/don-mua");
    }

    private void redirectBack(HttpServletResponse response, String contextPath, String currentStatus, String errorMessage)
            throws IOException {
        response.sendRedirect(contextPath + "/don-mua?status=" + currentStatus + "&error="
                + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank() || "all".equalsIgnoreCase(status)) {
            return "all";
        }
        return status.trim().toUpperCase();
    }
}
