package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.CartItemDao;
import vn.edu.nlu.fit.thuctapltw.DAO.OrderDao;
import vn.edu.nlu.fit.thuctapltw.DAO.ProductVariantDao;
import vn.edu.nlu.fit.thuctapltw.model.Order;
import vn.edu.nlu.fit.thuctapltw.model.OrderItem;

import java.util.List;

public class OrderService {
    private final OrderDao dao = new OrderDao();
    private final CartItemDao cartItemDao = new CartItemDao();
    private final ProductVariantDao productVariantDao = new ProductVariantDao();

    public List<Order> getAllOrders() {
        return dao.getAllOrders();
    }

    public Order findById(int id) {
        return dao.findById(id);
    }

    public List<OrderItem> getOrderItems(int orderId) {
        return dao.getItems(orderId);
    }

    public void updateStatus(int id, String status) {
        dao.updateStatus(id, status);
    }

    public String getUserEmailByOrderId(int orderId) {
        return dao.getUserEmailByOrderId(orderId);
    }

    public List<Order> searchOrders(String keyword, String status) {
        return dao.searchOrders(keyword, status);
    }

    public List<Order> getOrdersByUser(int userId, String status) {
        List<Order> orders = "all".equalsIgnoreCase(status)
                ? dao.getByUserId(userId)
                : dao.getByUserIdAndStatus(userId, status);

        for (Order order : orders) {
            order.setItems(dao.getItems(order.getId()));
        }
        return orders;
    }

    public void cancelOrder(int orderId, int userId) {
        Order order = dao.findByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new RuntimeException("Không tìm thấy đơn hàng");
        }
        if (!"PENDING".equals(order.getOrderStatus())) {
            throw new RuntimeException("Chỉ có thể huỷ đơn đang chờ xác nhận");
        }
        dao.updateStatusByUser(orderId, userId, "CANCELLED");
    }

    public void confirmReceived(int orderId, int userId) {
        Order order = dao.findByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new RuntimeException("Không tìm thấy đơn hàng");
        }
        if (!"SHIPPING".equals(order.getOrderStatus())) {
            throw new RuntimeException("Chỉ xác nhận nhận hàng với đơn đang giao");
        }
        dao.updateStatusByUser(orderId, userId, "COMPLETED");
    }

    public String rebuyOrder(int orderId, int userId, int cartId) {
        Order order = dao.findByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new RuntimeException("Không tìm thấy đơn hàng để mua lại");
        }

        List<OrderItem> items = dao.getItems(orderId);
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("Đơn hàng này không có sản phẩm để mua lại");
        }

        int addedCount = 0;
        int skippedCount = 0;

        for (OrderItem item : items) {
            try {
                int stock = productVariantDao.getStockByVariantId(item.getVariantId());
                if (stock <= 0) {
                    skippedCount++;
                    continue;
                }

                int productId = productVariantDao.getProductIdByVariantId(item.getVariantId());
                double price = productVariantDao.getPriceByVariantId(item.getVariantId());
                int quantityToAdd = Math.min(item.getQuantity(), stock);

                if (quantityToAdd <= 0) {
                    skippedCount++;
                    continue;
                }

                cartItemDao.addOrUpdate(cartId, item.getVariantId(), productId, quantityToAdd, price);
                addedCount++;
            } catch (Exception e) {
                skippedCount++;
            }
        }

        if (addedCount == 0) {
            throw new RuntimeException("Không thể mua lại vì các sản phẩm trong đơn không còn khả dụng");
        }

        if (skippedCount > 0) {
            return "Đã thêm lại " + addedCount + " sản phẩm vào giỏ hàng. Một số sản phẩm không còn khả dụng.";
        }

        return "Đã thêm lại đơn hàng vào giỏ hàng";
    }

    public int getCartSize(int cartId) {
        return cartItemDao.countTotalQuantity(cartId);
    }
}
