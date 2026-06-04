package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.ProfitReportService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet(name = "ProfitReportAdminController", value = "/profit-report")
public class ProfitReportAdminController extends HttpServlet {
    private ProfitReportService profitReportService;

    @Override
    public void init() {
        profitReportService = new ProfitReportService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        LocalDate today = LocalDate.now();
        LocalDate defaultFromDate = today.minusDays(29);

        LocalDate fromDate = parseDate(request.getParameter("fromDate"), defaultFromDate);
        LocalDate toDate = parseDate(request.getParameter("toDate"), today);

        if (fromDate.isAfter(toDate)) {
            LocalDate temp = fromDate;
            fromDate = toDate;
            toDate = temp;
        }

        request.setAttribute("fromDate", fromDate.toString());
        request.setAttribute("toDate", toDate.toString());
        request.setAttribute("summary", profitReportService.getSummary(fromDate, toDate));
        request.setAttribute("dailyReports", profitReportService.getDailyReports(fromDate, toDate));
        request.setAttribute("productReports", profitReportService.getProductReports(fromDate, toDate));

        request.getRequestDispatcher("/profit-report.jsp").forward(request, response);
    }

    private LocalDate parseDate(String value, LocalDate defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            return defaultValue;
        }
    }
}
