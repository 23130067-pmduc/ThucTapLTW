package vn.edu.nlu.fit.thuctapltw.DAO;

import org.jdbi.v3.core.Jdbi;
import vn.edu.nlu.fit.thuctapltw.model.Product;

import java.util.List;

public class ProductDao extends BaseDao {

    public List<Product> findAll() {
        String sql = """
        SELECT p.*, c.name AS categoryName
        FROM products p
        JOIN category_product c ON p.category_id = c.id
        WHERE p.status <> 'Đã xoá'
        ORDER BY p.id DESC
    """;

        return getJdbi().withHandle(h ->
                h.createQuery(sql)
                        .mapToBean(Product.class)
                        .list()
        );
    }


    public Product findById(int id) {
        return getJdbi().withHandle(h ->
                h.createQuery("""
                SELECT p.*, c.name AS categoryName
                FROM products p
                JOIN category_product c ON p.category_id = c.id
                WHERE p.id = :id
            """)
                        .bind("id", id)
                        .mapToBean(Product.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public List<Product> findByCategory(int categoryId) {
        Jdbi jdbi = getJdbi();
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT * FROM products WHERE category_id = :cid AND status = 'Đang bán'"
                        )
                        .bind("cid", categoryId)
                        .mapToBean(Product.class)
                        .list()
        );
    }

    public List<Product> findByCategories(List<Integer> categoryIds) {

        if (categoryIds == null || categoryIds.isEmpty()) {
            return List.of();
        }

        String sql = "SELECT * FROM products " +
                "WHERE category_id IN (<ids>) AND status = 'Đang bán'";

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bindList("ids", categoryIds)
                        .mapToBean(Product.class)
                        .list()
        );
    }


    public List<Product> findLatest(int limit) {
        Jdbi jdbi = getJdbi();
        return jdbi.withHandle(handle ->
                handle.createQuery(
                                "SELECT * FROM products WHERE status = 'Đang bán' ORDER BY created_at DESC LIMIT :limit"
                        )
                        .bind("limit", limit)
                        .mapToBean(Product.class)
                        .list()
        );
    }


    public List<Product> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return java.util.List.of();
        }

        String[] words = keyword.trim().split("\\s+");
        StringBuilder scoreCalc = new StringBuilder();
        StringBuilder whereClause = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            scoreCalc.append("(CASE WHEN p.name LIKE :w").append(i).append(" THEN 1 ELSE 0 END)");
            whereClause.append("p.name LIKE :w").append(i);

            if (i < words.length - 1) {
                scoreCalc.append(" + ");
                whereClause.append(" OR ");
            }
        }

        String sql = "SELECT p.*, c.name AS categoryName, (" + scoreCalc.toString() + ") AS match_score " +
                "FROM products p " +
                "JOIN category_product c ON p.category_id = c.id " +
                "WHERE p.status <> 'Đã xoá' " +
                "AND (" + whereClause.toString() + ") " +
                "ORDER BY match_score DESC, p.id DESC";

        return getJdbi().withHandle(h -> {
            org.jdbi.v3.core.statement.Query query = h.createQuery(sql);
            for (int i = 0; i < words.length; i++) {
                query.bind("w" + i, "%" + words[i] + "%");
            }
            return query.mapToBean(Product.class).list();
        });
    }



    public List<Product> findBoyProducts(int limit) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
            SELECT * FROM products
            WHERE category_id IN (1,2,3)
              AND status = 'Đang bán'
            ORDER BY created_at DESC
            LIMIT :limit
        """)
                        .bind("limit", limit)
                        .mapToBean(Product.class)
                        .list()
        );
    }
    public List<Product> findGirlProducts(int limit) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
            SELECT * FROM products
            WHERE category_id IN (4,5,6,7)
              AND status = 'Đang bán'
            ORDER BY created_at DESC
            LIMIT :limit
        """)
                        .bind("limit", limit)
                        .mapToBean(Product.class)
                        .list()
        );
    }
    public List<Product> findAccessoryProducts(int limit) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
            SELECT * FROM products
            WHERE category_id IN (8,9,10)
              AND status = 'Đang bán'
            ORDER BY created_at DESC
            LIMIT :limit
        """)
                        .bind("limit", limit)
                        .mapToBean(Product.class)
                        .list()
        );
    }

    public List<Product> getRelatedProductByCategory(int categoryId, int currentProductId, int limit){
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                                SELECT *
                                FROM products
                                WHERE category_id = :categoryId
                                AND id <> :currentProductId
                                AND status = 'Đang bán'
                                ORDER BY created_at DESC
                                LIMIT :limit
                                """
                        ).bind("categoryId", categoryId)
                        .bind("currentProductId",currentProductId)
                        .bind("limit", limit).
                        mapToBean(Product.class)
                        .list());
    }
    public List<Product> findFlashSaleProducts(int limit) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                SELECT *
                FROM products
                WHERE sale_price IS NOT NULL
                  AND sale_price < price
                  AND sale_price <= price * 0.7
                  AND status = 'Đang bán'
                ORDER BY (price - sale_price) DESC
                LIMIT :limit
            """)
                        .bind("limit", limit)
                        .mapToBean(Product.class)
                        .list()
        );
    }
    public List<Product> findDiscountProducts() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                SELECT *
                FROM products
                WHERE sale_price IS NOT NULL
                  AND sale_price < price
                  AND status = 'Đang bán'
                ORDER BY created_at DESC
            """)
                        .mapToBean(Product.class)
                        .list()
        );
    }
    public void insert(Product p) {
        String sql = """
        INSERT INTO products
        (category_id, name, price, thumbnail, status)
        VALUES (:category_id, :name, :price, :thumbnail, 'Đang bán')
    """;

        getJdbi().withHandle(h ->
                h.createUpdate(sql)
                        .bind("category_id", p.getCategory_id())
                        .bind("name", p.getName())
                        .bind("price", p.getPrice())
                        .bind("thumbnail", p.getThumbnail())
                        .execute()
        );
    }

    public void update(Product p) {
        String sql = """
        UPDATE products
        SET name = :name,
            price = :price,
            category_id = :category_id,
            thumbnail = :thumbnail,
            status = :status
        WHERE id = :id
    """;

        getJdbi().withHandle(h ->
                h.createUpdate(sql)
                        .bind("id", p.getId())
                        .bind("name", p.getName())
                        .bind("price", p.getPrice())
                        .bind("category_id", p.getCategory_id())
                        .bind("thumbnail", p.getThumbnail())
                        .bind("status", p.getStatus())
                        .execute()
        );
    }

    public void softDelete(int id) {
        String sql = """
        UPDATE products
        SET status = 'Đã xoá'
        WHERE id = :id
    """;

        getJdbi().withHandle(h ->
                h.createUpdate(sql)
                        .bind("id", id)
                        .execute()
        );
    }

}
