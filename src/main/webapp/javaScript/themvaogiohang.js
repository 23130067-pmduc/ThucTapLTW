function showToast(message) {
    const toast = document.getElementById("toast");
    if (!toast) {
        alert(message);
        return;
    }

    toast.textContent = message;
    toast.className = "toast show";

    setTimeout(() => {
        toast.className = "toast";
    }, 3000);
}

document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("quickAddModal");
    const closeBtn = document.getElementById("closeQuickAdd");

    const quickAddName = document.getElementById("quickAddName");
    const quickAddPrice = document.getElementById("quickAddPrice");
    const quickImage = document.getElementById("quickImage");
    const quickAddColors = document.getElementById("quickAddColors");
    const quickAddSizes = document.getElementById("quickAddSizes");

    const quickDecrease = document.querySelector(".quick-btn-decrease");
    const quickIncrease = document.querySelector(".quick-btn-increase");
    const quickQuantity = document.getElementById("quickQuantity");

    let currentVariants = [];
    let currentStock = 0;

    const quickAddButtons = document.querySelectorAll(".btn-add");
    const ctx = window.ctxPath || "";

    let selectedColorId = null;
    let selectedSizeId = null;


    quickAddButtons.forEach(button => {
        button.addEventListener("click", function (e) {
            e.preventDefault();

            const productId = this.getAttribute("data-product-id");

            if (!productId) {
                showToast("Nút này chưa có mã sản phẩm");
                return;
            }

            fetch(`${ctx}/quick-add-info?id=${productId}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Không lấy được dữ liệu sản phẩm");
                    }
                    return response.json();
                })
                .then(data => {
                    console.log("Dữ liệu nhận được:", data);

                    selectedColorId = null;
                    selectedSizeId = null;
                    currentVariants = data.variants || [];
                    currentStock = 0;

                    if (quickQuantity) {
                        quickQuantity.value = 1;
                    }

                    if (quickAddName) {
                        quickAddName.textContent = data.name || "Không có tên sản phẩm";
                    }

                    if (quickAddPrice) {
                        quickAddPrice.textContent = data.price != null
                            ? Number(data.price).toLocaleString("vi-VN") + "đ"
                            : "Không có giá";
                    }

                    if (quickImage) {
                        quickImage.src = data.image || "";
                        quickImage.alt = data.name || "Ảnh sản phẩm";
                    }

                    if (quickAddColors) {
                        quickAddColors.innerHTML = "";

                        if (data.colors && data.colors.length > 0) {
                            data.colors.forEach((color, index) => {
                                const colorBtn = document.createElement("button");
                                colorBtn.type = "button";
                                colorBtn.className = "quick-color-thumb";
                                colorBtn.dataset.colorId = color.id;
                                colorBtn.title = color.name || "";
                                colorBtn.style.backgroundColor = color.hex || "#ccc";

                                if ((color.hex || "").toUpperCase() === "#FFFFFF") {
                                    colorBtn.classList.add("white-color");
                                }



                                colorBtn.addEventListener("click", function () {
                                    const allColorBtns = quickAddColors.querySelectorAll(".quick-color-thumb");
                                    allColorBtns.forEach(btn => btn.classList.remove("active"));

                                    this.classList.add("active");
                                    selectedColorId = Number(this.dataset.colorId);

                                    selectedSizeId = null;
                                    currentStock = 0;

                                    const allSizeBtns = quickAddSizes.querySelectorAll(".quick-size-btn");
                                    allSizeBtns.forEach(btn => btn.classList.remove("active"));

                                    if (quickQuantity) {
                                        quickQuantity.value = 1;
                                    }

                                    updateQuickSizeAvailability();
                                });


                                quickAddColors.appendChild(colorBtn);
                            });
                        } else {
                            quickAddColors.innerHTML = "<span>Không có màu</span>";
                        }
                    }

                    if (quickAddSizes) {
                        quickAddSizes.innerHTML = "";

                        if (data.sizes && data.sizes.length > 0) {
                            data.sizes.forEach((size, index) => {
                                const sizeBtn = document.createElement("button");
                                sizeBtn.type = "button";
                                sizeBtn.className = "quick-size-btn";
                                sizeBtn.textContent = size.code;
                                sizeBtn.dataset.sizeId = size.id;



                                sizeBtn.addEventListener("click", function () {
                                    if (!selectedColorId) {
                                        showToast("Vui lòng chọn màu trước");
                                        return;
                                    }

                                    if (this.disabled) return;

                                    const allSizeBtns = quickAddSizes.querySelectorAll(".quick-size-btn");
                                    allSizeBtns.forEach(btn => btn.classList.remove("active"));

                                    this.classList.add("active");
                                    selectedSizeId = Number(this.dataset.sizeId);

                                    const variant = currentVariants.find(v =>
                                        Number(v.colorId) === selectedColorId &&
                                        Number(v.sizeId) === selectedSizeId
                                    );

                                    currentStock = variant ? Number(variant.stock) : 0;

                                    if (variant && quickAddPrice) {
                                        const price = Number(variant.price);
                                        const salePrice = Number(variant.salePrice ?? variant.sale_price);

                                        const finalPrice =
                                            salePrice > 0 && salePrice < price
                                                ? salePrice
                                                : price;

                                        quickAddPrice.textContent = finalPrice.toLocaleString("vi-VN") + "đ";
                                    }

                                    if (quickQuantity) {
                                        quickQuantity.value = 1;
                                    }

                                });

                                quickAddSizes.appendChild(sizeBtn);
                            });
                        } else {
                            quickAddSizes.innerHTML = "<span>Không có size</span>";
                        }
                    }






                    modal.style.display = "flex";
                })
                .catch(error => {
                    console.error("Lỗi:", error);

                    selectedColorId = null;
                    selectedSizeId = null;

                    if (quickAddName) {
                        quickAddName.textContent = "Lỗi tải sản phẩm";
                    }

                    if (quickAddPrice) {
                        quickAddPrice.textContent = "";
                    }

                    if (quickImage) {
                        quickImage.src = "";
                        quickImage.alt = "Ảnh sản phẩm";
                    }

                    if (quickAddColors) {
                        quickAddColors.innerHTML = "";
                    }

                    if (quickAddSizes) {
                        quickAddSizes.innerHTML = "";
                    }

                    modal.style.display = "flex";
                    showToast("Không thể tải thông tin sản phẩm");
                });
        });
    });

    if (quickDecrease && quickIncrease && quickQuantity) {
        quickDecrease.addEventListener("click", function () {
            if (!selectedColorId) {
                showToast("Vui lòng chọn màu trước");
                return;
            }

            if (!selectedSizeId) {
                showToast("Vui lòng chọn size trước");
                return;
            }

            let value = parseInt(quickQuantity.value) || 1;

            if (value > 1) {
                quickQuantity.value = value - 1;
            }
        });

        quickIncrease.addEventListener("click", function () {
            if (!selectedColorId) {
                showToast("Vui lòng chọn màu trước");
                return;
            }

            if (!selectedSizeId) {
                showToast("Vui lòng chọn size trước");
                return;
            }

            let value = parseInt(quickQuantity.value) || 1;

            if (value < currentStock) {
                quickQuantity.value = value + 1;
            } else {
                showToast(`Chỉ còn ${currentStock} sản phẩm`);
            }
        });
    }

    function updateQuickSizeAvailability() {
        const sizeButtons = quickAddSizes.querySelectorAll(".quick-size-btn");

        sizeButtons.forEach(btn => {
            const sizeId = Number(btn.dataset.sizeId);

            const variant = currentVariants.find(v =>
                Number(v.colorId) === selectedColorId &&
                Number(v.sizeId) === sizeId
            );

            if (!variant || Number(variant.stock) <= 0) {
                btn.disabled = true;
                btn.classList.add("disabled");
            } else {
                btn.disabled = false;
                btn.classList.remove("disabled");
            }
        });
    }

    if (closeBtn) {
        closeBtn.addEventListener("click", function () {
            modal.style.display = "none";
        });
    }

    window.addEventListener("click", function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    });
});