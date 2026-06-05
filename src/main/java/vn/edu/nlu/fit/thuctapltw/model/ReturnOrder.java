package vn.edu.nlu.fit.thuctapltw.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReturnOrder {
    private int id;
    private String code;

    @ColumnName("order_id")
    private int orderId;

    @ColumnName("user_id")
    private int userId;

    @ColumnName("receiver_name")
    private String receiverName;

    private String phone;
    private String reason;
    private String status;

    @ColumnName("admin_note")
    private String adminNote;

    @ColumnName("inventory_transaction_id")
    private Integer inventoryTransactionId;

    @ColumnName("inventory_transaction_code")
    private String inventoryTransactionCode;

    @ColumnName("created_by")
    private Integer createdBy;

    @ColumnName("created_at")
    private LocalDateTime createdAt;

    @ColumnName("updated_at")
    private LocalDateTime updatedAt;

    @ColumnName("created_at_text")
    private String createdAtText;

    @ColumnName("updated_at_text")
    private String updatedAtText;

    @ColumnName("item_count")
    private int itemCount;

    @ColumnName("total_quantity")
    private int totalQuantity;

    private List<ReturnOrderItem> items;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAdminNote() { return adminNote; }
    public void setAdminNote(String adminNote) { this.adminNote = adminNote; }

    public Integer getInventoryTransactionId() { return inventoryTransactionId; }
    public void setInventoryTransactionId(Integer inventoryTransactionId) { this.inventoryTransactionId = inventoryTransactionId; }

    public String getInventoryTransactionCode() { return inventoryTransactionCode; }
    public void setInventoryTransactionCode(String inventoryTransactionCode) { this.inventoryTransactionCode = inventoryTransactionCode; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCreatedAtText() {
        if (createdAtText != null && !createdAtText.isBlank()) return createdAtText;
        if (createdAt == null) return "";
        return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    public void setCreatedAtText(String createdAtText) { this.createdAtText = createdAtText; }

    public String getUpdatedAtText() {
        if (updatedAtText != null && !updatedAtText.isBlank()) return updatedAtText;
        if (updatedAt == null) return "";
        return updatedAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    public void setUpdatedAtText(String updatedAtText) { this.updatedAtText = updatedAtText; }

    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public List<ReturnOrderItem> getItems() { return items; }
    public void setItems(List<ReturnOrderItem> items) { this.items = items; }

    public String getStatusText() {
        if (status == null) return "Không xác định";
        return switch (status) {
            case "PENDING" -> "Chờ duyệt";
            case "APPROVED" -> "Đã duyệt";
            case "REJECTED" -> "Đã từ chối";
            case "COMPLETED" -> "Đã nhập kho";
            default -> status;
        };
    }
}
