package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.InventoryItem;

import java.util.List;

public class InventoryDao extends BaseDao {
    public List<InventoryItem> searchInventory(String keyword, String stockStatus, String sortField, String sortDir, int limit, int offset) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedStatus = stockStatus == null ? "" : stockStatus.trim();
        String keywordForId = normalizeKeywordForId(normalizedKeyword);
        String orderBy = buildInventoryOrderBy(sortField, sortDir);

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT pv.id AS variant_id,
                       p.id AS product_id,
                       p.name AS product_name,
                       p.thumbnail,
                       c.name AS category_name,
                       co.name AS color_name,
                       s.code AS size_name,
                       pv.stock,
                       pv.price,
                       pv.sale_price,
                       p.status AS product_status,
                       (
                           SELECT ib.unit_cost
                           FROM inventory_batches ib
                           WHERE ib.product_variant_id = pv.id
                           ORDER BY ib.created_at DESC, ib.id DESC
                           LIMIT 1
                       ) AS latest_unit_cost,
                       (
                           SELECT ib.batch_code
                           FROM inventory_batches ib
                           WHERE ib.product_variant_id = pv.id
                           ORDER BY ib.created_at DESC, ib.id DESC
                           LIMIT 1
                       ) AS latest_batch_code,
                       (
                           SELECT DATE_FORMAT(ib.created_at, '%d/%m/%Y')
                           FROM inventory_batches ib
                           WHERE ib.product_variant_id = pv.id
                           ORDER BY ib.created_at DESC, ib.id DESC
                           LIMIT 1
                       ) AS latest_import_date_text,
                       COALESCE((
                           SELECT SUM(ib.remaining_quantity)
                           FROM inventory_batches ib
                           WHERE ib.product_variant_id = pv.id
                       ), 0) AS remaining_batch_quantity
                FROM product_variants pv
                JOIN products p ON pv.product_id = p.id
                LEFT JOIN category_product c ON p.category_id = c.id
                LEFT JOIN colors co ON pv.color_id = co.id
                LEFT JOIN sizes s ON pv.size_id = s.id
                WHERE p.status <> 'Đã xoá'
                  AND (
                        :keyword = ''
                        OR CAST(pv.id AS CHAR) = :keywordForId
                        OR CAST(p.id AS CHAR) = :keywordForId
                        OR CAST(pv.id AS CHAR) LIKE :keywordForIdLike
                        OR CAST(p.id AS CHAR) LIKE :keywordForIdLike
                        OR p.name LIKE :keywordLike
                        OR c.name LIKE :keywordLike
                        OR co.name LIKE :keywordLike
                        OR s.code LIKE :keywordLike
                      )
                  AND (
                        :stockStatus = ''
                        OR (:stockStatus = 'OUT' AND pv.stock = 0)
                        OR (:stockStatus = 'LOW' AND pv.stock > 0 AND pv.stock <= 10)
                        OR (:stockStatus = 'AVAILABLE' AND pv.stock > 10)
                      )
                """ + orderBy + """
                LIMIT :limit OFFSET :offset
                """)
                .bind("keyword", normalizedKeyword)
                .bind("keywordLike", "%" + normalizedKeyword + "%")
                .bind("keywordForId", keywordForId)
                .bind("keywordForIdLike", "%" + keywordForId + "%")
                .bind("stockStatus", normalizedStatus)
                .bind("limit", limit)
                .bind("offset", offset)
                .mapToBean(InventoryItem.class)
                .list());
    }

    public int countInventoryByFilter(String keyword, String stockStatus) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedStatus = stockStatus == null ? "" : stockStatus.trim();
        String keywordForId = normalizeKeywordForId(normalizedKeyword);

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM product_variants pv
                JOIN products p ON pv.product_id = p.id
                LEFT JOIN category_product c ON p.category_id = c.id
                LEFT JOIN colors co ON pv.color_id = co.id
                LEFT JOIN sizes s ON pv.size_id = s.id
                WHERE p.status <> 'Đã xoá'
                  AND (
                        :keyword = ''
                        OR CAST(pv.id AS CHAR) = :keywordForId
                        OR CAST(p.id AS CHAR) = :keywordForId
                        OR CAST(pv.id AS CHAR) LIKE :keywordForIdLike
                        OR CAST(p.id AS CHAR) LIKE :keywordForIdLike
                        OR p.name LIKE :keywordLike
                        OR c.name LIKE :keywordLike
                        OR co.name LIKE :keywordLike
                        OR s.code LIKE :keywordLike
                      )
                  AND (
                        :stockStatus = ''
                        OR (:stockStatus = 'OUT' AND pv.stock = 0)
                        OR (:stockStatus = 'LOW' AND pv.stock > 0 AND pv.stock <= 10)
                        OR (:stockStatus = 'AVAILABLE' AND pv.stock > 10)
                      )
                """)
                .bind("keyword", normalizedKeyword)
                .bind("keywordLike", "%" + normalizedKeyword + "%")
                .bind("keywordForId", keywordForId)
                .bind("keywordForIdLike", "%" + keywordForId + "%")
                .bind("stockStatus", normalizedStatus)
                .mapTo(int.class)
                .one());
    }

    public int countTotalVariants() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM product_variants pv
                JOIN products p ON pv.product_id = p.id
                WHERE p.status <> 'Đã xoá'
                """)
                .mapTo(int.class)
                .one());
    }

    public int countLowStock() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM product_variants pv
                JOIN products p ON pv.product_id = p.id
                WHERE p.status <> 'Đã xoá'
                  AND pv.stock > 0
                  AND pv.stock <= 10
                """)
                .mapTo(int.class)
                .one());
    }

    public int countOutOfStock() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM product_variants pv
                JOIN products p ON pv.product_id = p.id
                WHERE p.status <> 'Đã xoá'
                  AND pv.stock = 0
                """)
                .mapTo(int.class)
                .one());
    }

    public int sumStock() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COALESCE(SUM(pv.stock), 0)
                FROM product_variants pv
                JOIN products p ON pv.product_id = p.id
                WHERE p.status <> 'Đã xoá'
                """)
                .mapTo(int.class)
                .one());
    }
    public List<InventoryItem> getInventoryItemsForTransaction() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT pv.id AS variant_id,
                       p.id AS product_id,
                       p.name AS product_name,
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
                       c.name AS category_name,
                       co.name AS color_name,
                       s.code AS size_name,
                       pv.stock,
                       pv.price,
                       pv.sale_price,
                       p.status AS product_status,
                       (
                           SELECT ib.unit_cost
                           FROM inventory_batches ib
                           WHERE ib.product_variant_id = pv.id
                           ORDER BY ib.created_at DESC, ib.id DESC
                           LIMIT 1
                       ) AS latest_unit_cost,
                       (
                           SELECT ib.batch_code
                           FROM inventory_batches ib
                           WHERE ib.product_variant_id = pv.id
                           ORDER BY ib.created_at DESC, ib.id DESC
                           LIMIT 1
                       ) AS latest_batch_code,
                       (
                           SELECT DATE_FORMAT(ib.created_at, '%d/%m/%Y')
                           FROM inventory_batches ib
                           WHERE ib.product_variant_id = pv.id
                           ORDER BY ib.created_at DESC, ib.id DESC
                           LIMIT 1
                       ) AS latest_import_date_text,
                       COALESCE((
                           SELECT SUM(ib.remaining_quantity)
                           FROM inventory_batches ib
                           WHERE ib.product_variant_id = pv.id
                       ), 0) AS remaining_batch_quantity
                FROM product_variants pv
                JOIN products p ON pv.product_id = p.id
                LEFT JOIN category_product c ON p.category_id = c.id
                LEFT JOIN colors co ON pv.color_id = co.id
                LEFT JOIN sizes s ON pv.size_id = s.id
                WHERE p.status <> 'Đã xoá'
                ORDER BY p.name ASC, co.name ASC, s.sort_order ASC, pv.id ASC
                """)
                .mapToBean(InventoryItem.class)
                .list());
    }

    public List<InventoryItem> getInventoryItemsForGoogleSheet() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT pv.id AS variant_id,
                       p.id AS product_id,
                       p.name AS product_name,
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
                       c.name AS category_name,
                       co.name AS color_name,
                       s.code AS size_name,
                       pv.stock,
                       pv.price,
                       pv.sale_price,
                       p.status AS product_status,
                       (
                           SELECT ib.unit_cost
                           FROM inventory_batches ib
                           WHERE ib.product_variant_id = pv.id
                           ORDER BY ib.created_at DESC, ib.id DESC
                           LIMIT 1
                       ) AS latest_unit_cost,
                       (
                           SELECT ib.batch_code
                           FROM inventory_batches ib
                           WHERE ib.product_variant_id = pv.id
                           ORDER BY ib.created_at DESC, ib.id DESC
                           LIMIT 1
                       ) AS latest_batch_code,
                       (
                           SELECT DATE_FORMAT(ib.created_at, '%d/%m/%Y')
                           FROM inventory_batches ib
                           WHERE ib.product_variant_id = pv.id
                           ORDER BY ib.created_at DESC, ib.id DESC
                           LIMIT 1
                       ) AS latest_import_date_text,
                       COALESCE((
                           SELECT SUM(ib.remaining_quantity)
                           FROM inventory_batches ib
                           WHERE ib.product_variant_id = pv.id
                       ), 0) AS remaining_batch_quantity,
                       COALESCE((
                           SELECT SUM(oi.quantity)
                           FROM order_items oi
                           JOIN orders o ON o.id = oi.order_id
                           WHERE oi.variant_id = pv.id
                             AND o.order_status = 'COMPLETED'
                       ), 0) AS sold_quantity,
                       COALESCE((
                           SELECT SUM(itd.quantity)
                           FROM inventory_transactions it
                           JOIN inventory_transaction_details itd ON it.id = itd.transaction_id
                           WHERE it.type = 'IMPORT'
                             AND it.status = 'COMPLETED'
                             AND itd.product_variant_id = pv.id
                       ), 0) AS imported_quantity,
                       COALESCE((
                           SELECT SUM(itd.quantity)
                           FROM inventory_transactions it
                           JOIN inventory_transaction_details itd ON it.id = itd.transaction_id
                           WHERE it.type = 'EXPORT'
                             AND it.status = 'COMPLETED'
                             AND itd.product_variant_id = pv.id
                       ), 0) AS exported_quantity
                FROM product_variants pv
                JOIN products p ON pv.product_id = p.id
                LEFT JOIN category_product c ON p.category_id = c.id
                LEFT JOIN colors co ON pv.color_id = co.id
                LEFT JOIN sizes s ON pv.size_id = s.id
                WHERE p.status <> 'Đã xoá'
                ORDER BY pv.stock ASC, p.name ASC, co.name ASC, s.sort_order ASC, pv.id ASC
                """)
                .mapToBean(InventoryItem.class)
                .list());
    }

    public boolean isActiveVariantForImport(int variantId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM product_variants pv
                JOIN products p ON pv.product_id = p.id
                WHERE pv.id = :variantId
                  AND p.status <> 'Đã xoá'
                  AND (pv.status IS NULL OR pv.status = 'Đang bán')
                """)
                .bind("variantId", variantId)
                .mapTo(int.class)
                .one() > 0);
    }



    public boolean isActiveSupplierCode(String supplierCode) {
        if (supplierCode == null || supplierCode.trim().isBlank()) {
            return false;
        }

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM suppliers
                WHERE UPPER(code) = UPPER(:supplierCode)
                  AND status = 'ACTIVE'
                """)
                .bind("supplierCode", supplierCode.trim())
                .mapTo(int.class)
                .one() > 0);
    }



    private String buildInventoryOrderBy(String sortField, String sortDir) {
        String direction = "desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC";

        if ("id".equals(sortField)) {
            return " ORDER BY pv.id " + direction + " ";
        }

        if ("productName".equals(sortField)) {
            return " ORDER BY p.name " + direction + ", pv.id DESC ";
        }

        return " ORDER BY pv.stock ASC, p.name ASC, co.name ASC, s.sort_order ASC ";
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
