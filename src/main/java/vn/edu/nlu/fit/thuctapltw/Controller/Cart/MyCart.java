package vn.edu.nlu.fit.thuctapltw.Controller.Cart;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.DAO.CartItemDao;
import vn.edu.nlu.fit.thuctapltw.model.CartItem;
import java.util.List;

import java.io.IOException;

@WebServlet(name = "MyCart", value = "/my-cart")
public class MyCart extends HttpServlet {

    private CartItemDao cartItemDao;

    @Override
    public void init() {
        cartItemDao = new CartItemDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("cartId") == null) {
            response.sendRedirect("login");
            return;
        }

        Integer cartId = (Integer) session.getAttribute("cartId");

        List<CartItem> items = cartItemDao.getItemsByCartId(cartId);
        request.setAttribute("cartItems", items);

        request.getRequestDispatcher("giohang.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}