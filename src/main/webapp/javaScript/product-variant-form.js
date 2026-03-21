document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('variantForm');
    const priceInput = document.getElementById('price');
    const salePriceInput = document.getElementById('salePrice');
    const stockInput = document.getElementById('stock');

    if (!form) {
        return;
    }

    form.addEventListener('submit', function (event) {
        const price = Number(priceInput.value || 0);
        const salePrice = Number(salePriceInput.value || 0);
        const stock = Number(stockInput.value || 0);

        if (price < 0 || salePrice < 0 || stock < 0) {
            event.preventDefault();
            alert('Giá, giá sale và tồn kho không được nhỏ hơn 0.');
            return;
        }

        if (salePrice > price) {
            event.preventDefault();
            alert('Giá sale không được lớn hơn giá gốc.');
        }
    });
});
