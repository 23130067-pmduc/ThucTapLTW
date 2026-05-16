(function () {
    'use strict';

    document.addEventListener("DOMContentLoaded", function () {
        setupLoadMore();
        setupCopyCoupon();
    });

    function setupLoadMore() {
        const discountGrid = document.querySelector(".discount-grid");
        const loadMoreBtn = document.getElementById("load-more");

        if (!discountGrid || !loadMoreBtn) return;

        const products = discountGrid.querySelectorAll(".product-card");
        const showFirst = 8;
        const showMore = 8;

        products.forEach((item, index) => {
            if (index >= showFirst) {
                item.classList.add("hidden");
            }
        });

        if (products.length <= showFirst) {
            loadMoreBtn.style.display = "none";
            return;
        }

        loadMoreBtn.addEventListener("click", function () {
            const hiddenProducts = discountGrid.querySelectorAll(".product-card.hidden");

            for (let i = 0; i < showMore && i < hiddenProducts.length; i++) {
                hiddenProducts[i].classList.remove("hidden");
            }

            if (discountGrid.querySelectorAll(".product-card.hidden").length === 0) {
                loadMoreBtn.style.display = "none";
            }
        });
    }

    function setupCopyCoupon() {
        const copyButtons = document.querySelectorAll(".btn-copy-coupon");

        copyButtons.forEach(button => {
            button.addEventListener("click", function () {
                const code = this.dataset.code;

                if (!code) {
                    showToast("Không tìm thấy mã giảm giá!", "error");
                    return;
                }

                copyText(code)
                    .then(() => {
                        this.textContent = "Đã sao chép";
                        showToast("Đã sao chép mã " + code, "success");

                        setTimeout(() => {
                            this.textContent = "Sao chép mã";
                        }, 1800);
                    })
                    .catch(() => {
                        showToast("Không thể sao chép mã!", "error");
                    });
            });
        });
    }

    function copyText(text) {
        if (navigator.clipboard && window.isSecureContext) {
            return navigator.clipboard.writeText(text);
        }

        return new Promise((resolve, reject) => {
            const input = document.createElement("textarea");
            input.value = text;
            input.style.position = "fixed";
            input.style.opacity = "0";
            document.body.appendChild(input);
            input.focus();
            input.select();

            try {
                const success = document.execCommand("copy");
                document.body.removeChild(input);
                success ? resolve() : reject();
            } catch (error) {
                document.body.removeChild(input);
                reject(error);
            }
        });
    }

    function showToast(message, type) {
        const toast = document.getElementById("toast");
        if (!toast) return;

        toast.textContent = message;
        toast.className = "show " + type;

        setTimeout(() => {
            toast.className = toast.className.replace("show", "").trim();
        }, 3000);
    }
})();
