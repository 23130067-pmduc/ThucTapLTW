<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Quản lý phân quyền</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/permission-admin.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
</head>
<body>

<div class="user">
  <aside class="sidebar">
    <img src="img/gau.png" alt="Logo">
    <p>ADMIN</p>

    <div class="nav" id="menu">

      <c:if test="${userlogin.permissions.contains('DASHBOARD_VIEW')}">
        <a href="${pageContext.request.contextPath}/dashboard" class="nav-item">
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

      <c:if test="${userlogin.permissions.contains('PERMISSION_VIEW')}">
        <a href="${pageContext.request.contextPath}/permission-admin" class="nav-item active">
          <i class="fa-solid fa-user-shield"></i><span>Phân quyền</span>
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

      <c:if test="${userlogin.permissions.contains('PROMOTION_EVENT_VIEW')}">
        <a href="${pageContext.request.contextPath}/promotion-event-admin" class="nav-item">
          <i class="fa-solid fa-tags"></i><span>Khuyến mãi</span>
        </a>
      </c:if>

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
      <div>
        <h1 id="pageTitle">Quản lý phân quyền</h1>
      </div>

      <div class="actions">
        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
      </div>
    </header>

    <main id="page">
      <section id="permissionPage" class="page active">

        <div class="permission-layout">

          <div class="role-card">
            <div class="card-header">
              <div>
                <h2>Vai trò</h2>
                <p>Chọn vai trò cần phân quyền</p>
              </div>
              <span class="count-badge">5 vai trò</span>
            </div>

            <div class="role-list">
              <button type="button" class="role-item active" data-role="ADMIN">
                <div>
                  <strong>ADMIN</strong>
                  <p>Toàn quyền hệ thống</p>
                </div>
                <i class="fa-solid fa-chevron-right"></i>
              </button>

              <button type="button" class="role-item" data-role="STAFF_PRODUCT">
                <div>
                  <strong>STAFF_PRODUCT</strong>
                  <p>Quản lý sản phẩm và kho hàng</p>
                </div>
                <i class="fa-solid fa-chevron-right"></i>
              </button>

              <button type="button" class="role-item" data-role="STAFF_ORDER">
                <div>
                  <strong>STAFF_ORDER</strong>
                  <p>Quản lý đơn hàng và hoàn hàng</p>
                </div>
                <i class="fa-solid fa-chevron-right"></i>
              </button>

              <button type="button" class="role-item" data-role="STAFF_MARKETING">
                <div>
                  <strong>STAFF_MARKETING</strong>
                  <p>Quản lý banner, tin tức, khuyến mãi</p>
                </div>
                <i class="fa-solid fa-chevron-right"></i>
              </button>

              <button type="button" class="role-item" data-role="CUSTOMER">
                <div>
                  <strong>CUSTOMER</strong>
                  <p>Khách hàng mua sắm</p>
                </div>
                <i class="fa-solid fa-chevron-right"></i>
              </button>
            </div>
          </div>

          <div class="permission-card-wrapper">
            <form action="permission-admin" method="post" id="permissionForm">

              <input type="hidden" name="roleName" id="selectedRoleInput" value="ADMIN">

              <div class="card-header permission-header">
                <div>
                  <h2>Danh sách quyền</h2>
                  <p>Vai trò đang chọn: <strong id="selectedRoleName">ADMIN</strong></p>
                </div>

                <c:if test="${userlogin.permissions.contains('PERMISSION_UPDATE')}">
                  <button type="submit" class="btn-save">
                    <i class="fa-solid fa-floppy-disk"></i>
                    Cập nhật quyền
                  </button>
                </c:if>
              </div>

              <div class="permission-group">
                <div class="group-title">
                  <i class="fa-solid fa-gauge"></i>
                  <span>Thống kê & báo cáo</span>
                </div>

                <div class="permission-grid">
                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="DASHBOARD_VIEW" checked>
                    <div>
                      <strong>DASHBOARD_VIEW</strong>
                      <p>Xem trang thống kê tổng quan</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="REPORT_VIEW" checked>
                    <div>
                      <strong>REPORT_VIEW</strong>
                      <p>Xem báo cáo lợi nhuận</p>
                    </div>
                  </label>
                </div>
              </div>

              <div class="permission-group">
                <div class="group-title">
                  <i class="fa-solid fa-shirt"></i>
                  <span>Sản phẩm</span>
                </div>

                <div class="permission-grid">
                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="PRODUCT_VIEW" checked>
                    <div>
                      <strong>PRODUCT_VIEW</strong>
                      <p>Xem danh sách sản phẩm</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="PRODUCT_CREATE" checked>
                    <div>
                      <strong>PRODUCT_CREATE</strong>
                      <p>Thêm sản phẩm mới</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="PRODUCT_UPDATE" checked>
                    <div>
                      <strong>PRODUCT_UPDATE</strong>
                      <p>Chỉnh sửa thông tin sản phẩm</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="PRODUCT_DELETE" checked>
                    <div>
                      <strong>PRODUCT_DELETE</strong>
                      <p>Xóa hoặc ngừng bán sản phẩm</p>
                    </div>
                  </label>
                </div>
              </div>

              <div class="permission-group">
                <div class="group-title">
                  <i class="fa-solid fa-boxes-stacked"></i>
                  <span>Kho hàng</span>
                </div>

                <div class="permission-grid">
                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="WAREHOUSE_VIEW" checked>
                    <div>
                      <strong>WAREHOUSE_VIEW</strong>
                      <p>Xem thông tin kho hàng</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="WAREHOUSE_UPDATE" checked>
                    <div>
                      <strong>WAREHOUSE_UPDATE</strong>
                      <p>Cập nhật số lượng tồn kho</p>
                    </div>
                  </label>
                </div>
              </div>

              <div class="permission-group">
                <div class="group-title">
                  <i class="fa-solid fa-list"></i>
                  <span>Danh mục</span>
                </div>

                <div class="permission-grid">
                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="CATEGORY_VIEW" checked>
                    <div>
                      <strong>CATEGORY_VIEW</strong>
                      <p>Xem danh mục sản phẩm</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="CATEGORY_CREATE" checked>
                    <div>
                      <strong>CATEGORY_CREATE</strong>
                      <p>Thêm danh mục mới</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="CATEGORY_UPDATE" checked>
                    <div>
                      <strong>CATEGORY_UPDATE</strong>
                      <p>Chỉnh sửa danh mục</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="CATEGORY_DELETE" checked>
                    <div>
                      <strong>CATEGORY_DELETE</strong>
                      <p>Xóa hoặc khóa danh mục</p>
                    </div>
                  </label>
                </div>
              </div>

              <div class="permission-group">
                <div class="group-title">
                  <i class="fa-solid fa-receipt"></i>
                  <span>Đơn hàng</span>
                </div>

                <div class="permission-grid">
                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="ORDER_VIEW" checked>
                    <div>
                      <strong>ORDER_VIEW</strong>
                      <p>Xem danh sách đơn hàng</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="ORDER_UPDATE" checked>
                    <div>
                      <strong>ORDER_UPDATE</strong>
                      <p>Cập nhật trạng thái đơn hàng</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="RETURN_RECEIPT_VIEW" checked>
                    <div>
                      <strong>RETURN_RECEIPT_VIEW</strong>
                      <p>Xem yêu cầu hoàn hàng</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="RETURN_RECEIPT_UPDATE" checked>
                    <div>
                      <strong>RETURN_RECEIPT_UPDATE</strong>
                      <p>Xử lý yêu cầu hoàn hàng</p>
                    </div>
                  </label>
                </div>
              </div>

              <div class="permission-group">
                <div class="group-title">
                  <i class="fa-solid fa-users"></i>
                  <span>Người dùng</span>
                </div>

                <div class="permission-grid">
                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="USER_VIEW" checked>
                    <div>
                      <strong>USER_VIEW</strong>
                      <p>Xem danh sách người dùng</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="USER_CREATE" checked>
                    <div>
                      <strong>USER_CREATE</strong>
                      <p>Thêm người dùng mới</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="USER_UPDATE" checked>
                    <div>
                      <strong>USER_UPDATE</strong>
                      <p>Chỉnh sửa thông tin người dùng</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="USER_LOCK" checked>
                    <div>
                      <strong>USER_LOCK</strong>
                      <p>Khóa tài khoản người dùng</p>
                    </div>
                  </label>
                </div>
              </div>

              <div class="permission-group">
                <div class="group-title">
                  <i class="fa-solid fa-ticket"></i>
                  <span>Mã giảm giá & khuyến mãi</span>
                </div>

                <div class="permission-grid">
                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="VOUCHER_VIEW" checked>
                    <div>
                      <strong>VOUCHER_VIEW</strong>
                      <p>Xem danh sách mã giảm giá</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="VOUCHER_CREATE" checked>
                    <div>
                      <strong>VOUCHER_CREATE</strong>
                      <p>Thêm mã giảm giá</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="VOUCHER_UPDATE" checked>
                    <div>
                      <strong>VOUCHER_UPDATE</strong>
                      <p>Chỉnh sửa mã giảm giá</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="VOUCHER_DELETE" checked>
                    <div>
                      <strong>VOUCHER_DELETE</strong>
                      <p>Xóa hoặc khóa mã giảm giá</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="PROMOTION_EVENT_VIEW" checked>
                    <div>
                      <strong>PROMOTION_EVENT_VIEW</strong>
                      <p>Xem chương trình khuyến mãi</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="PROMOTION_EVENT_CREATE" checked>
                    <div>
                      <strong>PROMOTION_EVENT_CREATE</strong>
                      <p>Thêm chương trình khuyến mãi</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="PROMOTION_EVENT_UPDATE" checked>
                    <div>
                      <strong>PROMOTION_EVENT_UPDATE</strong>
                      <p>Chỉnh sửa chương trình khuyến mãi</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="PROMOTION_EVENT_DELETE" checked>
                    <div>
                      <strong>PROMOTION_EVENT_DELETE</strong>
                      <p>Xóa hoặc khóa chương trình khuyến mãi</p>
                    </div>
                  </label>
                </div>
              </div>

              <div class="permission-group">
                <div class="group-title">
                  <i class="fa-solid fa-image"></i>
                  <span>Nội dung hệ thống</span>
                </div>

                <div class="permission-grid">
                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="BANNER_VIEW" checked>
                    <div>
                      <strong>BANNER_VIEW</strong>
                      <p>Xem danh sách banner</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="BANNER_CREATE" checked>
                    <div>
                      <strong>BANNER_CREATE</strong>
                      <p>Thêm banner</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="BANNER_UPDATE" checked>
                    <div>
                      <strong>BANNER_UPDATE</strong>
                      <p>Chỉnh sửa banner</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="BANNER_DELETE" checked>
                    <div>
                      <strong>BANNER_DELETE</strong>
                      <p>Xóa hoặc ẩn banner</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="NEWS_VIEW" checked>
                    <div>
                      <strong>NEWS_VIEW</strong>
                      <p>Xem danh sách tin tức</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="NEWS_CREATE" checked>
                    <div>
                      <strong>NEWS_CREATE</strong>
                      <p>Thêm tin tức</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="NEWS_UPDATE" checked>
                    <div>
                      <strong>NEWS_UPDATE</strong>
                      <p>Chỉnh sửa tin tức</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="NEWS_DELETE" checked>
                    <div>
                      <strong>NEWS_DELETE</strong>
                      <p>Xóa hoặc ẩn tin tức</p>
                    </div>
                  </label>
                </div>
              </div>

              <div class="permission-group">
                <div class="group-title">
                  <i class="fa-solid fa-bell"></i>
                  <span>Thông báo & liên hệ</span>
                </div>

                <div class="permission-grid">
                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="NOTIFICATION_VIEW" checked>
                    <div>
                      <strong>NOTIFICATION_VIEW</strong>
                      <p>Xem thông báo hệ thống</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="NOTIFICATION_CREATE" checked>
                    <div>
                      <strong>NOTIFICATION_CREATE</strong>
                      <p>Tạo thông báo mới</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="CONTACT_VIEW" checked>
                    <div>
                      <strong>CONTACT_VIEW</strong>
                      <p>Xem liên hệ từ khách hàng</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="CONTACT_UPDATE" checked>
                    <div>
                      <strong>CONTACT_UPDATE</strong>
                      <p>Cập nhật trạng thái liên hệ</p>
                    </div>
                  </label>
                </div>
              </div>

              <div class="permission-group">
                <div class="group-title">
                  <i class="fa-solid fa-user-shield"></i>
                  <span>Phân quyền</span>
                </div>

                <div class="permission-grid">
                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="PERMISSION_VIEW" checked>
                    <div>
                      <strong>PERMISSION_VIEW</strong>
                      <p>Xem trang quản lý quyền</p>
                    </div>
                  </label>

                  <label class="permission-item">
                    <input type="checkbox" name="permissions" value="PERMISSION_UPDATE" checked>
                    <div>
                      <strong>PERMISSION_UPDATE</strong>
                      <p>Cập nhật quyền cho vai trò</p>
                    </div>
                  </label>
                </div>
              </div>

            </form>
          </div>

        </div>

      </section>
    </main>

  </section>
</div>

<script>
  const roleItems = document.querySelectorAll(".role-item");
  const selectedRoleName = document.getElementById("selectedRoleName");
  const selectedRoleInput = document.getElementById("selectedRoleInput");

  roleItems.forEach(function (item) {
    item.addEventListener("click", function () {
      roleItems.forEach(function (role) {
        role.classList.remove("active");
      });

      item.classList.add("active");

      const roleName = item.dataset.role;
      selectedRoleName.textContent = roleName;
      selectedRoleInput.value = roleName;
    });
  });
</script>

</body>
</html>