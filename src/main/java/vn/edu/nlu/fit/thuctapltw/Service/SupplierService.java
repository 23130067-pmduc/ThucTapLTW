package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.SupplierDao;
import vn.edu.nlu.fit.thuctapltw.model.Supplier;

import java.util.List;

public class SupplierService {
    private final SupplierDao supplierDao = new SupplierDao();

    public int countSuppliers(String keyword, String status) {
        return supplierDao.countSuppliers(keyword, status);
    }

    public List<Supplier> getSuppliersByPage(String keyword, String status, int pageSize, int offset) {
        return supplierDao.getSuppliersByPage(keyword, status, pageSize, offset);
    }

    public int countActive() {
        return supplierDao.countByStatus("ACTIVE");
    }

    public int countLocked() {
        return supplierDao.countByStatus("LOCKED");
    }
}
