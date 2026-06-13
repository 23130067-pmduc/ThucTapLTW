package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Order;
import vn.edu.nlu.fit.thuctapltw.model.TopSellingProduct;

import java.time.LocalDate;
import java.time.YearMonth;
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

    public Map<String, Double> getRevenueByDay(int days) {
        List<Map<String, Object>> rows = getJdbi().withHandle(h ->
            h.createQuery("""
                SELECT DATE(o.created_at) AS rev_date, COALESCE(SUM(o.total_price), 0) AS revenue
                FROM orders o
                WHERE o.order_status != 'CANCELLED'
                  AND (o.order_status = 'COMPLETED' OR (o.payment_statuses = 'PAID' AND o.payment_methods != 'COD'))
                  AND o.created_at >= DATE_SUB(CURDATE(), INTERVAL :days DAY)
                GROUP BY DATE(o.created_at)
                ORDER BY rev_date ASC
            """).bind("days", days).mapToMap().list());

        Map<String, Double> fromDb = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            String dateKey = row.get("rev_date").toString().substring(0, 10);
            Object rev = row.get("revenue");
            fromDb.put(dateKey, rev != null ? ((Number) rev).doubleValue() : 0.0);
        }

        DateTimeFormatter labelFmt = DateTimeFormatter.ofPattern("dd/MM");
        DateTimeFormatter keyFmt   = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        Map<String, Double> result = new LinkedHashMap<>();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String key   = date.format(keyFmt);
            String label = date.format(labelFmt);
            result.put(label, fromDb.getOrDefault(key, 0.0));
        }
        return result;
    }

    public Map<String, Double> getRevenueByMonth(int months) {
        List<Map<String, Object>> rows = getJdbi().withHandle(h ->
            h.createQuery("""
                SELECT DATE_FORMAT(o.created_at, '%Y-%m') AS rev_month,
                       COALESCE(SUM(o.total_price), 0) AS revenue
                FROM orders o
                WHERE o.order_status != 'CANCELLED'
                  AND (
                    o.order_status = 'COMPLETED'
                    OR (o.payment_statuses = 'PAID' AND o.payment_methods != 'COD')
                  )
                  AND o.created_at >= DATE_SUB(CURDATE(), INTERVAL :months MONTH)
                GROUP BY rev_month
                ORDER BY rev_month ASC
            """).bind("months", months).mapToMap().list()
        );

        Map<String, Double> fromDb = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            String monthKey = row.get("rev_month").toString();
            Object rev = row.get("revenue");
            fromDb.put(monthKey, rev != null ? ((Number) rev).doubleValue() : 0.0);
        }

        DateTimeFormatter labelFmt = DateTimeFormatter.ofPattern("MM/yyyy");
        DateTimeFormatter keyFmt   = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth current = YearMonth.now();
        Map<String, Double> result = new LinkedHashMap<>();
        for (int i = months - 1; i >= 0; i--) {
            YearMonth ym  = current.minusMonths(i);
            String key    = ym.format(keyFmt);
            String label  = ym.format(labelFmt);
            result.put(label, fromDb.getOrDefault(key, 0.0));
        }
        return result;
    }

}
