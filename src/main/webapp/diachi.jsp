<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    request.setAttribute("pageCss", "diachi.css");
    request.setAttribute("pageTitle", "Địa chỉ của tôi");
%>

<%@ include file="header.jsp" %>

<section class="address-container">
    <aside class="address-sidebar">
        <nav class="profile-menu">
            <ul>
                <li>
                    <a href="${pageContext.request.contextPath}/profile">
                        <i class="fas fa-user"></i> Thông tin cá nhân
                    </a>
                </li>
                <li class="active">
                    <a href="${pageContext.request.contextPath}/dia-chi">
                        <i class="fas fa-map-marker-alt"></i> Địa chỉ của tôi
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/don-mua">
                        <i class="fas fa-clipboard-list"></i> Đơn hàng của tôi
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/doi-mat-khau">
                        <i class="fas fa-lock"></i> Đổi mật khẩu
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/logout">
                        <i class="fa fa-sign-out"></i> Đăng xuất
                    </a>
                </li>
            </ul>
        </nav>
    </aside>

    <div class="address-panel">
        <div class="address-page-header">
            <h2> Địa chỉ của tôi</h2>
            <button class="btn-add-address" id="btnOpenModal" type="button">
                <i class="fas fa-plus"></i> Thêm địa chỉ mới
            </button>
        </div>

        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <div class="address-list">
            <c:if test="${empty addressList}">
                <div class="empty-state">Bạn chưa có địa chỉ nào.</div>
            </c:if>

            <c:forEach var="a" items="${addressList}">
                <article class="address-item ${a.defaultAddress ? 'default' : ''}">
                    <c:if test="${a.defaultAddress}">
                        <span class="address-badge">Mặc định</span>
                    </c:if>

                    <div class="address-item-content">
                        <h3>${a.receiverName}</h3>
                        <p><i class="fas fa-phone"></i> ${a.phone}</p>
                        <p><i class="fas fa-location-dot"></i> ${a.detailAddress}, ${a.ward}, ${a.district}, ${a.city}</p>
                    </div>

                    <div class="address-actions">
                        <c:if test="${!a.defaultAddress}">
                            <form method="post" action="${pageContext.request.contextPath}/dia-chi" class="inline-form">
                                <input type="hidden" name="action" value="setDefault">
                                <input type="hidden" name="id" value="${a.id}">
                                <button type="submit" class="btn-set-default">
                                    <i class="fas fa-check-circle"></i> Đặt làm mặc định
                                </button>
                            </form>
                        </c:if>

                        <button
                                type="button"
                                class="btn-edit js-edit-address"
                                data-id="${a.id}"
                                data-receiver-name="${a.receiverName}"
                                data-phone="${a.phone}"
                                data-city="${a.city}"
                                data-district="${a.district}"
                                data-ward="${a.ward}"
                                data-detail-address="${a.detailAddress}"
                                data-default-address="${a.defaultAddress}">
                            <i class="fas fa-pen"></i> Sửa
                        </button>

                        <form action="${pageContext.request.contextPath}/dia-chi" method="post" class="inline-form delete-address-form">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${a.id}">
                            <button type="button" class="btn-delete btn-open-delete-modal">
                                <i class="fas fa-trash"></i> Xóa
                            </button>
                        </form>
                    </div>
                </article>
            </c:forEach>
        </div>
    </div>
</section>

<div class="modal-overlay" id="addressModal">
    <div class="modal-content">
        <div class="modal-header">
            <h3 id="modalTitle">Thêm địa chỉ mới</h3>
            <button class="modal-close" id="btnCloseModal" type="button">&times;</button>
        </div>

        <form class="address-form" method="post" action="${pageContext.request.contextPath}/dia-chi" id="addressForm">
            <input type="hidden" name="action" id="formAction" value="add">
            <input type="hidden" name="id" id="addressId">

            <div class="form-group">
                <label for="receiverName">Họ và tên người nhận <span class="required">*</span></label>
                <input type="text" id="receiverName" name="receiverName" required>
            </div>

            <div class="form-group">
                <label for="phoneInput">Số điện thoại <span class="required">*</span></label>
                <input type="tel" id="phoneInput" name="phone" required>
                <small id="phoneError" class="error-message"></small>
            </div>

            <div class="form-group">
                <label for="citySelect">Tỉnh / Thành phố <span class="required">*</span></label>
                <select name="city" id="citySelect" required>
                    <option value="">-- Chọn Tỉnh / Thành phố --</option>
                    <option value="Hồ Chí Minh">Hồ Chí Minh</option>
                    <option value="Hà Nội">Hà Nội</option>
                    <option value="Bình Dương">Bình Dương</option>
                </select>
            </div>

            <div class="form-group">
                <label for="districtSelect">Quận / Huyện <span class="required">*</span></label>
                <select name="district" id="districtSelect" required disabled>
                    <option value="">-- Chọn Quận / Huyện --</option>
                </select>
            </div>

            <div class="form-group">
                <label for="wardSelect">Phường / Xã <span class="required">*</span></label>
                <select name="ward" id="wardSelect" required disabled>
                    <option value="">-- Chọn Phường / Xã --</option>
                </select>
            </div>

            <div class="form-group">
                <label for="detailAddress">Địa chỉ chi tiết <span class="required">*</span></label>
                <textarea id="detailAddress" name="detailAddress" rows="3" required></textarea>
            </div>

            <div class="form-group">
                <label class="checkbox-label">
                    <input type="checkbox" name="isDefault" id="isDefault">
                    Đặt làm địa chỉ mặc định
                </label>
            </div>

            <div class="form-actions">
                <button type="button" class="btn-cancel" id="btnCancelModal">Hủy</button>
                <button type="submit" class="btn-save" id="submitButton">Lưu địa chỉ</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/javaScript/address.js"></script>

<div id="deleteConfirmModal" class="custom-modal-overlay">
    <div class="custom-modal">
        <div class="custom-modal-header">
            <h3>Xác nhận xoá</h3>
        </div>

        <div class="custom-modal-body">
            <p>Bạn có chắc muốn xoá địa chỉ này?</p>
        </div>

        <div class="custom-modal-footer">
            <button type="button" id="cancelDeleteBtn" class="modal-btn modal-btn-cancel">Huỷ</button>
            <button type="button" id="confirmDeleteBtn" class="modal-btn modal-btn-confirm">Xác nhận</button>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>
