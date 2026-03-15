document.addEventListener("DOMContentLoaded", () => {

    const mainImage = document.getElementById("main-image");
    const thumbs = document.querySelectorAll(".thumb");
    const colorThumbs = document.querySelectorAll(".color-thumb");
    const sizeButtons = document.querySelectorAll(".size-btn");
    const btnAddCart = document.querySelector(".btn-add-cart");

    const decreaseBtn = document.querySelector(".btn-decrease");
    const increaseBtn = document.querySelector(".btn-increase");
    const quantityInput = document.getElementById("quantity");

    const stars = document.querySelectorAll(".star-select .star");
    const submitBtn = document.getElementById("submit-review");
    const ratingInput = document.getElementById("rating-value");

    let selectedColorId = null;
    let selectedSizeId = null;
    let selectedRating = 0;
    let currentStock = 0;


    thumbs.forEach(t => {
        t.addEventListener("click", () => {
            thumbs.forEach(th => th.classList.remove("active"));
            t.classList.add("active");

            mainImage.classList.add("fade");
            setTimeout(() => {
                mainImage.src = t.src;
                mainImage.classList.remove("fade");
            }, 200);
        });
    });

    sizeButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            if (btn.disabled) return;

            sizeButtons.forEach(b => b.classList.remove("active"));
            btn.classList.add("active");

            selectedSizeId = Number(btn.dataset.sizeId);

            const variant = variants.find(v =>
                v.colorId === selectedColorId &&
                v.sizeId === selectedSizeId
            );

            currentStock = variant ? variant.stock : 0;
            quantityInput.value = 1;
        });
    });


    colorThumbs.forEach(thumb => {
        thumb.addEventListener("click", () => {

            colorThumbs.forEach(t => t.classList.remove("active"));
            thumb.classList.add("active");

            selectedColorId = Number(thumb.dataset.colorId);
            selectedSizeId = null;
            currentStock = 0;
            quantityInput.value = 1;

            sizeButtons.forEach(b => b.classList.remove("active"));

            updateSizeAvailability();
        });
    });

    function updateSizeAvailability() {
        sizeButtons.forEach(btn => {
            const sizeId = Number(btn.dataset.sizeId);

            const variant = variants.find(v =>
                v.colorId === selectedColorId &&
                v.sizeId === sizeId
            );

            if (!variant || variant.stock <= 0) {
                btn.disabled = true;
                btn.classList.add("disabled");
            } else {
                btn.disabled = false;
                btn.classList.remove("disabled");
            }
        });

        currentStock = 0;
        quantityInput.value = 1;
    }


    if (decreaseBtn && increaseBtn && quantityInput) {
        decreaseBtn.addEventListener("click", () => {
            if (!selectedSizeId) {
                showToast("Vui lòng chọn size trước");
                return;
            }
            let val = parseInt(quantityInput.value);
            if (val > 1) quantityInput.value = val - 1;
        });

        increaseBtn.addEventListener("click", () => {
            if (!selectedSizeId) {
                showToast("Vui lòng chọn size trước");
                return;
            }

            let val = parseInt(quantityInput.value);

            if (val < currentStock) {
                quantityInput.value = val + 1;
            } else {
                showToast(`Chỉ còn ${currentStock} sản phẩm`);
            }
        });
    }


    stars.forEach(star => {
        star.addEventListener("click", () => {
            selectedRating = parseInt(star.dataset.value);
            ratingInput.value = selectedRating;

            stars.forEach(s => s.classList.remove("active"));
            for (let i = 0; i < selectedRating; i++) {
                stars[i].classList.add("active");
            }
        });
    });

    if (submitBtn) {
        submitBtn.addEventListener("click", (e) => {
            if (selectedRating === 0) {
                e.preventDefault();
                alert("Vui lòng chọn số sao trước khi gửi đánh giá!");
            }
        });
    }

    function showToast(message) {
        const toast = document.getElementById("toast");
        toast.textContent = message;
        toast.className = "show";

        setTimeout(() => {
            toast.className = toast.className.replace("show", "");
        }, 3000);
    }


    btnAddCart.addEventListener("click", () => {

        if (!selectedColorId || !selectedSizeId) {
            showToast("Vui lòng chọn màu và size trước!");
            return;
        }

        let quantity = parseInt(quantityInput.value);
        if (quantity <= 0 || quantity > currentStock) {
            showToast(`Số lượng không hợp lệ. Chỉ còn ${currentStock} sản phẩm`);
            return;
        }

        const variant = variants.find(v =>
            v.colorId === selectedColorId &&
            v.sizeId === selectedSizeId
        );

        if (!variant) {
            showToast("Biến thể không tồn tại");
            return;
        }

        fetch("add-cart", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "X-Requested-With": "XMLHttpRequest"
            },
            body: new URLSearchParams({
                variantId: variant.id,
                quantity: quantity
            })
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    showToast("Đã thêm vào giỏ hàng!");
                    if (window.updateCartBadge) {
                        updateCartBadge(data.totalQuantity);
                    }
                }
            })
            .catch(() => showToast("Lỗi kết nối"));
    });


});