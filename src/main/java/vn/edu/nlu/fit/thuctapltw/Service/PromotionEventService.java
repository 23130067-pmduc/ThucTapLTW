package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.PromotionEventDao;
import vn.edu.nlu.fit.thuctapltw.model.Product;
import vn.edu.nlu.fit.thuctapltw.model.PromotionEvent;

import java.util.List;

public class PromotionEventService {
    private final PromotionEventDao promotionEventDao;

    public PromotionEventService() {
        this.promotionEventDao = new PromotionEventDao();
    }

    public List<PromotionEvent> getActiveEventsWithProducts() {
        List<PromotionEvent> events = promotionEventDao.findActiveEvents();

        for (PromotionEvent event : events) {
            event.setProducts(promotionEventDao.findProductsByEventId(event.getId(), 4));
        }

        return events;
    }

    public List<PromotionEvent> getAdminEvents(String keyword, int page, int pageSize) {
        int safePage = Math.max(page, 1);
        return promotionEventDao.findAdminEvents(keyword, pageSize, (safePage - 1) * pageSize);
    }

    public int countAdminEvents(String keyword) {
        return promotionEventDao.countAdminEvents(keyword);
    }

    public int createEvent(PromotionEvent event) {
        prepareAndValidate(event);
        return promotionEventDao.create(event);
    }

    public int createEvent(PromotionEvent event, String scopeType, List<Integer> categoryIds,
                           List<Integer> productIds, int discountPercent) {
        prepareAndValidate(event);
        validateDiscountPercent(discountPercent);
        List<Integer> resolvedProductIds = resolveProductIds(scopeType, categoryIds, productIds);

        event.setScopeType(scopeType);
        event.setDiscountPercent(discountPercent);

        int eventId = promotionEventDao.create(event);
        promotionEventDao.syncEventProducts(eventId, resolvedProductIds, discountPercent);
        return eventId;
    }

    public PromotionEvent getById(int id) {
        return id <= 0 ? null : promotionEventDao.findById(id);
    }

    public List<Product> getProductsByEventId(int eventId) {
        return eventId <= 0 ? List.of() : promotionEventDao.findProductsByEventId(eventId);
    }

    public List<Integer> getProductIdsByEventId(int eventId) {
        return eventId <= 0 ? List.of() : promotionEventDao.findProductIdsByEventId(eventId);
    }

    public List<Integer> getCategoryIdsByEventId(int eventId) {
        return eventId <= 0 ? List.of() : promotionEventDao.findCategoryIdsByEventId(eventId);
    }

    public void updateEvent(PromotionEvent event) {
        if (event == null || event.getId() <= 0) {
            throw new IllegalArgumentException("Dữ liệu chương trình khuyến mãi không hợp lệ.");
        }
        if (promotionEventDao.findById(event.getId()) == null) {
            throw new IllegalArgumentException("Không tìm thấy chương trình khuyến mãi cần sửa.");
        }

        prepareAndValidate(event);
        if (!promotionEventDao.update(event)) {
            throw new IllegalArgumentException("Không thể cập nhật chương trình khuyến mãi. Vui lòng thử lại.");
        }
    }

    public void updateEvent(PromotionEvent event, String scopeType, List<Integer> categoryIds,
                            List<Integer> productIds, int discountPercent) {
        if (event == null || event.getId() <= 0) {
            throw new IllegalArgumentException("Dữ liệu chương trình khuyến mãi không hợp lệ.");
        }
        if (promotionEventDao.findById(event.getId()) == null) {
            throw new IllegalArgumentException("Không tìm thấy chương trình khuyến mãi cần sửa.");
        }

        prepareAndValidate(event);
        validateDiscountPercent(discountPercent);
        List<Integer> resolvedProductIds = resolveProductIds(scopeType, categoryIds, productIds);
        event.setScopeType(scopeType);
        event.setDiscountPercent(discountPercent);

        if (!promotionEventDao.update(event)) {
            throw new IllegalArgumentException("Không thể cập nhật chương trình khuyến mãi. Vui lòng thử lại.");
        }
        promotionEventDao.syncEventProducts(event.getId(), resolvedProductIds, discountPercent);
    }

    public void updateStatus(int id, int status) {
        if (id <= 0 || (status != 0 && status != 1)) {
            throw new IllegalArgumentException("Dữ liệu chương trình khuyến mãi không hợp lệ.");
        }
        if (promotionEventDao.findById(id) == null) {
            throw new IllegalArgumentException("Không tìm thấy chương trình khuyến mãi.");
        }
        if (!promotionEventDao.updateStatus(id, status)) {
            throw new IllegalArgumentException("Không thể thay đổi trạng thái chương trình khuyến mãi.");
        }
    }

    public int countAllEvents() {
        return promotionEventDao.countAllEvents();
    }

    public int countActiveEvents() {
        return promotionEventDao.countActiveEvents();
    }

    public int countUpcomingEvents() {
        return promotionEventDao.countUpcomingEvents();
    }

    public int countEndedEvents() {
        return promotionEventDao.countEndedEvents();
    }

    private List<Integer> resolveProductIds(String scopeType, List<Integer> categoryIds, List<Integer> productIds) {
        if ("all".equals(scopeType)) {
            List<Integer> ids = promotionEventDao.findActiveProductIds();
            if (ids.isEmpty()) {
                throw new IllegalArgumentException("Không có sản phẩm đang bán để áp dụng chương trình.");
            }
            return ids;
        }

        if ("category".equals(scopeType)) {
            if (categoryIds == null || categoryIds.isEmpty()) {
                throw new IllegalArgumentException("Vui lòng chọn ít nhất một danh mục áp dụng.");
            }
            List<Integer> ids = promotionEventDao.findActiveProductIdsByCategories(categoryIds);
            if (ids.isEmpty()) {
                throw new IllegalArgumentException("Danh mục đã chọn chưa có sản phẩm đang bán.");
            }
            return ids;
        }

        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn ít nhất một sản phẩm áp dụng.");
        }
        return productIds;
    }

    private void validateDiscountPercent(int discountPercent) {
        if (discountPercent <= 0 || discountPercent >= 100) {
            throw new IllegalArgumentException("Phần trăm giảm phải nằm trong khoảng từ 1 đến 99.");
        }
    }

    private void prepareAndValidate(PromotionEvent event) {
        validate(event);
        event.setTitle(event.getTitle().trim());
        event.setDescription(trimToNull(event.getDescription()));
        event.setIcon(trimToDefault(event.getIcon(), "fa-gift"));
        event.setTag(event.getTag().trim());
        event.setDiscountLabel(event.getDiscountLabel().trim());
        event.setStatus(event.getStatus() == 0 ? 0 : 1);
    }

    private void validate(PromotionEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Thông tin chương trình không hợp lệ.");
        }
        if (isBlank(event.getTitle())) {
            throw new IllegalArgumentException("Vui lòng nhập tên chương trình.");
        }
        if (isBlank(event.getTag())) {
            throw new IllegalArgumentException("Vui lòng nhập nhãn chương trình.");
        }
        if (isBlank(event.getDiscountLabel())) {
            throw new IllegalArgumentException("Vui lòng nhập nội dung ưu đãi.");
        }
        if (event.getStartDate() == null || event.getEndDate() == null) {
            throw new IllegalArgumentException("Vui lòng chọn đầy đủ thời gian bắt đầu và kết thúc.");
        }
        if (!event.getEndDate().isAfter(event.getStartDate())) {
            throw new IllegalArgumentException("Thời gian kết thúc phải sau thời gian bắt đầu.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String trimToNull(String value) {
        return isBlank(value) ? null : value.trim();
    }

    private String trimToDefault(String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value.trim();
    }
}
