package vn.edu.nlu.fit.thuctapltw.model;

import java.time.LocalDateTime;

public class Banner {
    private int id;
    private String imageUrl;
    private String navigateTo;
    private String title;
    private boolean status;
    private LocalDateTime createdAt;


    public Banner(int id, String imageUrl, String navigateTo, String title, boolean status, LocalDateTime createdAt) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.navigateTo = navigateTo;
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Banner() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNavigateTo() {
        return navigateTo;
    }

    public void setNavigateTo(String navigateTo) {
        this.navigateTo = navigateTo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}