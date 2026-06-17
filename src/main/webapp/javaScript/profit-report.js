(function () {
    const chartData = window.profitReportChartData || {};

    function formatCurrency(value) {
        const number = Number(value || 0);
        return number.toLocaleString('en-US', { maximumFractionDigits: 0 }) + 'đ';
    }

    function createLineChart(canvasId, labels, values, label) {
        const canvas = document.getElementById(canvasId);
        if (!canvas || typeof Chart === 'undefined') return;

        const ctx = canvas.getContext('2d');
        const gradient = ctx.createLinearGradient(0, 0, 0, 280);
        gradient.addColorStop(0, 'rgba(169, 200, 125, 0.35)');
        gradient.addColorStop(1, 'rgba(169, 200, 125, 0.03)');

        new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels || [],
                datasets: [{
                    label: label,
                    data: values || [],
                    borderColor: '#A9C87D',
                    backgroundColor: gradient,
                    borderWidth: 2.5,
                    pointRadius: 4,
                    pointHoverRadius: 6,
                    tension: 0.35,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                interaction: { mode: 'index', intersect: false },
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: { label: ctx => ' ' + label + ': ' + formatCurrency(ctx.parsed.y) },
                        backgroundColor: '#111827',
                        titleColor: '#A9C87D',
                        bodyColor: '#fff',
                        padding: 12,
                        cornerRadius: 8
                    }
                },
                scales: {
                    x: { grid: { color: 'rgba(0,0,0,0.04)' }, ticks: { color: '#6b7280' } },
                    y: { grid: { color: 'rgba(0,0,0,0.06)' }, ticks: { color: '#6b7280', callback: formatCurrency } }
                }
            }
        });
    }

    function createBarChart(canvasId, labels, values) {
        const canvas = document.getElementById(canvasId);
        if (!canvas || typeof Chart === 'undefined') return;

        new Chart(canvas.getContext('2d'), {
            type: 'bar',
            data: {
                labels: labels || [],
                datasets: [{
                    label: 'Số lượng bán',
                    data: values || [],
                    backgroundColor: 'rgba(169, 200, 125, 0.75)',
                    borderColor: '#A9C87D',
                    borderWidth: 1,
                    borderRadius: 8,
                    maxBarThickness: 80
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: { label: ctx => ' Số lượng bán: ' + Number(ctx.parsed.y || 0).toLocaleString('vi-VN') },
                        backgroundColor: '#111827',
                        titleColor: '#A9C87D',
                        bodyColor: '#fff',
                        padding: 12,
                        cornerRadius: 8
                    }
                },
                scales: {
                    x: { grid: { display: false }, ticks: { color: '#6b7280' } },
                    y: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.06)' }, ticks: { color: '#6b7280', precision: 0 } }
                }
            }
        });
    }

    createLineChart('profitRevenueChart', chartData.dailyLabels, chartData.dailyRevenue, 'Doanh thu');
    createLineChart('profitImportCostChart', chartData.dailyLabels, chartData.dailyImportCost, 'Chi phí nhập hàng');
    createBarChart('profitSoldProductChart', chartData.topSoldLabels, chartData.topSoldValues);

    function initTablePagination() {
        const tables = document.querySelectorAll('.js-paginated-table');

        tables.forEach(table => {
            const tbody = table.querySelector('tbody');
            if (!tbody) return;

            const rows = Array.from(tbody.querySelectorAll('tr')).filter(row => !row.querySelector('.empty-row'));
            const pageSize = Number(table.dataset.pageSize || 10);
            const pager = document.querySelector(`.table-pagination[data-pagination-for="${table.id}"]`);

            if (!pager) return;

            if (rows.length <= pageSize) {
                pager.innerHTML = '';
                pager.style.display = 'none';
                rows.forEach(row => row.style.display = '');
                return;
            }

            pager.style.display = 'flex';
            let currentPage = 1;
            const totalPages = Math.ceil(rows.length / pageSize);

            function getPageNumbers() {
                const pages = [];
                const start = Math.max(1, currentPage - 2);
                const end = Math.min(totalPages, currentPage + 2);

                if (start > 1) {
                    pages.push(1);
                    if (start > 2) pages.push('...');
                }

                for (let page = start; page <= end; page++) pages.push(page);

                if (end < totalPages) {
                    if (end < totalPages - 1) pages.push('...');
                    pages.push(totalPages);
                }

                return pages;
            }

            function render() {
                const startIndex = (currentPage - 1) * pageSize;
                const endIndex = startIndex + pageSize;

                rows.forEach((row, index) => {
                    row.style.display = index >= startIndex && index < endIndex ? '' : 'none';
                });

                const from = startIndex + 1;
                const to = Math.min(endIndex, rows.length);
                const pageButtons = getPageNumbers().map(page => {
                    if (page === '...') return '<span class="pagination-ellipsis">...</span>';
                    return `<button type="button" class="pagination-page ${page === currentPage ? 'active' : ''}" data-page="${page}">${page}</button>`;
                }).join('');

                pager.innerHTML = `
                    <div class="pagination-info">Hiển thị ${from}-${to} / ${rows.length} sản phẩm</div>
                    <div class="pagination-actions">
                        <button type="button" class="pagination-btn" data-action="prev" ${currentPage === 1 ? 'disabled' : ''}>Trước</button>
                        ${pageButtons}
                        <button type="button" class="pagination-btn" data-action="next" ${currentPage === totalPages ? 'disabled' : ''}>Sau</button>
                    </div>
                `;
            }

            pager.addEventListener('click', event => {
                const button = event.target.closest('button');
                if (!button || button.disabled) return;

                const action = button.dataset.action;
                const page = Number(button.dataset.page);

                if (action === 'prev') currentPage = Math.max(1, currentPage - 1);
                if (action === 'next') currentPage = Math.min(totalPages, currentPage + 1);
                if (page) currentPage = page;

                render();
            });

            render();
        });
    }

    initTablePagination();

})();
