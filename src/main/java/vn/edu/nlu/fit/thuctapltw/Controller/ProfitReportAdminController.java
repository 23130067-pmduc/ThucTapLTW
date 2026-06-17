package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.ProfitReportService;
import vn.edu.nlu.fit.thuctapltw.model.ProfitDailyReport;
import vn.edu.nlu.fit.thuctapltw.model.ProfitProductReport;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

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
        int selectedProductId = parseInt(request.getParameter("productId"), 0);

        if (fromDate.isAfter(toDate)) {
            LocalDate temp = fromDate;
            fromDate = toDate;
            toDate = temp;
        }

        List<ProfitDailyReport> dailyReports = profitReportService.getDailyReports(fromDate, toDate);
        List<ProfitProductReport> productReports = profitReportService.getProductReports(fromDate, toDate, selectedProductId);
        List<ProfitProductReport> soldProductReports = profitReportService.getSoldProductReports(fromDate, toDate);
        List<ProfitProductReport> unsoldProductReports = profitReportService.getUnsoldProductReports(fromDate, toDate);

        request.setAttribute("fromDate", fromDate.toString());
        request.setAttribute("toDate", toDate.toString());
        request.setAttribute("selectedProductId", selectedProductId);
        request.setAttribute("summary", profitReportService.getSummary(fromDate, toDate));
        request.setAttribute("dailyReports", dailyReports);
        request.setAttribute("productReports", productReports);
        request.setAttribute("soldProductReports", soldProductReports);
        request.setAttribute("unsoldProductReports", unsoldProductReports);
        request.setAttribute("productOptions", profitReportService.getProductOptions());

        request.setAttribute("dailyLabelsJson", toJsonLabels(dailyReports));
        request.setAttribute("dailyRevenueJson", toJsonDailyValues(dailyReports, "revenue"));
        request.setAttribute("dailyImportCostJson", toJsonDailyValues(dailyReports, "importCost"));
        request.setAttribute("dailyCostJson", toJsonDailyValues(dailyReports, "costOfGoods"));
        request.setAttribute("topSoldLabelsJson", toJsonProductLabels(soldProductReports));
        request.setAttribute("topSoldValuesJson", toJsonSoldValues(soldProductReports));

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

    private int parseInt(String value, int defaultValue) {
        if (value == null || value.isBlank()) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private String toJsonLabels(List<ProfitDailyReport> reports) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = reports.size() - 1; i >= 0; i--) {
            sb.append('"').append(escapeJson(reports.get(i).getReportDate())).append('"');
            if (i > 0) sb.append(',');
        }
        sb.append(']');
        return sb.toString();
    }

    private String toJsonDailyValues(List<ProfitDailyReport> reports, String field) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = reports.size() - 1; i >= 0; i--) {
            ProfitDailyReport report = reports.get(i);
            BigDecimal value = switch (field) {
                case "importCost" -> report.getImportCost();
                case "costOfGoods" -> report.getCostOfGoods();
                default -> report.getRevenue();
            };
            sb.append(value == null ? 0 : value.doubleValue());
            if (i > 0) sb.append(',');
        }
        sb.append(']');
        return sb.toString();
    }

    private String toJsonProductLabels(List<ProfitProductReport> reports) {
        StringBuilder sb = new StringBuilder("[");
        int count = Math.min(reports.size(), 8);
        for (int i = 0; i < count; i++) {
            ProfitProductReport report = reports.get(i);
            sb.append('"').append(escapeJson(shorten(report.getProductName(), 22))).append('"');
            if (i < count - 1) sb.append(',');
        }
        sb.append(']');
        return sb.toString();
    }

    private String toJsonSoldValues(List<ProfitProductReport> reports) {
        StringBuilder sb = new StringBuilder("[");
        int count = Math.min(reports.size(), 8);
        for (int i = 0; i < count; i++) {
            sb.append(reports.get(i).getSoldQuantity());
            if (i < count - 1) sb.append(',');
        }
        sb.append(']');
        return sb.toString();
    }

    private String shorten(String value, int maxLength) {
        if (value == null) return "";
        if (value.length() <= maxLength) return value;
        return value.substring(0, Math.max(0, maxLength - 3)) + "...";
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
