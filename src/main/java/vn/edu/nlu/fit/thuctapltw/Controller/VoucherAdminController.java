package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.VoucherService;
import vn.edu.nlu.fit.thuctapltw.model.Voucher;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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

        String action = request.getParameter("action");
        if ("create".equalsIgnoreCase(action)) {
            request.setAttribute("mode", "create");
            request.getRequestDispatcher("/voucher-form.jsp").forward(request, response);
            return;
        }

        if ("edit".equalsIgnoreCase(action)) {
            showEditForm(request, response);
            return;
        }

        if ("detail".equalsIgnoreCase(action) || "view".equalsIgnoreCase(action)) {
            showDetail(request, response);
            return;
        }

        showVoucherList(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if ("create".equalsIgnoreCase(action)) {
            createVoucher(request, response);
            return;
        }

        if ("update".equalsIgnoreCase(action)) {
            updateVoucher(request, response);
            return;
        }


        response.sendRedirect(request.getContextPath() + "/voucher-admin");
    }

    private void showVoucherList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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


    private void showDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = parsePositiveInt(request.getParameter("id"), 0);
        Voucher voucher = voucherService.getById(id);
        if (voucher == null) {
            response.sendRedirect(request.getContextPath() + "/voucher-admin?error=" + urlEncode("Không tìm thấy mã giảm giá."));
            return;
        }

        request.setAttribute("voucher", voucher);
        request.getRequestDispatcher("/voucher-detail.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = parsePositiveInt(request.getParameter("id"), 0);
        Voucher voucher = voucherService.getById(id);
        if (voucher == null) {
            response.sendRedirect(request.getContextPath() + "/voucher-admin?error=" + urlEncode("Không tìm thấy mã giảm giá."));
            return;
        }

        request.setAttribute("mode", "edit");
        request.setAttribute("voucher", voucher);
        request.getRequestDispatcher("/voucher-form.jsp").forward(request, response);
    }

    private void createVoucher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Voucher voucher = readVoucherFromRequest(request);
        String errorMessage = voucherService.createVoucher(voucher);

        if (errorMessage != null) {
            request.setAttribute("mode", "create");
            request.setAttribute("voucher", voucher);
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/voucher-form.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/voucher-admin?success=create");
    }

    private void updateVoucher(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Voucher voucher = readVoucherFromRequest(request);
        voucher.setId(parsePositiveInt(request.getParameter("id"), 0));

        String errorMessage = voucherService.updateVoucher(voucher);
        if (errorMessage != null) {
            request.setAttribute("mode", "edit");
            request.setAttribute("voucher", voucher);
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/voucher-form.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/voucher-admin?success=update");
    }

    private Voucher readVoucherFromRequest(HttpServletRequest request) {
        Voucher voucher = new Voucher();

        voucher.setCode(trim(request.getParameter("code")));
        voucher.setName(trim(request.getParameter("name")));
        voucher.setDescription(trim(request.getParameter("description")));
        voucher.setDiscount_type(trim(request.getParameter("discount_type")));
        voucher.setDiscount_value(parseDouble(request.getParameter("discount_value"), 0));
        voucher.setMax_discount(parseNullableDouble(request.getParameter("max_discount")));
        voucher.setMin_order_value(parseDouble(request.getParameter("min_order_value"), 0));
        voucher.setVoucher_scope(trim(request.getParameter("voucher_scope")));
        voucher.setCustomer_id(parseNullableInt(request.getParameter("customer_id")));
        voucher.setProduct_id(parseNullableInt(request.getParameter("product_id")));
        voucher.setQuantity(parsePositiveInt(request.getParameter("quantity"), 0));
        voucher.setUsed_quantity(parsePositiveInt(request.getParameter("used_quantity"), 0));
        voucher.setStatus("0".equals(request.getParameter("status")) ? 0 : 1);
        voucher.setStart_date(parseLocalDateTime(request.getParameter("start_date")));
        voucher.setEnd_date(parseLocalDateTime(request.getParameter("end_date")));

        return voucher;
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private int parsePositiveInt(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) return defaultValue;
        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed < 0 ? defaultValue : parsed;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Integer parseNullableInt(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed <= 0 ? null : parsed;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private double parseDouble(String value, double defaultValue) {
        if (value == null || value.trim().isEmpty()) return defaultValue;
        try {
            double parsed = Double.parseDouble(value.trim());
            return parsed < 0 ? defaultValue : parsed;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Double parseNullableDouble(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            double parsed = Double.parseDouble(value.trim());
            return parsed < 0 ? null : parsed;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDateTime parseLocalDateTime(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return LocalDateTime.parse(value.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private String urlEncode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }
}
