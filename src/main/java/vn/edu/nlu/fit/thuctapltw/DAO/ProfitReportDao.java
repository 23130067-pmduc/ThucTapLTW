package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.ProfitDailyReport;
import vn.edu.nlu.fit.thuctapltw.model.ProfitProductReport;
import vn.edu.nlu.fit.thuctapltw.model.ProfitSummary;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ProfitReportDao extends BaseDao {

    public ProfitSummary getSummary(LocalDate fromDate, LocalDate toDate) {
        return getJdbi().withHandle(handle -> {
            Map<String, Object> revenue = handle.createQuery("""
                    SELECT COUNT(*) AS completed_orders,
                           COALESCE(SUM(total_price), 0) AS gross_revenue,
                           COALESCE(SUM(discount), 0) AS discount_amount,
                           COALESCE(SUM(shipping_fee), 0) AS shipping_fee,
                           COALESCE(SUM(GREATEST(total_price - COALESCE(discount, 0), 0)), 0) AS net_revenue
                    FROM orders
                    WHERE order_status = 'COMPLETED'
                      AND DATE(created_at) BETWEEN :fromDate AND :toDate
                    """)
                    .bind("fromDate", fromDate)
                    .bind("toDate", toDate)
                    .mapToMap()
                    .one();

            Map<String, Object> cost = handle.createQuery("""
                    SELECT COALESCE(SUM(itd.quantity), 0) AS exported_quantity,
                           COALESCE(SUM(itd.quantity * itd.unit_cost), 0) AS cost_of_goods
                    FROM inventory_transactions it
                    JOIN inventory_transaction_details itd ON it.id = itd.transaction_id
                    WHERE it.type = 'EXPORT'
                      AND it.status = 'COMPLETED'
                      AND DATE(it.updated_at) BETWEEN :fromDate AND :toDate
                    """)
                    .bind("fromDate", fromDate)
                    .bind("toDate", toDate)
                    .mapToMap()
                    .one();

            ProfitSummary summary = new ProfitSummary();
            summary.setCompletedOrders(toInt(revenue.get("completed_orders")));
            summary.setGrossRevenue(toBigDecimal(revenue.get("gross_revenue")));
            summary.setDiscount(toBigDecimal(revenue.get("discount_amount")));
            summary.setShippingFee(toBigDecimal(revenue.get("shipping_fee")));
            summary.setNetRevenue(toBigDecimal(revenue.get("net_revenue")));
            summary.setExportedQuantity(toInt(cost.get("exported_quantity")));
            summary.setCostOfGoods(toBigDecimal(cost.get("cost_of_goods")));
            summary.setGrossProfit(summary.getNetRevenue().subtract(summary.getCostOfGoods()));
            return summary;
        });
    }

    public List<ProfitDailyReport> getDailyReports(LocalDate fromDate, LocalDate toDate) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT DATE_FORMAT(d.report_date, '%d/%m/%Y') AS report_date,
                       COALESCE(r.completed_orders, 0) AS completed_orders,
                       COALESCE(r.revenue, 0) AS revenue,
                       COALESCE(c.exported_quantity, 0) AS exported_quantity,
                       COALESCE(c.cost_of_goods, 0) AS cost_of_goods,
                       COALESCE(r.revenue, 0) - COALESCE(c.cost_of_goods, 0) AS profit
                FROM (
                    SELECT DATE(created_at) AS report_date
                    FROM orders
                    WHERE order_status = 'COMPLETED'
                      AND DATE(created_at) BETWEEN :fromDate AND :toDate
                    UNION
                    SELECT DATE(updated_at) AS report_date
                    FROM inventory_transactions
                    WHERE type = 'EXPORT'
                      AND status = 'COMPLETED'
                      AND DATE(updated_at) BETWEEN :fromDate AND :toDate
                ) d
                LEFT JOIN (
                    SELECT DATE(created_at) AS report_date,
                           COUNT(*) AS completed_orders,
                           COALESCE(SUM(GREATEST(total_price - COALESCE(discount, 0), 0)), 0) AS revenue
                    FROM orders
                    WHERE order_status = 'COMPLETED'
                      AND DATE(created_at) BETWEEN :fromDate AND :toDate
                    GROUP BY DATE(created_at)
                ) r ON d.report_date = r.report_date
                LEFT JOIN (
                    SELECT DATE(it.updated_at) AS report_date,
                           COALESCE(SUM(itd.quantity), 0) AS exported_quantity,
                           COALESCE(SUM(itd.quantity * itd.unit_cost), 0) AS cost_of_goods
                    FROM inventory_transactions it
                    JOIN inventory_transaction_details itd ON it.id = itd.transaction_id
                    WHERE it.type = 'EXPORT'
                      AND it.status = 'COMPLETED'
                      AND DATE(it.updated_at) BETWEEN :fromDate AND :toDate
                    GROUP BY DATE(it.updated_at)
                ) c ON d.report_date = c.report_date
                ORDER BY d.report_date DESC
                """)
                .bind("fromDate", fromDate)
                .bind("toDate", toDate)
                .map((rs, ctx) -> {
                    ProfitDailyReport row = new ProfitDailyReport();
                    row.setReportDate(rs.getString("report_date"));
                    row.setCompletedOrders(rs.getInt("completed_orders"));
                    row.setRevenue(rs.getBigDecimal("revenue"));
                    row.setExportedQuantity(rs.getInt("exported_quantity"));
                    row.setCostOfGoods(rs.getBigDecimal("cost_of_goods"));
                    row.setProfit(rs.getBigDecimal("profit"));
                    return row;
                })
                .list());
    }

    public List<ProfitProductReport> getProductReports(LocalDate fromDate, LocalDate toDate) {
        return getProductReports(fromDate, toDate, 20);
    }

    public List<ProfitProductReport> getProductReportsForExcel(LocalDate fromDate, LocalDate toDate) {
        return getProductReports(fromDate, toDate, 0);
    }

    private List<ProfitProductReport> getProductReports(LocalDate fromDate, LocalDate toDate, int limit) {
        String limitSql = limit > 0 ? "LIMIT :limit" : "";
        return getJdbi().withHandle(handle -> {
            var query = handle.createQuery("""
                    SELECT p.id AS product_id,
                           p.name AS product_name,
                           COALESCE(c.name, 'Chưa phân loại') AS category_name,
                           COALESCE(s.sold_quantity, 0) AS sold_quantity,
                           COALESCE(e.exported_quantity, 0) AS exported_quantity,
                           COALESCE(s.revenue, 0) AS revenue,
                           COALESCE(e.cost_of_goods, 0) AS cost_of_goods,
                           COALESCE(s.revenue, 0) - COALESCE(e.cost_of_goods, 0) AS profit
                    FROM products p
                    LEFT JOIN category_product c ON p.category_id = c.id
                    LEFT JOIN (
                        SELECT pv.product_id,
                               COALESCE(SUM(oi.quantity), 0) AS sold_quantity,
                               COALESCE(SUM(oi.total), 0) AS revenue
                        FROM orders o
                        JOIN order_items oi ON o.id = oi.order_id
                        JOIN product_variants pv ON oi.variant_id = pv.id
                        WHERE o.order_status = 'COMPLETED'
                          AND DATE(o.created_at) BETWEEN :fromDate AND :toDate
                        GROUP BY pv.product_id
                    ) s ON p.id = s.product_id
                    LEFT JOIN (
                        SELECT pv.product_id,
                               COALESCE(SUM(itd.quantity), 0) AS exported_quantity,
                               COALESCE(SUM(itd.quantity * itd.unit_cost), 0) AS cost_of_goods
                        FROM inventory_transactions it
                        JOIN inventory_transaction_details itd ON it.id = itd.transaction_id
                        JOIN product_variants pv ON itd.product_variant_id = pv.id
                        WHERE it.type = 'EXPORT'
                          AND it.status = 'COMPLETED'
                          AND DATE(it.updated_at) BETWEEN :fromDate AND :toDate
                        GROUP BY pv.product_id
                    ) e ON p.id = e.product_id
                    WHERE COALESCE(s.sold_quantity, 0) > 0 OR COALESCE(e.exported_quantity, 0) > 0
                    ORDER BY profit DESC, revenue DESC
                    """ + limitSql)
                    .bind("fromDate", fromDate)
                    .bind("toDate", toDate);

            if (limit > 0) {
                query.bind("limit", limit);
            }

            return query.map((rs, ctx) -> mapProductReport(rs)).list();
        });
    }

    private ProfitProductReport mapProductReport(ResultSet rs) throws SQLException {
        ProfitProductReport row = new ProfitProductReport();
        row.setProductId(rs.getInt("product_id"));
        row.setProductName(rs.getString("product_name"));
        row.setCategoryName(rs.getString("category_name"));
        row.setSoldQuantity(rs.getInt("sold_quantity"));
        row.setExportedQuantity(rs.getInt("exported_quantity"));
        row.setRevenue(rs.getBigDecimal("revenue"));
        row.setCostOfGoods(rs.getBigDecimal("cost_of_goods"));
        row.setProfit(rs.getBigDecimal("profit"));
        return row;
    }

    private int toInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Number number) return number.intValue();
        return Integer.parseInt(value.toString());
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) return BigDecimal.ZERO;
        if (value instanceof BigDecimal bigDecimal) return bigDecimal;
        if (value instanceof Number number) return BigDecimal.valueOf(number.doubleValue());
        return new BigDecimal(value.toString());
    }
}
