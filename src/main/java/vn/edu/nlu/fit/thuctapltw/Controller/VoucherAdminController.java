package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.VoucherService;
import vn.edu.nlu.fit.thuctapltw.model.Voucher;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "VoucherAdminController", value = "/voucher-admin")
public class VoucherAdminController extends HttpServlet {
    private VoucherService voucherService;

    @Override
    public void init() {
        voucherService = new VoucherService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int pageSize = 20;
        int currentPage = parsePositiveInt(request.getParameter("page"), 1);

        int total = voucherService.countAdminVouchers(null, "ALL", "ALL");
        int totalPages = (int) Math.ceil((double) total / pageSize);
        if (totalPages == 0) totalPages = 1;
        if (currentPage > totalPages) currentPage = totalPages;

        List<Voucher> vouchers = voucherService.getAdminVouchers(null, "ALL", "ALL", currentPage, pageSize);

        request.setAttribute("vouchers", vouchers);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("total", total);
        request.setAttribute("totalAll", voucherService.countAllVouchers());
        request.setAttribute("totalActive", voucherService.countActiveVouchers());
        request.setAttribute("totalLocked", voucherService.countLockedVouchers());
        request.setAttribute("totalExpired", voucherService.countExpiredVouchers());

        request.getRequestDispatcher("/voucher-admin.jsp").forward(request, response);
    }

    private int parsePositiveInt(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) return defaultValue;
        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed < 1 ? defaultValue : parsed;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
