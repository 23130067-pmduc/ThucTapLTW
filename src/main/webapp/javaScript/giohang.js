document.addEventListener("DOMContentLoaded", () => {

    const totalQtyEl = document.getElementById("totalQuantity");
    const totalPriceEl = document.getElementById("totalPrice");
    const totalFinalEl = document.getElementById("totalFinal");

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
            plus.disabled = stock > 0 && qty >= stock;
        }

        minus.addEventListener("click", () => {
            let qty = Number(qtyInput.value);
            if (qty > 1) {
                qtyInput.value = qty - 1;
                form.submit();
            }
            updateButtons();
        });

        plus.addEventListener("click", () => {
            const qty = Number(qtyInput.value);
            if (stock > 0 && qty >= stock) {
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
