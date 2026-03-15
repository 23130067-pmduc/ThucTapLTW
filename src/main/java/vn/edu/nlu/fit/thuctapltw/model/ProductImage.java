package vn.edu.nlu.fit.thuctapltw.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class ProductImage {
    private int id;
    private int productId;

    @ColumnName("image_url")
    private String imageUrl;

    @ColumnName("is_main")
    private boolean main;

    public ProductImage(int id, int productId, String imageUrl, boolean main) {
        this.id = id;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.main = main;
    }

    public ProductImage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }
}