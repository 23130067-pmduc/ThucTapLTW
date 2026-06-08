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

    public Voucher findById(int id) {
        return getJdbi().withHandle(handle ->
                handle.createQuery(VOUCHER_SELECT + " WHERE id = :id LIMIT 1")
                        .bind("id", id)
                        .mapToBean(Voucher.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public boolean existsByCode(String code) {
        String sql = "SELECT COUNT(*) FROM vouchers WHERE UPPER(code) = UPPER(:code)";
        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("code", code)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public boolean existsByCodeExceptId(String code, int id) {
        String sql = "SELECT COUNT(*) FROM vouchers WHERE UPPER(code) = UPPER(:code) AND id <> :id";
        return getJdbi().withHandle(handle ->
                handle.createQuery(sql)
                        .bind("code", code)
                        .bind("id", id)
                        .mapTo(Integer.class)
                        .one() > 0
        );
    }

    public void insert(Voucher voucher) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("""
                    INSERT INTO vouchers (
                        code, name, description, discount_type, discount_value, max_discount,
                        min_order_value, voucher_scope, customer_id, product_id, order_id,
                        start_date, end_date, quantity, used_quantity, status
                    )
                    VALUES (
                        :code, :name, :description, :discountType, :discountValue, :maxDiscount,
                        :minOrderValue, :voucherScope, :customerId, :productId, NULL,
                        :startDate, :endDate, :quantity, 0, :status
                    )
                    """)
                        .bind("code", voucher.getCode())
                        .bind("name", voucher.getName())
                        .bind("description", voucher.getDescription())
                        .bind("discountType", voucher.getDiscount_type())
                        .bind("discountValue", voucher.getDiscount_value())
                        .bind("maxDiscount", voucher.getMax_discount())
                        .bind("minOrderValue", voucher.getMin_order_value())
                        .bind("voucherScope", voucher.getVoucher_scope())
                        .bind("customerId", voucher.getCustomer_id())
                        .bind("productId", voucher.getProduct_id())
                        .bind("startDate", voucher.getStart_date())
                        .bind("endDate", voucher.getEnd_date())
                        .bind("quantity", voucher.getQuantity())
                        .bind("status", voucher.getStatus())
                        .execute()
        );
    }

    public void update(Voucher voucher) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("""
                    UPDATE vouchers
                    SET code = :code,
                        name = :name,
                        description = :description,
                        discount_type = :discountType,
                        discount_value = :discountValue,
                        max_discount = :maxDiscount,
                        min_order_value = :minOrderValue,
                        voucher_scope = :voucherScope,
                        customer_id = :customerId,
                        product_id = :productId,
                        start_date = :startDate,
                        end_date = :endDate,
                        quantity = :quantity,
                        status = :status
                    WHERE id = :id
                    """)
                        .bind("id", voucher.getId())
                        .bind("code", voucher.getCode())
                        .bind("name", voucher.getName())
                        .bind("description", voucher.getDescription())
                        .bind("discountType", voucher.getDiscount_type())
                        .bind("discountValue", voucher.getDiscount_value())
                        .bind("maxDiscount", voucher.getMax_discount())
                        .bind("minOrderValue", voucher.getMin_order_value())
                        .bind("voucherScope", voucher.getVoucher_scope())
                        .bind("customerId", voucher.getCustomer_id())
                        .bind("productId", voucher.getProduct_id())
                        .bind("startDate", voucher.getStart_date())
                        .bind("endDate", voucher.getEnd_date())
                        .bind("quantity", voucher.getQuantity())
                        .bind("status", voucher.getStatus())
                        .execute()
        );
    }

}
