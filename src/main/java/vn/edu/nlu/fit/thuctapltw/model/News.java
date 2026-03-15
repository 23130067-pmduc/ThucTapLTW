package vn.edu.nlu.fit.thuctapltw.model;

import java.sql.Timestamp;

public class News {
    private int id, author_id, status;
    private String title, slug, thumbnail, short_description, content;
    private Timestamp created_at, updated_at;

    // No-args constructor for JDBI
    public News() {
    }

    public News(int id, int author_id, int status, String title, String slug, String thumbnail, String short_description, String content, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.author_id = author_id;
        this.status = status;
        this.title = title;
        this.slug = slug;
        this.thumbnail = thumbnail;
        this.short_description = short_description;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getSlug() {
        return slug;
    }
    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getShortDescription() {
        return short_description;
    }
    public void setShortDescription(String shortDescription) {
        this.short_description = shortDescription;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public int getAuthorId() {
        return author_id;
    }
    public void setAuthorId(int authorId) {
        this.author_id = authorId;
    }

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return created_at;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.created_at = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updated_at;
    }
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updated_at = updatedAt;
    }
}