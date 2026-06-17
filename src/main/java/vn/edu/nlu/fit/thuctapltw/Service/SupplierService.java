package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.SupplierDao;
import vn.edu.nlu.fit.thuctapltw.model.Supplier;

import java.util.List;
import java.util.regex.Pattern;

public class SupplierService {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9+\\-\\s]{9,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

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

    public void create(Supplier supplier) {
        normalizeSupplier(supplier);
        validateForCreate(supplier);
        supplierDao.create(supplier);
    }

    private void validateForCreate(Supplier supplier) {
        if (supplier.getCode().isEmpty()) {
            throw new IllegalArgumentException("Mã nhà cung cấp không được để trống.");
        }

        if (supplier.getCode().length() > 30) {
            throw new IllegalArgumentException("Mã nhà cung cấp không được vượt quá 30 ký tự.");
        }

        if (supplierDao.existsByCode(supplier.getCode())) {
            throw new IllegalArgumentException("Mã nhà cung cấp đã tồn tại.");
        }

        if (supplier.getName().isEmpty()) {
            throw new IllegalArgumentException("Tên nhà cung cấp không được để trống.");
        }

        if (supplier.getName().length() > 150) {
            throw new IllegalArgumentException("Tên nhà cung cấp không được vượt quá 150 ký tự.");
        }

        if (!supplier.getPhone().isEmpty() && !PHONE_PATTERN.matcher(supplier.getPhone()).matches()) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ.");
        }

        if (!supplier.getEmail().isEmpty() && !EMAIL_PATTERN.matcher(supplier.getEmail()).matches()) {
            throw new IllegalArgumentException("Email không hợp lệ.");
        }

        if (!supplier.getEmail().isEmpty() && supplierDao.existsByEmail(supplier.getEmail())) {
            throw new IllegalArgumentException("Email nhà cung cấp đã tồn tại.");
        }
    }

    private void normalizeSupplier(Supplier supplier) {
        supplier.setCode(normalize(supplier.getCode()).toUpperCase());
        supplier.setName(normalize(supplier.getName()));
        supplier.setPhone(normalize(supplier.getPhone()));
        supplier.setEmail(normalize(supplier.getEmail()).toLowerCase());
        supplier.setAddress(normalize(supplier.getAddress()));
        supplier.setNote(normalize(supplier.getNote()));

        String status = normalize(supplier.getStatus()).toUpperCase();
        if (!"LOCKED".equals(status)) {
            status = "ACTIVE";
        }
        supplier.setStatus(status);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
