<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Thống kê</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user.css?v=20260612-urgent-tasks">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">


</head>
<body>
<div class="user">
  <aside class="sidebar">
    <img src="img/gau.png" alt="" Logo>
    <p>ADMIN</p>

    <div class="nav" id="menu">

      <c:if test="${userlogin.permissions.contains('DASHBOARD_VIEW')}">
        <a href="${pageContext.request.contextPath}/dashboard" class="nav-item active">
          <i class="fa-solid fa-gauge"></i><span>Thống kê</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('PRODUCT_VIEW')}">
        <a href="${pageContext.request.contextPath}/product-admin" class="nav-item">
          <i class="fa-solid fa-shirt"></i><span>Sản phẩm</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('WAREHOUSE_VIEW')}">
        <a href="${pageContext.request.contextPath}/inventory-admin" class="nav-item">
          <i class="fa-solid fa-boxes-stacked"></i><span>Kho hàng</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('RETURN_RECEIPT_VIEW')}">
        <a href="${pageContext.request.contextPath}/return-order-admin" class="nav-item">
          <i class="fa-solid fa-rotate-left"></i><span>Hoàn hàng</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('REPORT_VIEW')}">
        <a href="${pageContext.request.contextPath}/profit-report" class="nav-item">
          <i class="fa-solid fa-chart-line"></i><span>Lợi nhuận</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('CATEGORY_VIEW')}">
        <a href="${pageContext.request.contextPath}/category-admin" class="nav-item">
          <i class="fa-solid fa-list"></i><span>Danh mục</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('ORDER_VIEW')}">
        <a href="${pageContext.request.contextPath}/order-admin" class="nav-item">
          <i class="fa-solid fa-receipt"></i><span>Đơn hàng</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('USER_VIEW')}">
        <a href="${pageContext.request.contextPath}/user-admin" class="nav-item">
          <i class="fa-solid fa-users"></i><span>Người dùng</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('BANNER_VIEW')}">
        <a href="${pageContext.request.contextPath}/banner-admin" class="nav-item">
          <i class="fa-solid fa-image"></i><span>Banner</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('VOUCHER_VIEW')}">
        <a href="${pageContext.request.contextPath}/voucher-admin" class="nav-item">
          <i class="fa-solid fa-ticket"></i><span>Mã giảm giá</span>
        </a>
      </c:if>

      <a href="${pageContext.request.contextPath}/promotion-event-admin" class="nav-item">
        <i class="fa-solid fa-tags"></i><span>Khuyến mãi</span>
      </a>

      <c:if test="${userlogin.permissions.contains('NEWS_VIEW')}">
        <a href="${pageContext.request.contextPath}/news-admin" class="nav-item">
          <i class="fa-solid fa-newspaper"></i><span>Tin tức</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('NOTIFICATION_VIEW')}">
        <a href="${pageContext.request.contextPath}/notification-admin" class="nav-item">
          <i class="fa-solid fa-bell"></i><span>Thông báo</span>
        </a>
      </c:if>

      <c:if test="${userlogin.permissions.contains('CONTACT_VIEW')}">
        <a href="${pageContext.request.contextPath}/contact-admin" class="nav-item">
          <i class="fa-solid fa-envelope"></i><span>Liên hệ</span>
        </a>
      </c:if>

      <a href="${pageContext.request.contextPath}/admin-profile" class="nav-item">
        <i class="fa-solid fa-user-gear"></i><span>Hồ sơ</span>
      </a>

    </div>
  </aside>

  <section class="content">
    <header class="topbar">
      <h1 id="pageTitle">Thống kê</h1>
      <div class="actions">
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
      </div>
    </header>



    <main id="page">
      <section id="dashboard" class="page active">
        <div class="cards">
          <div class="card">
            Tổng đơn hàng
            <span>${totalOrders}</span>
          </div>

            <div class="card">Doanh thu<span><fmt:formatNumber value="${totalRevenue}"/>đ</span></div>

          <div class="card">
            Sản phẩm
            <span>${totalProducts}</span>
          </div>

          <div class="card">
            Người dùng
            <span>${totalUsers}</span>
          </div>
        </div>

        <div class="cards status-cards">
          <div class="card card-pending">Chờ xử lý
            <span>${pendingOrders}</span>
          </div>
          <div class="card card-shipping">Đang giao
            <span>${shippingOrders}</span>
          </div>
          <div class="card card-completed">Hoàn thành
            <span>${completedOrders}</span>
          </div>
          <div class="card card-cancelled">Đã hủy
            <span>${cancelledOrders}</span>
          </div>
        </div>

        <div class="urgent-tasks-card">
          <div class="urgent-tasks-header">
            <h2 class="urgent-tasks-title">
              <i class="fa-solid fa-triangle-exclamation"></i> Cần xử lý ngay
            </h2>
            <c:set var="totalUrgent" value="${pendingOrders + pendingReturnOrders + lowStockProducts + newContacts}" />
              <c:choose>
                <c:when test="${totalUrgent > 0}">
                  <span class="urgent-total-badge">${totalUrgent} việc cần làm</span>
                </c:when>
                <c:otherwise>
                  <span class="urgent-all-done"><i class="fa-solid fa-circle-check"></i> Đã xử lý hết</span>
                </c:otherwise>
              </c:choose>
          </div>

          <div class="urgent-tasks-grid">

            <c:choose>
              <c:when test="${pendingOrders > 0}">
                <a href="${pageContext.request.contextPath}/order-admin" class="urgent-task-item has-alert">
                  <div class="urgent-task-icon warning"><i class="fa-solid fa-receipt"></i></div>
                  <div class="urgent-task-info">
                    <span class="urgent-task-name">Đơn hàng chờ xử lý</span>
                    <span class="urgent-task-count count-alert">${pendingOrders}</span>
                  </div>
                    <span class="urgent-arrow"><i class="fa-solid fa-arrow-right"></i></span>
                </a>
              </c:when>
              <c:otherwise>
                <a href="${pageContext.request.contextPath}/order-admin" class="urgent-task-item no-alert">
                  <div class="urgent-task-icon warning"><i class="fa-solid fa-receipt"></i></div>
                  <div class="urgent-task-info">
                    <span class="urgent-task-name">Đơn hàng chờ xử lý</span>
                    <span class="urgent-task-count count-ok">${pendingOrders}</span>
                  </div>
                </a>
              </c:otherwise>
            </c:choose>

            <c:choose>
              <c:when test="${pendingReturnOrders > 0}">
                <a href="${pageContext.request.contextPath}/return-order-admin" class="urgent-task-item has-alert">
                  <div class="urgent-task-icon danger"><i class="fa-solid fa-rotate-left"></i></div>
                    <div class="urgent-task-info">
                      <span class="urgent-task-name">Hoàn hàng chờ duyệt</span>
                      <span class="urgent-task-count count-alert">${pendingReturnOrders}</span>
                    </div>
                      <span class="urgent-arrow"><i class="fa-solid fa-arrow-right"></i></span>
                </a>
              </c:when>
            <c:otherwise>
              <a href="${pageContext.request.contextPath}/return-order-admin" class="urgent-task-item no-alert">
                <div class="urgent-task-icon danger"><i class="fa-solid fa-rotate-left"></i></div>
                <div class="urgent-task-info">
                  <span class="urgent-task-name">Hoàn hàng chờ duyệt</span>
                  <span class="urgent-task-count count-ok">${pendingReturnOrders}</span>
                </div>
              </a>
            </c:otherwise>
            </c:choose>

            <c:choose>
              <c:when test="${lowStockProducts > 0}">
                <a href="${pageContext.request.contextPath}/inventory-admin?stockStatus=LOW" class="urgent-task-item has-alert">
                  <div class="urgent-task-icon orange"><i class="fa-solid fa-boxes-stacked"></i></div>
                  <div class="urgent-task-info">
                    <span class="urgent-task-name">Biến thể sắp hết hàng</span>
                    <span class="urgent-task-count count-alert">${lowStockProducts}</span>
                  </div>
                    <span class="urgent-arrow"><i class="fa-solid fa-arrow-right"></i></span>
                </a>
              </c:when>
              <c:otherwise>
                <a href="${pageContext.request.contextPath}/inventory-admin?stockStatus=LOW" class="urgent-task-item no-alert">
                  <div class="urgent-task-icon orange"><i class="fa-solid fa-boxes-stacked"></i></div>
                  <div class="urgent-task-info">
                    <span class="urgent-task-name">Biến thể sắp hết hàng</span>
                    <span class="urgent-task-count count-ok">${lowStockProducts}</span>
                  </div>
                </a>
              </c:otherwise>
            </c:choose>

            <c:choose>
              <c:when test="${newContacts > 0}">
                <a href="${pageContext.request.contextPath}/contact-admin" class="urgent-task-item has-alert">
                  <div class="urgent-task-icon info"><i class="fa-solid fa-envelope"></i></div>
                  <div class="urgent-task-info">
                    <span class="urgent-task-name">Liên hệ chưa phản hồi</span>
                    <span class="urgent-task-count count-alert">${newContacts}</span>
                  </div>
                    <span class="urgent-arrow"><i class="fa-solid fa-arrow-right"></i></span>
                </a>
              </c:when>
              <c:otherwise>
                <a href="${pageContext.request.contextPath}/contact-admin" class="urgent-task-item no-alert">
                  <div class="urgent-task-icon info"><i class="fa-solid fa-envelope"></i></div>
                  <div class="urgent-task-info">
                    <span class="urgent-task-name">Liên hệ chưa phản hồi</span>
                    <span class="urgent-task-count count-ok">${newContacts}</span>
                  </div>
                </a>
              </c:otherwise>
            </c:choose>

          </div>
        </div>

        <h2 style="margin-bottom: 12px;">Đơn hàng mới nhất</h2>

        <div class="dashboard-grid">
        <div class="user-table-wrapper">
          <table class="order-table">
            <tr>
              <th>Mã</th>
              <th>Người nhận</th>
              <th>Tổng tiền</th>
              <th>Trạng thái</th>
            </tr>

            <c:forEach items="${latestOrders}" var="o">
              <tr>
                <td>#${o.id}</td>
                <td>${o.receiverName}</td>
                <td>
                  <fmt:formatNumber value="${o.totalPrice}"/>đ
                </td>
                <td>
                    <span class="order-status ${o.orderStatus}">
                      <c:choose>
                          <c:when test="${o.orderStatus == 'PENDING'}">Chờ xử lý</c:when>
                          <c:when test="${o.orderStatus == 'SHIPPING'}">Đang giao</c:when>
                          <c:when test="${o.orderStatus == 'COMPLETED'}">Hoàn thành</c:when>
                          <c:when test="${o.orderStatus == 'CANCELLED'}">Đã hủy</c:when>
                          <c:otherwise>${o.orderStatus}</c:otherwise>
                      </c:choose>
                    </span>
                </td>
              </tr>
            </c:forEach>
          </table>
        </div>

          <div class="top-selling-card">
            <h2 class="top-selling-title">
              <i class="fa-solid fa-fire" style="color:#ff6b35;"></i>Top 5 sản phẩm bán chạy</h2>
              <div class="top-selling-list">
                <c:forEach items="${topSellingProducts}" var="p" varStatus="loop">
                  <div class="top-selling-item">
                    <span class="rank-badge rank-${loop.index + 1}">${loop.index + 1}</span>
                      <c:choose>
                        <c:when test="${not empty p.thumbnail}"><img src="${p.thumbnail}" alt="${p.name}" class="top-selling-img" onerror="this.src='${pageContext.request.contextPath}/img/default-product.png'">
                        </c:when>
                          <c:otherwise><div class="top-selling-img-placeholder"><i class="fa-solid fa-image"></i></div></c:otherwise>
                      </c:choose>
                      <div class="top-selling-info">
                        <span class="top-selling-name">${p.name}</span>
                        <span class="top-selling-price">
                      <c:choose>
                        <c:when test="${p.salePrice > 0}">
                          <fmt:formatNumber value="${p.salePrice}"/>đ
                        </c:when>
                        <c:otherwise>
                          <fmt:formatNumber value="${p.price}"/>đ
                        </c:otherwise>
                      </c:choose>
                    </span>
                      </div>
                      <div class="top-selling-stat">
                        <span class="sold-count">${p.totalSold}</span>
                          <span class="sold-label">đã bán</span>
                          <c:if test="${loop.index == 0}">
                            <span class="hot-badge"><i class="fa-solid fa-fire-flame-curved"></i> Hot</span>
                          </c:if>
                          <c:if test="${loop.index > 0 && p.totalSold > 0}">
                            <span class="selling-badge">Bán chạy</span>
                          </c:if>
                      </div>
                  </div>
                </c:forEach>
                  <c:if test="${empty topSellingProducts}">
                    <div class="empty-state">
                      <i class="fa-solid fa-box-open"></i>
                        <p>Chưa có dữ liệu bán hàng</p>
                    </div>
                  </c:if>
              </div>
          </div>
        </div>

      </section>

    </main>
  </section>
</div>
</body>
<script>
  function openConfirmModal(userId) {
    document.getElementById("confirmUserId").value = userId;
    document.getElementById("confirmModal").style.display = "flex";
  }

  function closeModal() {
    document.getElementById("confirmModal").style.display = "none";
  }
</script>

</html>
