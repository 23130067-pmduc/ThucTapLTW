package vn.edu.nlu.fit.thuctapltw.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Product implements Serializable {
    private int id;
    private int category_id;
    private String name;
    private String description;
    private double price;
    private double sale_price;
    private String thumbnail;
    private LocalDateTime created_at;
    private int views;
    private String status;
    private String categoryName;

    public Product(int id, int category_id, String name, String description, double price, double
            sale_price, String thumbnail, LocalDateTime created_at, int views, String status) {
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.sale_price = sale_price;
        this.thumbnail = thumbnail;
        this.created_at = created_at;
        this.views = views;
        this.status = status;
    }

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSale_price() {
        return sale_price;
    }

    public void setSale_price(double sale_price) {
        this.sale_price = sale_price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getDiscountPercent() {
        if (sale_price <= 0 || sale_price >= price) return 0;
        return (int) Math.round((price - sale_price) * 100 / price);
    }
    public boolean isOnSale() {
        return sale_price > 0 && sale_price < price;
    }

}