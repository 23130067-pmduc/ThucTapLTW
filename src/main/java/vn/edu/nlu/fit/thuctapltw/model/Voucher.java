package vn.edu.nlu.fit.thuctapltw.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Voucher implements Serializable {
    private int id;
    private String code;
    private String name;
    private String description;
    private String discount_type;
    private double discount_value;
    private Double max_discount;
    private double min_order_value;
    private String voucher_scope;
    private Integer customer_id;
    private Integer product_id;
    private Integer order_id;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private int quantity;
    private int used_quantity;
    private int status;
    private LocalDateTime created_at;

    public Voucher() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public double getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(double discount_value) {
        this.discount_value = discount_value;
    }

    public Double getMax_discount() {
        return max_discount;
    }

    public void setMax_discount(Double max_discount) {
        this.max_discount = max_discount;
    }

    public double getMin_order_value() {
        return min_order_value;
    }

    public void setMin_order_value(double min_order_value) {
        this.min_order_value = min_order_value;
    }

    public String getVoucher_scope() {
        return voucher_scope;
    }

    public void setVoucher_scope(String voucher_scope) {
        this.voucher_scope = voucher_scope;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUsed_quantity() {
        return used_quantity;
    }

    public void setUsed_quantity(int used_quantity) {
        this.used_quantity = used_quantity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }


    public String getScopeLabel() {
        if ("PRODUCT".equalsIgnoreCase(voucher_scope)) return "Sản phẩm";
        if ("SHIPPING".equalsIgnoreCase(voucher_scope)) return "Vận chuyển";
        return "Đơn hàng";
    }

    public String getDiscountTypeLabel() {
        if ("PERCENT".equalsIgnoreCase(discount_type)) return "Theo phần trăm";
        return "Số tiền cố định";
    }

    public String getStatusLabel() {
        if (status == 0) return "Đã khóa";
        if (end_date != null && end_date.isBefore(LocalDateTime.now())) return "Hết hạn";
        if (used_quantity >= quantity) return "Hết lượt";
        if (start_date != null && start_date.isAfter(LocalDateTime.now())) return "Chưa bắt đầu";
        return "Đang hoạt động";
    }

    public String getStatusClass() {
        if (status == 0) return "blocked";
        if (end_date != null && end_date.isBefore(LocalDateTime.now())) return "expired";
        if (used_quantity >= quantity) return "soldout";
        if (start_date != null && start_date.isAfter(LocalDateTime.now())) return "waiting";
        return "active";
    }

    public String getStartDateText() {
        if (start_date == null) return "-";
        return start_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getEndDateText() {
        if (end_date == null) return "-";
        return end_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    public String getStartDateInputValue() {
        if (start_date == null) return "";
        return start_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    public String getEndDateInputValue() {
        if (end_date == null) return "";
        return end_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    public String getMinOrderText() {
        if (min_order_value <= 0) return "Không yêu cầu";
        return formatCurrency(min_order_value);
    }

    public String getMaxDiscountText() {
        if (max_discount == null || max_discount <= 0) return "Không giới hạn";
        return formatCurrency(max_discount);
    }

    public String getUsageText() {
        return used_quantity + "/" + quantity;
    }

    public int getRemainingQuantity() {
        return Math.max(0, quantity - used_quantity);
    }

    public String getCreatedAtText() {
        if (created_at == null) return "-";
        return created_at.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getTypeLabel() {
        if ("PRODUCT".equalsIgnoreCase(voucher_scope)) return "Giảm sản phẩm";
        return "Giảm đơn hàng";
    }

    public String getDiscountText() {
        if ("PERCENT".equalsIgnoreCase(discount_type)) return "Giảm " + formatSimpleNumber(discount_value) + "%";
        return "Giảm " + formatCurrency(discount_value);
    }

    public String getConditionText() {
        if (min_order_value <= 0) return "Áp dụng không yêu cầu giá trị tối thiểu";
        return "Áp dụng cho đơn từ " + formatCurrency(min_order_value);
    }

    public String getExpireText() {
        if (end_date == null) return "Không giới hạn thời hạn";
        return "Hạn dùng: " + end_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String formatCurrency(double value) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(value) + "đ";
    }

    private String formatSimpleNumber(double value) {
        if (value == Math.floor(value)) return String.valueOf((int) value);
        return String.valueOf(value);
    }
}
