package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.DashboardDao;
import vn.edu.nlu.fit.thuctapltw.DAO.InventoryDao;
import vn.edu.nlu.fit.thuctapltw.model.Order;
import vn.edu.nlu.fit.thuctapltw.model.TopSellingProduct;

import java.util.List;

public class DashboardService {

    private DashboardDao dao = new DashboardDao();
    private InventoryDao inventoryDao = new InventoryDao();

    public int countOrders() {
        return dao.countOrders();
    }

    public double totalRevenue() {
        return dao.totalRevenue();
    }

    public int countProducts() {
        return dao.countProducts();
    }

    public int countUsers() {
        return dao.countUsers();
    }

    public List<Order> latestOrders(int limit) {
        return dao.latestOrders(limit);
    }

    public List<TopSellingProduct> topSellingProducts(int limit) {return dao.topSellingProducts(limit);
    }

    public int countOrdersByStatus(String status) {
        return dao.countOrdersByStatus(status);
    }

    public int countPendingReturnOrders() {
        return dao.countPendingReturnOrders();
    }

    public int countNewContacts() {
        return dao.countNewContacts();
    }

    public int countLowStockProducts() {
        return inventoryDao.countLowStock();
    }
}
