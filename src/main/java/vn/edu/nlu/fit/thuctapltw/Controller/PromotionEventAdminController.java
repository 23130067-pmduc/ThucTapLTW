package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.PromotionEventService;

import java.io.IOException;

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
        int pageSize = 20;
        int currentPage = parsePositiveInt(request.getParameter("page"), 1);
        int total = promotionEventService.countAllEvents();
        int totalPages = Math.max(1, (int) Math.ceil((double) total / pageSize));
        currentPage = Math.min(currentPage, totalPages);

        request.setAttribute("promotionEvents", promotionEventService.getAdminEvents(currentPage, pageSize));
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("total", total);
        request.setAttribute("totalActive", promotionEventService.countActiveEvents());
        request.setAttribute("totalUpcoming", promotionEventService.countUpcomingEvents());
        request.setAttribute("totalEnded", promotionEventService.countEndedEvents());
        request.getRequestDispatcher("/promotion-event-admin.jsp").forward(request, response);
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
