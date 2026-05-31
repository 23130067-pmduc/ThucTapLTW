package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.InventoryTransaction;

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


    public int createTransaction(String type, String supplierName, String note, Integer createdBy, List<Integer> variantIds, List<Integer> quantities) {
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
                handle.createUpdate("""
                        INSERT INTO inventory_transaction_details(transaction_id, product_variant_id, quantity, note)
                        VALUES(:transactionId, :productVariantId, :quantity, NULL)
                        """)
                        .bind("transactionId", transactionId)
                        .bind("productVariantId", variantIds.get(i))
                        .bind("quantity", quantities.get(i))
                        .execute();
            }

            return transactionId;
        });
    }

    public boolean updateStatusIfPending(int id, String status) {
        int affectedRows = getJdbi().withHandle(handle -> handle.createUpdate("""
                UPDATE inventory_transactions
                SET status = :status,
                    updated_at = NOW()
                WHERE id = :id
                  AND status = 'PENDING'
                """)
                .bind("id", id)
                .bind("status", status)
                .execute());
        return affectedRows > 0;
    }

}
