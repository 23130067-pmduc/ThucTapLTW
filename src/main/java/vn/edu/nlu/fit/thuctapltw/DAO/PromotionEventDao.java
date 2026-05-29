package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Product;
import vn.edu.nlu.fit.thuctapltw.model.PromotionEvent;

import java.util.List;

public class PromotionEventDao extends BaseDao {
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
}
