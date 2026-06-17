package vn.edu.nlu.fit.thuctapltw.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProfitProductReport {
    private int productId;
    private String productName;
    private String categoryName;
    private BigDecimal price = BigDecimal.ZERO;
    private int soldQuantity;
    private int exportedQuantity;
    private int importedQuantity;
    private int currentStock;
    private BigDecimal importCost = BigDecimal.ZERO;
    private BigDecimal revenue = BigDecimal.ZERO;
    private BigDecimal costOfGoods = BigDecimal.ZERO;
    private BigDecimal profit = BigDecimal.ZERO;
    private String lastSoldDate;
    private String createdDate;

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = safe(price); }

    public int getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(int soldQuantity) { this.soldQuantity = soldQuantity; }

    public int getExportedQuantity() { return exportedQuantity; }
    public void setExportedQuantity(int exportedQuantity) { this.exportedQuantity = exportedQuantity; }

    public int getImportedQuantity() { return importedQuantity; }
    public void setImportedQuantity(int importedQuantity) { this.importedQuantity = importedQuantity; }

    public int getCurrentStock() { return currentStock; }
    public void setCurrentStock(int currentStock) { this.currentStock = currentStock; }

    public BigDecimal getImportCost() { return importCost; }
    public void setImportCost(BigDecimal importCost) { this.importCost = safe(importCost); }

    public BigDecimal getRevenue() { return revenue; }
    public void setRevenue(BigDecimal revenue) { this.revenue = safe(revenue); }

    public BigDecimal getCostOfGoods() { return costOfGoods; }
    public void setCostOfGoods(BigDecimal costOfGoods) { this.costOfGoods = safe(costOfGoods); }

    public BigDecimal getProfit() { return profit; }
    public void setProfit(BigDecimal profit) { this.profit = safe(profit); }

    public String getLastSoldDate() { return lastSoldDate; }
    public void setLastSoldDate(String lastSoldDate) { this.lastSoldDate = lastSoldDate; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public boolean isNegativeProfit() { return profit != null && profit.compareTo(BigDecimal.ZERO) < 0; }

    public BigDecimal getProfitMargin() {
        if (revenue == null || revenue.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return profit.multiply(BigDecimal.valueOf(100)).divide(revenue, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal safe(BigDecimal value) { return value == null ? BigDecimal.ZERO : value; }
}
