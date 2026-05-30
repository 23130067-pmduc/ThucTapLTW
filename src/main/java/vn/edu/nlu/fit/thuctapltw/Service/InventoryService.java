package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.InventoryDao;
import vn.edu.nlu.fit.thuctapltw.model.InventoryItem;

import java.util.List;

public class InventoryService {
    private final InventoryDao inventoryDao = new InventoryDao();

    public List<InventoryItem> searchInventory(String keyword, String stockStatus, int limit, int offset) {
        return inventoryDao.searchInventory(keyword, stockStatus, limit, offset);
    }

    public int countInventoryByFilter(String keyword, String stockStatus) {
        return inventoryDao.countInventoryByFilter(keyword, stockStatus);
    }

    public int countTotalVariants() {
        return inventoryDao.countTotalVariants();
    }

    public int countLowStock() {
        return inventoryDao.countLowStock();
    }

    public int countOutOfStock() {
        return inventoryDao.countOutOfStock();
    }

    public int sumStock() {
        return inventoryDao.sumStock();
    }
}
