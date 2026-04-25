(function () {
    'use strict';

    document.addEventListener("DOMContentLoaded", function () {
        setupLoadMore();
        //setupAddToCart();
    });

    function setupLoadMore() {
        const discountGrid = document.querySelector(".discount-grid");
        const loadMoreBtn = document.getElementById("load-more");

        if (!discountGrid || !loadMoreBtn) return;

        const products = discountGrid.querySelectorAll(".product-card");
        const SHOW_FIRST = 8;
        const SHOW_MORE = 8;

        products.forEach((item, index) => {
            if (index >= SHOW_FIRST) {
                item.classList.add("hidden");
            }
        });

        if (products.length <= SHOW_FIRST) {
            loadMoreBtn.style.display = "none";
            return;
        }

        loadMoreBtn.addEventListener("click", function () {
            const hiddenProducts = discountGrid.querySelectorAll(".product-card.hidden");

            for (let i = 0; i < SHOW_MORE && i < hiddenProducts.length; i++) {
                hiddenProducts[i].classList.remove("hidden");
            }

            if (discountGrid.querySelectorAll(".product-card.hidden").length === 0) {
                loadMoreBtn.style.display = "none";
            }
        });
    }

    function setupAddToCart() {
        const addToCartButtons = document.querySelectorAll(".btn-add");

        addToCartButtons.forEach(button => {
            button.addEventListener("click", function (e) {
                e.preventDefault();

                const productCard = this.closest(".product-card");
                if (!productCard) return;

                const productId = productCard.dataset.productId;
                const salePrice = productCard.dataset.salePrice;

                if (!productId) {
                    showToast("Không tìm thấy thông tin sản phẩm!", "error");
                    return;
                }

                const url = `${CONTEXT_PATH}/add-cart?productId=${encodeURIComponent(productId)}&quantity=1&salePrice=${encodeURIComponent(salePrice || 0)}`;

                fetch(url, {
                    method: "GET",
                    headers: {
                        "X-Requested-With": "XMLHttpRequest"
                    }
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error("HTTP error: " + response.status);
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (data.success) {
                            showToast(data.message || "Đã thêm vào giỏ hàng!", "success");
                            updateCartBadge(data.cartSize);
                        } else {
                            showToast(data.message || "Có lỗi xảy ra!", "error");
                        }
                    })
                    .catch(error => {
                        console.error("Add cart error:", error);
                        showToast("Không thể thêm vào giỏ hàng!", "error");
                    });
            });
        });
    }

    function updateCartBadge(count) {
        const iconCart = document.querySelector(".iconCart");
        if (!iconCart) return;

        let cartCount = iconCart.querySelector(".cart-count");

        if (!cartCount) {
            cartCount = document.createElement("span");
            cartCount.className = "cart-count";
            iconCart.appendChild(cartCount);
        }

        cartCount.textContent = count;

        if (count > 0) {
            cartCount.style.display = "block";
            cartCount.style.animation = "none";

            setTimeout(() => {
                cartCount.style.animation = "cartBounce 0.5s ease";
            }, 10);
        } else {
            cartCount.style.display = "none";
        }
    }

    function showToast(message, type) {
        const toast = document.getElementById("toast");
        if (!toast) return;

        toast.textContent = message;
        toast.className = `show ${type}`;

        setTimeout(() => {
            toast.className = toast.className.replace("show", "").trim();
        }, 3000);
    }
})();