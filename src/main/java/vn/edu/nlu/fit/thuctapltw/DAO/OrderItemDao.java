package vn.edu.nlu.fit.thuctapltw.DAO;
import java.util.List;

import vn.edu.nlu.fit.thuctapltw.model.OrderItem;

public class OrderItemDao extends BaseDao{
    public void insert(int orderId, int variantId, String productName, String size, String color, int quantity, double price, double total) {
        getJdbi().useHandle(h -> h.createUpdate("""
            INSERT INTO order_items (order_id, variant_id, product_name, size, color, quantity, price, total) VALUES (:oid, :vid, :name, :size, :color, :qty, :price, :total)""").bind("oid", orderId).bind("vid", variantId).bind("name", productName).bind("size", size).bind("color", color).bind("qty", quantity).bind("price", price).bind("total", total).execute()
        );
    }


    public List<OrderItem> getByOrderId(int orderId) {
        return getJdbi().withHandle(h -> h.createQuery("""
                SELECT oi.*, p.thumbnail
                FROM order_items oi
                LEFT JOIN product_variants pv ON oi.variant_id = pv.id
                LEFT JOIN products p ON pv.product_id = p.id
                WHERE oi.order_id = :oid""").bind("oid", orderId).mapToBean(OrderItem.class).list()
        );
    }
}