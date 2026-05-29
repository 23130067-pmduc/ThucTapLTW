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
}
