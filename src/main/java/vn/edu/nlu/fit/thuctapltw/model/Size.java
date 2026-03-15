package vn.edu.nlu.fit.thuctapltw.model;

public class Size {
    private int id;
    private String code;
    private int sortOrder;

    public Size() {
    }

    public Size(int id, String code, int sortOrder) {
        this.id = id;
        this.code = code;
        this.sortOrder = sortOrder;
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

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}