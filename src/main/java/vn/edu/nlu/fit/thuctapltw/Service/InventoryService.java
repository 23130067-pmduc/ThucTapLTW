package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.InventoryDao;
import vn.edu.nlu.fit.thuctapltw.model.InventoryItem;

import java.math.BigDecimal;
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

    public SheetVariantResolution resolveVariantForSheetImport(Integer productId,
                                                               String productName,
                                                               String categoryName,
                                                               String colorName,
                                                               String sizeName,
                                                               BigDecimal sellingPrice,
                                                               String thumbnail,
                                                               String description) {
        InventoryDao.SheetVariantResolution resolution = inventoryDao.resolveVariantForSheetImport(
                productId, productName, categoryName, colorName, sizeName, sellingPrice, thumbnail, description
        );
        if (resolution.isSuccess()) {
            return SheetVariantResolution.success(resolution.getVariantId(), resolution.getProductId(), resolution.getAction());
        }
        return SheetVariantResolution.error(resolution.getMessage());
    }

    public static class SheetVariantResolution {
        private final boolean success;
        private final Integer variantId;
        private final Integer productId;
        private final String action;
        private final String message;

        private SheetVariantResolution(boolean success, Integer variantId, Integer productId, String action, String message) {
            this.success = success;
            this.variantId = variantId;
            this.productId = productId;
            this.action = action;
            this.message = message;
        }

        public static SheetVariantResolution success(Integer variantId, Integer productId, String action) {
            return new SheetVariantResolution(true, variantId, productId, action, "");
        }

        public static SheetVariantResolution error(String message) {
            return new SheetVariantResolution(false, null, null, "", message);
        }

        public boolean isSuccess() { return success; }
        public Integer getVariantId() { return variantId; }
        public Integer getProductId() { return productId; }
        public String getAction() { return action; }
        public String getMessage() { return message; }
    }

}
