package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.DAO.CartItemDao;
import vn.edu.nlu.fit.thuctapltw.DAO.OrderDao;
import vn.edu.nlu.fit.thuctapltw.DAO.OrderItemDao;
import vn.edu.nlu.fit.thuctapltw.DAO.ProductVariantDao;
import vn.edu.nlu.fit.thuctapltw.Service.EmailService;
import vn.edu.nlu.fit.thuctapltw.Service.OrderEmailService;
import vn.edu.nlu.fit.thuctapltw.Util.VNPayUtil;
import vn.edu.nlu.fit.thuctapltw.model.Order;
import vn.edu.nlu.fit.thuctapltw.model.OrderItem;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "VNPayReturnController", value = "/vnpay-return")
public class VNPayReturnController extends HttpServlet {

    private OrderDao orderDao;
    private OrderItemDao orderItemDao;
    private ProductVariantDao variantDao;
    private CartItemDao cartItemDao;

    @Override
    public void init() {
        orderDao = new OrderDao();
        orderItemDao = new OrderItemDao();
        variantDao = new ProductVariantDao();
        cartItemDao = new CartItemDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String[]> params = request.getParameterMap();

        if (!VNPayUtil.verifyReturnUrl(params)) {
            response.sendRedirect(request.getContextPath() + "/checkout?error=invalid_signature");
            return;
        }

        String responseCode = VNPayUtil.getSingleParam(params, "vnp_ResponseCode");
        String txnRef = VNPayUtil.getSingleParam(params, "vnp_TxnRef");
        Integer orderId = VNPayUtil.extractOrderIdFromTxnRef(txnRef);

        if (orderId == null) {
            response.sendRedirect(request.getContextPath() + "/checkout?error=payment_failed");
            return;
        }

        if (!"00".equals(responseCode)) {
            response.sendRedirect(request.getContextPath() + "/checkout?error=payment_failed");
            return;
        }

        Order order = orderDao.getById(orderId);
        if (order != null && "PAID".equalsIgnoreCase(order.getPaymentStatuses())) {
            HttpSession session = request.getSession(true);
            session.setAttribute("lastOrderId", orderId);
            response.sendRedirect(request.getContextPath() + "/paysuccess");
            return;
        }

        orderDao.updateStatus(orderId, "PAID");

        try {
            List<OrderItem> orderItems = orderItemDao.getByOrderId(orderId);
            for (OrderItem item : orderItems) {
                variantDao.decreaseStock(item.getVariantId(), item.getQuantity());
            }

            HttpSession session = request.getSession(true);
            Integer cartId = (Integer) session.getAttribute("cartId");
            if (cartId != null) {
                cartItemDao.clearCart(cartId);
                session.setAttribute("cartSize", 0);
            }

            if (order != null) {
                String userEmail = orderDao.getUserEmailByOrderId(orderId);
                if (userEmail != null) {
                    String emailContent = OrderEmailService.build(order, orderItems);
                    EmailService.sendEmail(
                            userEmail,
                            "Da xac nhan don hang #" + orderId + " - SunnyBear",
                            emailContent
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("[VNPayReturn] Loi gui email xac nhan: " + e.getMessage());
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("lastOrderId", orderId);

        response.sendRedirect(request.getContextPath() + "/paysuccess");
    }



@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}