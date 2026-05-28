package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Voucher;

import java.util.List;

public class VoucherDao extends BaseDao {
    public List<Voucher> findActiveOrderAndProductVouchers() {
        String sql = """
                SELECT id, code, name, description, discount_type, discount_value, max_discount,
                       min_order_value, voucher_scope, customer_id, product_id, order_id,
                       start_date, end_date, quantity, used_quantity, status, created_at
                FROM vouchers
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
        String sql = """
                SELECT id, code, name, description, discount_type, discount_value, max_discount,
                       min_order_value, voucher_scope, customer_id, product_id, order_id,
                       start_date, end_date, quantity, used_quantity, status, created_at
                FROM vouchers
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
        String sql = """
            SELECT id, code, name, description, discount_type, discount_value, max_discount,
                   min_order_value, voucher_scope, customer_id, product_id, order_id,
                   start_date, end_date, quantity, used_quantity, status, created_at
            FROM vouchers
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
        String sql = """
            SELECT id, code, name, description, discount_type, discount_value, max_discount,
                   min_order_value, voucher_scope, customer_id, product_id, order_id,
                   start_date, end_date, quantity, used_quantity, status, created_at
            FROM vouchers
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
    
}
