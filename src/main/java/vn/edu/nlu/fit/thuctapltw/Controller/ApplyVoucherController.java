package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.DAO.CartItemDao;
import vn.edu.nlu.fit.thuctapltw.Service.VoucherService;
import vn.edu.nlu.fit.thuctapltw.model.CartItem;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ApplyVoucherController", value = "/apply-voucher")
public class ApplyVoucherController extends HttpServlet {
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
            writeJson(response, false, "Vui lòng đăng nhập để sử dụng mã giảm giá.", 0, 0, 0, 0, "");
            return;
        }

        Integer cartId = (Integer) session.getAttribute("cartId");
        if (cartId == null) {
            writeJson(response, false, "Không tìm thấy giỏ hàng.", 0, 0, 0, 0, "");
            return;
        }

        String voucherCode = request.getParameter("voucherCode");
        double shippingFee = parseDouble(request.getParameter("shippingFee"));

        List<CartItem> cartItems = cartItemDao.getByCartId(cartId);
        VoucherService.ApplyResult result = voucherService.applyVoucher(voucherCode, cartItems, shippingFee);

        double subtotal = voucherService.calculateSubtotal(cartItems);
        double orderDiscount = result.isSuccess() ? result.getDiscountAmount() : 0;
        double shippingDiscount = result.isSuccess() ? result.getShippingDiscountAmount() : 0;
        double shippingAfterDiscount = Math.max(0, shippingFee - shippingDiscount);
        double finalAmount = Math.max(0, subtotal + shippingAfterDiscount - orderDiscount);
        String scope = result.isSuccess() && result.getVoucher() != null ? result.getVoucher().getVoucher_scope() : "";

        writeJson(response, result.isSuccess(), result.getMessage(), orderDiscount, shippingDiscount, shippingAfterDiscount, finalAmount, scope);
    }

    private void writeJson(HttpServletResponse response, boolean success, String message, double discount, double shippingDiscount,
                           double shippingFeeAfterDiscount, double finalAmount, String voucherScope) throws IOException {
        String json = "{" +
                "\"success\":" + success + "," +
                "\"message\":\"" + escapeJson(message) + "\"," +
                "\"discount\":" + Math.round(discount) + "," +
                "\"shippingDiscount\":" + Math.round(shippingDiscount) + "," +
                "\"totalDiscount\":" + Math.round(discount + shippingDiscount) + "," +
                "\"shippingFeeAfterDiscount\":" + Math.round(shippingFeeAfterDiscount) + "," +
                "\"finalAmount\":" + Math.round(finalAmount) + "," +
                "\"voucherScope\":\"" + escapeJson(voucherScope) + "\"" +
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
