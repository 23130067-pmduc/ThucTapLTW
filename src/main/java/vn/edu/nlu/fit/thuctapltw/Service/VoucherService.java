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

    public ApplyResult applyVoucher(String code, List<CartItem> cartItems, double shippingFee) {
        if (code == null || code.trim().isEmpty()) {
            return ApplyResult.fail("Vui lòng nhập mã giảm giá.");
        }

        Voucher voucher = voucherDao.findActiveVoucherByCode(code.trim());
        if (voucher == null) {
            return ApplyResult.fail("Mã giảm giá không tồn tại, đã hết hạn hoặc đã hết lượt sử dụng.");
        }

        if ("SHIPPING".equalsIgnoreCase(voucher.getVoucher_scope())) {
            return applyShippingVoucher(voucher, cartItems, shippingFee);
        }

        return applyOrderOrProductVoucher(voucher, cartItems);
    }

    public ApplyResult applyOrderOrProductVoucher(String code, List<CartItem> cartItems) {
        if (code == null || code.trim().isEmpty()) {
            return ApplyResult.fail("Vui lòng nhập mã giảm giá.");
        }

        Voucher voucher = voucherDao.findActiveOrderOrProductVoucherByCode(code.trim());
        if (voucher == null) {
            return ApplyResult.fail("Mã giảm giá không tồn tại, đã hết hạn hoặc đã hết lượt sử dụng.");
        }

        return applyOrderOrProductVoucher(voucher, cartItems);
    }

    private ApplyResult applyOrderOrProductVoucher(Voucher voucher, List<CartItem> cartItems) {
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

        return ApplyResult.success(voucher, Math.round(discount), 0, "Áp dụng mã giảm giá thành công.");
    }

    public ApplyResult applyShippingVoucher(String code, List<CartItem> cartItems, double shippingFee) {
        if (code == null || code.trim().isEmpty()) {
            return ApplyResult.fail("Vui lòng nhập mã giảm giá vận chuyển.");
        }

        Voucher voucher = voucherDao.findActiveShippingVoucherByCode(code.trim());
        if (voucher == null) {
            return ApplyResult.fail("Mã vận chuyển không tồn tại, đã hết hạn hoặc đã hết lượt sử dụng.");
        }

        return applyShippingVoucher(voucher, cartItems, shippingFee);
    }

    private ApplyResult applyShippingVoucher(Voucher voucher, List<CartItem> cartItems, double shippingFee) {
        double subtotal = calculateSubtotal(cartItems);
        if (subtotal < voucher.getMin_order_value()) {
            return ApplyResult.fail("Đơn hàng chưa đạt giá trị tối thiểu để dùng mã vận chuyển này.");
        }

        if (shippingFee <= 0) {
            return ApplyResult.fail("Đơn hàng hiện chưa có phí vận chuyển để áp dụng mã này.");
        }

        double shippingDiscount = calculateDiscount(voucher, shippingFee);
        shippingDiscount = Math.min(shippingDiscount, shippingFee);

        return ApplyResult.success(voucher, 0, Math.round(shippingDiscount), "Áp dụng mã giảm giá vận chuyển thành công.");
    }

    public double calculateSubtotal(List<CartItem> cartItems) {
        double subtotal = 0;
        if (cartItems == null) return subtotal;

        for (CartItem item : cartItems) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        return subtotal;
    }

    public void increaseUsedQuantity(int voucherId) {
        voucherDao.increaseUsedQuantity(voucherId);
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
        private final double shippingDiscountAmount;

        private ApplyResult(boolean success, String message, Voucher voucher, double discountAmount, double shippingDiscountAmount) {
            this.success = success;
            this.message = message;
            this.voucher = voucher;
            this.discountAmount = discountAmount;
            this.shippingDiscountAmount = shippingDiscountAmount;
        }

        public static ApplyResult success(Voucher voucher, double discountAmount, double shippingDiscountAmount, String message) {
            return new ApplyResult(true, message, voucher, discountAmount, shippingDiscountAmount);
        }

        public static ApplyResult fail(String message) {
            return new ApplyResult(false, message, null, 0, 0);
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

        public double getShippingDiscountAmount() {
            return shippingDiscountAmount;
        }

        public double getTotalDiscountAmount() {
            return discountAmount + shippingDiscountAmount;
        }
    }
}
