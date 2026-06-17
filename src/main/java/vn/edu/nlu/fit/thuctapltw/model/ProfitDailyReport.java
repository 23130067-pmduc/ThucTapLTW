package vn.edu.nlu.fit.thuctapltw.model;

import java.math.BigDecimal;

public class ProfitDailyReport {
    private String reportDate;
    private BigDecimal revenue = BigDecimal.ZERO;
    private BigDecimal importCost = BigDecimal.ZERO;
    private BigDecimal costOfGoods = BigDecimal.ZERO;
    private BigDecimal profit = BigDecimal.ZERO;
    private int completedOrders;
    private int importedQuantity;
    private int exportedQuantity;

    public String getReportDate() { return reportDate; }
    public void setReportDate(String reportDate) { this.reportDate = reportDate; }

    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = safe(revenue); }

    public BigDecimal getImportCost() { return importCost; }
    public void setImportCost(BigDecimal importCost) { this.importCost = safe(importCost); }

    public BigDecimal getCostOfGoods() { return costOfGoods; }
    public void setCostOfGoods(BigDecimal costOfGoods) { this.costOfGoods = safe(costOfGoods); }

    public BigDecimal getProfit() { return profit; }
    public void setProfit(BigDecimal profit) { this.profit = safe(profit); }

    public int getCompletedOrders() { return completedOrders; }
    public void setCompletedOrders(int completedOrders) { this.completedOrders = completedOrders; }

    public int getImportedQuantity() { return importedQuantity; }
    public void setImportedQuantity(int importedQuantity) { this.importedQuantity = importedQuantity; }

    public int getExportedQuantity() { return exportedQuantity; }
    public void setExportedQuantity(int exportedQuantity) { this.exportedQuantity = exportedQuantity; }

    public boolean isNegativeProfit() { return profit != null && profit.compareTo(BigDecimal.ZERO) < 0; }

    private BigDecimal safe(BigDecimal value) { return value == null ? BigDecimal.ZERO : value; }
}
