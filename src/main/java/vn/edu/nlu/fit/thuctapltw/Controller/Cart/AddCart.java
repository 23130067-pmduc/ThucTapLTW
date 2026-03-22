package vn.edu.nlu.fit.thuctapltw.Controller.Cart;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.DAO.CartItemDao;
import vn.edu.nlu.fit.thuctapltw.DAO.ProductVariantDao;
import vn.edu.nlu.fit.thuctapltw.model.ProductVariant;

@WebServlet(name = "AddCart", value = "/add-cart")
public class AddCart extends HttpServlet {
    private CartItemDao cartItemDao;
    private ProductVariantDao variantDao;

    @Override
    public void init() {
        cartItemDao = new CartItemDao();
        variantDao  = new ProductVariantDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("san-pham");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("cartId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer cartId = null;
        Integer variantId = null;

        try {
            cartId = (Integer) session.getAttribute("cartId");
            if (cartId == null) {
                System.out.println("[AddCart] ERROR: cartId is null in session");
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            String variantParam = request.getParameter("variantId");
            String productParam = request.getParameter("productId");

            System.out.println("[AddCart] Request params - variantId: " + variantParam + ", productId: " + productParam);

            if (variantParam != null && !variantParam.trim().isEmpty()) {
                variantId = Integer.parseInt(variantParam);
                System.out.println("[AddCart] Using variantId: " + variantId);
            } else if (productParam != null && !productParam.trim().isEmpty()) {
                int productId = Integer.parseInt(productParam);
                System.out.println("[AddCart] Getting first variant for productId: " + productId);

                try {
                    ProductVariant defaultVariant = variantDao.getFirstVariantByProductId(productId);
                    if (defaultVariant == null) {
                        System.out.println("[AddCart] ERROR: No variant found for productId: " + productId);
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sản phẩm này không có biến thể nào");
                        return;
                    }
                    variantId = defaultVariant.getId();
                    System.out.println("[AddCart] Using variantId: " + variantId + " from productId: " + productId);
                } catch (Exception e) {
                    System.out.println("[AddCart] ERROR getting variant: " + e.getMessage());
                    e.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi lấy dữ liệu biến thể: " + e.getMessage());
                    return;
                }
            } else {
                System.out.println("[AddCart] ERROR: Missing both variantId and productId");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số variantId hoặc productId");
                return;
            }

            if (variantId == null || variantId <= 0) {
                System.out.println("[AddCart] ERROR: Invalid variantId: " + variantId);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID biến thể không hợp lệ");
                return;
            }

            int quantity = 1;
            String qtyParam = request.getParameter("quantity");
            if (qtyParam != null && !qtyParam.trim().isEmpty()) {
                try {
                    quantity = Integer.parseInt(qtyParam);
                } catch (NumberFormatException e) {
                    System.out.println("[AddCart] Invalid quantity: " + qtyParam);
                    quantity = 1;
                }
            }
            if (quantity <= 0) quantity = 1;

            System.out.println("[AddCart] Parameters - cartId: " + cartId + ", variantId: " + variantId + ", quantity: " + quantity);

            double price;
            try {
                price = variantDao.getPriceByVariantId(variantId);
                System.out.println("[AddCart] Price for variantId " + variantId + ": " + price);
            } catch (Exception e) {
                System.out.println("[AddCart] ERROR getting price: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi lấy giá sản phẩm: " + e.getMessage());
                return;
            }

            int productId;
            try {
                productId = variantDao.getProductIdByVariantId(variantId);
                System.out.println("[AddCart] ProductId for variantId " + variantId + ": " + productId);
            } catch (Exception e) {
                System.out.println("[AddCart] ERROR getting productId: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi lấy ID sản phẩm: " + e.getMessage());
                return;
            }

            try {
                cartItemDao.addOrUpdate(cartId, variantId, productId, quantity, price);
                System.out.println("[AddCart] Successfully added to cart");
            } catch (Exception e) {
                System.out.println("[AddCart] ERROR adding to cart: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi thêm vào giỏ hàng: " + e.getMessage());
                return;
            }

            try {
                int totalQty = cartItemDao.countTotalQuantity(cartId);
                session.setAttribute("cartSize", totalQty);
                System.out.println("[AddCart] Cart size: " + totalQty);
            } catch (Exception e) {
                System.out.println("[AddCart] WARNING: Error getting cart size: " + e.getMessage());
                e.printStackTrace();
            }

            String ajaxHeader = request.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(ajaxHeader)) {
                System.out.println("[AddCart] Responding with JSON (AJAX request)");
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                int totalQty = cartItemDao.countTotalQuantity(cartId);
                response.getWriter().write(
                        "{\"success\":true,\"totalQuantity\":" + totalQty + "}"
                );
                return;
            }

            String referer = request.getHeader("Referer");
            String redirectUrl = (referer != null && !referer.isEmpty()) ? referer : request.getContextPath() + "/san-pham";
            System.out.println("[AddCart] Redirecting to: " + redirectUrl);
            response.sendRedirect(redirectUrl);


        } catch (NumberFormatException e) {
            System.out.println("[AddCart] NumberFormatException: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tham số không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[AddCart] Unexpected Exception: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi thêm vào giỏ hàng: " + e.getMessage());
        }
    }
}