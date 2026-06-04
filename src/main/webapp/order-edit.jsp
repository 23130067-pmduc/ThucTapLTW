<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Cập nhật trạng thái đơn hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body class="order-form-page">

<div class="order-form-wrapper">
    <div class="order-form-top">
        <a href="${pageContext.request.contextPath}/order-admin" class="btn-back-green">← Quay lại</a>
        <h1>Cập nhật trạng thái đơn hàng</h1>
    </div>

    <c:if test="${not empty success}">
        <div class="order-alert success">${success}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="order-alert error">${error}</div>
    </c:if>

    <div class="order-form-card">
        <h3>• Thông tin đơn hàng</h3>

        <div class="order-form-grid">
            <div class="form-group">
                <label>ID đơn hàng</label>
                <input type="text" value="${order.id}" readonly>
            </div>

            <div class="form-group">
                <label>Người nhận</label>
                <input type="text" value="${order.receiverName}" readonly>
            </div>

            <div class="form-group">
                <label>Số điện thoại</label>
                <input type="text" value="${order.phone}" readonly>
            </div>

            <div class="form-group">
                <label>Ngày tạo</label>
                <input type="text" value="${fn:substring(order.createdAtFormatted, 0, 10)}" readonly>
            </div>

            <div class="form-group form-group-full">
                <label>Địa chỉ giao</label>
                <textarea readonly>${order.shippingAddress}</textarea>
            </div>

            <div class="form-group">
                <label>Trạng thái hiện tại</label>
                <input type="text" value="<c:choose><c:when test='${order.orderStatus eq "PENDING"}'>Chờ xử lý</c:when><c:when test='${order.orderStatus eq "SHIPPING"}'>Đang giao</c:when><c:when test='${order.orderStatus eq "COMPLETED"}'>Hoàn thành</c:when><c:when test='${order.orderStatus eq "CANCELLED"}'>Đã hủy</c:when><c:otherwise>${order.orderStatus}</c:otherwise></c:choose>" readonly>
            </div>

            <div class="form-group">
                <label>Tổng tiền</label>
                <input type="text" value="<fmt:formatNumber value='${order.finalAmount}' pattern='#,##0' /> đ" readonly>
            </div>
        </div>
    </div>

    <div class="order-form-card">
        <h3>• Bàn giao đơn hàng</h3>

        <c:choose>
            <c:when test="${order.orderStatus eq 'PENDING'}">
                <div class="shipping-handover-box">
                    <div class="shipping-handover-icon">
                        <i class="fa-solid fa-box-open"></i>
                    </div>
                    <div class="shipping-handover-content">
                        <h4>Bàn giao vận chuyển</h4>
                        <p>Admin chỉ được chuyển đơn từ <strong>Chờ xử lý</strong> sang <strong>Đang giao</strong>.</p>
                        <ul>
                            <li><i class="fa-solid fa-check"></i> Tạo phiếu xuất kho theo đơn hàng</li>
                            <li><i class="fa-solid fa-check"></i> Trừ tồn kho theo FIFO</li>
                            <li><i class="fa-solid fa-check"></i> Ghi nhận giá vốn cho phiếu xuất</li>
                        </ul>
                    </div>
                </div>

                <form method="post" action="${pageContext.request.contextPath}/order-admin" class="order-status-form" id="shippingHandoverForm">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" value="${order.id}">
                    <input type="hidden" name="orderStatus" value="SHIPPING">

                    <button type="button" class="btn-start-shipping" id="openShippingModalBtn">
                        <i class="fa-solid fa-truck-fast"></i>
                        <span>Bàn giao đơn hàng</span>
                    </button>
                </form>
            </c:when>
            <c:when test="${order.orderStatus eq 'SHIPPING'}">
                <div class="order-alert success">
                    Đơn hàng đang giao. Khách hàng sẽ bấm Đã nhận hàng để chuyển đơn sang Hoàn thành.
                </div>
            </c:when>
            <c:otherwise>
                <div class="order-alert error">
                    Trạng thái này không thể cập nhật tại trang admin.
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${not empty exportTransaction}">
        <div class="order-form-card order-inventory-export-card">
            <h3>• Phiếu xuất kho theo đơn hàng</h3>
            <div class="order-form-grid">
                <div class="form-group">
                    <label>Mã phiếu xuất</label>
                    <input type="text" value="${exportTransaction.code}" readonly>
                </div>
                <div class="form-group">
                    <label>Trạng thái phiếu xuất</label>
                    <input type="text" value="${exportTransaction.statusText}" readonly>
                </div>
                <div class="form-group">
                    <label>Tổng số lượng xuất</label>
                    <input type="text" value="${exportTransaction.totalQuantity}" readonly>
                </div>
                <div class="form-group">
                    <label>Ngày xuất</label>
                    <input type="text" value="${exportTransaction.createdAtText}" readonly>
                </div>
                <div class="form-group form-group-full">
                    <label>Ghi chú</label>
                    <textarea readonly>${exportTransaction.note}</textarea>
                </div>
            </div>
            <div class="order-export-actions">
                <a href="${pageContext.request.contextPath}/inventory-history-detail?id=${exportTransaction.id}" class="btn-search">
                    <i class="fa-solid fa-box-open"></i> Xem chi tiết phiếu xuất
                </a>
            </div>
        </div>
    </c:if>

    <div class="order-form-card">
        <h3>• Sản phẩm trong đơn</h3>

        <table class="order-detail-table">
            <thead>
            <tr>
                <th>Ảnh</th>
                <th>Sản phẩm</th>
                <th>Size</th>
                <th>Màu</th>
                <th>SL</th>
                <th>Giá</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${items}" var="i">
                <tr>
                    <td>
                        <c:set var="thumbUrl" value="${i.thumbnail}" />
                        <c:choose>
                            <c:when test="${empty thumbUrl}">
                                <img src="${pageContext.request.contextPath}/img/gau.png" class="order-detail-thumb" onerror="this.src='${pageContext.request.contextPath}/img/gau.png'">
                            </c:when>
                            <c:when test="${fn:startsWith(thumbUrl, 'http://') || fn:startsWith(thumbUrl, 'https://')}">
                                <img src="${thumbUrl}" class="order-detail-thumb" onerror="this.src='${pageContext.request.contextPath}/img/gau.png'">
                            </c:when>
                            <c:when test="${fn:startsWith(thumbUrl, '/')}">
                                <img src="${pageContext.request.contextPath}${thumbUrl}" class="order-detail-thumb" onerror="this.src='${pageContext.request.contextPath}/img/gau.png'">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/${thumbUrl}" class="order-detail-thumb" onerror="this.src='${pageContext.request.contextPath}/img/gau.png'">
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${i.productName}</td>
                    <td>${i.size}</td>
                    <td>${i.color}</td>
                    <td>${i.quantity}</td>
                    <td><fmt:formatNumber value="${i.price}" pattern="#,##0" /> đ</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>


<c:if test="${order.orderStatus eq 'PENDING'}">
    <div class="order-modal-overlay" id="shippingConfirmModal">
        <div class="order-modal-card">
            <button type="button" class="order-modal-close" id="closeShippingModalBtn" aria-label="Đóng">
                <i class="fa-solid fa-xmark"></i>
            </button>

            <div class="order-modal-icon">
                <i class="fa-solid fa-truck-fast"></i>
            </div>

            <h3>Xác nhận bàn giao đơn hàng</h3>
            <p class="order-modal-desc">
                Đơn hàng <strong>#${order.id}</strong> sẽ được chuyển sang trạng thái
                <strong>Đang giao</strong>. Hệ thống sẽ tự động tạo phiếu xuất kho, trừ tồn theo FIFO
                và ghi nhận giá vốn.
            </p>

            <div class="order-modal-checklist">
                <div><i class="fa-solid fa-check"></i> Tạo phiếu xuất kho theo đơn hàng</div>
                <div><i class="fa-solid fa-check"></i> Trừ tồn kho theo lô nhập cũ nhất trước</div>
                <div><i class="fa-solid fa-check"></i> Lưu giá vốn phục vụ thống kê lợi nhuận</div>
            </div>

            <div class="order-modal-actions">
                <button type="button" class="btn-modal-cancel" id="cancelShippingModalBtn">Hủy</button>
                <button type="button" class="btn-modal-confirm" id="confirmShippingBtn">
                    <i class="fa-solid fa-check"></i> Xác nhận bàn giao
                </button>
            </div>
        </div>
    </div>

    <script>
        const shippingModal = document.getElementById('shippingConfirmModal');
        const openShippingModalBtn = document.getElementById('openShippingModalBtn');
        const closeShippingModalBtn = document.getElementById('closeShippingModalBtn');
        const cancelShippingModalBtn = document.getElementById('cancelShippingModalBtn');
        const confirmShippingBtn = document.getElementById('confirmShippingBtn');
        const shippingHandoverForm = document.getElementById('shippingHandoverForm');

        function openShippingModal() {
            if (shippingModal) {
                shippingModal.classList.add('show');
            }
        }

        function closeShippingModal() {
            if (shippingModal) {
                shippingModal.classList.remove('show');
            }
        }

        if (openShippingModalBtn) {
            openShippingModalBtn.addEventListener('click', openShippingModal);
        }

        if (closeShippingModalBtn) {
            closeShippingModalBtn.addEventListener('click', closeShippingModal);
        }

        if (cancelShippingModalBtn) {
            cancelShippingModalBtn.addEventListener('click', closeShippingModal);
        }

        if (shippingModal) {
            shippingModal.addEventListener('click', function (event) {
                if (event.target === shippingModal) {
                    closeShippingModal();
                }
            });
        }

        if (confirmShippingBtn && shippingHandoverForm) {
            confirmShippingBtn.addEventListener('click', function () {
                confirmShippingBtn.disabled = true;
                confirmShippingBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Đang xử lý...';
                shippingHandoverForm.submit();
            });
        }
    </script>
</c:if>

</body>
</html>
