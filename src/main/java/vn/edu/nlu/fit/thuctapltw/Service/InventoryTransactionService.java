package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.InventoryTransactionDao;
import vn.edu.nlu.fit.thuctapltw.model.InventoryTransaction;

import java.util.List;

public class InventoryTransactionService {
    private final InventoryTransactionDao inventoryTransactionDao = new InventoryTransactionDao();

    public List<InventoryTransaction> searchTransactions(String keyword, String type, String status, int limit, int offset) {
        return inventoryTransactionDao.searchTransactions(keyword, type, status, limit, offset);
    }

    public int countTransactions(String keyword, String type, String status) {
        return inventoryTransactionDao.countTransactions(keyword, type, status);
    }

    public int countAll() {
        return inventoryTransactionDao.countAll();
    }

    public int countImport() {
        return inventoryTransactionDao.countByType("IMPORT");
    }

    public int countExport() {
        return inventoryTransactionDao.countByType("EXPORT");
    }

    public int countPending() {
        return inventoryTransactionDao.countByStatus("PENDING");
    }

    public InventoryTransaction getById(int id) {
        return inventoryTransactionDao.getById(id);
    }

    public int createTransaction(String type, String supplierName, String note, Integer createdBy, List<Integer> variantIds, List<Integer> quantities) {
        return inventoryTransactionDao.createTransaction(type, supplierName, note, createdBy, variantIds, quantities);
    }

    public boolean updateStatusIfPending(int id, String status) {
        if (id <= 0 || status == null) {
            return false;
        }

        String normalizedStatus = status.trim().toUpperCase();
        if (!"COMPLETED".equals(normalizedStatus) && !"CANCELLED".equals(normalizedStatus)) {
            return false;
        }

        return inventoryTransactionDao.updateStatusIfPending(id, normalizedStatus);
    }

}
