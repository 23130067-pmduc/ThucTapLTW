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
import vn.edu.nlu.fit.thuctapltw.Service.AddressService;
import vn.edu.nlu.fit.thuctapltw.Service.EmailService;
import vn.edu.nlu.fit.thuctapltw.Service.GhnShippingService;
import vn.edu.nlu.fit.thuctapltw.Service.OrderEmailService;
import vn.edu.nlu.fit.thuctapltw.model.Address;
import vn.edu.nlu.fit.thuctapltw.model.CartItem;
import vn.edu.nlu.fit.thuctapltw.model.Order;
import vn.edu.nlu.fit.thuctapltw.model.OrderItem;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CheckoutController", value = "/checkout")
public class CheckoutController extends HttpServlet {
    private CartItemDao cartItemDao;
    private AddressService addressService;
    private OrderDao orderDao;
    private OrderItemDao orderItemDao;
    private ProductVariantDao variantDao;
    private GhnShippingService ghnShippingService;

    @Override
    public void init() {
        cartItemDao = new CartItemDao();
        addressService = new AddressService();
        orderDao = new OrderDao();
        orderItemDao = new OrderItemDao();
        variantDao = new ProductVariantDao();
        ghnShippingService = new GhnShippingService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userlogin") == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("userlogin");

        Integer cartIdObj = (Integer) session.getAttribute("cartId");
        if (cartIdObj == null) {
            response.sendRedirect("my-cart");
            return;
        }
        int cartId = cartIdObj;

        List<CartItem> items = cartItemDao.getByCartId(cartId);
        if (items.isEmpty()) {
            response.sendRedirect("my-cart");
            return;
        }

        List<Address> addresses = addressService.getAddressesByUser(user.getId());
        request.setAttribute("addresses", addresses);
        request.setAttribute("checkoutItems", items);
        request.getRequestDispatcher("thanhtoan.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userlogin") == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("userlogin");

        Integer cartIdObj = (Integer) session.getAttribute("cartId");
        if (cartIdObj == null) {
            response.sendRedirect("my-cart");
            return;
        }
        int cartId = cartIdObj;

        String selectedAddressRaw = request.getParameter("selectedAddress");
        Integer selectedAddressId = parseInteger(selectedAddressRaw);

        Address selectedAddress = null;
        if (selectedAddressId != null) {
            selectedAddress = addressService.getAddressById(selectedAddressId, user.getId());
        }
        if (selectedAddress == null) {
            List<Address> addresses = addressService.getAddressesByUser(user.getId());
            if (!addresses.isEmpty()) {
                selectedAddress = addresses.get(0);
            }
        }

        if (selectedAddress == null) {
            response.sendRedirect("checkout?error=missing_info");
            return;
        }

        String receiverName = selectedAddress.getReceiverName();
        String phone = selectedAddress.getPhone();
        String address = selectedAddress.getFullAddress();
        String note = request.getParameter("note");
        String paymentMethod = request.getParameter("paymentMethod");

        List<CartItem> cartItems = cartItemDao.getByCartId(cartId);
        if (cartItems.isEmpty()) {
            response.sendRedirect("my-cart");
            return;
        }

        for (CartItem item : cartItems) {
            int stock = variantDao.getStockByVariantId(item.getVariantId());
            if (stock < item.getQuantity()) {
                response.sendRedirect("checkout?error=out_of_stock");
                return;
            }
        }

        double totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        GhnShippingService.ShippingQuote shippingQuote = ghnShippingService.calculateFee(selectedAddress);
        if (!shippingQuote.success() && ghnShippingService.isConfigured()) {
            response.sendRedirect("checkout?error=shipping_unavailable");
            return;
        }
        double shippingFee = shippingQuote.success() ? shippingQuote.fee() : 0;

        int orderId = orderDao.createOrder(
                user.getId(),
                receiverName,
                phone,
                address,
                note,
                paymentMethod,
                totalPrice,
                shippingFee
        );

        for (CartItem item : cartItems) {
            int variantId = item.getVariantId();
            int qty = item.getQuantity();
            double price = item.getPrice();

            orderItemDao.insert(orderId, variantId, item.getProduct().getName(), item.getSize(), item.getColor(), qty, price, price * qty);
            variantDao.decreaseStock(variantId, qty);
        }

        cartItemDao.clearCart(cartId);

        session.setAttribute("cartSize", 0);
        session.setAttribute("lastOrderId", orderId);

        Order order = orderDao.getById(orderId);
        List<OrderItem> orderItems = orderItemDao.getByOrderId(orderId);

        String emailContent = OrderEmailService.build(order, orderItems);
        EmailService.sendEmail(user.getEmail(), "Đã xác nhận đơn hàng #" + orderId + " - SunnyBear", emailContent);

        response.sendRedirect("paysuccess");
    }

    private Integer parseInteger(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
