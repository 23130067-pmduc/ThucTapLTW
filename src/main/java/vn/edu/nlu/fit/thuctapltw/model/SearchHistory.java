package vn.edu.nlu.fit.thuctapltw.model;

import java.sql.Timestamp;

public class SearchHistory {
    private int id;
    private int user_id;
    private String keyword;
    private Timestamp searched_at;

    public SearchHistory() {
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Timestamp getSearched_at() {
        return searched_at;
    }

    public void setSearched_at(Timestamp searched_at) {
        this.searched_at = searched_at;
    }
}