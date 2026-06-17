package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.InventoryDao;
import vn.edu.nlu.fit.thuctapltw.model.InventoryItem;

import java.util.List;

public class InventoryService {
    private final InventoryDao inventoryDao = new InventoryDao();

    public List<InventoryItem> searchInventory(String keyword, String stockStatus, String sortField, String sortDir, int limit, int offset) {
        return inventoryDao.searchInventory(keyword, stockStatus, sortField, sortDir, limit, offset);
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

    public List<InventoryItem> getInventoryItemsForTransaction() {
        return inventoryDao.getInventoryItemsForTransaction();
    }

    public List<InventoryItem> getInventoryItemsForGoogleSheet() {
        return inventoryDao.getInventoryItemsForGoogleSheet();
    }

    public boolean isActiveVariantForImport(int variantId) {
        return inventoryDao.isActiveVariantForImport(variantId);
    }

    public boolean isActiveSupplierCode(String supplierCode) {
        return inventoryDao.isActiveSupplierCode(supplierCode);
    }
}
