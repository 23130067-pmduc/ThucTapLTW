package vn.edu.nlu.fit.thuctapltw.model;

import java.sql.Timestamp;

public class ReviewImage {
    private int id;
    private int reviewId;
    private String imageUrl;
    private Timestamp createdAt;

    public ReviewImage() {
    }

    public ReviewImage(int id, int reviewId, String imageUrl, Timestamp createdAt) {
        this.id = id;
        this.reviewId = reviewId;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
