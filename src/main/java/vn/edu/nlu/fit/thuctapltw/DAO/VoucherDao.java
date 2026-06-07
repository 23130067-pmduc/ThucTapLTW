package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Voucher;

import java.util.List;

public class VoucherDao extends BaseDao {
    private static final String VOUCHER_SELECT = """
            SELECT id, code, name, description, discount_type, discount_value, max_discount,
                   min_order_value, voucher_scope, customer_id, product_id, order_id,
                   start_date, end_date, quantity, used_quantity, status, created_at
            FROM vouchers
            """;

    public List<Voucher> findActiveOrderAndProductVouchers() {
        String sql = VOUCHER_SELECT + """
                WHERE status = 1
                  AND voucher_scope IN ('ORDER', 'PRODUCT')
                  AND start_date <= NOW()
                  AND end_date >= NOW()
                  AND used_quantity < quantity
                ORDER BY voucher_scope, id DESC
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(Voucher.class)
                        .list()
        );
    }

    public List<Voucher> findActiveShippingVouchers() {
        String sql = VOUCHER_SELECT + """
                WHERE status = 1
                  AND voucher_scope = 'SHIPPING'
                  AND start_date <= NOW()
                  AND end_date >= NOW()
                  AND used_quantity < quantity
                ORDER BY id DESC
                """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .mapToBean(Voucher.class)
                        .list()
        );
    }

    public Voucher findActiveOrderOrProductVoucherByCode(String code) {
        String sql = VOUCHER_SELECT + """
            WHERE UPPER(code) = UPPER(:code)
              AND status = 1
              AND voucher_scope IN ('ORDER', 'PRODUCT')
              AND start_date <= NOW()
              AND end_date >= NOW()
              AND used_quantity < quantity
            LIMIT 1
            """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("code", code)
                        .mapToBean(Voucher.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public void markVoucherUsed(int voucherId, int orderId) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("""
                    UPDATE vouchers
                    SET used_quantity = used_quantity + 1,
                        order_id = :orderId
                    WHERE id = :voucherId
                      AND used_quantity < quantity
                    """)
                        .bind("voucherId", voucherId)
                        .bind("orderId", orderId)
                        .execute()
        );
    }

    public Voucher findActiveShippingVoucherByCode(String code) {
        String sql = VOUCHER_SELECT + """
            WHERE UPPER(code) = UPPER(:code)
              AND status = 1
              AND voucher_scope = 'SHIPPING'
              AND start_date <= NOW()
              AND end_date >= NOW()
              AND used_quantity < quantity
            LIMIT 1
            """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("code", code)
                        .mapToBean(Voucher.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public List<Voucher> findAdminVouchers(String keyword, String scope, String status, int limit, int offset) {
        StringBuilder sql = new StringBuilder(VOUCHER_SELECT);
        sql.append(" WHERE 1 = 1 ");

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasScope = scope != null && !scope.trim().isEmpty() && !"ALL".equalsIgnoreCase(scope);
        boolean hasStatus = status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status);

        if (hasKeyword) {
            sql.append(" AND (UPPER(code) LIKE UPPER(:keyword) OR UPPER(name) LIKE UPPER(:keyword) OR UPPER(description) LIKE UPPER(:keyword)) ");
        }

        if (hasScope) {
            sql.append(" AND voucher_scope = :scope ");
        }

        if (hasStatus) {
            if ("ACTIVE".equalsIgnoreCase(status)) {
                sql.append(" AND status = 1 AND start_date <= NOW() AND end_date >= NOW() AND used_quantity < quantity ");
            } else if ("LOCKED".equalsIgnoreCase(status)) {
                sql.append(" AND status = 0 ");
            } else if ("EXPIRED".equalsIgnoreCase(status)) {
                sql.append(" AND end_date < NOW() ");
            } else if ("SOLD_OUT".equalsIgnoreCase(status)) {
                sql.append(" AND used_quantity >= quantity ");
            }
        }

        sql.append(" ORDER BY id DESC LIMIT :limit OFFSET :offset ");

        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql.toString())
                    .bind("limit", limit)
                    .bind("offset", offset);

            if (hasKeyword) {
                query.bind("keyword", "%" + keyword.trim() + "%");
            }
            if (hasScope) {
                query.bind("scope", scope.trim().toUpperCase());
            }

            return query.mapToBean(Voucher.class).list();
        });
    }

    public int countAdminVouchers(String keyword, String scope, String status) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM vouchers WHERE 1 = 1 ");

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasScope = scope != null && !scope.trim().isEmpty() && !"ALL".equalsIgnoreCase(scope);
        boolean hasStatus = status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status);

        if (hasKeyword) {
            sql.append(" AND (UPPER(code) LIKE UPPER(:keyword) OR UPPER(name) LIKE UPPER(:keyword) OR UPPER(description) LIKE UPPER(:keyword)) ");
        }

        if (hasScope) {
            sql.append(" AND voucher_scope = :scope ");
        }

        if (hasStatus) {
            if ("ACTIVE".equalsIgnoreCase(status)) {
                sql.append(" AND status = 1 AND start_date <= NOW() AND end_date >= NOW() AND used_quantity < quantity ");
            } else if ("LOCKED".equalsIgnoreCase(status)) {
                sql.append(" AND status = 0 ");
            } else if ("EXPIRED".equalsIgnoreCase(status)) {
                sql.append(" AND end_date < NOW() ");
            } else if ("SOLD_OUT".equalsIgnoreCase(status)) {
                sql.append(" AND used_quantity >= quantity ");
            }
        }

        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery(sql.toString());
            if (hasKeyword) {
                query.bind("keyword", "%" + keyword.trim() + "%");
            }
            if (hasScope) {
                query.bind("scope", scope.trim().toUpperCase());
            }
            return query.mapTo(Integer.class).one();
        });
    }

    public int countAllVouchers() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM vouchers")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public int countActiveVouchers() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                    SELECT COUNT(*)
                    FROM vouchers
                    WHERE status = 1
                      AND start_date <= NOW()
                      AND end_date >= NOW()
                      AND used_quantity < quantity
                    """)
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public int countLockedVouchers() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM vouchers WHERE status = 0")
                        .mapTo(Integer.class)
                        .one()
        );
    }

    public int countExpiredVouchers() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM vouchers WHERE end_date < NOW()")
                        .mapTo(Integer.class)
                        .one()
        );
    }
}
