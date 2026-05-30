package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.DAO.OrderDao;
import vn.edu.nlu.fit.thuctapltw.model.Order;
import vn.edu.nlu.fit.thuctapltw.model.OrderItem;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderDetailController", value = "/chitietorder")
public class OrderDetailController extends HttpServlet {

    private OrderDao orderDao;

    @Override
    public void init() {
        orderDao = new OrderDao();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userlogin") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("userlogin");

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/don-mua");
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/don-mua");
            return;
        }

        Order order = orderDao.findByIdAndUserId(orderId, user.getId());
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/don-mua");
            return;
        }

        List<OrderItem> items = orderDao.getItems(orderId);

        request.setAttribute("order", order);
        request.setAttribute("items", items);
        request.setAttribute("pageCss", "chitietorder.css");
        request.setAttribute("pageTitle", "Chi tiết đơn hàng #" + orderId);

        request.getRequestDispatcher("/chitietorder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}