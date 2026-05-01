document.addEventListener("DOMContentLoaded", () => {

    const totalQtyEl = document.getElementById("totalQuantity");
    const totalPriceEl = document.getElementById("totalPrice");
    const totalFinalEl = document.getElementById("totalFinal");

    function showStockError(stock) {
        const message = `Không thể tăng thêm. Chỉ còn ${stock} sản phẩm trong kho.`;

        let toast = document.getElementById("toast");
        if (!toast) {
            toast = document.createElement("div");
            toast.id = "toast";
            document.body.appendChild(toast);
        }

        toast.textContent = message;
        toast.className = "toast";

        clearTimeout(toast.hideTimer);
        requestAnimationFrame(() => {
            toast.className = "toast show";
        });

        toast.hideTimer = setTimeout(() => {
            toast.className = "toast";
        }, 3000);
    }

    function updateTotal() {
        let totalQty = 0;
        let totalPrice = 0;

        document.querySelectorAll(".qty-display").forEach(qtyInput => {
            const row = qtyInput.closest("tr");
            const price = Number(row.dataset.price);
            const qty = Number(qtyInput.value);

            totalQty += qty;
            totalPrice += qty * price;
        });

        totalQtyEl.innerText = totalQty;
        totalPriceEl.innerText = totalPrice.toLocaleString("vi-VN");
        totalFinalEl.innerText = totalPrice.toLocaleString("vi-VN");
    }

    document.querySelectorAll(".qty-form").forEach(form => {
        const minus = form.querySelector(".btn-minus");
        const plus = form.querySelector(".btn-plus");
        const qtyInput = form.querySelector(".qty-display");
        const row = qtyInput.closest("tr");
        const stock = Number(row.dataset.stock || qtyInput.getAttribute("max") || 0);

        function updateButtons() {
            const qty = Number(qtyInput.value);
            minus.disabled = qty <= 1;
        }

        minus.addEventListener("click", () => {
            const qty = Number(qtyInput.value);
            if (qty > 1) {
                qtyInput.value = qty - 1;
                form.submit();
            }
            updateButtons();
        });

        plus.addEventListener("click", () => {
            const qty = Number(qtyInput.value);
            if (stock > 0 && qty >= stock) {
                showStockError(stock);
                updateButtons();
                return;
            }

            qtyInput.value = qty + 1;
            form.submit();
        });

        updateButtons();
    });

    updateTotal();
});
