package vn.edu.nlu.fit.thuctapltw.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.math.BigDecimal;

public class InventoryItem {
    @ColumnName("variant_id")
    private int variantId;

    @ColumnName("product_id")
    private int productId;

    @ColumnName("product_name")
    private String productName;

    private String thumbnail;

    @ColumnName("category_name")
    private String categoryName;

    @ColumnName("color_name")
    private String colorName;

    @ColumnName("size_name")
    private String sizeName;

    private int stock;
    private double price;

    @ColumnName("sale_price")
    private double salePrice;

    @ColumnName("product_status")
    private String productStatus;

    @ColumnName("latest_unit_cost")
    private BigDecimal latestUnitCost;

    @ColumnName("latest_batch_code")
    private String latestBatchCode;

    @ColumnName("latest_import_date_text")
    private String latestImportDateText;

    @ColumnName("remaining_batch_quantity")
    private int remainingBatchQuantity;

    public InventoryItem() {
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }


    public BigDecimal getLatestUnitCost() {
        return latestUnitCost;
    }

    public void setLatestUnitCost(BigDecimal latestUnitCost) {
        this.latestUnitCost = latestUnitCost;
    }

    public String getLatestBatchCode() {
        return latestBatchCode;
    }

    public void setLatestBatchCode(String latestBatchCode) {
        this.latestBatchCode = latestBatchCode;
    }

    public String getLatestImportDateText() {
        return latestImportDateText;
    }

    public void setLatestImportDateText(String latestImportDateText) {
        this.latestImportDateText = latestImportDateText;
    }

    public int getRemainingBatchQuantity() {
        return remainingBatchQuantity;
    }

    public void setRemainingBatchQuantity(int remainingBatchQuantity) {
        this.remainingBatchQuantity = remainingBatchQuantity;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }
}
