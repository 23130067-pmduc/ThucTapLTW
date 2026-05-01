package vn.edu.nlu.fit.thuctapltw.model;

import vn.edu.nlu.fit.thuctapltw.model.Product;

import java.io.Serializable;

public class CartItem implements Serializable {
    private int quantity;
    private double price;
    private Product product;
    private int variantId;
    private String size;
    private String color;
    private int stock;
    public int getVariantId() {
        return variantId;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public int getStock() {
        return stock;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public CartItem(int quantity, double price, Product product, int variantId, String size, String color) {
        this.quantity = quantity;
        this.price = price;
        this.product = product;
        this.variantId = variantId;
        this.size = size;
        this.color = color;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public CartItem(){

    }
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }
}