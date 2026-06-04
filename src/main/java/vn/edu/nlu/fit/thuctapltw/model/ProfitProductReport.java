package vn.edu.nlu.fit.thuctapltw.model;

import java.math.BigDecimal;

public class ProfitProductReport {
    private int productId;
    private String productName;
    private int soldQuantity;
    private int exportedQuantity;
    private BigDecimal revenue = BigDecimal.ZERO;
    private BigDecimal costOfGoods = BigDecimal.ZERO;
    private BigDecimal profit = BigDecimal.ZERO;

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(int soldQuantity) { this.soldQuantity = soldQuantity; }
    public int getExportedQuantity() { return exportedQuantity; }
    public void setExportedQuantity(int exportedQuantity) { this.exportedQuantity = exportedQuantity; }
    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = revenue == null ? BigDecimal.ZERO : revenue; }
    public BigDecimal getCostOfGoods() { return costOfGoods; }
    public void setCostOfGoods(BigDecimal costOfGoods) { this.costOfGoods = costOfGoods == null ? BigDecimal.ZERO : costOfGoods; }
    public BigDecimal getProfit() { return profit; }
    public void setProfit(BigDecimal profit) { this.profit = profit == null ? BigDecimal.ZERO : profit; }
    public boolean isNegativeProfit() { return profit != null && profit.compareTo(BigDecimal.ZERO) < 0; }
}
