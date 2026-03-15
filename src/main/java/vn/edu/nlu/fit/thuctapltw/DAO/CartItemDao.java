package vn.edu.nlu.fit.thuctapltw.DAO;


import vn.edu.nlu.fit.thuctapltw.model.CartItem;
import vn.edu.nlu.fit.thuctapltw.model.Product;

import java.util.List;

public class CartItemDao extends BaseDao {

    public void addOrUpdate(int cartId, int variantId, int productId, int quantity, double price) {
        getJdbi().useHandle(h -> {
            Integer exists = h.createQuery("""
                SELECT id FROM cart_items WHERE cart_id = :cid AND variant_id = :vid """).bind("cid", cartId).bind("vid", variantId).mapTo(Integer.class).findOne().orElse(null);

            if (exists != null) { h.createUpdate(""" 
                UPDATE cart_items
                SET quantity = quantity + :q
                WHERE id = :id""").bind("q", quantity).bind("id", exists).execute();
            } else { h.createUpdate("""
                INSERT INTO cart_items(cart_id, product_id, variant_id, quantity, price) VALUES (:cid, :pid, :vid, :q, :price)""").bind("cid", cartId).bind("pid", productId).bind("vid", variantId).bind("q", quantity).bind("price", price).execute();}});
    }

    public int countTotalQuantity(int cartId) {
        return getJdbi().withHandle(h -> h.createQuery("SELECT COALESCE(SUM(quantity),0) FROM cart_items WHERE cart_id = :cid").bind("cid", cartId).mapTo(int.class).one());
    }

    public List<CartItem> getItemsByCartId(int cartId) {
        String sql = """
        SELECT ci.variant_id, ci.quantity, ci.price, p.id   AS pid, p.name, p.thumbnail, s.code AS size, c.name AS color
        FROM cart_items ci
        JOIN product_variants v ON ci.variant_id = v.id
        JOIN products p ON v.product_id = p.id
        JOIN sizes s ON v.size_id = s.id
        JOIN colors c ON v.color_id = c.id
        WHERE ci.cart_id = :cid """;
        return getJdbi().withHandle(h -> h.createQuery(sql).bind("cid", cartId).map((rs, ctx) -> {
            CartItem item = new CartItem();
            item.setVariantId(rs.getInt("variant_id"));
            item.setQuantity(rs.getInt("quantity"));
            item.setPrice(rs.getDouble("price"));
            item.setSize(rs.getString("size"));
            item.setColor(rs.getString("color"));

            Product p = new Product();
            p.setId(rs.getInt("pid"));
            p.setName(rs.getString("name"));
            p.setThumbnail(rs.getString("thumbnail"));
            item.setProduct(p);
            return item;}).list()
        );
    }



    public void delete(int cartId, int variantId) {
        getJdbi().useHandle(h ->
                h.createUpdate("""
                    DELETE FROM cart_items WHERE cart_id = :cid AND variant_id = :vid """).bind("cid", cartId).bind("vid", variantId).execute()
        );
    }

    public void updateQuantity(int cartId, int variantId, int quantity) {
        getJdbi().useHandle(h -> h.createUpdate("""
            UPDATE cart_items SET quantity = :q WHERE cart_id = :cid AND variant_id = :vid """).bind("q", quantity).bind("cid", cartId).bind("vid", variantId).execute());
    }


    public List<CartItem> getByCartId(int cartId) {
        return getJdbi().withHandle(h -> h.createQuery("""
            SELECT ci.variant_id, ci.quantity, ci.price, s.code AS size, c.name AS color, p.id   AS product_id, p.name AS product_name, p.thumbnail
            FROM cart_items ci
            JOIN product_variants pv ON ci.variant_id = pv.id
            JOIN products p ON pv.product_id = p.id
            LEFT JOIN sizes s ON pv.size_id = s.id
            LEFT JOIN colors c ON pv.color_id = c.id
            WHERE ci.cart_id = :cartId """).bind("cartId", cartId).map((rs, ctx) -> {
                CartItem item = new CartItem();
                item.setVariantId(rs.getInt("variant_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                item.setSize(rs.getString("size"));
                item.setColor(rs.getString("color"));

                Product p = new Product();
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("product_name"));
                p.setThumbnail(rs.getString("thumbnail"));
                item.setProduct(p);
                return item;}).list());
    }


    public void clearCart(int cartId) {
        getJdbi().useHandle(h -> h.createUpdate("DELETE FROM cart_items WHERE cart_id = :cid").bind("cid", cartId).execute());
    }
}
