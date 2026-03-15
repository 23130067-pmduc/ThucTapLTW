package vn.edu.nlu.fit.thuctapltw.Controller.Cart;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.DAO.CartItemDao;

import java.io.IOException;

@WebServlet(name = "RemoveItem", value = "/del-item")
public class RemoveItem extends HttpServlet {
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
        if (session == null || session.getAttribute("cartId") == null) {
            response.sendRedirect("login");
            return;
        }

        int cartId = (Integer) session.getAttribute("cartId");

        try {
            int variantId = Integer.parseInt(request.getParameter("variantId"));

            cartItemDao.delete(cartId, variantId);

            int cartSize = cartItemDao.countTotalQuantity(cartId);
            session.setAttribute("cartSize", cartSize);
            response.sendRedirect("my-cart");
        } catch (NumberFormatException e) {response.sendError(HttpServletResponse.SC_BAD_REQUEST);}
    }
}