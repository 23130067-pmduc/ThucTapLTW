package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.InventoryTransactionDao;
import vn.edu.nlu.fit.thuctapltw.model.InventoryTransaction;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

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
    public Map<Integer, String> validateExportStock(List<Integer> variantIds, List<Integer> quantities) {
        Map<Integer, String> errors = new LinkedHashMap<>();

        if (variantIds == null || quantities == null || variantIds.isEmpty()) {
            return errors;
        }

        Map<Integer, Integer> requestedQuantityMap = new LinkedHashMap<>();
        int length = Math.min(variantIds.size(), quantities.size());

        for (int i = 0; i < length; i++) {
            int variantId = variantIds.get(i);
            int quantity = quantities.get(i);

            if (variantId > 0 && quantity > 0) {
                requestedQuantityMap.merge(variantId, quantity, Integer::sum);
            }
        }

        if (requestedQuantityMap.isEmpty()) {
            return errors;
        }

        Map<Integer, Integer> stockMap = inventoryTransactionDao.getVariantStockMap(
                requestedQuantityMap.keySet().stream().toList()
        );

        for (Map.Entry<Integer, Integer> entry : requestedQuantityMap.entrySet()) {
            int variantId = entry.getKey();
            int requestedQuantity = entry.getValue();
            int currentStock = stockMap.getOrDefault(variantId, 0);

            if (requestedQuantity > currentStock) {
                errors.put(variantId, "Biến thể #" + variantId + " chỉ còn " + currentStock
                        + " sản phẩm, không thể xuất " + requestedQuantity + " sản phẩm.");
            }
        }

        return errors;
    }


    public int createTransaction(String type, String supplierName, String note, Integer createdBy, List<Integer> variantIds, List<Integer> quantities) {
        return inventoryTransactionDao.createTransaction(type, supplierName, note, createdBy, variantIds, quantities);
    }

    public String updateStatusIfPending(int id, String status) {
        if (id <= 0 || status == null) {
            return "INVALID_STATUS";
        }

        String normalizedStatus = status.trim().toUpperCase();
        if (!"COMPLETED".equals(normalizedStatus) && !"CANCELLED".equals(normalizedStatus)) {
            return "INVALID_STATUS";
        }

        return inventoryTransactionDao.updateStatusIfPending(id, normalizedStatus);
    }

}
