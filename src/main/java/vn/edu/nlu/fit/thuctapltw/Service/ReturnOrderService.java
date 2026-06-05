package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.ReturnOrderDao;
import vn.edu.nlu.fit.thuctapltw.model.ReturnOrder;
import vn.edu.nlu.fit.thuctapltw.model.ReturnOrderItem;

import java.util.List;

public class ReturnOrderService {
    private final ReturnOrderDao dao = new ReturnOrderDao();

    public List<ReturnOrder> search(String keyword, String status) {
        return dao.search(keyword, status);
    }

    public ReturnOrder findById(int id) {
        ReturnOrder returnOrder = dao.findById(id);
        if (returnOrder != null) {
            returnOrder.setItems(dao.getItems(id));
        }
        return returnOrder;
    }

    public List<ReturnOrderItem> getItems(int returnOrderId) {
        return dao.getItems(returnOrderId);
    }

    public ReturnOrder findActiveByOrderId(int orderId) {
        return dao.findActiveByOrderId(orderId);
    }

    public int createReturnRequest(int orderId, int userId, String reason) {
        String cleanReason = reason == null ? "" : reason.trim();
        if (cleanReason.isBlank()) {
            throw new RuntimeException("Vui lòng nhập lý do hoàn hàng");
        }
        if (cleanReason.length() > 1000) {
            cleanReason = cleanReason.substring(0, 1000);
        }
        return dao.createReturnRequest(orderId, userId, cleanReason);
    }

    public String approve(int id, Integer adminId, String adminNote) {
        return dao.approve(id, adminId, normalizeNote(adminNote));
    }

    public String reject(int id, Integer adminId, String adminNote) {
        return dao.reject(id, adminId, normalizeNote(adminNote));
    }

    public String completeReturn(int id, Integer adminId, String adminNote) {
        return dao.completeReturn(id, adminId, normalizeNote(adminNote));
    }

    public String getMessage(String result) {
        if (result == null) return "Không thể xử lý phiếu hoàn hàng";
        return switch (result) {
            case "SUCCESS" -> "Cập nhật phiếu hoàn hàng thành công";
            case "SUCCESS_IMPORTED" -> "Đã nhập lại kho từ phiếu hoàn hàng";
            case "RETURN_NOT_FOUND" -> "Không tìm thấy phiếu hoàn hàng";
            case "INVALID_STATUS" -> "Trạng thái phiếu hoàn hàng không hợp lệ cho thao tác này";
            case "ALREADY_IMPORTED" -> "Phiếu hoàn hàng này đã được nhập kho trước đó";
            case "RETURN_EMPTY" -> "Phiếu hoàn hàng không có sản phẩm";
            default -> "Không thể xử lý phiếu hoàn hàng";
        };
    }

    private String normalizeNote(String note) {
        if (note == null) return null;
        String clean = note.trim();
        if (clean.isBlank()) return null;
        return clean.length() > 1000 ? clean.substring(0, 1000) : clean;
    }
}
