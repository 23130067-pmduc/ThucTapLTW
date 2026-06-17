package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Supplier;

import java.util.List;

public class SupplierDao extends BaseDao {

    public int countSuppliers(String keyword, String status) {
        String keywordLike = "%" + normalize(keyword) + "%";
        String normalizedStatus = normalize(status);

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM suppliers
                WHERE (:keyword = '' OR code LIKE :keywordLike OR name LIKE :keywordLike
                       OR phone LIKE :keywordLike OR email LIKE :keywordLike OR address LIKE :keywordLike)
                  AND (:status = '' OR status = :status)
                """)
                .bind("keyword", normalize(keyword))
                .bind("keywordLike", keywordLike)
                .bind("status", normalizedStatus)
                .mapTo(Integer.class)
                .one());
    }

    public List<Supplier> getSuppliersByPage(String keyword, String status, int pageSize, int offset) {
        String keywordLike = "%" + normalize(keyword) + "%";
        String normalizedStatus = normalize(status);

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT id,
                       code,
                       name,
                       phone,
                       email,
                       address,
                       note,
                       status,
                       DATE_FORMAT(created_at, '%d/%m/%Y %H:%i') AS created_at_text,
                       DATE_FORMAT(updated_at, '%d/%m/%Y %H:%i') AS updated_at_text
                FROM suppliers
                WHERE (:keyword = '' OR code LIKE :keywordLike OR name LIKE :keywordLike
                       OR phone LIKE :keywordLike OR email LIKE :keywordLike OR address LIKE :keywordLike)
                  AND (:status = '' OR status = :status)
                ORDER BY id DESC
                LIMIT :pageSize OFFSET :offset
                """)
                .bind("keyword", normalize(keyword))
                .bind("keywordLike", keywordLike)
                .bind("status", normalizedStatus)
                .bind("pageSize", pageSize)
                .bind("offset", offset)
                .mapToBean(Supplier.class)
                .list());
    }

    public int countByStatus(String status) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM suppliers
                WHERE status = :status
                """)
                .bind("status", status)
                .mapTo(Integer.class)
                .one());
    }

    public void create(Supplier supplier) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                INSERT INTO suppliers (code, name, phone, email, address, note, status, created_at, updated_at)
                VALUES (:code, :name, :phone, :email, :address, :note, :status, NOW(), NOW())
                """)
                .bind("code", normalize(supplier.getCode()))
                .bind("name", normalize(supplier.getName()))
                .bind("phone", normalize(supplier.getPhone()))
                .bind("email", normalize(supplier.getEmail()))
                .bind("address", normalize(supplier.getAddress()))
                .bind("note", normalize(supplier.getNote()))
                .bind("status", normalize(supplier.getStatus()))
                .execute());
    }

    public boolean existsByCode(String code) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM suppliers
                WHERE code = :code
                """)
                .bind("code", normalize(code))
                .mapTo(Integer.class)
                .one()) > 0;
    }

    public boolean existsByEmail(String email) {
        String normalizedEmail = normalize(email);
        if (normalizedEmail.isEmpty()) {
            return false;
        }

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM suppliers
                WHERE email = :email
                """)
                .bind("email", normalizedEmail)
                .mapTo(Integer.class)
                .one()) > 0;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
