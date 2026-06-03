package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.InventoryBatch;

import java.math.BigDecimal;
import java.util.List;

public class InventoryBatchDao extends BaseDao {
    public List<InventoryBatch> searchBatches(String keyword, String batchStatus, int limit, int offset) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedStatus = batchStatus == null ? "" : batchStatus.trim();
        String keywordForId = normalizeKeywordForId(normalizedKeyword);

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT ib.id,
                       ib.batch_code,
                       ib.transaction_id,
                       it.code AS transaction_code,
                       ib.transaction_detail_id,
                       ib.product_variant_id,
                       pv.product_id,
                       p.name AS product_name,
                       cp.name AS category_name,
                       c.name AS color_name,
                       s.code AS size_name,
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
                       ) AS thumbnail,
                       ib.import_quantity,
                       ib.remaining_quantity,
                       ib.unit_cost,
                       (ib.import_quantity * ib.unit_cost) AS import_value,
                       (ib.remaining_quantity * ib.unit_cost) AS remaining_value,
                       DATE_FORMAT(ib.created_at, '%d/%m/%Y %H:%i') AS created_at_text
                FROM inventory_batches ib
                JOIN inventory_transactions it ON ib.transaction_id = it.id
                JOIN product_variants pv ON ib.product_variant_id = pv.id
                JOIN products p ON pv.product_id = p.id
                LEFT JOIN category_product cp ON p.category_id = cp.id
                LEFT JOIN colors c ON pv.color_id = c.id
                LEFT JOIN sizes s ON pv.size_id = s.id
                WHERE (:keyword = ''
                       OR ib.batch_code LIKE :keywordLike
                       OR it.code LIKE :keywordLike
                       OR p.name LIKE :keywordLike
                       OR cp.name LIKE :keywordLike
                       OR c.name LIKE :keywordLike
                       OR s.code LIKE :keywordLike
                       OR CAST(ib.id AS CHAR) = :keywordForId
                       OR CAST(ib.product_variant_id AS CHAR) = :keywordForId
                       OR CAST(p.id AS CHAR) = :keywordForId)
                  AND (:batchStatus = ''
                       OR (:batchStatus = 'AVAILABLE' AND ib.remaining_quantity = ib.import_quantity AND ib.remaining_quantity > 0)
                       OR (:batchStatus = 'PARTIAL' AND ib.remaining_quantity > 0 AND ib.remaining_quantity < ib.import_quantity)
                       OR (:batchStatus = 'EMPTY' AND ib.remaining_quantity = 0))
                ORDER BY ib.created_at DESC, ib.id DESC
                LIMIT :limit OFFSET :offset
                """)
                .bind("keyword", normalizedKeyword)
                .bind("keywordLike", "%" + normalizedKeyword + "%")
                .bind("keywordForId", keywordForId)
                .bind("batchStatus", normalizedStatus)
                .bind("limit", limit)
                .bind("offset", offset)
                .mapToBean(InventoryBatch.class)
                .list());
    }

    public int countBatches(String keyword, String batchStatus) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedStatus = batchStatus == null ? "" : batchStatus.trim();
        String keywordForId = normalizeKeywordForId(normalizedKeyword);

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM inventory_batches ib
                JOIN inventory_transactions it ON ib.transaction_id = it.id
                JOIN product_variants pv ON ib.product_variant_id = pv.id
                JOIN products p ON pv.product_id = p.id
                LEFT JOIN category_product cp ON p.category_id = cp.id
                LEFT JOIN colors c ON pv.color_id = c.id
                LEFT JOIN sizes s ON pv.size_id = s.id
                WHERE (:keyword = ''
                       OR ib.batch_code LIKE :keywordLike
                       OR it.code LIKE :keywordLike
                       OR p.name LIKE :keywordLike
                       OR cp.name LIKE :keywordLike
                       OR c.name LIKE :keywordLike
                       OR s.code LIKE :keywordLike
                       OR CAST(ib.id AS CHAR) = :keywordForId
                       OR CAST(ib.product_variant_id AS CHAR) = :keywordForId
                       OR CAST(p.id AS CHAR) = :keywordForId)
                  AND (:batchStatus = ''
                       OR (:batchStatus = 'AVAILABLE' AND ib.remaining_quantity = ib.import_quantity AND ib.remaining_quantity > 0)
                       OR (:batchStatus = 'PARTIAL' AND ib.remaining_quantity > 0 AND ib.remaining_quantity < ib.import_quantity)
                       OR (:batchStatus = 'EMPTY' AND ib.remaining_quantity = 0))
                """)
                .bind("keyword", normalizedKeyword)
                .bind("keywordLike", "%" + normalizedKeyword + "%")
                .bind("keywordForId", keywordForId)
                .bind("batchStatus", normalizedStatus)
                .mapTo(int.class)
                .one());
    }

    public int countAll() {
        return getJdbi().withHandle(handle -> handle.createQuery("SELECT COUNT(*) FROM inventory_batches")
                .mapTo(int.class)
                .one());
    }

    public int countRemainingBatches() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM inventory_batches
                WHERE remaining_quantity > 0
                """)
                .mapTo(int.class)
                .one());
    }

    public int sumRemainingQuantity() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COALESCE(SUM(remaining_quantity), 0)
                FROM inventory_batches
                """)
                .mapTo(int.class)
                .one());
    }

    public BigDecimal sumRemainingValue() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COALESCE(SUM(remaining_quantity * unit_cost), 0)
                FROM inventory_batches
                """)
                .mapTo(BigDecimal.class)
                .one());
    }

    public InventoryBatch getById(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT ib.id,
                       ib.batch_code,
                       ib.transaction_id,
                       it.code AS transaction_code,
                       ib.transaction_detail_id,
                       ib.product_variant_id,
                       pv.product_id,
                       p.name AS product_name,
                       cp.name AS category_name,
                       c.name AS color_name,
                       s.code AS size_name,
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
                       ) AS thumbnail,
                       ib.import_quantity,
                       ib.remaining_quantity,
                       ib.unit_cost,
                       (ib.import_quantity * ib.unit_cost) AS import_value,
                       (ib.remaining_quantity * ib.unit_cost) AS remaining_value,
                       it.supplier_name,
                       COALESCE(u.full_name, u.username, 'Chưa xác định') AS created_by_name,
                       it.note,
                       DATE_FORMAT(ib.created_at, '%d/%m/%Y %H:%i') AS created_at_text,
                       DATE_FORMAT(ib.updated_at, '%d/%m/%Y %H:%i') AS updated_at_text
                FROM inventory_batches ib
                JOIN inventory_transactions it ON ib.transaction_id = it.id
                JOIN product_variants pv ON ib.product_variant_id = pv.id
                JOIN products p ON pv.product_id = p.id
                LEFT JOIN users u ON it.created_by = u.id
                LEFT JOIN category_product cp ON p.category_id = cp.id
                LEFT JOIN colors c ON pv.color_id = c.id
                LEFT JOIN sizes s ON pv.size_id = s.id
                WHERE ib.id = :id
                """)
                .bind("id", id)
                .mapToBean(InventoryBatch.class)
                .findOne()
                .orElse(null));
    }

    private String normalizeKeywordForId(String keyword) {
        if (keyword == null) {
            return "";
        }
        String value = keyword.trim();
        if (value.startsWith("#")) {
            value = value.substring(1);
        }
        return value.trim();
    }
}
