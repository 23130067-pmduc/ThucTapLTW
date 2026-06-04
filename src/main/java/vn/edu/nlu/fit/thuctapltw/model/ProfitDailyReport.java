package vn.edu.nlu.fit.thuctapltw.model;

import java.math.BigDecimal;

public class ProfitDailyReport {
    private String reportDate;
    private BigDecimal revenue = BigDecimal.ZERO;
    private BigDecimal costOfGoods = BigDecimal.ZERO;
    private BigDecimal profit = BigDecimal.ZERO;
    private int completedOrders;
    private int exportedQuantity;

    public String getReportDate() { return reportDate; }
    public void setReportDate(String reportDate) { this.reportDate = reportDate; }
    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue == null ? BigDecimal.ZERO : revenue; }
    public BigDecimal getCostOfGoods() { return costOfGoods; }
    public void setCostOfGoods(BigDecimal costOfGoods) { this.costOfGoods = costOfGoods == null ? BigDecimal.ZERO : costOfGoods; }
    public BigDecimal getProfit() { return profit; }
    public void setProfit(BigDecimal profit) { this.profit = profit == null ? BigDecimal.ZERO : profit; }
    public int getCompletedOrders() { return completedOrders; }
    public void setCompletedOrders(int completedOrders) { this.completedOrders = completedOrders; }
    public int getExportedQuantity() { return exportedQuantity; }
    public void setExportedQuantity(int exportedQuantity) { this.exportedQuantity = exportedQuantity; }
    public boolean isNegativeProfit() { return profit != null && profit.compareTo(BigDecimal.ZERO) < 0; }
}
