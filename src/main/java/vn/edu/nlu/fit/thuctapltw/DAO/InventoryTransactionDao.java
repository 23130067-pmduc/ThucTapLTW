package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.InventoryTransaction;

import java.util.List;

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

}
