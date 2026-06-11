<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<c:set var="isEdit" value="${mode == 'edit'}" />
<c:set var="isView" value="${mode == 'view'}" />
<c:set var="currentScope" value="${empty scopeType ? 'all' : scopeType}" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${isView ? 'Chi tiết chương trình khuyến mãi' : (isEdit ? 'Sửa chương trình khuyến mãi' : 'Thêm chương trình khuyến mãi')}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/promotion-event-admin.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="promotion-form-body">
<div class="promotion-form-page">
    <header class="promotion-form-header">
        <div>
            <a href="${pageContext.request.contextPath}/promotion-event-admin" class="btn-back">
                <i class="fa-solid fa-arrow-left"></i>
                Quay lại
            </a>
            <h1>${isView ? 'Chi tiết chương trình khuyến mãi' : (isEdit ? 'Sửa chương trình khuyến mãi' : 'Thêm chương trình khuyến mãi')}</h1>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
    </header>

    <main class="promotion-form-content">
        <section class="promotion-form-card">
            <div class="promotion-form-title">
                <div>
                    <h2>${isView ? 'Thông tin chương trình khuyến mãi' : (isEdit ? 'Cập nhật chương trình khuyến mãi' : 'Tạo chương trình mới')}</h2>
                    <p>${isView ? 'Xem nội dung, thời gian hiệu lực, phạm vi áp dụng và trạng thái hiện tại của chương trình.' : (isEdit ? 'Chỉnh sửa nội dung, thời gian hiệu lực, mức ưu đãi và phạm vi áp dụng của chương trình.' : 'Tạo chương trình khuyến mãi và thiết lập phạm vi áp dụng cho toàn cửa hàng, danh mục hoặc sản phẩm cụ thể.')}</p>
                </div>
                <span><i class="fa-solid fa-tags"></i> Khuyến mãi</span>
            </div>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    <span>${errorMessage}</span>
                </div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/promotion-event-admin" class="promotion-form ${isView ? 'promotion-form-readonly' : ''}">
                <input type="hidden" name="action" value="${isEdit ? 'update' : 'create'}">
                <c:if test="${isEdit || isView}">
                    <input type="hidden" name="id" value="${promotionEvent.id}">
                </c:if>

                <div class="promotion-form-grid">
                    <div class="promotion-form-group">
                        <label for="title">Tên chương trình <b>*</b></label>
                        <input type="text" id="title" name="title" maxlength="255" value="${promotionEvent.title}"
                               placeholder="VD: Sale hè cho bé" required ${isView ? 'readonly' : ''}>
                    </div>

                    <div class="promotion-form-group">
                        <label for="tag">Nhãn chương trình <b>*</b></label>
                        <input type="text" id="tag" name="tag" maxlength="100" value="${promotionEvent.tag}"
                               placeholder="VD: Sale hè" required ${isView ? 'readonly' : ''}>
                    </div>

                    <div class="promotion-form-group">
                        <label for="discountLabel">Nội dung ưu đãi <b>*</b></label>
                        <input type="text" id="discountLabel" name="discountLabel" maxlength="255"
                               value="${promotionEvent.discountLabel}" placeholder="VD: Giảm đến 30%" required ${isView ? 'readonly' : ''}>
                    </div>

                    <div class="promotion-form-group">
                        <label for="discountPercent">Phần trăm giảm <b>*</b></label>
                        <input type="number" id="discountPercent" name="discountPercent" min="1" max="99"
                               value="${empty discountPercent ? 10 : discountPercent}" required ${isView ? 'readonly' : ''}>
                    </div>

                    <div class="promotion-form-group">
                        <label for="icon">Biểu tượng</label>
                        <select id="icon" name="icon" ${isView ? 'disabled' : ''}>
                            <option value="fa-gift" ${promotionEvent.icon == 'fa-gift' ? 'selected' : ''}>Quà tặng</option>
                            <option value="fa-tags" ${promotionEvent.icon == 'fa-tags' ? 'selected' : ''}>Nhãn khuyến mãi</option>
                            <option value="fa-percent" ${promotionEvent.icon == 'fa-percent' ? 'selected' : ''}>Phần trăm</option>
                            <option value="fa-bolt" ${promotionEvent.icon == 'fa-bolt' ? 'selected' : ''}>Flash sale</option>
                            <option value="fa-school" ${promotionEvent.icon == 'fa-school' ? 'selected' : ''}>Trường học</option>
                        </select>
                    </div>

                    <div class="promotion-form-group">
                        <label for="status">Trạng thái</label>
                        <select id="status" name="status" ${isView ? 'disabled' : ''}>
                            <option value="1" ${promotionEvent.status != 0 ? 'selected' : ''}>Kích hoạt</option>
                            <option value="0" ${promotionEvent.status == 0 ? 'selected' : ''}>Tạm khóa</option>
                        </select>
                    </div>

                    <div class="promotion-form-group">
                        <label for="startDate">Thời gian bắt đầu <b>*</b></label>
                        <input type="datetime-local" id="startDate" name="startDate"
                               value="${promotionEvent.startDateInputValue}" required ${isView ? 'readonly' : ''}>
                    </div>

                    <div class="promotion-form-group">
                        <label for="endDate">Thời gian kết thúc <b>*</b></label>
                        <input type="datetime-local" id="endDate" name="endDate"
                               value="${promotionEvent.endDateInputValue}" required ${isView ? 'readonly' : ''}>
                    </div>

                    <div class="promotion-form-group promotion-form-group-full">
                        <label for="description">Mô tả</label>
                        <textarea id="description" name="description" rows="5" maxlength="1000"
                                  placeholder="Mô tả ngắn về chương trình và đối tượng áp dụng" ${isView ? 'readonly' : ''}>${promotionEvent.description}</textarea>
                    </div>
                </div>

                <div class="promotion-scope-box">
                    <div class="promotion-scope-head">
                        <div>
                            <h3>Phạm vi áp dụng</h3>
                            <p>Chọn nhóm sản phẩm được áp dụng chương trình. Hệ thống sẽ tự đồng bộ vào danh sách sản phẩm khuyến mãi.</p>
                        </div>
                        <span class="scope-helper"><i class="fa-solid fa-layer-group"></i> Issue #120</span>
                    </div>

                    <div class="promotion-scope-options">
                        <label class="promotion-scope-option ${currentScope == 'all' ? 'active' : ''}">
                            <input type="radio" name="scopeType" value="all" ${currentScope == 'all' ? 'checked' : ''} ${isView ? 'disabled' : ''}>
                            <span>
                                <strong>Toàn bộ cửa hàng</strong>
                                <small>Áp dụng cho tất cả sản phẩm đang bán.</small>
                            </span>
                        </label>

                        <label class="promotion-scope-option ${currentScope == 'category' ? 'active' : ''}">
                            <input type="radio" name="scopeType" value="category" ${currentScope == 'category' ? 'checked' : ''} ${isView ? 'disabled' : ''}>
                            <span>
                                <strong>Theo danh mục</strong>
                                <small>Áp dụng cho sản phẩm thuộc danh mục được chọn.</small>
                            </span>
                        </label>

                        <label class="promotion-scope-option ${currentScope == 'product' ? 'active' : ''}">
                            <input type="radio" name="scopeType" value="product" ${currentScope == 'product' ? 'checked' : ''} ${isView ? 'disabled' : ''}>
                            <span>
                                <strong>Theo sản phẩm cụ thể</strong>
                                <small>Chỉ áp dụng cho các sản phẩm được chọn.</small>
                            </span>
                        </label>
                    </div>

                    <div class="scope-panel" data-scope-panel="category">
                        <div class="scope-panel-title">
                            <i class="fa-solid fa-list"></i>
                            Chọn danh mục áp dụng
                        </div>
                        <div class="scope-checkbox-grid">
                            <c:forEach items="${categories}" var="category">
                                <label class="scope-check-item">
                                    <input type="checkbox" name="categoryIds" value="${category.id}"
                                           ${selectedCategoryIds.contains(category.id) ? 'checked' : ''} ${isView ? 'disabled' : ''}>
                                    <span>${category.name}</span>
                                </label>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="scope-panel" data-scope-panel="product">
                        <div class="scope-panel-title">
                            <i class="fa-solid fa-shirt"></i>
                            Chọn sản phẩm áp dụng
                        </div>
                        <div class="scope-product-list">
                            <c:forEach items="${products}" var="product">
                                <label class="scope-product-item">
                                    <input type="checkbox" name="productIds" value="${product.id}"
                                           ${selectedProductIds.contains(product.id) ? 'checked' : ''} ${isView ? 'disabled' : ''}>
                                    <span class="scope-product-name">#${product.id} - ${product.name}</span>
                                    <small>${product.categoryName}</small>
                                </label>
                            </c:forEach>
                        </div>
                    </div>

                    <c:if test="${isView && not empty selectedProducts}">
                        <div class="scope-selected-products">
                            <h4>Sản phẩm đang áp dụng</h4>
                            <div class="scope-selected-list">
                                <c:forEach items="${selectedProducts}" var="product">
                                    <span>#${product.id} - ${product.name}</span>
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>
                </div>

                <div class="promotion-form-actions">
                    <a href="${pageContext.request.contextPath}/promotion-event-admin" class="btn-secondary">${isView ? 'Quay lại' : 'Hủy'}</a>
                    <c:choose>
                        <c:when test="${isView}">
                            <a href="${pageContext.request.contextPath}/promotion-event-admin?action=edit&id=${promotionEvent.id}" class="btn-primary">
                                <i class="fa-solid fa-pen"></i>
                                Sửa chương trình
                            </a>
                        </c:when>
                        <c:otherwise>
                            <button type="submit" class="btn-primary">
                                <i class="fa-solid ${isEdit ? 'fa-floppy-disk' : 'fa-plus'}"></i>
                                ${isEdit ? 'Lưu thay đổi' : 'Thêm chương trình'}
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </form>
        </section>
    </main>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const scopeInputs = document.querySelectorAll('input[name="scopeType"]');
        const panels = document.querySelectorAll('[data-scope-panel]');
        const options = document.querySelectorAll('.promotion-scope-option');

        function updateScopePanels() {
            const checked = document.querySelector('input[name="scopeType"]:checked');
            const value = checked ? checked.value : 'all';

            panels.forEach(function (panel) {
                panel.style.display = panel.dataset.scopePanel === value ? 'block' : 'none';
            });

            options.forEach(function (option) {
                const input = option.querySelector('input[name="scopeType"]');
                option.classList.toggle('active', input && input.checked);
            });
        }

        scopeInputs.forEach(function (input) {
            input.addEventListener('change', updateScopePanels);
        });

        updateScopePanels();
    });
</script>
</body>
</html>
