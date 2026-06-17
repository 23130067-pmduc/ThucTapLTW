package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Order;
import vn.edu.nlu.fit.thuctapltw.model.TopSellingProduct;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardDao extends BaseDao {
    public int countOrders() {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT COUNT(*) FROM orders")
                        .mapTo(int.class)
                        .one()
        );
    }

    public double totalRevenue() {
        return getJdbi().withHandle(h ->
                h.createQuery("""
            SELECT COALESCE(SUM(total_price),0)
            FROM orders
            WHERE order_status != 'CANCELLED'
              AND (
                order_status = 'COMPLETED'
                OR (payment_statuses = 'PAID' AND payment_methods != 'COD')
              )
        """).mapTo(double.class).one()
        );
    }

    public List<Order> latestOrders(int limit) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
            SELECT o.id, o.receiver_name AS receiverName, o.total_price   AS totalPrice, o.order_status  AS orderStatus, o.created_at    AS createdAt
            FROM orders o
            ORDER BY o.created_at DESC
            LIMIT :limit
        """).bind("limit", limit).mapToBean(Order.class).list());
    }


    public int countProducts() {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT COUNT(*) FROM products")
                        .mapTo(int.class)
                        .one()
        );
    }

    public int countUsers() {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT COUNT(*) FROM users")
                        .mapTo(int.class)
                        .one()
        );
    }

    public List<TopSellingProduct> topSellingProducts(int limit) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
            SELECT p.id AS productId, p.name, p.thumbnail, p.price, p.sale_price AS salePrice,
                   COALESCE(SUM(oi.quantity), 0) AS totalSold
            FROM products p
            LEFT JOIN product_variants pv ON pv.product_id = p.id
            LEFT JOIN order_items oi ON oi.variant_id = pv.id
            LEFT JOIN orders o ON o.id = oi.order_id AND o.order_status = 'COMPLETED'
            GROUP BY p.id, p.name, p.thumbnail, p.price, p.sale_price
            ORDER BY totalSold DESC
            LIMIT :limit
        """).bind("limit", limit).mapToBean(TopSellingProduct.class).list()
        );
    }

    public int countOrdersByStatus(String status) {
        return getJdbi().withHandle(h -> h.createQuery("SELECT COUNT(*) FROM orders WHERE order_status = :status").bind("status", status).mapTo(int.class).one()
        );
    }

    public int countPendingReturnOrders() {
        return getJdbi().withHandle(h -> h.createQuery("SELECT COUNT(*) FROM return_orders WHERE status = 'PENDING'").mapTo(int.class).one()
        );
    }

    public int countNewContacts() {
        return getJdbi().withHandle(h -> h.createQuery("SELECT COUNT(*) FROM contacts WHERE status = 'New' AND is_deleted = 0").mapTo(int.class).one()
        );
    }

    public Map<String, Double> getRevenueByDateRange(LocalDate fromDate, LocalDate toDate) {
        String startDate = fromDate.toString();
        String endDate = toDate.plusDays(1).toString();

        List<Map<String, Object>> rows = getJdbi().withHandle(h ->
            h.createQuery("""
                SELECT DATE(o.created_at) AS rev_date, COALESCE(SUM(o.total_price), 0) AS revenue
                FROM orders o
                WHERE o.order_status != 'CANCELLED'
                  AND (o.order_status = 'COMPLETED' OR (o.payment_statuses = 'PAID' AND o.payment_methods != 'COD'))
                  AND o.created_at >= :startDate
                  AND o.created_at < :endDate
                GROUP BY DATE(o.created_at)
                ORDER BY rev_date ASC
            """).bind("startDate", startDate)
                    .bind("endDate", endDate)
                    .mapToMap()
                    .list());

        Map<String, Double> fromDb = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            String dateKey = row.get("rev_date").toString().substring(0, 10);
            Object rev = row.get("revenue");
            fromDb.put(dateKey, rev != null ? ((Number) rev).doubleValue() : 0.0);
        }

        DateTimeFormatter labelFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter keyFmt   = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, Double> result = new LinkedHashMap<>();
        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
            String key   = date.format(keyFmt);
            String label = date.format(labelFmt);
            result.put(label, fromDb.getOrDefault(key, 0.0));
        }
        return result;
    }

}
