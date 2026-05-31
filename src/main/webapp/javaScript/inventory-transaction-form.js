(() => {
    const variantSelect = document.getElementById('variantSelect');
    const quantityInput = document.getElementById('quantityInput');
    const addItemBtn = document.getElementById('addItemBtn');
    const selectedItemsBody = document.getElementById('selectedItemsBody');
    const emptyRow = document.getElementById('emptyRow');
    const transactionForm = document.getElementById('transactionForm');
    const transactionType = window.transactionType || '';

    function showNotify(message) {
        const notifyBox = document.getElementById('notifyBox');
        const notifyMessage = document.getElementById('notifyMessage');

        if (!notifyBox || !notifyMessage) {
            return;
        }

        notifyMessage.textContent = message;
        notifyBox.classList.add('show');

        setTimeout(() => {
            notifyBox.classList.remove('show');
        }, 3000);
    }

    function showEmptyRowIfNeeded() {
        const hasItem = selectedItemsBody.querySelector('.selected-item-row') !== null;
        emptyRow.style.display = hasItem ? 'none' : '';
    }

    function addItem() {
        const option = variantSelect.options[variantSelect.selectedIndex];
        const variantId = variantSelect.value;
        const quantity = Number(quantityInput.value);

        if (!variantId) {
            showNotify('Vui lòng chọn sản phẩm cần nhập/xuất.');
            return;
        }

        if (!quantity || quantity <= 0) {
            showNotify('Số lượng phải lớn hơn 0.');
            return;
        }

        const oldRow = selectedItemsBody.querySelector('tr[data-variant-id="' + variantId + '"]');
        const stock = Number(option.dataset.stock || 0);

        if (oldRow) {
            const quantityField = oldRow.querySelector('input[name="quantities"]');
            const quantityText = oldRow.querySelector('.quantity-text');
            const newQuantity = Number(quantityField.value) + quantity;

            if (transactionType === 'EXPORT' && newQuantity > stock) {
                showNotify('Số lượng xuất không được vượt quá tồn kho hiện tại. Biến thể này chỉ còn ' + stock + ' sản phẩm.');
                return;
            }

            quantityField.value = newQuantity;
            quantityText.textContent = newQuantity;
            variantSelect.value = '';
            quantityInput.value = 1;
            return;
        }

        if (transactionType === 'EXPORT' && quantity > stock) {
            showNotify('Số lượng xuất không được vượt quá tồn kho hiện tại. Biến thể này chỉ còn ' + stock + ' sản phẩm.');
            return;
        }

        const row = document.createElement('tr');
        row.className = 'selected-item-row';
        row.dataset.variantId = variantId;
        row.dataset.stock = stock;
        row.innerHTML =
            '<td>#' + variantId + '<input type="hidden" name="variantIds" value="' + variantId + '"></td>' +
            '<td>' + (option.dataset.name || '-') + '</td>' +
            '<td>' + (option.dataset.color || '-') + '</td>' +
            '<td>' + (option.dataset.size || '-') + '</td>' +
            '<td>' + (option.dataset.stock || '0') + '</td>' +
            '<td><span class="quantity-text">' + quantity + '</span><input type="hidden" name="quantities" value="' + quantity + '"></td>' +
            '<td><button type="button" class="btn-remove-item"><i class="fa-solid fa-trash"></i></button></td>';

        selectedItemsBody.appendChild(row);
        variantSelect.value = '';
        quantityInput.value = 1;
        showEmptyRowIfNeeded();
    }

    addItemBtn.addEventListener('click', addItem);

    selectedItemsBody.addEventListener('click', (event) => {
        const removeBtn = event.target.closest('.btn-remove-item');
        if (removeBtn) {
            removeBtn.closest('tr').remove();
            showEmptyRowIfNeeded();
        }
    });

    transactionForm.addEventListener('submit', (event) => {
        if (!selectedItemsBody.querySelector('.selected-item-row')) {
            event.preventDefault();
            showNotify('Vui lòng thêm ít nhất một sản phẩm vào phiếu.');
            return;
        }

        if (transactionType === 'EXPORT') {
            const invalidRow = Array.from(selectedItemsBody.querySelectorAll('.selected-item-row')).find(row => {
                const stock = Number(row.dataset.stock || 0);
                const quantity = Number(row.querySelector('input[name="quantities"]').value || 0);
                return quantity > stock;
            });

            if (invalidRow) {
                event.preventDefault();
                showNotify('Có sản phẩm xuất vượt quá số lượng tồn kho. Vui lòng kiểm tra lại phiếu xuất.');
            }
        }
    });
})();