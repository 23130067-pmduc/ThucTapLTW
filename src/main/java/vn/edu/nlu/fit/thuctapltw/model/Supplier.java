package vn.edu.nlu.fit.thuctapltw.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class Supplier {
    private int id;
    private String code;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String note;
    private String status;

    @ColumnName("created_at_text")
    private String createdAtText;

    @ColumnName("updated_at_text")
    private String updatedAtText;

    public Supplier() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAtText() {
        return createdAtText;
    }

    public void setCreatedAtText(String createdAtText) {
        this.createdAtText = createdAtText;
    }

    public String getUpdatedAtText() {
        return updatedAtText;
    }

    public void setUpdatedAtText(String updatedAtText) {
        this.updatedAtText = updatedAtText;
    }

    public String getStatusText() {
        if ("ACTIVE".equalsIgnoreCase(status)) {
            return "Đang hoạt động";
        }
        if ("LOCKED".equalsIgnoreCase(status)) {
            return "Đã khóa";
        }
        return "Không xác định";
    }
}
