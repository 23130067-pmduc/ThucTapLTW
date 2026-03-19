package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.DashboardService;

import java.io.IOException;

@WebServlet(name = "DashboardAdminController", value = "/dashboard")
public class DashboardAdminController extends HttpServlet {
    private DashboardService service;

    @Override
    public void init() {
        service = new DashboardService();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("totalOrders", service.countOrders());
        request.setAttribute("totalRevenue", service.totalRevenue());
        request.setAttribute("totalProducts", service.countProducts());
        request.setAttribute("totalUsers", service.countUsers());

        request.setAttribute("latestOrders", service.latestOrders(5));

        request.getRequestDispatcher("/Dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}