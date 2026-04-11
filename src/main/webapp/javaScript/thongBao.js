document.addEventListener("DOMContentLoaded", function () {
    const thongBaoBtn = document.getElementById("thongBao");
    const notificationBox = document.getElementById("notification-box");
    const notificationList = document.getElementById("notification-list");
    const notificationBadge = document.getElementById("notification-badge");

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

    document.addEventListener("click", function (e) {
        if (!notificationBox.contains(e.target) && !thongBaoBtn.contains(e.target)) {
            notificationBox.style.display = "none";
        }
    });

    function loadUnreadCount() {
        fetch(window.ctxPath + "/notifications/unread-count")
            .then(response => response.json())
            .then(data => {
                const count = data.count || 0;
                if (count > 0) {
                    notificationBadge.textContent = count;
                    notificationBadge.style.display = "inline-block";
                } else {
                    notificationBadge.style.display = "none";
                }
            })
            .catch(error => {
                console.error("Lỗi unread-count:", error);
                notificationBadge.style.display = "none";
            });
    }

    function loadNotifications() {
        notificationList.innerHTML = "<li class='notification-empty'>Đang tải thông báo...</li>";

        fetch(window.ctxPath + "/notifications")
            .then(response => response.json())
            .then(data => {
                renderNotifications(data);
            })
            .catch(error => {
                console.error("Lỗi notifications:", error);
                notificationList.innerHTML = "<li class='notification-empty'>Không thể tải thông báo.</li>";
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
                <li class="notification-item ${unreadClass}" data-id="${notification.id}" data-link="${link}">
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
                    .then(data => {
                        loadUnreadCount();
                        if (link && link !== "#") {
                            if (link.startsWith("http")) {
                                window.location.href = link;
                            } else {
                                window.location.href = window.ctxPath + link;
                            }
                        }
                    })
                    .catch(error => {
                        console.error("Lỗi mark read:", error);
                    });
            });
        });
    }

    function formatDate(dateString) {
        if (!dateString) return "";
        const date = new Date(dateString);
        if (isNaN(date.getTime())) return "";
        return date.toLocaleString("vi-VN");
    }

    function escapeHtml(str) {
        return str
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;")
            .replaceAll("'", "&#39;");
    }
});