package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.SearchHistoryService;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;

@WebServlet(name = "DelsearchHistoryController", value = "/clear-search-history")
public class DelsearchHistoryController extends HttpServlet {
    private final SearchHistoryService searchHistoryService = new SearchHistoryService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userlogin");

        searchHistoryService.clearHistory(session, user);

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/trang-chu");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}