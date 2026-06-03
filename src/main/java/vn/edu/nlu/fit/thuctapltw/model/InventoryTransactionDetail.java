package vn.edu.nlu.fit.thuctapltw.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.math.BigDecimal;

public class InventoryTransactionDetail {
    private int id;

    @ColumnName("transaction_id")
    private int transactionId;

    @ColumnName("product_variant_id")
    private int productVariantId;

    @ColumnName("product_id")
    private int productId;

    @ColumnName("product_name")
    private String productName;

    @ColumnName("category_name")
    private String categoryName;

    @ColumnName("color_name")
    private String colorName;

    @ColumnName("size_name")
    private String sizeName;

    private String thumbnail;
    private int quantity;

    @ColumnName("unit_cost")
    private BigDecimal unitCost;

    @ColumnName("line_total")
    private BigDecimal lineTotal;

    private String note;

    public InventoryTransactionDetail() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(int productVariantId) {
        this.productVariantId = productVariantId;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
