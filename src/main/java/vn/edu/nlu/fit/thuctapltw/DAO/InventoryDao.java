package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.InventoryItem;

import java.math.BigDecimal;
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




    public SheetVariantResolution resolveVariantForSheetImport(Integer productId,
                                                               String productName,
                                                               String categoryName,
                                                               String colorName,
                                                               String sizeName,
                                                               BigDecimal sellingPrice,
                                                               String thumbnail,
                                                               String description) {
        return getJdbi().inTransaction(handle -> {
            Integer resolvedProductId = null;
            BigDecimal productPrice = BigDecimal.ZERO;
            boolean createdProduct = false;

            if (productId != null && productId > 0) {
                var product = handle.createQuery("""
                        SELECT id, price
                        FROM products
                        WHERE id = :productId
                          AND status <> 'Đã xoá'
                        LIMIT 1
                        """)
                        .bind("productId", productId)
                        .mapToMap()
                        .findOne()
                        .orElse(null);

                if (product == null) {
                    return SheetVariantResolution.error("Không tìm thấy sản phẩm đang dùng với mã #" + productId + ".");
                }

                resolvedProductId = ((Number) product.get("id")).intValue();
                productPrice = toBigDecimal(product.get("price"));
            } else {
                String normalizedProductName = normalize(productName);
                if (normalizedProductName == null) {
                    return SheetVariantResolution.error("Tên sản phẩm không được để trống khi tạo sản phẩm mới.");
                }

                var product = handle.createQuery("""
                        SELECT id, price
                        FROM products
                        WHERE LOWER(TRIM(name)) = LOWER(TRIM(:name))
                          AND status <> 'Đã xoá'
                        ORDER BY id DESC
                        LIMIT 1
                        """)
                        .bind("name", normalizedProductName)
                        .mapToMap()
                        .findOne()
                        .orElse(null);

                if (product != null) {
                    resolvedProductId = ((Number) product.get("id")).intValue();
                    productPrice = toBigDecimal(product.get("price"));
                } else {
                    Integer categoryId = resolveCategoryId(handle, categoryName);
                    if (categoryId == null) {
                        return SheetVariantResolution.error("Không tìm thấy danh mục đang dùng: " + safeForError(categoryName) + ".");
                    }

                    if (sellingPrice == null || sellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
                        return SheetVariantResolution.error("Giá bán phải lớn hơn 0 khi tạo sản phẩm mới.");
                    }

                    String image = normalize(thumbnail);
                    if (image == null) {
                        image = "img/gau.png";
                    }

                    resolvedProductId = handle.createUpdate("""
                            INSERT INTO products(category_id, name, description, price, sale_price, thumbnail, created_at, views, status)
                            VALUES(:categoryId, :name, :description, :price, 0, :thumbnail, NOW(), 0, 'Đang bán')
                            """)
                            .bind("categoryId", categoryId)
                            .bind("name", normalizedProductName)
                            .bind("description", normalize(description))
                            .bind("price", sellingPrice)
                            .bind("thumbnail", image)
                            .executeAndReturnGeneratedKeys("id")
                            .mapTo(int.class)
                            .one();
                    productPrice = sellingPrice;
                    createdProduct = true;
                }
            }

            Integer colorId = resolveColorId(handle, colorName);
            if (colorId == null) {
                return SheetVariantResolution.error("Không tìm thấy màu đang dùng: " + safeForError(colorName) + ".");
            }

            Integer sizeId = resolveSizeId(handle, sizeName);
            if (sizeId == null) {
                return SheetVariantResolution.error("Không tìm thấy size đang dùng: " + safeForError(sizeName) + ".");
            }

            Integer existingVariantId = handle.createQuery("""
                    SELECT id
                    FROM product_variants
                    WHERE product_id = :productId
                      AND color_id = :colorId
                      AND size_id = :sizeId
                    ORDER BY id DESC
                    LIMIT 1
                    """)
                    .bind("productId", resolvedProductId)
                    .bind("colorId", colorId)
                    .bind("sizeId", sizeId)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(null);

            if (existingVariantId != null) {
                String action = createdProduct ? "Tạo sản phẩm mới và dùng biến thể vừa có" : "Cập nhật tồn kho biến thể đã có theo sản phẩm/màu/size";
                return SheetVariantResolution.success(existingVariantId, resolvedProductId, action);
            }

            BigDecimal variantPrice = sellingPrice != null && sellingPrice.compareTo(BigDecimal.ZERO) > 0
                    ? sellingPrice
                    : productPrice;
            if (variantPrice == null || variantPrice.compareTo(BigDecimal.ZERO) <= 0) {
                return SheetVariantResolution.error("Giá bán phải lớn hơn 0 khi tạo biến thể mới.");
            }

            int newVariantId = handle.createUpdate("""
                    INSERT INTO product_variants(product_id, size_id, color_id, stock, price, sale_price, status)
                    VALUES(:productId, :sizeId, :colorId, 0, :price, 0, 'Đang bán')
                    """)
                    .bind("productId", resolvedProductId)
                    .bind("sizeId", sizeId)
                    .bind("colorId", colorId)
                    .bind("price", variantPrice)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(int.class)
                    .one();

            String action = createdProduct ? "Tạo sản phẩm mới và biến thể mới" : "Tạo biến thể mới cho sản phẩm đã có";
            return SheetVariantResolution.success(newVariantId, resolvedProductId, action);
        });
    }

    private Integer resolveCategoryId(org.jdbi.v3.core.Handle handle, String categoryName) {
        String value = normalize(categoryName);
        if (value == null) {
            return null;
        }

        Integer id = parsePositiveInt(value);
        if (id != null) {
            return handle.createQuery("""
                    SELECT id
                    FROM category_product
                    WHERE id = :id
                      AND status = 1
                    LIMIT 1
                    """)
                    .bind("id", id)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(null);
        }

        return handle.createQuery("""
                SELECT id
                FROM category_product
                WHERE LOWER(TRIM(name)) = LOWER(TRIM(:name))
                  AND status = 1
                ORDER BY id ASC
                LIMIT 1
                """)
                .bind("name", value)
                .mapTo(Integer.class)
                .findOne()
                .orElse(null);
    }

    private Integer resolveColorId(org.jdbi.v3.core.Handle handle, String colorName) {
        String value = normalize(colorName);
        if (value == null) {
            return null;
        }

        Integer id = parsePositiveInt(value);
        if (id != null) {
            return handle.createQuery("""
                    SELECT id
                    FROM colors
                    WHERE id = :id
                    LIMIT 1
                    """)
                    .bind("id", id)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(null);
        }

        return handle.createQuery("""
                SELECT id
                FROM colors
                WHERE LOWER(TRIM(name)) = LOWER(TRIM(:name))
                ORDER BY id ASC
                LIMIT 1
                """)
                .bind("name", value)
                .mapTo(Integer.class)
                .findOne()
                .orElse(null);
    }

    private Integer resolveSizeId(org.jdbi.v3.core.Handle handle, String sizeName) {
        String value = normalize(sizeName);
        if (value == null) {
            return null;
        }

        Integer id = parsePositiveInt(value);
        if (id != null) {
            return handle.createQuery("""
                    SELECT id
                    FROM sizes
                    WHERE id = :id
                    LIMIT 1
                    """)
                    .bind("id", id)
                    .mapTo(Integer.class)
                    .findOne()
                    .orElse(null);
        }

        return handle.createQuery("""
                SELECT id
                FROM sizes
                WHERE LOWER(TRIM(code)) = LOWER(TRIM(:code))
                ORDER BY sort_order ASC, id ASC
                LIMIT 1
                """)
                .bind("code", value)
                .mapTo(Integer.class)
                .findOne()
                .orElse(null);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isBlank() ? null : trimmed;
    }

    private String safeForError(String value) {
        String normalized = normalize(value);
        return normalized == null ? "(trống)" : normalized;
    }

    private Integer parsePositiveInt(String value) {
        try {
            int parsed = Integer.parseInt(value.trim());
            return parsed > 0 ? parsed : null;
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal bd) {
            return bd;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        try {
            return new BigDecimal(value.toString());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    public static class SheetVariantResolution {
        private final boolean success;
        private final Integer variantId;
        private final Integer productId;
        private final String action;
        private final String message;

        private SheetVariantResolution(boolean success, Integer variantId, Integer productId, String action, String message) {
            this.success = success;
            this.variantId = variantId;
            this.productId = productId;
            this.action = action;
            this.message = message;
        }

        public static SheetVariantResolution success(Integer variantId, Integer productId, String action) {
            return new SheetVariantResolution(true, variantId, productId, action, "");
        }

        public static SheetVariantResolution error(String message) {
            return new SheetVariantResolution(false, null, null, "", message);
        }

        public boolean isSuccess() {
            return success;
        }

        public Integer getVariantId() {
            return variantId;
        }

        public Integer getProductId() {
            return productId;
        }

        public String getAction() {
            return action;
        }

        public String getMessage() {
            return message;
        }
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
