package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.VoucherDao;
import vn.edu.nlu.fit.thuctapltw.model.CartItem;
import vn.edu.nlu.fit.thuctapltw.model.Voucher;

import java.util.List;

public class VoucherService {
    private final VoucherDao voucherDao;

    public VoucherService() {
        this.voucherDao = new VoucherDao();
    }

    public List<Voucher> getActiveOrderAndProductVouchers() {
        return voucherDao.findActiveOrderAndProductVouchers();
    }

    public List<Voucher> getActiveShippingVouchers() {
        return voucherDao.findActiveShippingVouchers();
    }

    public List<Voucher> getAdminVouchers(String keyword, String scope, String status, int page, int pageSize) {
        if (page < 1) {
            page = 1;
        }
        int offset = (page - 1) * pageSize;
        return voucherDao.findAdminVouchers(keyword, scope, status, pageSize, offset);
    }

    public int countAdminVouchers(String keyword, String scope, String status) {
        return voucherDao.countAdminVouchers(keyword, scope, status);
    }

    public int countAllVouchers() {
        return voucherDao.countAllVouchers();
    }

    public int countActiveVouchers() {
        return voucherDao.countActiveVouchers();
    }

    public int countLockedVouchers() {
        return voucherDao.countLockedVouchers();
    }

    public int countExpiredVouchers() {
        return voucherDao.countExpiredVouchers();
    }

    public Voucher getById(int id) {
        return voucherDao.findById(id);
    }

    public String updateStatus(int id, int status) {
        if (id <= 0 || (status != 0 && status != 1)) {
            return "Dữ liệu khóa/mở mã giảm giá không hợp lệ.";
        }

        Voucher voucher = voucherDao.findById(id);
        if (voucher == null) {
            return "Không tìm thấy mã giảm giá.";
        }

        if (voucher.getStatus() == status) {
            return status == 1 ? "Mã giảm giá đã được mở." : "Mã giảm giá đã bị khóa.";
        }

        if (!voucherDao.updateStatus(id, status)) {
            return "Không thể cập nhật trạng thái mã giảm giá. Vui lòng thử lại.";
        }

        return null;
    }

    public String createVoucher(Voucher voucher) {
        if (voucher == null) {
            return "Dữ liệu mã giảm giá không hợp lệ.";
        }

        voucher.setCode(normalizeUpper(voucher.getCode()));
        voucher.setDiscount_type(normalizeUpper(voucher.getDiscount_type()));
        voucher.setVoucher_scope(normalizeUpper(voucher.getVoucher_scope()));

        String validationMessage = validateVoucher(voucher);
        if (validationMessage != null) {
            return validationMessage;
        }

        if (voucherDao.existsByCode(voucher.getCode())) {
            return "Mã giảm giá đã tồn tại.";
        }

        voucherDao.insert(voucher);
        return null;
    }

    public String updateVoucher(Voucher voucher) {
        if (voucher == null || voucher.getId() <= 0) {
            return "Dữ liệu mã giảm giá không hợp lệ.";
        }

        Voucher existing = voucherDao.findById(voucher.getId());
        if (existing == null) {
            return "Không tìm thấy mã giảm giá cần sửa.";
        }

        voucher.setUsed_quantity(existing.getUsed_quantity());
        // Issue #99 chỉ chỉnh sửa thông tin mã giảm giá.
        // Trạng thái khóa/mở được xử lý ở issue riêng nên khi sửa phải giữ nguyên trạng thái hiện tại.
        voucher.setStatus(existing.getStatus());
        voucher.setCode(normalizeUpper(voucher.getCode()));
        voucher.setDiscount_type(normalizeUpper(voucher.getDiscount_type()));
        voucher.setVoucher_scope(normalizeUpper(voucher.getVoucher_scope()));

        String validationMessage = validateVoucher(voucher);
        if (validationMessage != null) {
            return validationMessage;
        }

        if (voucher.getQuantity() < existing.getUsed_quantity()) {
            return "Số lượt sử dụng không được nhỏ hơn số lượt đã dùng.";
        }

        if (voucherDao.existsByCodeExceptId(voucher.getCode(), voucher.getId())) {
            return "Mã giảm giá đã tồn tại.";
        }

        voucherDao.update(voucher);
        return null;
    }

    private String validateVoucher(Voucher voucher) {
        if (isBlank(voucher.getCode())) {
            return "Vui lòng nhập mã giảm giá.";
        }

        if (isBlank(voucher.getName())) {
            return "Vui lòng nhập tên mã giảm giá.";
        }

        if (!"PERCENT".equals(voucher.getDiscount_type()) && !"FIXED".equals(voucher.getDiscount_type())) {
            return "Loại giảm giá không hợp lệ.";
        }

        if (voucher.getDiscount_value() <= 0) {
            return "Giá trị giảm phải lớn hơn 0.";
        }

        if ("PERCENT".equals(voucher.getDiscount_type()) && voucher.getDiscount_value() > 100) {
            return "Giá trị giảm theo phần trăm không được vượt quá 100%.";
        }

        if (!"ORDER".equals(voucher.getVoucher_scope())
                && !"PRODUCT".equals(voucher.getVoucher_scope())
                && !"SHIPPING".equals(voucher.getVoucher_scope())) {
            return "Phạm vi áp dụng không hợp lệ.";
        }

        if ("PRODUCT".equals(voucher.getVoucher_scope()) && voucher.getProduct_id() == null) {
            return "Mã giảm giá sản phẩm cần nhập ID sản phẩm áp dụng.";
        }

        if (voucher.getMin_order_value() < 0) {
            return "Giá trị đơn tối thiểu không được âm.";
        }

        if (voucher.getMax_discount() != null && voucher.getMax_discount() < 0) {
            return "Giảm tối đa không được âm.";
        }

        if (voucher.getQuantity() <= 0) {
            return "Số lượt sử dụng phải lớn hơn 0.";
        }

        if (voucher.getStart_date() == null || voucher.getEnd_date() == null) {
            return "Vui lòng chọn thời gian bắt đầu và kết thúc.";
        }

        if (!voucher.getEnd_date().isAfter(voucher.getStart_date())) {
            return "Thời gian kết thúc phải sau thời gian bắt đầu.";
        }

        return null;
    }

    private String normalizeUpper(String value) {
        return value == null ? "" : value.trim().toUpperCase();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public ApplyResult applyOrderOrProductVoucher(String code, List<CartItem> cartItems) {
        if (code == null || code.trim().isEmpty()) {
            return ApplyResult.fail("Vui lòng nhập mã giảm giá.");
        }

        Voucher voucher = voucherDao.findActiveOrderOrProductVoucherByCode(code.trim());
        if (voucher == null) {
            return ApplyResult.fail("Mã giảm giá không tồn tại, đã hết hạn hoặc đã hết lượt sử dụng.");
        }

        double subtotal = calculateSubtotal(cartItems);
        if (subtotal < voucher.getMin_order_value()) {
            return ApplyResult.fail("Đơn hàng chưa đạt giá trị tối thiểu để dùng mã này.");
        }

        double applicableAmount = calculateApplicableAmount(voucher, cartItems);
        if (applicableAmount <= 0) {
            return ApplyResult.fail("Mã này không áp dụng cho sản phẩm trong giỏ hàng.");
        }

        double discount = calculateDiscount(voucher, applicableAmount);
        discount = Math.min(discount, subtotal);

        return ApplyResult.success(voucher, Math.round(discount), "Áp dụng mã giảm giá thành công.");
    }

    public ApplyResult applyShippingVoucher(String code, double subtotal, double shippingFee) {
        if (code == null || code.trim().isEmpty()) {
            return ApplyResult.fail("Vui lòng nhập mã giảm giá vận chuyển.");
        }

        Voucher voucher = voucherDao.findActiveShippingVoucherByCode(code.trim());
        if (voucher == null) {
            return ApplyResult.fail("Mã giảm giá vận chuyển không tồn tại, đã hết hạn hoặc đã hết lượt sử dụng.");
        }

        if (subtotal < voucher.getMin_order_value()) {
            return ApplyResult.fail("Đơn hàng chưa đạt giá trị tối thiểu để dùng mã vận chuyển này.");
        }

        if (shippingFee <= 0) {
            return ApplyResult.fail("Đơn hàng hiện chưa có phí vận chuyển để áp dụng mã.");
        }

        double discount = calculateDiscount(voucher, shippingFee);
        discount = Math.min(discount, shippingFee);

        return ApplyResult.success(voucher, Math.round(discount), "Áp dụng mã giảm giá vận chuyển thành công.");
    }

    public double calculateSubtotal(List<CartItem> cartItems) {
        double subtotal = 0;

        if (cartItems == null) {
            return subtotal;
        }

        for (CartItem item : cartItems) {
            subtotal += item.getPrice() * item.getQuantity();
        }

        return subtotal;
    }

    public void markVoucherUsed(int voucherId, int orderId) {
        voucherDao.markVoucherUsed(voucherId, orderId);
    }

    private double calculateApplicableAmount(Voucher voucher, List<CartItem> cartItems) {
        if ("PRODUCT".equalsIgnoreCase(voucher.getVoucher_scope()) && voucher.getProduct_id() != null) {
            double productTotal = 0;

            for (CartItem item : cartItems) {
                if (item.getProduct() != null && item.getProduct().getId() == voucher.getProduct_id()) {
                    productTotal += item.getPrice() * item.getQuantity();
                }
            }

            return productTotal;
        }

        return calculateSubtotal(cartItems);
    }

    private double calculateDiscount(Voucher voucher, double applicableAmount) {
        double discount;

        if ("PERCENT".equalsIgnoreCase(voucher.getDiscount_type())) {
            discount = applicableAmount * voucher.getDiscount_value() / 100;

            if (voucher.getMax_discount() != null && voucher.getMax_discount() > 0) {
                discount = Math.min(discount, voucher.getMax_discount());
            }
        } else {
            discount = voucher.getDiscount_value();
        }

        return Math.min(discount, applicableAmount);
    }

    public static class ApplyResult {
        private final boolean success;
        private final String message;
        private final Voucher voucher;
        private final double discountAmount;

        private ApplyResult(boolean success, String message, Voucher voucher, double discountAmount) {
            this.success = success;
            this.message = message;
            this.voucher = voucher;
            this.discountAmount = discountAmount;
        }

        public static ApplyResult success(Voucher voucher, double discountAmount, String message) {
            return new ApplyResult(true, message, voucher, discountAmount);
        }

        public static ApplyResult fail(String message) {
            return new ApplyResult(false, message, null, 0);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Voucher getVoucher() {
            return voucher;
        }

        public double getDiscountAmount() {
            return discountAmount;
        }
    }
}
