package vn.edu.nlu.fit.thuctapltw.Filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebFilter(urlPatterns = {
        "/dashboard",
        "/product-admin",
        "/inventory-admin",
        "/profit-report",
        "/category-admin",
        "/order-admin",
        "/user-admin",
        "/banner-admin",
        "/news-admin",
        "/notification-admin",
        "/contact-admin",
        "/return-order-admin"
})
public class PermissionFilter implements Filter {

    private final Map<String, String> permissionMap = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) {
        permissionMap.put("/dashboard", "DASHBOARD_VIEW");
        permissionMap.put("/product-admin", "PRODUCT_VIEW");
        permissionMap.put("/inventory-admin", "WAREHOUSE_VIEW");
        permissionMap.put("/profit-report", "REPORT_VIEW");
        permissionMap.put("/category-admin", "CATEGORY_VIEW");
        permissionMap.put("/order-admin", "ORDER_VIEW");
        permissionMap.put("/user-admin", "USER_VIEW");
        permissionMap.put("/banner-admin", "BANNER_VIEW");
        permissionMap.put("/news-admin", "NEWS_VIEW");
        permissionMap.put("/notification-admin", "NOTIFICATION_VIEW");
        permissionMap.put("/contact-admin", "CONTACT_VIEW");
        permissionMap.put("/return-order-admin", "RETURN_RECEIPT_VIEW");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("userlogin");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String path = request.getServletPath();
        String requiredPermission = permissionMap.get(path);

        if (requiredPermission != null && !user.hasPermission(requiredPermission)) {
            response.sendRedirect(request.getContextPath() + "/403.jsp");
            return;
        }

        chain.doFilter(req, res);
    }
}