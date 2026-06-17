package vn.edu.nlu.fit.thuctapltw.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProfitSummary {
    private BigDecimal grossRevenue = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal shippingFee = BigDecimal.ZERO;
    private BigDecimal netRevenue = BigDecimal.ZERO;
    private BigDecimal importCost = BigDecimal.ZERO;
    private BigDecimal costOfGoods = BigDecimal.ZERO;
    private BigDecimal grossProfit = BigDecimal.ZERO;
    private int completedOrders;
    private int importedQuantity;
    private int exportedQuantity;
    private int soldQuantity;

    public BigDecimal getGrossRevenue() { return grossRevenue; }
    public void setGrossRevenue(BigDecimal grossRevenue) { this.grossRevenue = safe(grossRevenue); }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = safe(discount); }

    public BigDecimal getShippingFee() { return shippingFee; }
    public void setShippingFee(BigDecimal shippingFee) { this.shippingFee = safe(shippingFee); }

    public BigDecimal getNetRevenue() { return netRevenue; }
    public void setNetRevenue(BigDecimal netRevenue) { this.netRevenue = safe(netRevenue); }

    public BigDecimal getImportCost() { return importCost; }
    public void setImportCost(BigDecimal importCost) { this.importCost = safe(importCost); }

    public BigDecimal getCostOfGoods() { return costOfGoods; }
    public void setCostOfGoods(BigDecimal costOfGoods) { this.costOfGoods = safe(costOfGoods); }

    public BigDecimal getGrossProfit() { return grossProfit; }
    public void setGrossProfit(BigDecimal grossProfit) { this.grossProfit = safe(grossProfit); }

    public int getCompletedOrders() { return completedOrders; }
    public void setCompletedOrders(int completedOrders) { this.completedOrders = completedOrders; }

    public int getImportedQuantity() { return importedQuantity; }
    public void setImportedQuantity(int importedQuantity) { this.importedQuantity = importedQuantity; }

    public int getExportedQuantity() { return exportedQuantity; }
    public void setExportedQuantity(int exportedQuantity) { this.exportedQuantity = exportedQuantity; }

    public int getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(int soldQuantity) { this.soldQuantity = soldQuantity; }

    public double getProfitMargin() {
        if (netRevenue == null || netRevenue.compareTo(BigDecimal.ZERO) <= 0) return 0;
        return grossProfit.multiply(BigDecimal.valueOf(100))
                .divide(netRevenue, 2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public boolean isNegativeProfit() {
        return grossProfit != null && grossProfit.compareTo(BigDecimal.ZERO) < 0;
    }

    private BigDecimal safe(BigDecimal value) { return value == null ? BigDecimal.ZERO : value; }
}
