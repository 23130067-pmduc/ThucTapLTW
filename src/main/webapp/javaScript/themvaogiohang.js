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

                                if (index === 0) {
                                    colorBtn.classList.add("active");
                                    selectedColorId = color.id;
                                }

                                colorBtn.addEventListener("click", function () {
                                    const allColorBtns = quickAddColors.querySelectorAll(".quick-color-thumb");
                                    allColorBtns.forEach(btn => btn.classList.remove("active"));

                                    this.classList.add("active");
                                    selectedColorId = this.dataset.colorId;
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

                                if (index === 0) {
                                    sizeBtn.classList.add("active");
                                    selectedSizeId = size.id;
                                }

                                sizeBtn.addEventListener("click", function () {
                                    const allSizeBtns = quickAddSizes.querySelectorAll(".quick-size-btn");
                                    allSizeBtns.forEach(btn => btn.classList.remove("active"));

                                    this.classList.add("active");
                                    selectedSizeId = this.dataset.sizeId;
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