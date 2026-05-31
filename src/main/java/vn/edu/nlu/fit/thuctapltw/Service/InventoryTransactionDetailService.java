package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.InventoryTransactionDetailDao;
import vn.edu.nlu.fit.thuctapltw.model.InventoryTransactionDetail;

import java.util.List;

public class InventoryTransactionDetailService {
    private final InventoryTransactionDetailDao inventoryTransactionDetailDao = new InventoryTransactionDetailDao();

    public List<InventoryTransactionDetail> getDetailsByTransactionId(int transactionId) {
        return inventoryTransactionDetailDao.getDetailsByTransactionId(transactionId);
    }
}
