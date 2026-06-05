package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.InventoryTransaction;
import vn.edu.nlu.fit.thuctapltw.model.Order;
import vn.edu.nlu.fit.thuctapltw.model.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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



    public String updateStatusWithInventoryExport(int orderId, String newStatus, Integer createdBy) {
        String normalizedStatus = newStatus == null ? "" : newStatus.trim().toUpperCase();

        return getJdbi().inTransaction(handle -> {
            Order order = handle.createQuery("""
                            SELECT *
                            FROM orders
                            WHERE id = :orderId
                            FOR UPDATE
                            """)
                    .bind("orderId", orderId)
                    .mapToBean(Order.class)
                    .findOne()
                    .orElse(null);

            if (order == null) {
                return "ORDER_NOT_FOUND";
            }

            String currentStatus = order.getOrderStatus() == null ? "" : order.getOrderStatus().trim().toUpperCase();

            if ("COMPLETED".equals(currentStatus) || "CANCELLED".equals(currentStatus)) {
                return "ORDER_FINAL";
            }

            if (!"PENDING".equals(normalizedStatus)
                    && !"SHIPPING".equals(normalizedStatus)
                    && !"COMPLETED".equals(normalizedStatus)
                    && !"CANCELLED".equals(normalizedStatus)) {
                return "INVALID_STATUS";
            }

            if ("PENDING".equals(currentStatus) && "COMPLETED".equals(normalizedStatus)) {
                return "INVALID_FLOW";
            }

            // Luồng đúng của issue #75:
            // Admin bàn giao đơn: PENDING -> SHIPPING => xuất kho FIFO + ghi giá vốn.
            // Khách xác nhận đã nhận: SHIPPING -> COMPLETED => chỉ đổi trạng thái, không xuất kho lần 2.
            if ("COMPLETED".equals(normalizedStatus)) {
                if (!"SHIPPING".equals(currentStatus)) {
                    return "INVALID_FLOW";
                }

                handle.createUpdate("""
                                UPDATE orders
                                SET order_status = 'COMPLETED'
                                WHERE id = :orderId
                                """)
                        .bind("orderId", orderId)
                        .execute();
                return "SUCCESS";
            }

            if (!"SHIPPING".equals(normalizedStatus)) {
                handle.createUpdate("""
                                UPDATE orders
                                SET order_status = :status
                                WHERE id = :orderId
                                """)
                        .bind("status", normalizedStatus)
                        .bind("orderId", orderId)
                        .execute();
                return "SUCCESS";
            }

            if (!"PENDING".equals(currentStatus)) {
                return "INVALID_FLOW";
            }

            Integer existingExportId = handle.createQuery("""
                            SELECT id
                            FROM inventory_transactions
                            WHERE order_id = :orderId
                              AND type = 'EXPORT'
                            LIMIT 1
                            """)
                    .bind("orderId", orderId)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(null);

            if (existingExportId != null) {
                handle.createUpdate("""
                                UPDATE orders
                                SET order_status = 'SHIPPING'
                                WHERE id = :orderId
                                """)
                        .bind("orderId", orderId)
                        .execute();
                return "SUCCESS_ALREADY_EXPORTED";
            }

            List<Map<String, Object>> rawItems = handle.createQuery("""
                            SELECT variant_id, quantity
                            FROM order_items
                            WHERE order_id = :orderId
                            """)
                    .bind("orderId", orderId)
                    .mapToMap()
                    .list();

            if (rawItems.isEmpty()) {
                return "ORDER_EMPTY";
            }

            Map<Integer, Integer> itemQuantityMap = new LinkedHashMap<>();
            for (Map<String, Object> item : rawItems) {
                Object variantValue = item.get("variant_id");
                Object quantityValue = item.get("quantity");

                if (variantValue == null || quantityValue == null) {
                    return "INVALID_ORDER_ITEM";
                }

                int variantId = ((Number) variantValue).intValue();
                int quantity = ((Number) quantityValue).intValue();

                if (variantId <= 0 || quantity <= 0) {
                    return "INVALID_ORDER_ITEM";
                }

                itemQuantityMap.merge(variantId, quantity, Integer::sum);
            }

            int totalQuantity = itemQuantityMap.values().stream().mapToInt(Integer::intValue).sum();

            for (Map.Entry<Integer, Integer> entry : itemQuantityMap.entrySet()) {
                int variantId = entry.getKey();
                int quantity = entry.getValue();

                Integer currentStock = handle.createQuery("""
                                SELECT stock
                                FROM product_variants
                                WHERE id = :variantId
                                FOR UPDATE
                                """)
                        .bind("variantId", variantId)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(null);

                if (currentStock == null || currentStock < quantity) {
                    return "INSUFFICIENT_STOCK";
                }

                Integer availableBatchQuantity = handle.createQuery("""
                                SELECT CAST(COALESCE(SUM(remaining_quantity), 0) AS SIGNED)
                                FROM inventory_batches
                                WHERE product_variant_id = :variantId
                                  AND remaining_quantity > 0
                                """)
                        .bind("variantId", variantId)
                        .mapTo(Integer.class)
                        .one();

                if (availableBatchQuantity == null || availableBatchQuantity < quantity) {
                    return "INSUFFICIENT_BATCH_STOCK";
                }
            }

            int transactionId = handle.createUpdate("""
                            INSERT INTO inventory_transactions(
                                code,
                                type,
                                total_quantity,
                                status,
                                supplier_name,
                                note,
                                created_by,
                                order_id,
                                created_at
                            ) VALUES (
                                '',
                                'EXPORT',
                                :totalQuantity,
                                'PENDING',
                                NULL,
                                :note,
                                :createdBy,
                                :orderId,
                                NOW()
                            )
                            """)
                    .bind("totalQuantity", totalQuantity)
                    .bind("note", "Xuất kho tự động khi bàn giao đơn hàng #" + orderId)
                    .bind("createdBy", createdBy)
                    .bind("orderId", orderId)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(int.class)
                    .one();

            String code = "PX" + String.format("%05d", transactionId);
            handle.createUpdate("""
                            UPDATE inventory_transactions
                            SET code = :code
                            WHERE id = :transactionId
                            """)
                    .bind("code", code)
                    .bind("transactionId", transactionId)
                    .execute();

            Map<Integer, Integer> detailIdMap = new LinkedHashMap<>();
            for (Map.Entry<Integer, Integer> entry : itemQuantityMap.entrySet()) {
                int detailId = handle.createUpdate("""
                                INSERT INTO inventory_transaction_details(
                                    transaction_id,
                                    product_variant_id,
                                    quantity,
                                    unit_cost,
                                    note
                                ) VALUES (
                                    :transactionId,
                                    :productVariantId,
                                    :quantity,
                                    0,
                                    'Tự động xuất khi bàn giao đơn hàng'
                                )
                                """)
                        .bind("transactionId", transactionId)
                        .bind("productVariantId", entry.getKey())
                        .bind("quantity", entry.getValue())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(int.class)
                        .one();
                detailIdMap.put(entry.getKey(), detailId);
            }

            for (Map.Entry<Integer, Integer> entry : itemQuantityMap.entrySet()) {
                int variantId = entry.getKey();
                int quantity = entry.getValue();
                int detailId = detailIdMap.get(variantId);
                int remainingToExport = quantity;
                BigDecimal totalCost = BigDecimal.ZERO;

                List<Map<String, Object>> batches = handle.createQuery("""
                                SELECT id, remaining_quantity, unit_cost
                                FROM inventory_batches
                                WHERE product_variant_id = :variantId
                                  AND remaining_quantity > 0
                                ORDER BY created_at ASC, id ASC
                                FOR UPDATE
                                """)
                        .bind("variantId", variantId)
                        .mapToMap()
                        .list();

                for (Map<String, Object> batch : batches) {
                    if (remainingToExport <= 0) {
                        break;
                    }

                    int batchId = ((Number) batch.get("id")).intValue();
                    int batchRemainingQuantity = ((Number) batch.get("remaining_quantity")).intValue();
                    BigDecimal batchUnitCost = (BigDecimal) batch.get("unit_cost");
                    int usedQuantity = Math.min(remainingToExport, batchRemainingQuantity);
                    BigDecimal usedTotalCost = batchUnitCost.multiply(BigDecimal.valueOf(usedQuantity));

                    int updatedBatchRows = handle.createUpdate("""
                                    UPDATE inventory_batches
                                    SET remaining_quantity = remaining_quantity - :usedQuantity,
                                        status = CASE
                                            WHEN remaining_quantity - :usedQuantity <= 0 THEN 'SOLD_OUT'
                                            ELSE status
                                        END,
                                        updated_at = NOW()
                                    WHERE id = :batchId
                                      AND remaining_quantity >= :usedQuantity
                                    """)
                            .bind("usedQuantity", usedQuantity)
                            .bind("batchId", batchId)
                            .execute();

                    if (updatedBatchRows <= 0) {
                        return "INSUFFICIENT_BATCH_STOCK";
                    }

                    handle.createUpdate("""
                                    INSERT INTO inventory_batch_usages(
                                        transaction_id,
                                        transaction_detail_id,
                                        batch_id,
                                        product_variant_id,
                                        quantity,
                                        unit_cost,
                                        total_cost,
                                        created_at
                                    ) VALUES (
                                        :transactionId,
                                        :transactionDetailId,
                                        :batchId,
                                        :productVariantId,
                                        :quantity,
                                        :unitCost,
                                        :totalCost,
                                        NOW()
                                    )
                                    """)
                            .bind("transactionId", transactionId)
                            .bind("transactionDetailId", detailId)
                            .bind("batchId", batchId)
                            .bind("productVariantId", variantId)
                            .bind("quantity", usedQuantity)
                            .bind("unitCost", batchUnitCost)
                            .bind("totalCost", usedTotalCost)
                            .execute();

                    totalCost = totalCost.add(usedTotalCost);
                    remainingToExport -= usedQuantity;
                }

                if (remainingToExport > 0) {
                    return "INSUFFICIENT_BATCH_STOCK";
                }

                BigDecimal averageCost = totalCost.divide(BigDecimal.valueOf(quantity), 2, java.math.RoundingMode.HALF_UP);

                handle.createUpdate("""
                                UPDATE inventory_transaction_details
                                SET unit_cost = :averageCost
                                WHERE id = :detailId
                                """)
                        .bind("averageCost", averageCost)
                        .bind("detailId", detailId)
                        .execute();

                int updatedVariantRows = handle.createUpdate("""
                                UPDATE product_variants
                                SET stock = stock - :quantity
                                WHERE id = :variantId
                                  AND stock >= :quantity
                                """)
                        .bind("quantity", quantity)
                        .bind("variantId", variantId)
                        .execute();

                if (updatedVariantRows <= 0) {
                    return "INSUFFICIENT_STOCK";
                }
            }

            handle.createUpdate("""
                            UPDATE inventory_transactions
                            SET status = 'COMPLETED',
                                updated_at = NOW()
                            WHERE id = :transactionId
                            """)
                    .bind("transactionId", transactionId)
                    .execute();

            handle.createUpdate("""
                            UPDATE orders
                            SET order_status = 'SHIPPING'
                            WHERE id = :orderId
                            """)
                    .bind("orderId", orderId)
                    .execute();

            return "SUCCESS_EXPORTED";
        });
    }

    public InventoryTransaction getExportTransactionByOrderId(int orderId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT it.id,
                               it.code,
                               it.type,
                               it.total_quantity,
                               it.status,
                               it.supplier_name,
                               it.note,
                               it.created_by,
                               it.order_id,
                               COALESCE(u.full_name, u.username, 'Chưa xác định') AS created_by_name,
                               DATE_FORMAT(it.created_at, '%d/%m/%Y %H:%i') AS created_at_text
                        FROM inventory_transactions it
                        LEFT JOIN users u ON it.created_by = u.id
                        WHERE it.order_id = :orderId
                          AND it.type = 'EXPORT'
                        ORDER BY it.id DESC
                        LIMIT 1
                        """)
                        .bind("orderId", orderId)
                        .mapToBean(InventoryTransaction.class)
                        .findOne()
                        .orElse(null)
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

    public int createOrder(int userId, String receiver, String phone, String address, String note,
                           String paymentMethod, double totalPrice, double shippingFee, double discount, LocalDate estimatedDeliveryDate) {
        double finalAmount = Math.max(0, totalPrice + shippingFee - discount);
        return getJdbi().withHandle(h -> h.createUpdate("""
            INSERT INTO orders(user_id, receiver_name, phone, shipping_address, note, total_price, discount, shipping_fee, estimated_delivery_date, final_amount, payment_methods, payment_statuses, order_status, created_at)
            VALUES(:uid, :receiver, :phone, :address, :note, :total, :discount, :shippingFee, :estimatedDeliveryDate, :finalAmount, :payment, 'UNPAID', 'PENDING', NOW())
            """)
                .bind("uid", userId)
                .bind("receiver", receiver)
                .bind("phone", phone)
                .bind("address", address)
                .bind("note", note)
                .bind("total", totalPrice)
                .bind("discount", discount)
                .bind("shippingFee", shippingFee)
                .bind("estimatedDeliveryDate", estimatedDeliveryDate)
                .bind("finalAmount", finalAmount)
                .bind("payment", paymentMethod)
                .executeAndReturnGeneratedKeys("id")
                .mapTo(int.class)
                .one()
        );
    }

    public Order getById(int orderId) {
        String sql = """
        SELECT id, user_id, receiver_name, phone, shipping_address, total_price, discount, shipping_fee, estimated_delivery_date, note, final_amount, payment_methods, payment_statuses, order_status, created_at
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
            java.sql.Date edd = rs.getDate("estimated_delivery_date");
            if (edd != null) o.setEstimatedDeliveryDate(edd.toLocalDate());
            return o;}).findOne().orElse(null));
    }

    public void updatePaymentStatus(int orderId, String paymentStatus) {
        getJdbi().useHandle(handle -> handle.createUpdate("UPDATE orders SET payment_statuses = :status WHERE id = :id").bind("status", paymentStatus).bind("id", orderId).execute()
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
