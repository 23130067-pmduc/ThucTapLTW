package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Product;
import vn.edu.nlu.fit.thuctapltw.model.PromotionEvent;

import java.util.List;

public class PromotionEventDao extends BaseDao {
    private static final String ADMIN_EVENT_SELECT = """
            SELECT pe.id, pe.title, pe.description, pe.icon, pe.tag,
                   pe.discount_label AS discountLabel, pe.start_date AS startDate,
                   pe.end_date AS endDate, pe.status, COUNT(pep.product_id) AS productCount
            FROM promotion_events pe
            LEFT JOIN promotion_event_products pep ON pe.id = pep.event_id
            """;

    public List<PromotionEvent> findActiveEvents() {
        String sql = """
                SELECT id, title, description, icon, tag, discount_label AS discountLabel,
                       start_date AS startDate, end_date AS endDate, status
                FROM promotion_events
                WHERE status = 1
                  AND start_date <= NOW()
                  AND end_date >= NOW()
                ORDER BY start_date DESC, id DESC
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(PromotionEvent.class)
                        .list()
        );
    }

    public List<Product> findProductsByEventId(int eventId, int limit) {
        String sql = """
                SELECT p.id, p.category_id, p.name, p.description, p.price,
                       COALESCE(pep.event_sale_price, p.sale_price) AS sale_price,
                       p.thumbnail, p.created_at, p.views, p.status
                FROM promotion_event_products pep
                JOIN products p ON pep.product_id = p.id
                WHERE pep.event_id = :eventId
                  AND p.status = 'Đang bán'
                  AND COALESCE(pep.event_sale_price, p.sale_price) IS NOT NULL
                  AND COALESCE(pep.event_sale_price, p.sale_price) > 0
                  AND COALESCE(pep.event_sale_price, p.sale_price) < p.price
                ORDER BY pep.id DESC
                LIMIT :limit
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("eventId", eventId)
                        .bind("limit", limit)
                        .mapToBean(Product.class)
                        .list()
        );
    }

    public List<PromotionEvent> findAdminEvents(int limit, int offset) {
        String sql = ADMIN_EVENT_SELECT + """
                GROUP BY pe.id, pe.title, pe.description, pe.icon, pe.tag, pe.discount_label,
                         pe.start_date, pe.end_date, pe.status
                ORDER BY pe.id DESC
                LIMIT :limit OFFSET :offset
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("limit", limit)
                        .bind("offset", offset)
                        .mapToBean(PromotionEvent.class)
                        .list()
        );
    }

    public int countAllEvents() {
        return countByCondition("1 = 1");
    }

    public int countActiveEvents() {
        return countByCondition("status = 1 AND start_date <= NOW() AND end_date >= NOW()");
    }

    public int countUpcomingEvents() {
        return countByCondition("status = 1 AND start_date > NOW()");
    }

    public int countEndedEvents() {
        return countByCondition("end_date < NOW()");
    }

    private int countByCondition(String condition) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM promotion_events WHERE " + condition)
                        .mapTo(Integer.class)
                        .one()
        );
    }
}
