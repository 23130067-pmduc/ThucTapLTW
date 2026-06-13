function initRevenueChart(chartData) {
    const ctx = document.getElementById('revenueChart').getContext('2d');

    const gradient = ctx.createLinearGradient(0, 0, 0, 300);
    gradient.addColorStop(0, 'rgba(169, 200, 125, 0.45)');
    gradient.addColorStop(1, 'rgba(169, 200, 125, 0.02)');

    const revenueChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: chartData['7day'].labels,
            datasets: [{
                label: 'Doanh thu (đ)',
                data: chartData['7day'].values,
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
    window._revenueChartData = chartData;
}

function switchPeriod(period, btn) {
    document.querySelectorAll('.period-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');

    const d = window._revenueChartData[period];
    window._revenueChart.data.labels = d.labels;
    window._revenueChart.data.datasets[0].data = d.values;
    window._revenueChart.update('active');
}
