package vn.edu.nlu.fit.thuctapltw.model;

import java.sql.Timestamp;

public class Notification {
    private int id;
    private String title;
    private String content;
    private String type;
    private String link;
    private Timestamp created_at;
    private int is_read;

    public Notification() {
    }

    public Notification(int id, String title, String content, String type, String link, Timestamp created_at, int is_read) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.link = link;
        this.created_at = created_at;
        this.is_read = is_read;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }
}