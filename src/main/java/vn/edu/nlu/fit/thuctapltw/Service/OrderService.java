package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.OrderDao;
import vn.edu.nlu.fit.thuctapltw.model.Order;
import vn.edu.nlu.fit.thuctapltw.model.OrderItem;

import java.util.List;

public class OrderService {
    private final OrderDao dao = new OrderDao();

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
}
