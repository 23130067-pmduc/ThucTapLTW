package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.InventoryItem;

import java.util.List;

public class InventoryDao extends BaseDao {
    public List<InventoryItem> searchInventory(String keyword, String stockStatus, int limit, int offset) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedStatus = stockStatus == null ? "" : stockStatus.trim();

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
                       p.status AS product_status
                FROM product_variants pv
                JOIN products p ON pv.product_id = p.id
                LEFT JOIN category_product c ON p.category_id = c.id
                LEFT JOIN colors co ON pv.color_id = co.id
                LEFT JOIN sizes s ON pv.size_id = s.id
                WHERE p.status <> 'Đã xoá'
                  AND (:keyword = '' OR p.name LIKE :keywordLike OR c.name LIKE :keywordLike OR co.name LIKE :keywordLike OR s.code LIKE :keywordLike)
                  AND (
                        :stockStatus = ''
                        OR (:stockStatus = 'OUT' AND pv.stock = 0)
                        OR (:stockStatus = 'LOW' AND pv.stock > 0 AND pv.stock <= 10)
                        OR (:stockStatus = 'AVAILABLE' AND pv.stock > 10)
                      )
                ORDER BY pv.stock ASC, p.name ASC, co.name ASC, s.sort_order ASC
                LIMIT :limit OFFSET :offset
                """)
                .bind("keyword", normalizedKeyword)
                .bind("keywordLike", "%" + normalizedKeyword + "%")
                .bind("stockStatus", normalizedStatus)
                .bind("limit", limit)
                .bind("offset", offset)
                .mapToBean(InventoryItem.class)
                .list());
    }

    public int countInventoryByFilter(String keyword, String stockStatus) {
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        String normalizedStatus = stockStatus == null ? "" : stockStatus.trim();

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM product_variants pv
                JOIN products p ON pv.product_id = p.id
                LEFT JOIN category_product c ON p.category_id = c.id
                LEFT JOIN colors co ON pv.color_id = co.id
                LEFT JOIN sizes s ON pv.size_id = s.id
                WHERE p.status <> 'Đã xoá'
                  AND (:keyword = '' OR p.name LIKE :keywordLike OR c.name LIKE :keywordLike OR co.name LIKE :keywordLike OR s.code LIKE :keywordLike)
                  AND (
                        :stockStatus = ''
                        OR (:stockStatus = 'OUT' AND pv.stock = 0)
                        OR (:stockStatus = 'LOW' AND pv.stock > 0 AND pv.stock <= 10)
                        OR (:stockStatus = 'AVAILABLE' AND pv.stock > 10)
                      )
                """)
                .bind("keyword", normalizedKeyword)
                .bind("keywordLike", "%" + normalizedKeyword + "%")
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
}
