package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.DashboardService;
import vn.edu.nlu.fit.thuctapltw.Util.MapJsonUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

        LocalDate fromDate = parseSelectedDate(request.getParameter("fromDate"), LocalDate.now().withDayOfMonth(1));
        LocalDate toDate = parseSelectedDate(request.getParameter("toDate"), LocalDate.now());
        if (fromDate.isAfter(toDate)) {
            LocalDate temp = fromDate;
            fromDate = toDate;
            toDate = temp;
        }
        Map<String, Double> revenueByDate = service.getRevenueByDateRange(fromDate, toDate);

        request.setAttribute("fromDate", fromDate.toString());
        request.setAttribute("toDate", toDate.toString());
        request.setAttribute("chartLabelsDate", MapJsonUtil.toJsonLabels(revenueByDate));
        request.setAttribute("chartValuesDate", MapJsonUtil.toJsonValues(revenueByDate));

        request.getRequestDispatcher("/Dashboard.jsp").forward(request, response);
    }

    private LocalDate parseSelectedDate(String date, LocalDate defaultDate) {
        if (date == null || date.isBlank()) {
            return defaultDate;
        }

        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return defaultDate;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
