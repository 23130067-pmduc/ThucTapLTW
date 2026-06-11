package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Order;
import vn.edu.nlu.fit.thuctapltw.model.TopSellingProduct;

import java.util.List;

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
            WHERE order_status = 'COMPLETED'
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
}
