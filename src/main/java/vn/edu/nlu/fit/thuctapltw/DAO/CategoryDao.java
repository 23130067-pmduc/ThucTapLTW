package vn.edu.nlu.fit.thuctapltw.DAO;

import java.util.List;

import org.jdbi.v3.core.Jdbi;

import vn.edu.nlu.fit.thuctapltw.model.Category;

public class CategoryDao extends BaseDao {
    public List<Category> findAll() {
        Jdbi jdbi = getJdbi();
        String sql = "SELECT * FROM category_product ORDER BY id";
        return jdbi.withHandle(handle -> handle.createQuery(sql).mapToBean(Category.class).list());
    }

    public Category findById(int id) {
        Jdbi jdbi = getJdbi();
        String sql = "SELECT * FROM category_product WHERE id = :id";
        return jdbi.withHandle(handle -> handle.createQuery(sql).bind("id", id).mapToBean(Category.class).findOne().orElse(null));
    }

    public int countProductsByCategory(int categoryId) {
        Jdbi jdbi = getJdbi();
        String sql = "SELECT COUNT(*) FROM products WHERE category_id = :categoryId AND status = 'Đang bán'";
        return jdbi.withHandle(handle -> handle.createQuery(sql).bind("categoryId", categoryId).mapTo(Integer.class).one());
    }

    public void insert(Category category) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("""
                INSERT INTO category_product (name, image, status)
                VALUES (:name, :image, :status)
            """).bind("name", category.getName()).bind("image", category.getImage()).bind("status", category.getStatus()).execute());
    }

    public void update(Category category) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("""
                    UPDATE category_product SET name = :name, image = :image WHERE id = :id """).bind("id", category.getId()).bind("name", category.getName()).bind("image", category.getImage()).execute());
    }
    public List<Category> searchByName(String keyword) {
        String sql = "SELECT * FROM category_product WHERE name LIKE :keyword ORDER BY id";
        return getJdbi().withHandle(handle -> handle.createQuery(sql).bind("keyword", "%" + keyword + "%").mapToBean(Category.class).list());
    }

    public int getCategoryActive() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM category_product
                WHERE status = 1""")
                .mapTo(Integer.class)
                .one());
    }

    public List<Category> findAllWithCountProduct() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT c.id, c.name, c.description, c.status, COUNT(p.id) AS countProduct
                FROM category_product c
                LEFT JOIN products p ON p.category_id = c.id
                GROUP BY c.id, c.name, c.description, c.status
                """)
                .mapToBean(Category.class)
                .list());
    }

    public void updateCategory(Category category) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("""
                UPDATE category_product
                SET name = :name,
                    description = :description,
                    status = :status
                WHERE id = :id
                """)
                .bind("id", category.getId())
                .bind("name", category.getName())
                .bind("description", category.getDescription())
                .bind("status", category.getStatus())
                .execute()
        );
    }

    public void create(Category category) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("""
                INSERT INTO category_product(name, description, status)
                VALUES (:name, :description, :status)
                """)
                .bind("name", category.getName())
                .bind("description", category.getDescription())
                .bind("status", category.getStatus())
                .execute()
        );
    }

    public void deleteCategory(int id) {
        getJdbi().withHandle(handle -> handle.createUpdate("""
                UPDATE category_product 
                SET status = 0
                WHERE id = :id
                """)
                .bind("id", id)
                .execute());
    }
}
