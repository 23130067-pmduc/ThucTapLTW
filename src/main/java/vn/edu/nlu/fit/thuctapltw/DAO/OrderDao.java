package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Order;
import vn.edu.nlu.fit.thuctapltw.model.OrderItem;

import java.util.List;

public class OrderDao extends BaseDao {

    public List<Order> getAllOrders() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM orders ORDER BY created_at DESC")
                        .mapToBean(Order.class)
                        .list()
        );
    }

    public Order findById(int id) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM orders WHERE id = :id")
                        .bind("id", id)
                        .mapToBean(Order.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public Order findByIdAndUserId(int orderId, int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM orders WHERE id = :orderId AND user_id = :userId")
                        .bind("orderId", orderId)
                        .bind("userId", userId)
                        .mapToBean(Order.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public List<OrderItem> getItems(int orderId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT oi.id, oi.order_id, oi.variant_id, oi.quantity, oi.price, oi.total,
                               oi.product_name, oi.size, oi.color, p.thumbnail
                        FROM order_items oi
                        LEFT JOIN product_variants pv ON oi.variant_id = pv.id
                        LEFT JOIN products p ON pv.product_id = p.id
                        WHERE oi.order_id = :orderId
                        ORDER BY oi.id ASC
                        """)
                        .bind("orderId", orderId)
                        .mapToBean(OrderItem.class)
                        .list()
        );
    }

    public void updateStatus(int id, String status) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("UPDATE orders SET order_status = :status WHERE id = :id")
                        .bind("status", status)
                        .bind("id", id)
                        .execute()
        );
    }

    public void updateStatusByUser(int orderId, int userId, String status) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("UPDATE orders SET order_status = :status WHERE id = :orderId AND user_id = :userId")
                        .bind("status", status)
                        .bind("orderId", orderId)
                        .bind("userId", userId)
                        .execute()
        );
    }

    public String getUserEmailByOrderId(int orderId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT u.email
                        FROM orders o
                        JOIN users u ON o.user_id = u.id
                        WHERE o.id = :orderId
                        """)
                        .bind("orderId", orderId)
                        .mapTo(String.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public List<Order> searchOrders(String keyword, String status) {
        return getJdbi().withHandle(handle -> {
            StringBuilder sql = new StringBuilder("""
                    SELECT *
                    FROM orders
                    WHERE 1=1
                    """);

            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append("""
                         AND (
                            CAST(id AS CHAR) LIKE :kw
                            OR receiver_name LIKE :kw
                         )
                        """);
            }

            if (status != null && !status.trim().isEmpty()) {
                sql.append(" AND order_status = :status ");
            }

            sql.append(" ORDER BY created_at DESC ");

            var query = handle.createQuery(sql.toString());

            if (keyword != null && !keyword.trim().isEmpty()) {
                query.bind("kw", "%" + keyword.trim() + "%");
            }

            if (status != null && !status.trim().isEmpty()) {
                query.bind("status", status.trim());
            }

            return query.mapToBean(Order.class).list();
        });
    }

    public List<Order> getByUserId(int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM orders WHERE user_id = :userId ORDER BY created_at DESC")
                        .bind("userId", userId)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    public List<Order> getByUserIdAndStatus(int userId, String status) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM orders WHERE user_id = :userId AND order_status = :status ORDER BY created_at DESC")
                        .bind("userId", userId)
                        .bind("status", status)
                        .mapToBean(Order.class)
                        .list()
        );
    }

    public int createOrder(int userId, String receiver, String phone, String address, String note, String paymentMethod, double totalPrice, double shippingFee) {
        double finalAmount = totalPrice + shippingFee;
        return getJdbi().withHandle(h -> h.createUpdate("""
            INSERT INTO orders( user_id, receiver_name, phone, shipping_address, note,total_price, discount, shipping_fee, final_amount,payment_methods, payment_statuses, order_status, created_at)
            VALUES(:uid, :receiver, :phone, :address, :note,:total, 0, :shippingFee, :finalAmount,:payment, 'UNPAID', 'PENDING', NOW()) """).bind("uid", userId).bind("receiver", receiver).bind("phone", phone).bind("address", address).bind("note", note).bind("total", totalPrice).bind("shippingFee", shippingFee).bind("finalAmount", finalAmount).bind("payment", paymentMethod).executeAndReturnGeneratedKeys("id").mapTo(int.class).one()
        );
    }

    public Order getById(int orderId) {
        String sql = """
        SELECT id, user_id, receiver_name, phone, shipping_address, total_price, discount, shipping_fee, note, final_amount, payment_methods, payment_statuses, order_status, created_at
        FROM orders
        WHERE id = :oid
    """;
        return getJdbi().withHandle(h -> h.createQuery(sql).bind("oid", orderId).map((rs, ctx) -> {
            Order o = new Order();
            o.setId(rs.getInt("id"));
            o.setUserId(rs.getInt("user_id"));
            o.setReceiverName(rs.getString("receiver_name"));
            o.setPhone(rs.getString("phone"));
            o.setShippingAddress(rs.getString("shipping_address"));
            o.setNote(rs.getString("note"));
            o.setTotalPrice(rs.getDouble("total_price"));
            o.setDiscount(rs.getDouble("discount"));
            o.setShippingFee(rs.getDouble("shipping_fee"));
            o.setFinalAmount(rs.getDouble("final_amount"));
            o.setPaymentMethods(rs.getString("payment_methods"));
            o.setPaymentStatuses(rs.getString("payment_statuses"));
            o.setOrderStatus(rs.getString("order_status"));
            o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return o;}).findOne().orElse(null)
        );
    }

    public int countCompletePurchaseByUserAndProduct(int userId, int productId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM orders O
                JOIN order_items OI ON O.id = OI.order_id
                JOIN product_variants PV ON OI.variant_id = PV.id
                WHERE O.user_id = :userId AND PV.product_id = :productId AND O.order_status IN ('DELIVERED', 'COMPLETED')""")
                .bind("userId", userId)
                .bind("productId", productId)
                .mapTo(Integer.class)
                .one());
    }
}
