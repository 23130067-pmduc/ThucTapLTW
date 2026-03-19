package vn.edu.nlu.fit.thuctapltw.DAO;

public class CartDao extends BaseDao {

    public Integer findCartIdByUser(int userId) {
        return getJdbi().withHandle(h -> h.createQuery("SELECT id FROM carts WHERE user_id = :uid").bind("uid", userId).mapTo(Integer.class).findOne().orElse(null));
    }

    public int createCart(int userId) {
        return getJdbi().withHandle(h -> h.createUpdate("INSERT INTO carts(user_id, created_at) VALUES (:uid, NOW())").bind("uid", userId).executeAndReturnGeneratedKeys("id").mapTo(int.class).one());
    }
}