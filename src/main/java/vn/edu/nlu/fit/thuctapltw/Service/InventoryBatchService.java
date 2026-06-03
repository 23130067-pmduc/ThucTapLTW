package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.InventoryBatchDao;
import vn.edu.nlu.fit.thuctapltw.model.InventoryBatch;

import java.math.BigDecimal;
import java.util.List;

public class InventoryBatchService {
    private final InventoryBatchDao inventoryBatchDao = new InventoryBatchDao();

    public List<InventoryBatch> searchBatches(String keyword, String batchStatus, int limit, int offset) {
        return inventoryBatchDao.searchBatches(keyword, batchStatus, limit, offset);
    }

    public int countBatches(String keyword, String batchStatus) {
        return inventoryBatchDao.countBatches(keyword, batchStatus);
    }

    public int countAll() {
        return inventoryBatchDao.countAll();
    }

    public int countRemainingBatches() {
        return inventoryBatchDao.countRemainingBatches();
    }

    public int sumRemainingQuantity() {
        return inventoryBatchDao.sumRemainingQuantity();
    }

    public BigDecimal sumRemainingValue() {
        return inventoryBatchDao.sumRemainingValue();
    }

    public InventoryBatch getById(int id) {
        return inventoryBatchDao.getById(id);
    }
}
