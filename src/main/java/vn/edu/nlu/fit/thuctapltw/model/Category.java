package vn.edu.nlu.fit.thuctapltw.model;

import java.io.Serializable;

public class Category implements Serializable {
    private int id;
    private String name;
    private String description;
    private int status;
    private String image;

    public Category() {
    }

    public Category(int id, String name, String description, int status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = "Đang dùng".equals(status) ? 1 : 0;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + ", status=" + status + ", image='" + image + '\'' + '}';
    }
}
