document.addEventListener("DOMContentLoaded", function () {
    const thongBaoBtn = document.getElementById("thongBao");
    const notificationBox = document.getElementById("notification-box");
    const notificationList = document.getElementById("notification-list");
    const notificationBadge = document.getElementById("notification-badge");
    const markAllBtn = document.getElementById("mark-all-notifications");

    if (!thongBaoBtn || !notificationBox || !notificationList || !notificationBadge) {
        return;
    }

    loadUnreadCount();

    thongBaoBtn.addEventListener("click", function (e) {
        e.stopPropagation();

        const isOpen = notificationBox.style.display === "block";
        notificationBox.style.display = isOpen ? "none" : "block";

        if (!isOpen) {
            loadNotifications();
        }
    });

    notificationBox.addEventListener("click", function (e) {
        e.stopPropagation();
    });

    if (markAllBtn) {
        markAllBtn.addEventListener("click", function (e) {
            e.preventDefault();
            e.stopPropagation();
            markAllNotificationsAsRead();
        });
    }

    document.addEventListener("click", function (e) {
        if (!notificationBox.contains(e.target) && !thongBaoBtn.contains(e.target)) {
            notificationBox.style.display = "none";
        }
    });

    function loadUnreadCount() {
        return fetch(window.ctxPath + "/notifications/unread-count")
            .then(response => response.json())
            .then(data => {
                const count = data.count || 0;
                updateUnreadBadge(count);
                updateMarkAllButton(count);
                return count;
            })
            .catch(() => {
                updateUnreadBadge(0);
                updateMarkAllButton(0);
                return 0;
            });
    }

    function updateUnreadBadge(count) {
        if (count > 0) {
            notificationBadge.textContent = count;
            notificationBadge.style.display = "inline-block";
        } else {
            notificationBadge.style.display = "none";
        }
    }

    function updateMarkAllButton(count) {
        if (!markAllBtn) return;

        if (count > 0) {
            markAllBtn.disabled = false;
            markAllBtn.textContent = "Đánh dấu tất cả đã đọc";
        } else {
            markAllBtn.disabled = true;
            markAllBtn.textContent = "Đã đọc tất cả";
        }
    }

    function loadNotifications() {
        notificationList.innerHTML = "<li class='notification-empty'>Đang tải thông báo...</li>";

        fetch(window.ctxPath + "/notifications")
            .then(response => response.json())
            .then(data => {
                renderNotifications(data);
                loadUnreadCount();
            })
            .catch(() => {
                notificationList.innerHTML = "<li class='notification-empty'>Không thể tải thông báo.</li>";
            });
    }

    function markAllNotificationsAsRead() {
        if (!markAllBtn || markAllBtn.disabled) return;

        markAllBtn.disabled = true;
        markAllBtn.textContent = "Đang xử lý...";

        fetch(window.ctxPath + "/notifications/read-all", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    notificationList.querySelectorAll(".notification-item.unread").forEach(item => {
                        item.classList.remove("unread");
                        item.classList.add("read");
                    });
                    updateUnreadBadge(0);
                    updateMarkAllButton(0);
                } else {
                    loadUnreadCount();
                }
            })
            .catch(() => {
                loadUnreadCount();
            });
    }

    function renderNotifications(notifications) {
        if (!Array.isArray(notifications) || notifications.length === 0) {
            notificationList.innerHTML = "<li class='notification-empty'>Bạn chưa có thông báo nào.</li>";
            return;
        }

        let html = "";

        notifications.forEach(notification => {
            const unreadClass = Number(notification.is_read) === 0 ? "unread" : "read";
            const title = escapeHtml(notification.title || "");
            const content = escapeHtml(notification.content || "");
            const link = notification.link || "#";
            const time = formatDate(notification.created_at);

            html += `
                <li class="notification-item ${unreadClass}" data-id="${notification.id}" data-link="${escapeHtml(link)}">
                    <div class="notification-item-title">${title}</div>
                    <div class="notification-item-content">${content}</div>
                    <div class="notification-item-time">${time}</div>
                </li>
            `;
        });

        notificationList.innerHTML = html;

        document.querySelectorAll(".notification-item").forEach(item => {
            item.addEventListener("click", function () {
                const id = this.getAttribute("data-id");
                const link = this.getAttribute("data-link");

                fetch(window.ctxPath + "/notifications/read", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    body: "notificationId=" + encodeURIComponent(id)
                })
                    .then(response => response.json())
                    .then(() => {
                        this.classList.remove("unread");
                        this.classList.add("read");
                        loadUnreadCount();
                        goToNotificationLink(link);
                    })
                    .catch(() => {
                        goToNotificationLink(link);
                    });
            });
        });
    }

    function goToNotificationLink(link) {
        if (!link || link === "#") return;

        if (link.startsWith("http")) {
            window.location.href = link;
        } else {
            window.location.href = window.ctxPath + link;
        }
    }

    function formatDate(dateString) {
        if (!dateString) return "";
        const date = new Date(dateString);
        if (isNaN(date.getTime())) return "";
        return date.toLocaleString("vi-VN");
    }

    function escapeHtml(str) {
        return String(str)
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;")
            .replaceAll("'", "&#39;");
    }
});
