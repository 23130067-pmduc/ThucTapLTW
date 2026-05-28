package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.fit.thuctapltw.DAO.CartItemDao;
import vn.edu.nlu.fit.thuctapltw.Service.VoucherService;
import vn.edu.nlu.fit.thuctapltw.model.CartItem;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ApplyShippingVoucherController", value = "/apply-shipping-voucher")
public class ApplyShippingVoucherController extends HttpServlet {
    private CartItemDao cartItemDao;
    private VoucherService voucherService;

    @Override
    public void init() {
        cartItemDao = new CartItemDao();
        voucherService = new VoucherService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userlogin") == null) {
            writeJson(response, false, "Vui lòng đăng nhập để sử dụng mã vận chuyển.", 0, 0);
            return;
        }

        Integer cartId = (Integer) session.getAttribute("cartId");
        if (cartId == null) {
            writeJson(response, false, "Không tìm thấy giỏ hàng.", 0, 0);
            return;
        }

        String shippingVoucherCode = request.getParameter("shippingVoucherCode");
        double shippingFee = parseDouble(request.getParameter("shippingFee"));
        double orderDiscount = parseDouble(request.getParameter("orderDiscount"));

        List<CartItem> cartItems = cartItemDao.getByCartId(cartId);
        double subtotal = voucherService.calculateSubtotal(cartItems);

        VoucherService.ApplyResult result = voucherService.applyShippingVoucher(shippingVoucherCode, subtotal, shippingFee);

        double shippingDiscount = result.isSuccess() ? result.getDiscountAmount() : 0;
        double finalAmount = Math.max(0, subtotal + shippingFee - orderDiscount - shippingDiscount);

        writeJson(response, result.isSuccess(), result.getMessage(), shippingDiscount, finalAmount);
    }

    private void writeJson(HttpServletResponse response, boolean success, String message, double shippingDiscount, double finalAmount) throws IOException {
        String json = "{" +
                "\"success\":" + success + "," +
                "\"message\":\"" + escapeJson(message) + "\"," +
                "\"shippingDiscount\":" + Math.round(shippingDiscount) + "," +
                "\"finalAmount\":" + Math.round(finalAmount) +
                "}";

        response.getWriter().write(json);
    }

    private double parseDouble(String raw) {
        if (raw == null || raw.isBlank()) return 0;

        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String escapeJson(String raw) {
        if (raw == null) return "";
        return raw.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}