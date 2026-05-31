package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.InventoryTransactionDetail;

import java.util.List;

public class InventoryTransactionDetailDao extends BaseDao {
    public List<InventoryTransactionDetail> getDetailsByTransactionId(int transactionId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT itd.id,
                       itd.transaction_id,
                       itd.product_variant_id,
                       pv.product_id,
                       p.name AS product_name,
                       cp.name AS category_name,
                       c.name AS color_name,
                       s.code AS size_name,
                       COALESCE(
                           NULLIF(p.thumbnail, ''),
                           (
                               SELECT pi.image_url
                               FROM product_images pi
                               WHERE pi.product_id = p.id
                               ORDER BY pi.is_main DESC, pi.id ASC
                               LIMIT 1
                           ),
                           'img/no-image.png'
                       ) AS thumbnail,
                       itd.quantity,
                       itd.note
                FROM inventory_transaction_details itd
                JOIN product_variants pv ON itd.product_variant_id = pv.id
                JOIN products p ON pv.product_id = p.id
                LEFT JOIN category_product cp ON p.category_id = cp.id
                LEFT JOIN colors c ON pv.color_id = c.id
                LEFT JOIN sizes s ON pv.size_id = s.id
                WHERE itd.transaction_id = :transactionId
                ORDER BY itd.id ASC
                """)
                .bind("transactionId", transactionId)
                .mapToBean(InventoryTransactionDetail.class)
                .list());
    }
}
