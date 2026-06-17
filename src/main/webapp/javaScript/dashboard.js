function initRevenueChart(chartData) {
    const ctx = document.getElementById('revenueChart').getContext('2d');

    const gradient = ctx.createLinearGradient(0, 0, 0, 300);
    gradient.addColorStop(0, 'rgba(169, 200, 125, 0.45)');
    gradient.addColorStop(1, 'rgba(169, 200, 125, 0.02)');

    const revenueChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: chartData.labels,
            datasets: [{
                label: 'Doanh thu (đ)',
                data: chartData.values,
                borderColor: '#A9C87D',
                backgroundColor: gradient,
                borderWidth: 2.5,
                pointBackgroundColor: '#A9C87D',
                pointBorderColor: '#fff',
                pointBorderWidth: 2,
                pointRadius: 5,
                pointHoverRadius: 7,
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            interaction: { mode: 'index', intersect: false },
            plugins: {
                legend: { display: false },
                tooltip: {
                    callbacks: {
                        label: ctx => ' ' + ctx.parsed.y.toLocaleString('vi-VN') + 'đ'
                    },
                    backgroundColor: '#1a1a2e',
                    titleColor: '#A9C87D',
                    bodyColor: '#fff',
                    padding: 12,
                    cornerRadius: 8
                }
            },
            scales: {
                x: {
                    grid: { color: 'rgba(0,0,0,0.04)' },
                    ticks: { color: '#888', font: { size: 12 } }
                },
                y: {
                    grid: { color: 'rgba(0,0,0,0.06)' },
                    ticks: {
                        color: '#888',
                        font: { size: 12 },
                        callback: val => {
                            if (val >= 1_000_000) return (val / 1_000_000).toFixed(1) + 'M';
                            if (val >= 1_000) return (val / 1_000).toFixed(0) + 'K';
                            return val;
                        }
                    }
                }
            }
        }
    });

    window._revenueChart = revenueChart;
}

function initDashboardDateFilters() {
    const dateInputs = document.querySelectorAll('.js-date-display');
    const nativeDateInputs = document.querySelectorAll('.js-native-date');

    const formatIsoToDisplay = value => {
        if (!value || !/^\d{4}-\d{2}-\d{2}$/.test(value)) return '';
        const [year, month, day] = value.split('-');
        return `${day}/${month}/${year}`;
    };

    const parseDisplayDate = value => {
        const match = value.trim().match(/^(\d{2})\/(\d{2})\/(\d{4})$/);
        if (!match) return null;

        const [, day, month, year] = match;
        const parsed = new Date(`${year}-${month}-${day}T00:00:00`);
        const isValidDate = parsed.getFullYear() === Number(year)
            && parsed.getMonth() + 1 === Number(month)
            && parsed.getDate() === Number(day);

        return isValidDate ? `${year}-${month}-${day}` : null;
    };

    dateInputs.forEach(input => {
        input.addEventListener('input', () => {
            const digits = input.value.replace(/\D/g, '').slice(0, 8);
            const parts = [];

            if (digits.length > 0) parts.push(digits.slice(0, 2));
            if (digits.length > 2) parts.push(digits.slice(2, 4));
            if (digits.length > 4) parts.push(digits.slice(4, 8));

            input.value = parts.join('/');

            const isoDate = parseDisplayDate(input.value);
            if (isoDate) {
                const target = document.getElementById(input.dataset.target);
                const picker = document.getElementById(input.dataset.picker);
                if (target) target.value = isoDate;
                if (picker) picker.value = isoDate;
            }
        });
    });

    nativeDateInputs.forEach(input => {
        input.addEventListener('change', () => {
            const display = document.getElementById(input.dataset.display);
            const target = document.getElementById(input.dataset.target);

            if (display) display.value = formatIsoToDisplay(input.value);
            if (target) target.value = input.value;
        });
    });

    const form = document.querySelector('.revenue-period-btns');
    if (!form) return;

    form.addEventListener('submit', event => {
        for (const input of dateInputs) {
            const target = document.getElementById(input.dataset.target);
            const picker = document.getElementById(input.dataset.picker);
            const isoDate = parseDisplayDate(input.value);

            if (!target || !isoDate) {
                event.preventDefault();
                input.focus();
                alert('Vui lòng nhập ngày hợp lệ theo định dạng ngày/tháng/năm.');
                return;
            }

            target.value = isoDate;
            if (picker) picker.value = isoDate;
        }
    });
}

initDashboardDateFilters();