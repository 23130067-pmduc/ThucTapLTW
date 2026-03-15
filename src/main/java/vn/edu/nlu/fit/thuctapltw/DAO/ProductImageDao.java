package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.ProductImage;

import java.util.List;

public class ProductImageDao extends BaseDao{
    public List<ProductImage> getImageByProduct(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT id, product_id, image_url, is_main AS main
                FROM product_images
                WHERE product_id = :id
                ORDER BY is_main DESC, id DESC
                """)
                .bind("id", id)
                .mapToBean(ProductImage.class)
                .list());
    }

    public ProductImage getImageById(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT id, product_id, image_url, is_main AS main
                FROM product_images
                WHERE id = :id
                """)
                .bind("id", id)
                .mapToBean(ProductImage.class)
                .findOne()
                .orElse(null));
    }

    public void insert(ProductImage image) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                INSERT INTO product_images (product_id, image_url, is_main)
                VALUES (:productId, :imageUrl, :main)
                """)
                .bind("productId", image.getProductId())
                .bind("imageUrl", image.getImageUrl())
                .bind("main", image.getMain())
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

    public void delete(int id) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                DELETE FROM product_images WHERE id = :id
                """)
                .bind("id", id)
                .execute());
    }
}
