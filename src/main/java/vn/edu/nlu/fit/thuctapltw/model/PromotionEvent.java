package vn.edu.nlu.fit.thuctapltw.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PromotionEvent implements Serializable {
    private int id;
    private String title;
    private String description;
    private String icon;
    private String tag;
    private String discountLabel;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int status;
    private int productCount;
    private List<Product> products = new ArrayList<>();

    public PromotionEvent() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon == null || icon.isBlank() ? "fa-gift" : icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDiscountLabel() {
        if (discountLabel != null && !discountLabel.isBlank()) {
            return discountLabel;
        }
        return tag;
    }

    public void setDiscountLabel(String discountLabel) {
        this.discountLabel = discountLabel;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products == null ? new ArrayList<>() : products;
    }

    public String getDateRangeText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (startDate == null && endDate == null) {
            return "Đang diễn ra";
        }
        if (startDate == null) {
            return "Đến " + endDate.format(formatter);
        }
        if (endDate == null) {
            return "Từ " + startDate.format(formatter);
        }
        return startDate.format(formatter) + " - " + endDate.format(formatter);
    }

    public String getStartDateText() {
        return formatDateTime(startDate);
    }

    public String getEndDateText() {
        return formatDateTime(endDate);
    }

    public String getStartDateInputValue() {
        return formatDateTimeInput(startDate);
    }

    public String getEndDateInputValue() {
        return formatDateTimeInput(endDate);
    }

    public String getStatusLabel() {
        LocalDateTime now = LocalDateTime.now();
        if (status == 0) return "Đã khóa";
        if (endDate != null && endDate.isBefore(now)) return "Đã kết thúc";
        if (startDate != null && startDate.isAfter(now)) return "Sắp diễn ra";
        return "Đang diễn ra";
    }

    public String getStatusClass() {
        LocalDateTime now = LocalDateTime.now();
        if (status == 0) return "locked";
        if (endDate != null && endDate.isBefore(now)) return "expired";
        if (startDate != null && startDate.isAfter(now)) return "upcoming";
        return "active";
    }

    public String getScopeLabel() {
        return productCount > 0 ? "Sản phẩm được chọn" : "Toàn cửa hàng";
    }

    private String formatDateTime(LocalDateTime value) {
        if (value == null) return "-";
        return value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    private String formatDateTimeInput(LocalDateTime value) {
        if (value == null) return "";
        return value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }
}
