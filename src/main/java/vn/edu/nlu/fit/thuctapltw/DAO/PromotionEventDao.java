package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Product;
import vn.edu.nlu.fit.thuctapltw.model.PromotionEvent;

import java.util.List;

public class PromotionEventDao extends BaseDao {
    private static final String ADMIN_EVENT_SELECT = """
            SELECT pe.id, pe.title, pe.description, pe.icon, pe.tag,
                   pe.discount_label AS discountLabel, pe.scope_type AS scopeType,
                   pe.discount_percent AS discountPercent, pe.start_date AS startDate,
                   pe.end_date AS endDate, pe.status, COUNT(pep.product_id) AS productCount
            FROM promotion_events pe
            LEFT JOIN promotion_event_products pep ON pe.id = pep.event_id
            """;

    public List<PromotionEvent> findActiveEvents() {
        String sql = """
                SELECT id, title, description, icon, tag, discount_label AS discountLabel,
                       scope_type AS scopeType, discount_percent AS discountPercent,
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

    public List<Product> findProductsByEventId(int eventId) {
        String sql = """
                SELECT p.id, p.category_id, p.name, p.description, p.price,
                       COALESCE(pep.event_sale_price, p.sale_price) AS sale_price,
                       p.thumbnail, p.created_at, p.views, p.status
                FROM promotion_event_products pep
                JOIN products p ON pep.product_id = p.id
                WHERE pep.event_id = :eventId
                ORDER BY p.id DESC
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("eventId", eventId)
                        .mapToBean(Product.class)
                        .list()
        );
    }

    public List<Integer> findProductIdsByEventId(int eventId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT product_id FROM promotion_event_products WHERE event_id = :eventId")
                        .bind("eventId", eventId)
                        .mapTo(Integer.class)
                        .list()
        );
    }

    public List<Integer> findCategoryIdsByEventId(int eventId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT DISTINCT p.category_id
                        FROM promotion_event_products pep
                        JOIN products p ON p.id = pep.product_id
                        WHERE pep.event_id = :eventId
                          AND p.category_id IS NOT NULL
                        ORDER BY p.category_id
                        """)
                        .bind("eventId", eventId)
                        .mapTo(Integer.class)
                        .list()
        );
    }

    public List<PromotionEvent> findAdminEvents(String keyword, int limit, int offset) {
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        String sql = ADMIN_EVENT_SELECT
                + (hasKeyword ? """
                WHERE CAST(pe.id AS CHAR) LIKE :keyword
                   OR UPPER(pe.title) LIKE UPPER(:keyword)
                   OR UPPER(pe.description) LIKE UPPER(:keyword)
                   OR UPPER(pe.tag) LIKE UPPER(:keyword)
                   OR UPPER(pe.discount_label) LIKE UPPER(:keyword)
                   OR UPPER(pe.scope_type) LIKE UPPER(:keyword)
                """ : "")
                + """
                GROUP BY pe.id, pe.title, pe.description, pe.icon, pe.tag, pe.discount_label,
                         pe.scope_type, pe.discount_percent, pe.start_date, pe.end_date, pe.status
                ORDER BY pe.id DESC
                LIMIT :limit OFFSET :offset
                """;

        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql)
                        .bind("limit", limit)
                        .bind("offset", offset);
            if (hasKeyword) {
                query.bind("keyword", "%" + keyword.trim() + "%");
            }
            return query.mapToBean(PromotionEvent.class).list();
        });
    }

    public int countAdminEvents(String keyword) {
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        String sql = """
                SELECT COUNT(*)
                FROM promotion_events pe
                """
                + (hasKeyword ? """
                WHERE CAST(pe.id AS CHAR) LIKE :keyword
                   OR UPPER(pe.title) LIKE UPPER(:keyword)
                   OR UPPER(pe.description) LIKE UPPER(:keyword)
                   OR UPPER(pe.tag) LIKE UPPER(:keyword)
                   OR UPPER(pe.discount_label) LIKE UPPER(:keyword)
                   OR UPPER(pe.scope_type) LIKE UPPER(:keyword)
                """ : "");

        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql);
            if (hasKeyword) {
                query.bind("keyword", "%" + keyword.trim() + "%");
            }
            return query.mapTo(Integer.class).one();
        });
    }

    public int create(PromotionEvent event) {
        String sql = """
                INSERT INTO promotion_events
                    (title, description, icon, tag, discount_label, scope_type, discount_percent, start_date, end_date, status)
                VALUES
                    (:title, :description, :icon, :tag, :discountLabel, :scopeType, :discountPercent, :startDate, :endDate, :status)
                """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bindBean(event)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public PromotionEvent findById(int id) {
        String sql = ADMIN_EVENT_SELECT + """
                WHERE pe.id = :id
                GROUP BY pe.id, pe.title, pe.description, pe.icon, pe.tag, pe.discount_label,
                         pe.scope_type, pe.discount_percent, pe.start_date, pe.end_date, pe.status
                LIMIT 1
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("id", id)
                        .mapToBean(PromotionEvent.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public boolean update(PromotionEvent event) {
        String sql = """
                UPDATE promotion_events
                SET title = :title,
                    description = :description,
                    icon = :icon,
                    tag = :tag,
                    discount_label = :discountLabel,
                    scope_type = :scopeType,
                    discount_percent = :discountPercent,
                    start_date = :startDate,
                    end_date = :endDate,
                    status = :status
                WHERE id = :id
                """;

        return getJdbi().withHandle(handle ->
                handle.createUpdate(sql)
                        .bindBean(event)
                        .execute() == 1
        );
    }

    public boolean updateStatus(int id, int status) {
        return getJdbi().withHandle(handle ->
                handle.createUpdate("UPDATE promotion_events SET status = :status WHERE id = :id")
                        .bind("id", id)
                        .bind("status", status)
                        .execute() == 1
        );
    }

    public void syncEventProducts(int eventId, List<Integer> productIds, int discountPercent) {
        getJdbi().useTransaction(handle -> {
            handle.createUpdate("DELETE FROM promotion_event_products WHERE event_id = :eventId")
                    .bind("eventId", eventId)
                    .execute();

            if (productIds == null || productIds.isEmpty()) {
                return;
            }

            for (Integer productId : productIds) {
                if (productId == null || productId <= 0) {
                    continue;
                }
                handle.createUpdate("""
                        INSERT INTO promotion_event_products(event_id, product_id, event_sale_price)
                        SELECT :eventId, p.id, ROUND(p.price * (100 - :discountPercent) / 100, 0)
                        FROM products p
                        WHERE p.id = :productId
                          AND p.status = 'Đang bán'
                          AND :discountPercent > 0
                          AND :discountPercent < 100
                        ON DUPLICATE KEY UPDATE event_sale_price = VALUES(event_sale_price)
                        """)
                        .bind("eventId", eventId)
                        .bind("productId", productId)
                        .bind("discountPercent", discountPercent)
                        .execute();
            }
        });
    }

    public List<Integer> findActiveProductIds() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT id
                        FROM products
                        WHERE status = 'Đang bán'
                        ORDER BY id DESC
                        """)
                        .mapTo(Integer.class)
                        .list()
        );
    }

    public List<Integer> findActiveProductIdsByCategories(List<Integer> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return List.of();
        }
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT id
                        FROM products
                        WHERE status = 'Đang bán'
                          AND category_id IN (<categoryIds>)
                        ORDER BY id DESC
                        """)
                        .bindList("categoryIds", categoryIds)
                        .mapTo(Integer.class)
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
