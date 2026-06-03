package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.InventoryTransaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class InventoryTransactionDao extends BaseDao {
    public List<InventoryTransaction> searchTransactions(String keyword, String type, String status, int limit, int offset) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedType = type == null ? "" : type.trim();
        String normalizedStatus = status == null ? "" : status.trim();

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT it.id,
                       it.code,
                       it.type,
                       it.total_quantity,
                       it.status,
                       it.supplier_name,
                       it.note,
                       it.created_by,
                       COALESCE(u.full_name, u.username, 'Chưa xác định') AS created_by_name,
                       DATE_FORMAT(it.created_at, '%d/%m/%Y %H:%i') AS created_at_text
                FROM inventory_transactions it
                LEFT JOIN users u ON it.created_by = u.id
                WHERE (:keyword = '' OR it.code LIKE :keywordLike OR it.supplier_name LIKE :keywordLike OR it.note LIKE :keywordLike)
                  AND (:type = '' OR it.type = :type)
                  AND (:status = '' OR it.status = :status)
                ORDER BY it.created_at DESC, it.id DESC
                LIMIT :limit OFFSET :offset
                """)
                .bind("keyword", normalizedKeyword)
                .bind("keywordLike", "%" + normalizedKeyword + "%")
                .bind("type", normalizedType)
                .bind("status", normalizedStatus)
                .bind("limit", limit)
                .bind("offset", offset)
                .mapToBean(InventoryTransaction.class)
                .list());
    }

    public int countTransactions(String keyword, String type, String status) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedType = type == null ? "" : type.trim();
        String normalizedStatus = status == null ? "" : status.trim();

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM inventory_transactions it
                WHERE (:keyword = '' OR it.code LIKE :keywordLike OR it.supplier_name LIKE :keywordLike OR it.note LIKE :keywordLike)
                  AND (:type = '' OR it.type = :type)
                  AND (:status = '' OR it.status = :status)
                """)
                .bind("keyword", normalizedKeyword)
                .bind("keywordLike", "%" + normalizedKeyword + "%")
                .bind("type", normalizedType)
                .bind("status", normalizedStatus)
                .mapTo(int.class)
                .one());
    }

    public int countAll() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM inventory_transactions
                """)
                .mapTo(int.class)
                .one());
    }

    public int countByType(String type) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM inventory_transactions
                WHERE type = :type
                """)
                .bind("type", type)
                .mapTo(int.class)
                .one());
    }

    public int countByStatus(String status) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM inventory_transactions
                WHERE status = :status
                """)
                .bind("status", status)
                .mapTo(int.class)
                .one());
    }

    public InventoryTransaction getById(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT it.id,
                       it.code,
                       it.type,
                       it.total_quantity,
                       it.status,
                       it.supplier_name,
                       it.note,
                       it.created_by,
                       COALESCE(u.full_name, u.username, 'Chưa xác định') AS created_by_name,
                       DATE_FORMAT(it.created_at, '%d/%m/%Y %H:%i') AS created_at_text
                FROM inventory_transactions it
                LEFT JOIN users u ON it.created_by = u.id
                WHERE it.id = :id
                """)
                .bind("id", id)
                .mapToBean(InventoryTransaction.class)
                .findOne()
                .orElse(null));
    }

    public Map<Integer, Integer> getVariantStockMap(List<Integer> variantIds) {
        if (variantIds == null || variantIds.isEmpty()) {
            return Map.of();
        }

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT id, stock
                FROM product_variants
                WHERE id IN (<variantIds>)
                """)
                .bindList("variantIds", variantIds)
                .mapToMap()
                .list()
                .stream()
                .collect(java.util.stream.Collectors.toMap(
                        row -> ((Number) row.get("id")).intValue(),
                        row -> ((Number) row.get("stock")).intValue()
                )));
    }


    public Map<Integer, Integer> getAvailableBatchQuantityMap(List<Integer> variantIds) {
        if (variantIds == null || variantIds.isEmpty()) {
            return Map.of();
        }

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT product_variant_id, CAST(COALESCE(SUM(remaining_quantity), 0) AS SIGNED) AS available_quantity
                FROM inventory_batches
                WHERE product_variant_id IN (<variantIds>)
                  AND remaining_quantity > 0
                GROUP BY product_variant_id
                """)
                .bindList("variantIds", variantIds)
                .mapToMap()
                .list()
                .stream()
                .collect(java.util.stream.Collectors.toMap(
                        row -> ((Number) row.get("product_variant_id")).intValue(),
                        row -> ((Number) row.get("available_quantity")).intValue()
                )));
    }


    public int createTransaction(String type, String supplierName, String note, Integer createdBy,
                                 List<Integer> variantIds, List<Integer> quantities, List<BigDecimal> unitCosts) {
        int totalQuantity = quantities.stream().mapToInt(Integer::intValue).sum();
        String prefix = "IMPORT".equals(type) ? "PN" : "PX";

        return getJdbi().inTransaction(handle -> {
            int transactionId = handle.createUpdate("""
                    INSERT INTO inventory_transactions(code, type, total_quantity, status, supplier_name, note, created_by, created_at)
                    VALUES('', :type, :totalQuantity, 'PENDING', :supplierName, :note, :createdBy, NOW())
                    """)
                    .bind("type", type)
                    .bind("totalQuantity", totalQuantity)
                    .bind("supplierName", supplierName == null || supplierName.isBlank() ? null : supplierName.trim())
                    .bind("note", note == null || note.isBlank() ? null : note.trim())
                    .bind("createdBy", createdBy)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(int.class)
                    .one();

            String code = prefix + String.format("%05d", transactionId);
            handle.createUpdate("""
                    UPDATE inventory_transactions
                    SET code = :code
                    WHERE id = :id
                    """)
                    .bind("code", code)
                    .bind("id", transactionId)
                    .execute();

            for (int i = 0; i < variantIds.size(); i++) {
                BigDecimal unitCost = BigDecimal.ZERO;
                if ("IMPORT".equals(type) && unitCosts != null && i < unitCosts.size() && unitCosts.get(i) != null) {
                    unitCost = unitCosts.get(i);
                }

                handle.createUpdate("""
                        INSERT INTO inventory_transaction_details(transaction_id, product_variant_id, quantity, unit_cost, note)
                        VALUES(:transactionId, :productVariantId, :quantity, :unitCost, NULL)
                        """)
                        .bind("transactionId", transactionId)
                        .bind("productVariantId", variantIds.get(i))
                        .bind("quantity", quantities.get(i))
                        .bind("unitCost", unitCost)
                        .execute();
            }

            return transactionId;
        });
    }


    public String updateStatusIfPending(int id, String status) {
        return getJdbi().inTransaction(handle -> {
            InventoryTransaction transaction = handle.createQuery("""
                    SELECT id, code, type, status
                    FROM inventory_transactions
                    WHERE id = :id
                    FOR UPDATE
                    """)
                    .bind("id", id)
                    .mapToBean(InventoryTransaction.class)
                    .findOne()
                    .orElse(null);

            if (transaction == null) {
                return "NOT_FOUND";
            }

            if (!"PENDING".equals(transaction.getStatus())) {
                return "ALREADY_PROCESSED";
            }

            if ("CANCELLED".equals(status)) {
                int affectedRows = handle.createUpdate("""
                        UPDATE inventory_transactions
                        SET status = 'CANCELLED',
                            updated_at = NOW()
                        WHERE id = :id
                          AND status = 'PENDING'
                        """)
                        .bind("id", id)
                        .execute();

                return affectedRows > 0 ? "SUCCESS" : "FAILED";
            }

            if (!"COMPLETED".equals(status)) {
                return "INVALID_STATUS";
            }

            List<Map<String, Object>> details = handle.createQuery("""
                    SELECT id, product_variant_id, quantity, unit_cost
                    FROM inventory_transaction_details
                    WHERE transaction_id = :transactionId
                    """)
                    .bind("transactionId", id)
                    .mapToMap()
                    .list();

            if ("EXPORT".equals(transaction.getType())) {
                for (Map<String, Object> detail : details) {
                    int variantId = ((Number) detail.get("product_variant_id")).intValue();
                    int quantity = ((Number) detail.get("quantity")).intValue();

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

                for (Map<String, Object> detail : details) {
                    int detailId = ((Number) detail.get("id")).intValue();
                    int variantId = ((Number) detail.get("product_variant_id")).intValue();
                    int quantity = ((Number) detail.get("quantity")).intValue();
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

                        handle.createUpdate("""
                                UPDATE inventory_batches
                                SET remaining_quantity = remaining_quantity - :usedQuantity,
                                    updated_at = NOW()
                                WHERE id = :batchId
                                  AND remaining_quantity >= :usedQuantity
                                """)
                                .bind("usedQuantity", usedQuantity)
                                .bind("batchId", batchId)
                                .execute();

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
                                .bind("transactionId", id)
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

                    BigDecimal averageCost = quantity > 0
                            ? totalCost.divide(BigDecimal.valueOf(quantity), 2, java.math.RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;

                    handle.createUpdate("""
                            UPDATE inventory_transaction_details
                            SET unit_cost = :averageCost
                            WHERE id = :detailId
                            """)
                            .bind("averageCost", averageCost)
                            .bind("detailId", detailId)
                            .execute();

                    handle.createUpdate("""
                            UPDATE product_variants
                            SET stock = stock - :quantity
                            WHERE id = :variantId
                            """)
                            .bind("quantity", quantity)
                            .bind("variantId", variantId)
                            .execute();
                }
            } else if ("IMPORT".equals(transaction.getType())) {
                for (Map<String, Object> detail : details) {
                    int detailId = ((Number) detail.get("id")).intValue();
                    int variantId = ((Number) detail.get("product_variant_id")).intValue();
                    int quantity = ((Number) detail.get("quantity")).intValue();
                    BigDecimal unitCost = detail.get("unit_cost") == null
                            ? BigDecimal.ZERO
                            : (BigDecimal) detail.get("unit_cost");

                    if (unitCost.compareTo(BigDecimal.ZERO) <= 0) {
                        return "MISSING_UNIT_COST";
                    }

                    handle.createUpdate("""
                            UPDATE product_variants
                            SET stock = stock + :quantity
                            WHERE id = :variantId
                            """)
                            .bind("quantity", quantity)
                            .bind("variantId", variantId)
                            .execute();

                    String batchCode = transaction.getCode() + "-BT" + detailId;
                    handle.createUpdate("""
                            INSERT INTO inventory_batches(
                                batch_code,
                                transaction_id,
                                transaction_detail_id,
                                product_variant_id,
                                import_quantity,
                                remaining_quantity,
                                unit_cost,
                                created_at
                            ) VALUES (
                                :batchCode,
                                :transactionId,
                                :transactionDetailId,
                                :productVariantId,
                                :importQuantity,
                                :remainingQuantity,
                                :unitCost,
                                NOW()
                            )
                            """)
                            .bind("batchCode", batchCode)
                            .bind("transactionId", id)
                            .bind("transactionDetailId", detailId)
                            .bind("productVariantId", variantId)
                            .bind("importQuantity", quantity)
                            .bind("remainingQuantity", quantity)
                            .bind("unitCost", unitCost)
                            .execute();
                }
            } else {
                return "INVALID_TYPE";
            }

            int affectedRows = handle.createUpdate("""
                    UPDATE inventory_transactions
                    SET status = 'COMPLETED',
                        updated_at = NOW()
                    WHERE id = :id
                      AND status = 'PENDING'
                    """)
                    .bind("id", id)
                    .execute();

            return affectedRows > 0 ? "SUCCESS" : "FAILED";
        });
    }


}
