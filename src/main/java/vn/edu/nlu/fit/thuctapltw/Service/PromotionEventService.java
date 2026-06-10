package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.PromotionEventDao;
import vn.edu.nlu.fit.thuctapltw.model.PromotionEvent;

import java.util.List;

public class PromotionEventService {
    private final PromotionEventDao promotionEventDao;

    public PromotionEventService() {
        this.promotionEventDao = new PromotionEventDao();
    }

    public List<PromotionEvent> getActiveEventsWithProducts() {
        List<PromotionEvent> events = promotionEventDao.findActiveEvents();

        for (PromotionEvent event : events) {
            event.setProducts(promotionEventDao.findProductsByEventId(event.getId(), 4));
        }

        return events;
    }

    public List<PromotionEvent> getAdminEvents(int page, int pageSize) {
        int safePage = Math.max(page, 1);
        return promotionEventDao.findAdminEvents(pageSize, (safePage - 1) * pageSize);
    }

    public int countAllEvents() {
        return promotionEventDao.countAllEvents();
    }

    public int countActiveEvents() {
        return promotionEventDao.countActiveEvents();
    }

    public int countUpcomingEvents() {
        return promotionEventDao.countUpcomingEvents();
    }

    public int countEndedEvents() {
        return promotionEventDao.countEndedEvents();
    }
}
