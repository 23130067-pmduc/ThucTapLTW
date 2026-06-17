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

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
