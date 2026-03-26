package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.DAO.CartItemDao;
import vn.edu.nlu.fit.thuctapltw.model.CartItem;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CheckoutController", value = "/checkout")
public class CheckoutController extends HttpServlet {
    private CartItemDao cartItemDao;

    @Override
    public void init() {
        cartItemDao = new CartItemDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("userlogin");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        int cartId = (int) session.getAttribute("cartId");

        List<CartItem> items = cartItemDao.getByCartId(cartId);
        if (items.isEmpty()) {
            response.sendRedirect("my-cart");
            return;
        }

        request.setAttribute("checkoutItems", items);
        request.getRequestDispatcher("thanhtoan.jsp").forward(request, response);
    }
}