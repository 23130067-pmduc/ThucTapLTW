(() => {
    const variantSelect = document.getElementById('variantSelect');
    const variantSearchInput = document.getElementById('variantSearchInput');
    const quantityInput = document.getElementById('quantityInput');
    const unitCostInput = document.getElementById('unitCostInput');
    const addItemBtn = document.getElementById('addItemBtn');
    const supplierSelect = document.getElementById('supplierSelect');
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

    function formatMoney(value) {
        return Number(value || 0).toLocaleString('vi-VN') + ' đ';
    }

    const originalVariantOptions = Array.from(variantSelect.options).map(option => option.cloneNode(true));

    function normalizeText(value) {
        return (value || '')
            .toString()
            .toLowerCase()
            .normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '')
            .replace('#', '')
            .trim();
    }

    function filterVariants() {
        const keyword = normalizeText(variantSearchInput.value);

        variantSelect.innerHTML = '';

        originalVariantOptions.forEach(originalOption => {
            const option = originalOption.cloneNode(true);

            if (!option.value) {
                variantSelect.appendChild(option);
                return;
            }

            const variantId = normalizeText(option.value);
            const optionText = normalizeText(option.textContent);
            const productName = normalizeText(option.dataset.name);
            const colorName = normalizeText(option.dataset.color);
            const sizeName = normalizeText(option.dataset.size);
            const categoryName = normalizeText(option.dataset.category);

            const matched =
                !keyword ||
                variantId.includes(keyword) ||
                optionText.includes(keyword) ||
                productName.includes(keyword) ||
                colorName.includes(keyword) ||
                sizeName.includes(keyword) ||
                categoryName.includes(keyword);

            if (matched) {
                variantSelect.appendChild(option);
            }
        });

        variantSelect.selectedIndex = 0;
    }

    function showEmptyRowIfNeeded() {
        const hasItem = selectedItemsBody.querySelector('.selected-item-row') !== null;
        emptyRow.style.display = hasItem ? 'none' : '';
    }

    function getRowKey(variantId, unitCost) {
        if (transactionType === 'IMPORT') {
            return variantId + '-' + unitCost;
        }
        return variantId;
    }

    function addItem() {
        const option = variantSelect.options[variantSelect.selectedIndex];
        const variantId = variantSelect.value;
        const quantity = Number(quantityInput.value);
        const stock = Number(option?.dataset?.stock || 0);
        const unitCost = unitCostInput ? Number(unitCostInput.value) : 0;

        if (!variantId) {
            showNotify('Vui lòng chọn sản phẩm cần nhập/xuất.');
            return;
        }

        if (!quantity || quantity <= 0) {
            showNotify('Số lượng phải lớn hơn 0.');
            return;
        }

        if (transactionType === 'IMPORT' && (!unitCost || unitCost <= 0)) {
            showNotify('Vui lòng nhập giá nhập lớn hơn 0.');
            return;
        }

        if (transactionType === 'EXPORT' && quantity > stock) {
            showNotify('Số lượng xuất không được vượt quá tồn kho hiện tại. Biến thể này chỉ còn ' + stock + ' sản phẩm.');
            return;
        }

        const rowKey = getRowKey(variantId, unitCost);
        const oldRow = selectedItemsBody.querySelector('tr[data-row-key="' + rowKey + '"]');

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
            if (unitCostInput) {
                unitCostInput.value = '';
            }
            return;
        }

        const row = document.createElement('tr');
        row.className = 'selected-item-row';
        row.dataset.variantId = variantId;
        row.dataset.stock = stock;
        row.dataset.rowKey = rowKey;

        let rowHtml =
            '<td>#' + variantId + '<input type="hidden" name="variantIds" value="' + variantId + '"></td>' +
            '<td>' + (option.dataset.name || '-') + '</td>' +
            '<td>' + (option.dataset.color || '-') + '</td>' +
            '<td>' + (option.dataset.size || '-') + '</td>' +
            '<td>' + (option.dataset.stock || '0') + '</td>' +
            '<td><span class="quantity-text">' + quantity + '</span><input type="hidden" name="quantities" value="' + quantity + '"></td>';

        if (transactionType === 'IMPORT') {
            rowHtml += '<td><span class="cost-price-badge">' + formatMoney(unitCost) + '</span><input type="hidden" name="unitCosts" value="' + unitCost + '"></td>';
        }

        rowHtml += '<td><button type="button" class="btn-remove-item"><i class="fa-solid fa-trash"></i></button></td>';

        row.innerHTML = rowHtml;
        selectedItemsBody.appendChild(row);
        variantSelect.value = '';
        quantityInput.value = 1;
        if (unitCostInput) {
            unitCostInput.value = '';
        }
        showEmptyRowIfNeeded();
    }

    if (variantSearchInput) {
        variantSearchInput.addEventListener('input', filterVariants);
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

        if (transactionType === 'IMPORT') {
            if (supplierSelect && !supplierSelect.value) {
                event.preventDefault();
                showNotify('Vui lòng chọn nhà cung cấp trước khi lưu phiếu nhập.');
                return;
            }

            const invalidCostRow = Array.from(selectedItemsBody.querySelectorAll('.selected-item-row')).find(row => {
                const costInput = row.querySelector('input[name="unitCosts"]');
                return !costInput || Number(costInput.value || 0) <= 0;
            });

            if (invalidCostRow) {
                event.preventDefault();
                showNotify('Phiếu nhập cần có giá nhập hợp lệ cho từng sản phẩm.');
            }
        }
    });
})();
