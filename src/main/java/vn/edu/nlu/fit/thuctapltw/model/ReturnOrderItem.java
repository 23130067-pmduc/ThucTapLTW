package vn.edu.nlu.fit.thuctapltw.model;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.math.BigDecimal;

public class ReturnOrderItem {
    private int id;

    @ColumnName("return_order_id")
    private int returnOrderId;

    @ColumnName("order_item_id")
    private int orderItemId;

    @ColumnName("product_variant_id")
    private int productVariantId;

    @ColumnName("product_name")
    private String productName;

    private String size;
    private String color;
    private int quantity;
    private BigDecimal price;
    private BigDecimal total;
    private String thumbnail;
    private String note;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReturnOrderId() { return returnOrderId; }
    public void setReturnOrderId(int returnOrderId) { this.returnOrderId = returnOrderId; }

    public int getOrderItemId() { return orderItemId; }
    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }

    public int getProductVariantId() { return productVariantId; }
    public void setProductVariantId(int productVariantId) { this.productVariantId = productVariantId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
