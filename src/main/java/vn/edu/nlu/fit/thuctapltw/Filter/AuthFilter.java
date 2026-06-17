package vn.edu.nlu.fit.thuctapltw.Filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;

@WebFilter(urlPatterns = {
        "/dashboard",
        "/product-admin",
        "/inventory-admin",
        "/supplier-admin",
        "/profit-report",
        "/category-admin",
        "/order-admin",
        "/user-admin",
        "/permission-admin",
        "/banner-admin",
        "/voucher-admin",
        "/promotion-event-admin",
        "/news-admin",
        "/notification-admin",
        "/contact-admin",
        "/admin-profile",
        "/return-order-admin"
})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);

        User user = null;
        if (session != null) {
            user = (User) session.getAttribute("userlogin");
        }

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        chain.doFilter(req, res);
    }
}