package vn.edu.nlu.fit.thuctapltw.DAO;

import org.jdbi.v3.core.Handle;
import vn.edu.nlu.fit.thuctapltw.model.Order;
import vn.edu.nlu.fit.thuctapltw.model.ReturnOrder;
import vn.edu.nlu.fit.thuctapltw.model.ReturnOrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ReturnOrderDao extends BaseDao {

    public List<ReturnOrder> search(String keyword, String status) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedStatus = status == null ? "" : status.trim().toUpperCase();

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT ro.id,
                       ro.code,
                       ro.order_id,
                       ro.user_id,
                       ro.reason,
                       ro.status,
                       ro.admin_note,
                       ro.inventory_transaction_id,
                       it.code AS inventory_transaction_code,
                       ro.created_by,
                       ro.created_at,
                       ro.updated_at,
                       DATE_FORMAT(ro.created_at, '%d/%m/%Y %H:%i') AS created_at_text,
                       DATE_FORMAT(ro.updated_at, '%d/%m/%Y %H:%i') AS updated_at_text,
                       o.receiver_name,
                       o.phone,
                       COUNT(roi.id) AS item_count,
                       COALESCE(SUM(roi.quantity), 0) AS total_quantity
                FROM return_orders ro
                JOIN orders o ON ro.order_id = o.id
                LEFT JOIN return_order_items roi ON ro.id = roi.return_order_id
                LEFT JOIN inventory_transactions it ON ro.inventory_transaction_id = it.id
                WHERE (:status = '' OR ro.status = :status)
                  AND (:keyword = ''
                       OR ro.code LIKE :keywordLike
                       OR CAST(ro.order_id AS CHAR) LIKE :keywordLike
                       OR o.receiver_name LIKE :keywordLike
                       OR o.phone LIKE :keywordLike)
                GROUP BY ro.id, ro.code, ro.order_id, ro.user_id, ro.reason, ro.status, ro.admin_note,
                         ro.inventory_transaction_id, it.code, ro.created_by, ro.created_at, ro.updated_at,
                         o.receiver_name, o.phone
                ORDER BY ro.created_at DESC, ro.id DESC
                """)
                .bind("status", normalizedStatus)
                .bind("keyword", normalizedKeyword)
                .bind("keywordLike", "%" + normalizedKeyword + "%")
                .mapToBean(ReturnOrder.class)
                .list());
    }

    public ReturnOrder findById(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT ro.id,
                       ro.code,
                       ro.order_id,
                       ro.user_id,
                       ro.reason,
                       ro.status,
                       ro.admin_note,
                       ro.inventory_transaction_id,
                       it.code AS inventory_transaction_code,
                       ro.created_by,
                       ro.created_at,
                       ro.updated_at,
                       DATE_FORMAT(ro.created_at, '%d/%m/%Y %H:%i') AS created_at_text,
                       DATE_FORMAT(ro.updated_at, '%d/%m/%Y %H:%i') AS updated_at_text,
                       o.receiver_name,
                       o.phone,
                       COUNT(roi.id) AS item_count,
                       COALESCE(SUM(roi.quantity), 0) AS total_quantity
                FROM return_orders ro
                JOIN orders o ON ro.order_id = o.id
                LEFT JOIN return_order_items roi ON ro.id = roi.return_order_id
                LEFT JOIN inventory_transactions it ON ro.inventory_transaction_id = it.id
                WHERE ro.id = :id
                GROUP BY ro.id, ro.code, ro.order_id, ro.user_id, ro.reason, ro.status, ro.admin_note,
                         ro.inventory_transaction_id, it.code, ro.created_by, ro.created_at, ro.updated_at,
                         o.receiver_name, o.phone
                """)
                .bind("id", id)
                .mapToBean(ReturnOrder.class)
                .findOne()
                .orElse(null));
    }

    public List<ReturnOrderItem> getItems(int returnOrderId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT roi.id,
                       roi.return_order_id,
                       roi.order_item_id,
                       roi.product_variant_id,
                       roi.product_name,
                       roi.size,
                       roi.color,
                       roi.quantity,
                       roi.price,
                       roi.total,
                       roi.note,
                       COALESCE(
                           NULLIF(p.thumbnail, ''),
                           (
                               SELECT pi.image_url
                               FROM product_images pi
                               WHERE pi.product_id = p.id
                               ORDER BY pi.is_main DESC, pi.id ASC
                               LIMIT 1
                           ),
                           'img/gau.png'
                       ) AS thumbnail
                FROM return_order_items roi
                LEFT JOIN product_variants pv ON roi.product_variant_id = pv.id
                LEFT JOIN products p ON pv.product_id = p.id
                WHERE roi.return_order_id = :returnOrderId
                ORDER BY roi.id ASC
                """)
                .bind("returnOrderId", returnOrderId)
                .mapToBean(ReturnOrderItem.class)
                .list());
    }

    public ReturnOrder findActiveByOrderId(int orderId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT ro.id,
                       ro.code,
                       ro.order_id,
                       ro.user_id,
                       ro.reason,
                       ro.status,
                       ro.admin_note,
                       ro.inventory_transaction_id,
                       ro.created_by,
                       ro.created_at,
                       ro.updated_at,
                       DATE_FORMAT(ro.created_at, '%d/%m/%Y %H:%i') AS created_at_text,
                       DATE_FORMAT(ro.updated_at, '%d/%m/%Y %H:%i') AS updated_at_text
                FROM return_orders ro
                WHERE ro.order_id = :orderId
                  AND ro.status IN ('PENDING', 'APPROVED', 'COMPLETED')
                ORDER BY ro.id DESC
                LIMIT 1
                """)
                .bind("orderId", orderId)
                .mapToBean(ReturnOrder.class)
                .findOne()
                .orElse(null));
    }

    public int createReturnRequest(int orderId, int userId, String reason) {
        return getJdbi().inTransaction(handle -> {
            Order order = handle.createQuery("""
                            SELECT *
                            FROM orders
                            WHERE id = :orderId AND user_id = :userId
                            FOR UPDATE
                            """)
                    .bind("orderId", orderId)
                    .bind("userId", userId)
                    .mapToBean(Order.class)
                    .findOne()
                    .orElse(null);

            if (order == null) {
                throw new RuntimeException("Không tìm thấy đơn hàng");
            }

            if (!"COMPLETED".equalsIgnoreCase(order.getOrderStatus())) {
                throw new RuntimeException("Chỉ có thể yêu cầu hoàn hàng với đơn đã hoàn thành");
            }

            Integer activeReturnId = handle.createQuery("""
                            SELECT id
                            FROM return_orders
                            WHERE order_id = :orderId
                              AND status IN ('PENDING', 'APPROVED', 'COMPLETED')
                            LIMIT 1
                            """)
                    .bind("orderId", orderId)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(null);

            if (activeReturnId != null) {
                throw new RuntimeException("Đơn hàng này đã có phiếu hoàn hàng");
            }

            int returnId = handle.createUpdate("""
                            INSERT INTO return_orders(
                                code,
                                order_id,
                                user_id,
                                reason,
                                status,
                                created_by,
                                created_at
                            ) VALUES (
                                '',
                                :orderId,
                                :userId,
                                :reason,
                                'PENDING',
                                :userId,
                                NOW()
                            )
                            """)
                    .bind("orderId", orderId)
                    .bind("userId", userId)
                    .bind("reason", reason)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(int.class)
                    .one();

            String code = "TH" + String.format("%05d", returnId);
            handle.createUpdate("UPDATE return_orders SET code = :code WHERE id = :id")
                    .bind("code", code)
                    .bind("id", returnId)
                    .execute();

            List<Map<String, Object>> orderItems = handle.createQuery("""
                            SELECT id, variant_id, product_name, size, color, quantity, price, total
                            FROM order_items
                            WHERE order_id = :orderId
                            """)
                    .bind("orderId", orderId)
                    .mapToMap()
                    .list();

            if (orderItems.isEmpty()) {
                throw new RuntimeException("Đơn hàng không có sản phẩm để hoàn");
            }

            for (Map<String, Object> item : orderItems) {
                handle.createUpdate("""
                                INSERT INTO return_order_items(
                                    return_order_id,
                                    order_item_id,
                                    product_variant_id,
                                    product_name,
                                    size,
                                    color,
                                    quantity,
                                    price,
                                    total,
                                    note
                                ) VALUES (
                                    :returnOrderId,
                                    :orderItemId,
                                    :productVariantId,
                                    :productName,
                                    :size,
                                    :color,
                                    :quantity,
                                    :price,
                                    :total,
                                    NULL
                                )
                                """)
                        .bind("returnOrderId", returnId)
                        .bind("orderItemId", ((Number) item.get("id")).intValue())
                        .bind("productVariantId", ((Number) item.get("variant_id")).intValue())
                        .bind("productName", item.get("product_name"))
                        .bind("size", item.get("size"))
                        .bind("color", item.get("color"))
                        .bind("quantity", ((Number) item.get("quantity")).intValue())
                        .bind("price", item.get("price"))
                        .bind("total", item.get("total"))
                        .execute();
            }

            return returnId;
        });
    }

    public String approve(int id, Integer adminId, String adminNote) {
        return updateSimpleStatus(id, "PENDING", "APPROVED", adminNote);
    }

    public String reject(int id, Integer adminId, String adminNote) {
        return updateSimpleStatus(id, "PENDING", "REJECTED", adminNote);
    }

    private String updateSimpleStatus(int id, String expectedCurrentStatus, String newStatus, String adminNote) {
        return getJdbi().inTransaction(handle -> {
            ReturnOrder ro = handle.createQuery("SELECT * FROM return_orders WHERE id = :id FOR UPDATE")
                    .bind("id", id)
                    .mapToBean(ReturnOrder.class)
                    .findOne()
                    .orElse(null);

            if (ro == null) return "RETURN_NOT_FOUND";
            if (!expectedCurrentStatus.equalsIgnoreCase(ro.getStatus())) return "INVALID_STATUS";

            handle.createUpdate("""
                            UPDATE return_orders
                            SET status = :status,
                                admin_note = :adminNote,
                                updated_at = NOW()
                            WHERE id = :id
                            """)
                    .bind("status", newStatus)
                    .bind("adminNote", adminNote)
                    .bind("id", id)
                    .execute();
            return "SUCCESS";
        });
    }

    public String completeReturn(int id, Integer adminId, String adminNote) {
        return getJdbi().inTransaction(handle -> {
            ReturnOrder ro = handle.createQuery("""
                            SELECT *
                            FROM return_orders
                            WHERE id = :id
                            FOR UPDATE
                            """)
                    .bind("id", id)
                    .mapToBean(ReturnOrder.class)
                    .findOne()
                    .orElse(null);

            if (ro == null) return "RETURN_NOT_FOUND";
            if (!"APPROVED".equalsIgnoreCase(ro.getStatus())) return "INVALID_STATUS";
            if (ro.getInventoryTransactionId() != null && ro.getInventoryTransactionId() > 0) return "ALREADY_IMPORTED";

            List<ReturnOrderItem> items = getItemsInTransaction(handle, id);
            if (items.isEmpty()) return "RETURN_EMPTY";

            int totalQuantity = items.stream().mapToInt(ReturnOrderItem::getQuantity).sum();
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
                                'IMPORT',
                                :totalQuantity,
                                'PENDING',
                                'Khách hoàn hàng',
                                :note,
                                :createdBy,
                                :orderId,
                                NOW()
                            )
                            """)
                    .bind("totalQuantity", totalQuantity)
                    .bind("note", "Nhập lại kho từ phiếu hoàn hàng " + ro.getCode() + " - đơn #" + ro.getOrderId())
                    .bind("createdBy", adminId)
                    .bind("orderId", ro.getOrderId())
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(int.class)
                    .one();

            String transactionCode = "PNH" + String.format("%05d", transactionId);
            handle.createUpdate("UPDATE inventory_transactions SET code = :code WHERE id = :id")
                    .bind("code", transactionCode)
                    .bind("id", transactionId)
                    .execute();

            for (ReturnOrderItem item : items) {
                BigDecimal unitCost = findReturnUnitCost(handle, ro.getOrderId(), item.getProductVariantId());

                int detailId = handle.createUpdate("""
                                INSERT INTO inventory_transaction_details(
                                    transaction_id,
                                    product_variant_id,
                                    quantity,
                                    unit_cost,
                                    note
                                ) VALUES (
                                    :transactionId,
                                    :variantId,
                                    :quantity,
                                    :unitCost,
                                    'Nhập lại từ hoàn hàng'
                                )
                                """)
                        .bind("transactionId", transactionId)
                        .bind("variantId", item.getProductVariantId())
                        .bind("quantity", item.getQuantity())
                        .bind("unitCost", unitCost)
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(int.class)
                        .one();

                handle.createUpdate("""
                                INSERT INTO inventory_batches(
                                    batch_code,
                                    transaction_id,
                                    transaction_detail_id,
                                    product_variant_id,
                                    import_quantity,
                                    remaining_quantity,
                                    unit_cost,
                                    status,
                                    created_at,
                                    note
                                ) VALUES (
                                    :batchCode,
                                    :transactionId,
                                    :detailId,
                                    :variantId,
                                    :quantity,
                                    :quantity,
                                    :unitCost,
                                    'ACTIVE',
                                    NOW(),
                                    :note
                                )
                                """)
                        .bind("batchCode", "RT-" + ro.getId() + "-" + item.getProductVariantId() + "-" + detailId)
                        .bind("transactionId", transactionId)
                        .bind("detailId", detailId)
                        .bind("variantId", item.getProductVariantId())
                        .bind("quantity", item.getQuantity())
                        .bind("unitCost", unitCost)
                        .bind("note", "Lô nhập lại từ phiếu hoàn hàng " + ro.getCode())
                        .execute();

                handle.createUpdate("""
                                UPDATE product_variants
                                SET stock = stock + :quantity
                                WHERE id = :variantId
                                """)
                        .bind("quantity", item.getQuantity())
                        .bind("variantId", item.getProductVariantId())
                        .execute();
            }

            handle.createUpdate("""
                            UPDATE inventory_transactions
                            SET status = 'COMPLETED', updated_at = NOW()
                            WHERE id = :id
                            """)
                    .bind("id", transactionId)
                    .execute();

            handle.createUpdate("""
                            UPDATE return_orders
                            SET status = 'COMPLETED',
                                inventory_transaction_id = :transactionId,
                                admin_note = :adminNote,
                                updated_at = NOW()
                            WHERE id = :id
                            """)
                    .bind("transactionId", transactionId)
                    .bind("adminNote", adminNote)
                    .bind("id", id)
                    .execute();

            return "SUCCESS_IMPORTED";
        });
    }

    private List<ReturnOrderItem> getItemsInTransaction(Handle handle, int returnOrderId) {
        return handle.createQuery("""
                SELECT id,
                       return_order_id,
                       order_item_id,
                       product_variant_id,
                       product_name,
                       size,
                       color,
                       quantity,
                       price,
                       total,
                       note
                FROM return_order_items
                WHERE return_order_id = :returnOrderId
                ORDER BY id ASC
                """)
                .bind("returnOrderId", returnOrderId)
                .mapToBean(ReturnOrderItem.class)
                .list();
    }

    private BigDecimal findReturnUnitCost(Handle handle, int orderId, int variantId) {
        BigDecimal fromExportUsage = handle.createQuery("""
                        SELECT COALESCE(SUM(ibu.total_cost) / NULLIF(SUM(ibu.quantity), 0), 0)
                        FROM inventory_batch_usages ibu
                        JOIN inventory_transactions it ON ibu.transaction_id = it.id
                        WHERE it.order_id = :orderId
                          AND it.type = 'EXPORT'
                          AND ibu.product_variant_id = :variantId
                        """)
                .bind("orderId", orderId)
                .bind("variantId", variantId)
                .mapTo(BigDecimal.class)
                .one();

        if (fromExportUsage != null && fromExportUsage.compareTo(BigDecimal.ZERO) > 0) {
            return fromExportUsage.setScale(2, java.math.RoundingMode.HALF_UP);
        }

        BigDecimal latestBatchCost = handle.createQuery("""
                        SELECT COALESCE(unit_cost, 0)
                        FROM inventory_batches
                        WHERE product_variant_id = :variantId
                        ORDER BY created_at DESC, id DESC
                        LIMIT 1
                        """)
                .bind("variantId", variantId)
                .mapTo(BigDecimal.class)
                .findOne()
                .orElse(BigDecimal.ZERO);

        if (latestBatchCost != null && latestBatchCost.compareTo(BigDecimal.ZERO) > 0) {
            return latestBatchCost.setScale(2, java.math.RoundingMode.HALF_UP);
        }

        return handle.createQuery("""
                        SELECT COALESCE(pv.price * 0.6, 0)
                        FROM product_variants pv
                        WHERE pv.id = :variantId
                        """)
                .bind("variantId", variantId)
                .mapTo(BigDecimal.class)
                .findOne()
                .orElse(BigDecimal.ZERO)
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
