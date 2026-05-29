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
}
