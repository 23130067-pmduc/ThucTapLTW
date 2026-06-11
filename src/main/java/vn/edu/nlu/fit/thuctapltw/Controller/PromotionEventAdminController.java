package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.PromotionEventService;
import vn.edu.nlu.fit.thuctapltw.model.PromotionEvent;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@WebServlet(name = "PromotionEventAdminController", value = "/promotion-event-admin")
public class PromotionEventAdminController extends HttpServlet {
    private PromotionEventService promotionEventService;

    @Override
    public void init() {
        promotionEventService = new PromotionEventService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("create".equals(request.getParameter("action"))) {
            PromotionEvent event = new PromotionEvent();
            event.setIcon("fa-gift");
            event.setStatus(1);
            request.setAttribute("promotionEvent", event);
            request.getRequestDispatcher("/promotion-event-form.jsp").forward(request, response);
            return;
        }

        if ("edit".equals(request.getParameter("action"))) {
            showEventForm(request, response, "edit");
            return;
        }

        if ("detail".equals(request.getParameter("action")) || "view".equals(request.getParameter("action"))) {
            showEventForm(request, response, "view");
            return;
        }

        int pageSize = 20;
        int currentPage = parsePositiveInt(request.getParameter("page"), 1);
        String keyword = trim(request.getParameter("keyword"));
        int total = promotionEventService.countAdminEvents(keyword);
        int totalPages = Math.max(1, (int) Math.ceil((double) total / pageSize));
        currentPage = Math.min(currentPage, totalPages);

        request.setAttribute("promotionEvents", promotionEventService.getAdminEvents(keyword, currentPage, pageSize));
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("total", total);
        request.setAttribute("totalAll", promotionEventService.countAllEvents());
        request.setAttribute("totalActive", promotionEventService.countActiveEvents());
        request.setAttribute("totalUpcoming", promotionEventService.countUpcomingEvents());
        request.setAttribute("totalEnded", promotionEventService.countEndedEvents());
        request.getRequestDispatcher("/promotion-event-admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if ("create".equals(action)) {
            createEvent(request, response);
            return;
        }

        if ("update".equals(action)) {
            updateEvent(request, response);
            return;
        }

        if ("toggle-status".equals(action)) {
            toggleStatus(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/promotion-event-admin");
    }

    private void showEventForm(HttpServletRequest request, HttpServletResponse response, String mode)
            throws ServletException, IOException {
        int id = parsePositiveInt(request.getParameter("id"), 0);
        PromotionEvent event = promotionEventService.getById(id);
        if (event == null) {
            response.sendRedirect(request.getContextPath() + "/promotion-event-admin?error="
                    + urlEncode("Không tìm thấy chương trình khuyến mãi."));
            return;
        }

        request.setAttribute("mode", mode);
        request.setAttribute("promotionEvent", event);
        request.getRequestDispatcher("/promotion-event-form.jsp").forward(request, response);
    }

    private void createEvent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PromotionEvent event = readEvent(request);
        try {
            promotionEventService.createEvent(event);
            response.sendRedirect(request.getContextPath() + "/promotion-event-admin?success=create");
        } catch (IllegalArgumentException e) {
            request.setAttribute("promotionEvent", event);
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/promotion-event-form.jsp").forward(request, response);
        }
    }

    private void toggleStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = parsePositiveInt(request.getParameter("id"), 0);
        int status = parseStatus(request.getParameter("status"));
        try {
            promotionEventService.updateStatus(id, status);
            response.sendRedirect(request.getContextPath() + "/promotion-event-admin?success="
                    + (status == 0 ? "lock" : "unlock"));
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/promotion-event-admin?error=" + urlEncode(e.getMessage()));
        }
    }

    private void updateEvent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PromotionEvent event = readEvent(request);
        event.setId(parsePositiveInt(request.getParameter("id"), 0));

        try {
            promotionEventService.updateEvent(event);
            response.sendRedirect(request.getContextPath() + "/promotion-event-admin?success=update");
        } catch (IllegalArgumentException e) {
            request.setAttribute("mode", "edit");
            request.setAttribute("promotionEvent", event);
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/promotion-event-form.jsp").forward(request, response);
        }
    }

    private PromotionEvent readEvent(HttpServletRequest request) {
        PromotionEvent event = new PromotionEvent();
        event.setTitle(request.getParameter("title"));
        event.setDescription(request.getParameter("description"));
        event.setIcon(request.getParameter("icon"));
        event.setTag(request.getParameter("tag"));
        event.setDiscountLabel(request.getParameter("discountLabel"));
        event.setStatus(parseStatus(request.getParameter("status")));
        event.setStartDate(parseDateTime(request.getParameter("startDate")));
        event.setEndDate(parseDateTime(request.getParameter("endDate")));
        return event;
    }

    private LocalDateTime parseDateTime(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private int parseStatus(String value) {
        return "0".equals(value) ? 0 : 1;
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private String urlEncode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }

    private int parsePositiveInt(String value, int defaultValue) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
