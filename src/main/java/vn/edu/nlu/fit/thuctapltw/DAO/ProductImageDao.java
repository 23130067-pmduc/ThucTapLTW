package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.ProductImage;

import java.util.List;

public class ProductImageDao extends BaseDao {
    public static final String STATUS_VISIBLE = "Hiển thị";
    public static final String STATUS_HIDDEN = "Ẩn";

    public List<ProductImage> getImageByProduct(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT id, product_id, image_url, is_main AS main, status
                FROM product_images
                WHERE product_id = :id
                ORDER BY is_main DESC, id DESC
                """)
                .bind("id", id)
                .mapToBean(ProductImage.class)
                .list());
    }

    public List<ProductImage> getVisibleImagesByProduct(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT id, product_id, image_url, is_main AS main, status
                FROM product_images
                WHERE product_id = :id
                  AND status = :status
                ORDER BY is_main DESC, id DESC
                """)
                .bind("id", id)
                .bind("status", STATUS_VISIBLE)
                .mapToBean(ProductImage.class)
                .list());
    }

    public ProductImage getImageById(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT id, product_id, image_url, is_main AS main, status
                FROM product_images
                WHERE id = :id
                """)
                .bind("id", id)
                .mapToBean(ProductImage.class)
                .findOne()
                .orElse(null));
    }

    public int countImagesByStatus(int productId, String status) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM product_images
                WHERE product_id = :productId
                  AND status = :status
                """)
                .bind("productId", productId)
                .bind("status", status)
                .mapTo(int.class)
                .one());
    }

    public void insert(ProductImage image) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                INSERT INTO product_images (product_id, image_url, is_main, status)
                VALUES (:productId, :imageUrl, :main, :status)
                """)
                .bind("productId", image.getProductId())
                .bind("imageUrl", image.getImageUrl())
                .bind("main", image.getMain())
                .bind("status", normalizeStatus(image.getStatus()))
                .execute());
    }

    public void update(ProductImage image) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                UPDATE product_images
                SET image_url = :imageUrl, is_main = :main
                WHERE id = :id
                """)
                .bind("id", image.getId())
                .bind("imageUrl", image.getImageUrl())
                .bind("main", image.getMain())
                .execute());
    }

    public void updateStatus(int id, String status) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                UPDATE product_images
                SET status = :status,
                    is_main = CASE WHEN :status = :hiddenStatus THEN 0 ELSE is_main END
                WHERE id = :id
                """)
                .bind("id", id)
                .bind("status", status)
                .bind("hiddenStatus", STATUS_HIDDEN)
                .execute());
    }

    private String normalizeStatus(String status) {
        if (STATUS_HIDDEN.equals(status)) {
            return STATUS_HIDDEN;
        }
        return STATUS_VISIBLE;
    }

    public void clearMainByProduct(int productId) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
            UPDATE product_images
            SET is_main = 0
            WHERE product_id = :productId
            """)
                .bind("productId", productId)
                .execute());
    }

    public void updateMain(int imageId, boolean main) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
            UPDATE product_images
            SET is_main = :main
            WHERE id = :imageId
            """)
                .bind("main", main)
                .bind("imageId", imageId)
                .execute());
    }

    public ProductImage getFirstVisibleImageByProduct(int productId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
            SELECT id, product_id, image_url, is_main AS main, status
            FROM product_images
            WHERE product_id = :productId
              AND status = :status
            ORDER BY id DESC
            LIMIT 1
            """)
                .bind("productId", productId)
                .bind("status", STATUS_VISIBLE)
                .mapToBean(ProductImage.class)
                .findOne()
                .orElse(null));
    }
}
