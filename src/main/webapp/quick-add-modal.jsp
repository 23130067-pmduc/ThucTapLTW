<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div id="quickAddModal" class="quick-add-modal">
  <div class="quick-add-box">
    <button type="button" class="quick-add-close" id="closeQuickAdd">&times;</button>

    <div class="quick-add-left">
      <img id="quickImage" src="" alt="Ảnh sản phẩm">
    </div>

    <div class="quick-add-right">
      <h3 id="quickAddName">Tên sản phẩm</h3>
      <p id="quickAddPrice" class="quick-add-price">0đ</p>
      <div id="quickAddRating" class="product-rating"></div>

      <div class="quick-product-colors">
        <p><strong>Màu sắc:</strong></p>
        <div class="quick-color-options" id="quickAddColors"></div>
      </div>

      <div class="quick-product-sizes">
        <p><strong>Chọn size:</strong></p>
        <div class="quick-size-options" id="quickAddSizes"></div>
      </div>

      <div class="quick-product-quantity">
        <p><strong>Số lượng:</strong></p>
        <div class="quick-quantity-control">
          <button type="button" class="quick-btn-decrease">−</button>
          <input type="number" id="quickQuantity" min="1" value="1">
          <button type="button" class="quick-btn-increase">+</button>
        </div>
      </div>

      <button type="button" class="quick-add-confirm">Thêm vào giỏ hàng</button>
    </div>
  </div>
</div>