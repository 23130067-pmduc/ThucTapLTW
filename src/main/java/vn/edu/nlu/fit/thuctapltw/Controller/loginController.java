package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.DAO.CartDao;
import vn.edu.nlu.fit.thuctapltw.DAO.CartItemDao;
import vn.edu.nlu.fit.thuctapltw.Service.UserService;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;

@WebServlet(name = "logoutController", value = "/login")
public class loginController extends HttpServlet {
    private UserService userService;
    private CartDao cartDao;

    @Override
    public void init() {
        userService = new UserService();
        cartDao = new CartDao();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null
                || username.trim().isEmpty()
                || password.trim().isEmpty()) {

            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        User user = userService.login(username, password);

        if (user == null) {
            request.setAttribute("error", "Tài khoản hoặc mật khẩu không đúng");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (user.getStatus() == null) {
            request.setAttribute("error", "Tài khoản chưa được kích hoạt");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if ("BLOCKED".equalsIgnoreCase(user.getStatus())) {
            request.setAttribute("error", "Tài khoản đã bị khóa");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (user.getIsActive() == 0) {
            request.setAttribute("error", "Tài khoản chưa xác nhận OTP. Vui lòng kiểm tra email.");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userlogin", user);

        Integer cartId = cartDao.findCartIdByUser(user.getId());
        if (cartId == null) {cartId = cartDao.createCart(user.getId());}
        session.setAttribute("cartId", cartId);
        int cartSize = new CartItemDao().countTotalQuantity(cartId);
        session.setAttribute("cartSize", cartSize);

        if ("admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/trang-chu");
        }
    }
}