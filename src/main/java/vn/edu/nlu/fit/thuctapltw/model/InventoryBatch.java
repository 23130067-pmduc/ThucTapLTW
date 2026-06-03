package vn.edu.nlu.fit.thuctapltw.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.math.BigDecimal;

public class InventoryBatch {
    private int id;

    @ColumnName("batch_code")
    private String batchCode;

    @ColumnName("transaction_id")
    private int transactionId;

    @ColumnName("transaction_code")
    private String transactionCode;

    @ColumnName("transaction_detail_id")
    private int transactionDetailId;

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

    @ColumnName("import_quantity")
    private int importQuantity;

    @ColumnName("remaining_quantity")
    private int remainingQuantity;

    @ColumnName("unit_cost")
    private BigDecimal unitCost;

    @ColumnName("import_value")
    private BigDecimal importValue;

    @ColumnName("remaining_value")
    private BigDecimal remainingValue;

    @ColumnName("supplier_name")
    private String supplierName;

    @ColumnName("created_by_name")
    private String createdByName;

    private String note;

    @ColumnName("created_at_text")
    private String createdAtText;

    @ColumnName("updated_at_text")
    private String updatedAtText;

    public InventoryBatch() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBatchCode() { return batchCode; }
    public void setBatchCode(String batchCode) { this.batchCode = batchCode; }

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public String getTransactionCode() { return transactionCode; }
    public void setTransactionCode(String transactionCode) { this.transactionCode = transactionCode; }

    public int getTransactionDetailId() { return transactionDetailId; }
    public void setTransactionDetailId(int transactionDetailId) { this.transactionDetailId = transactionDetailId; }

    public int getProductVariantId() { return productVariantId; }
    public void setProductVariantId(int productVariantId) { this.productVariantId = productVariantId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getColorName() { return colorName; }
    public void setColorName(String colorName) { this.colorName = colorName; }

    public String getSizeName() { return sizeName; }
    public void setSizeName(String sizeName) { this.sizeName = sizeName; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public int getImportQuantity() { return importQuantity; }
    public void setImportQuantity(int importQuantity) { this.importQuantity = importQuantity; }

    public int getRemainingQuantity() { return remainingQuantity; }
    public void setRemainingQuantity(int remainingQuantity) { this.remainingQuantity = remainingQuantity; }

    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public BigDecimal getImportValue() { return importValue; }
    public void setImportValue(BigDecimal importValue) { this.importValue = importValue; }

    public BigDecimal getRemainingValue() { return remainingValue; }
    public void setRemainingValue(BigDecimal remainingValue) { this.remainingValue = remainingValue; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getCreatedAtText() { return createdAtText; }
    public void setCreatedAtText(String createdAtText) { this.createdAtText = createdAtText; }

    public String getUpdatedAtText() { return updatedAtText; }
    public void setUpdatedAtText(String updatedAtText) { this.updatedAtText = updatedAtText; }

    public String getBatchStatusText() {
        if (remainingQuantity <= 0) {
            return "Đã hết lô";
        }
        if (remainingQuantity < importQuantity) {
            return "Đang bán/xuất";
        }
        return "Còn nguyên lô";
    }
}
