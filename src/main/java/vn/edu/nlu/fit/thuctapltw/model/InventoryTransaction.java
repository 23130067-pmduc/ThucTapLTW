package vn.edu.nlu.fit.thuctapltw.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class InventoryTransaction {
    private int id;
    private String code;
    private String type;

    @ColumnName("total_quantity")
    private int totalQuantity;

    private String status;

    @ColumnName("supplier_name")
    private String supplierName;

    private String note;

    @ColumnName("created_by")
    private Integer createdBy;

    @ColumnName("created_by_name")
    private String createdByName;

    @ColumnName("created_at_text")
    private String createdAtText;

    public InventoryTransaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCreatedAtText() {
        return createdAtText;
    }

    public void setCreatedAtText(String createdAtText) {
        this.createdAtText = createdAtText;
    }

    public String getTypeText() {
        if ("IMPORT".equals(type)) {
            return "Nhập kho";
        }
        if ("EXPORT".equals(type)) {
            return "Xuất kho";
        }
        return "Không xác định";
    }

    public String getStatusText() {
        if ("PENDING".equals(status)) {
            return "Đang xử lý";
        }
        if ("COMPLETED".equals(status)) {
            return "Đã hoàn thành";
        }
        if ("CANCELLED".equals(status)) {
            return "Đã hủy";
        }
        return "Không xác định";
    }
}
