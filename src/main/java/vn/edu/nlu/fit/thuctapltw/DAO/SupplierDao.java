package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Supplier;

import java.util.List;
import java.util.Optional;

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

    public Optional<Supplier> getById(int id) {
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
                WHERE id = :id
                """)
                .bind("id", id)
                .mapToBean(Supplier.class)
                .findOne());
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
                .bind("phone", nullable(supplier.getPhone()))
                .bind("email", nullable(supplier.getEmail()))
                .bind("address", normalize(supplier.getAddress()))
                .bind("note", normalize(supplier.getNote()))
                .bind("status", normalize(supplier.getStatus()))
                .execute());
    }

    public void update(Supplier supplier) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                UPDATE suppliers
                SET code = :code,
                    name = :name,
                    phone = :phone,
                    email = :email,
                    address = :address,
                    note = :note,
                    status = :status,
                    updated_at = NOW()
                WHERE id = :id
                """)
                .bind("id", supplier.getId())
                .bind("code", normalize(supplier.getCode()))
                .bind("name", normalize(supplier.getName()))
                .bind("phone", nullable(supplier.getPhone()))
                .bind("email", nullable(supplier.getEmail()))
                .bind("address", normalize(supplier.getAddress()))
                .bind("note", normalize(supplier.getNote()))
                .bind("status", normalize(supplier.getStatus()))
                .execute());
    }

    public void updateStatus(int id, String status) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                UPDATE suppliers
                SET status = :status,
                    updated_at = NOW()
                WHERE id = :id
                """)
                .bind("id", id)
                .bind("status", status)
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

    public boolean existsByCodeExceptId(String code, int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM suppliers
                WHERE code = :code
                  AND id <> :id
                """)
                .bind("code", normalize(code))
                .bind("id", id)
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

    public boolean existsByEmailExceptId(String email, int id) {
        String normalizedEmail = normalize(email);
        if (normalizedEmail.isEmpty()) {
            return false;
        }

        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM suppliers
                WHERE email = :email
                  AND id <> :id
                """)
                .bind("email", normalizedEmail)
                .bind("id", id)
                .mapTo(Integer.class)
                .one()) > 0;
    }

    private String nullable(String value) {
        String normalized = normalize(value);
        return normalized.isEmpty() ? null : normalized;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
