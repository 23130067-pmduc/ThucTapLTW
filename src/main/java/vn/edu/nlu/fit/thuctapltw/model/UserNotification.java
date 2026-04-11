package vn.edu.nlu.fit.thuctapltw.model;

import java.sql.Timestamp;

public class UserNotification {
    private int id;
    private int user_id;
    private int notification_id;
    private int is_read;
    private Timestamp read_at;

    public UserNotification() {
    }

    public UserNotification(int id, int user_id, int notification_id, int is_read, Timestamp read_at) {
        this.id = id;
        this.user_id = user_id;
        this.notification_id = notification_id;
        this.is_read = is_read;
        this.read_at = read_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public Timestamp getRead_at() {
        return read_at;
    }

    public void setRead_at(Timestamp read_at) {
        this.read_at = read_at;
    }
}