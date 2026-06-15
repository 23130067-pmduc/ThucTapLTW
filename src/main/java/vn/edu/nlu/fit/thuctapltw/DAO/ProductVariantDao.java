package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Color;
import vn.edu.nlu.fit.thuctapltw.model.ProductVariant;
import vn.edu.nlu.fit.thuctapltw.model.Size;

import java.util.List;

public class ProductVariantDao extends BaseDao {
    public static final String STATUS_ACTIVE = "Đang bán";
    public static final String STATUS_INACTIVE = "Ngừng bán";

    public List<ProductVariant> getVariantByProduct(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT *
                FROM product_variants
                WHERE product_id = :id
                  AND status = :status
                ORDER BY id ASC
                """)
                .bind("id", id)
                .bind("status", STATUS_ACTIVE)
                .mapToBean(ProductVariant.class)
                .list());
    }

    public double getPriceByVariantId(int variantId) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
            SELECT COALESCE(sale_price, price)
            FROM product_variants
            WHERE id = :vid
        """)
                        .bind("vid", variantId)
                        .mapTo(double.class)
                        .one()
        );
    }


    public ProductVariant getFirstVariantByProductId(int productId) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
            SELECT *
            FROM product_variants
            WHERE product_id = :pid
              AND status = :status
            ORDER BY id ASC
            LIMIT 1
        """)
                        .bind("pid", productId)
                        .bind("status", STATUS_ACTIVE)
                        .mapToBean(ProductVariant.class)
                        .findOne()
                        .orElse(null)
        );
    }


    public int getProductIdByVariantId(int variantId) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
            SELECT product_id
            FROM product_variants
            WHERE id = :vid
        """)
                        .bind("vid", variantId)
                        .mapTo(int.class)
                        .one()
        );
    }

    public int getStockByVariantId(int variantId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                        SELECT stock
                        FROM product_variants
                        WHERE id = :vid
                          AND status = :status
                        """)
                        .bind("vid", variantId)
                        .bind("status", STATUS_ACTIVE)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(0)
        );
    }

    public boolean isVariantActive(int variantId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM product_variants
                WHERE id = :id
                  AND status = :status
                """)
                .bind("id", variantId)
                .bind("status", STATUS_ACTIVE)
                .mapTo(Integer.class)
                .one() > 0);
    }


    public void decreaseStock(int variantId, int qty) {
        getJdbi().useHandle(h ->
                h.createUpdate("""
            UPDATE product_variants
            SET stock = stock - :q
            WHERE id = :vid
              AND status = :status
        """)
                        .bind("q", qty)
                        .bind("vid", variantId)
                        .bind("status", STATUS_ACTIVE)
                        .execute()
        );
    }

    public int countVariant(int productId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
            SELECT COUNT(*)
            FROM product_variants
            WHERE product_id = :productId
                """)
                .bind("productId", productId)
                .mapTo(int.class)
                .one()
        );
    }

    public int countStock(int productId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COALESCE(SUM(stock), 0)
                FROM product_variants
                WHERE product_id = :productId""")
                .bind("productId", productId)
                .mapTo(int.class)
                .one());
    }

    public int countSize(int productId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(DISTINCT size_id)
                FROM product_variants
                WHERE product_id = :productId""")
                .bind("productId", productId)
                .mapTo(int.class)
                .one());
    }

    public int countColor(int productId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(DISTINCT color_id)
                FROM product_variants
                WHERE product_id = :productId""")
                .bind("productId", productId)
                .mapTo(int.class)
                .one());
    }

    public List<Size> getAllSizes() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT id, code
                FROM sizes
                ORDER BY sort_order""")
                .mapToBean(Size.class)
                .list());
    }

    public List<Color> getAllColor() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT id, name
                FROM colors
                ORDER BY name""")
                .mapToBean(Color.class)
                .list());
    }

    public ProductVariant getVariantById(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT id, product_id, size_id, color_id, stock, price, sale_price, status
                FROM product_variants
                WHERE id = :id""")
                .bind("id", id)
                .mapToBean(ProductVariant.class)
                .findOne()
                .orElse(null));
    }

    public void createVariant(ProductVariant variant) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                INSERT INTO product_variants(product_id, size_id, color_id, stock, price, sale_price, status)
                VALUES (:productId, :sizeId, :colorId, :stock, :price, :salePrice, :status)""")
                .bind("productId", variant.getProductId())
                .bind("sizeId", variant.getSizeId())
                .bind("colorId", variant.getColorId())
                .bind("stock", variant.getStock())
                .bind("price", variant.getPrice())
                .bind("salePrice", variant.getSalePrice())
                .bind("status", variant.getStatus() == null || variant.getStatus().isBlank() ? STATUS_ACTIVE : variant.getStatus())
                .execute());
    }

    public void updateVariant(ProductVariant variant) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                        UPDATE product_variants
                        SET stock = :stock,
                            price = :price,
                            sale_price = :salePrice
                        WHERE id = :id""")
                .bindBean(variant)
                .execute());
    }

    public void updateVariantStatus(int id, String status) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                UPDATE product_variants
                SET status = :status
                WHERE id = :id""")
                .bind("id", id)
                .bind("status", status)
                .execute());
    }

    public List<ProductVariant> getProductVariantByProductId(int productId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT pv.id,
                       pv.product_id,
                       pv.size_id,
                       pv.color_id,
                       pv.stock,
                       pv.price,
                       pv.sale_price,
                       pv.status,
                       s.code AS sizeName,
                       c.name AS colorName
                FROM product_variants pv
                JOIN sizes s ON pv.size_id = s.id
                JOIN colors c ON pv.color_id = c.id
                WHERE pv.product_id = :productId
                ORDER BY pv.id DESC
                """)
                .bind("productId", productId)
                .mapToBean(ProductVariant.class)
                .list());
    }
}
