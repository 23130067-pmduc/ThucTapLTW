package vn.edu.nlu.fit.thuctapltw.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Order {
    private int id;
    private int userId;
    private String receiverName;
    private String phone;
    private String shippingAddress;
    private String note;
    private double totalPrice;
    private double discount;
    private double shippingFee;
    private double finalAmount;
    private String paymentMethods;
    private String paymentStatuses;
    private String orderStatus;
    private LocalDateTime createdAt;
    private List<OrderItem> items;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    public double getShippingFee() { return shippingFee; }
    public void setShippingFee(double shippingFee) { this.shippingFee = shippingFee; }
    public double getFinalAmount() { return finalAmount; }
    public void setFinalAmount(double finalAmount) { this.finalAmount = finalAmount; }
    public String getPaymentMethods() { return paymentMethods; }
    public void setPaymentMethods(String paymentMethods) { this.paymentMethods = paymentMethods; }
    public String getPaymentStatuses() { return paymentStatuses; }
    public void setPaymentStatuses(String paymentStatuses) { this.paymentStatuses = paymentStatuses; }
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public String getCreatedAtFormatted() {
        if (createdAt == null) return "";
        return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getCreatedDateOnly() {
        if (createdAt == null) return "";
        return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getOrderStatusLabel() {
        if (orderStatus == null) return "Không xác định";
        return switch (orderStatus) {
            case "PENDING" -> "Chờ xác nhận";
            case "SHIPPING" -> "Đang giao";
            case "COMPLETED" -> "Đã giao";
            case "CANCELLED" -> "Đã huỷ";
            default -> orderStatus;
        };
    }

    public String getPaymentStatusLabel() {
        if (paymentStatuses == null) return "Không xác định";
        return switch (paymentStatuses) {
            case "PAID" -> "Đã thanh toán";
            case "UNPAID" -> "Chưa thanh toán";
            default -> paymentStatuses;
        };
    }

    public String getPaymentMethodLabel() {
        if (paymentMethods == null) return "Không xác định";
        return switch (paymentMethods) {
            case "COD" -> "Thanh toán khi nhận hàng";
            case "BANK" -> "Chuyển khoản";
            case "MOMO" -> "Ví MoMo";
            case "VNPAY" -> "VNPay";
            default -> paymentMethods;
        };
    }
}
