package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.DashboardService;
import vn.edu.nlu.fit.thuctapltw.Util.MapJsonUtil;

import java.io.IOException;
import java.util.Map;

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
        request.setAttribute("topSellingProducts", service.topSellingProducts(5));

        request.setAttribute("pendingOrders",   service.countOrdersByStatus("PENDING"));
        request.setAttribute("shippingOrders",  service.countOrdersByStatus("SHIPPING"));
        request.setAttribute("completedOrders", service.countOrdersByStatus("COMPLETED"));
        request.setAttribute("cancelledOrders", service.countOrdersByStatus("CANCELLED"));

        request.setAttribute("pendingReturnOrders", service.countPendingReturnOrders());
        request.setAttribute("newContacts", service.countNewContacts());
        request.setAttribute("lowStockProducts", service.countLowStockProducts());

        Map<String, Double> rev7day    = service.getRevenueByDay(7);
        Map<String, Double> rev30day   = service.getRevenueByDay(30);
        Map<String, Double> rev12month = service.getRevenueByMonth(12);

        request.setAttribute("chartLabels7day",    MapJsonUtil.toJsonLabels(rev7day));
        request.setAttribute("chartValues7day",    MapJsonUtil.toJsonValues(rev7day));
        request.setAttribute("chartLabels30day",   MapJsonUtil.toJsonLabels(rev30day));
        request.setAttribute("chartValues30day",   MapJsonUtil.toJsonValues(rev30day));
        request.setAttribute("chartLabels12month", MapJsonUtil.toJsonLabels(rev12month));
        request.setAttribute("chartValues12month", MapJsonUtil.toJsonValues(rev12month));

        request.getRequestDispatcher("/Dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}