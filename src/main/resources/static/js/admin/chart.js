$(function() {

        const mainColor = $('html').css('--color-main').trim();
        const focusColor = $('html').css('--color-focus').trim();

        // 매출액 추이 차트
        const salesCtx = $('#salesChart')[0].getContext('2d');
        const salesChart = new Chart(salesCtx, {
            type: 'line',
            data: {
                labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
                datasets: [{
                    label: '매출액',
                    data: [500, 750, 120, 50, 60, 350, 200, 480, 230, 120, 280, 850],
                    borderColor: '#A52A2A',
                    backgroundColor: 'rgba(165, 42, 42, 0.1)',
                    tension: 0.1,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } }
            }
        });

        // 3. 사용자 / 매장 신규회원 차트
        const membersCtx = $('#membersChart')[0].getContext('2d');
        const membersChart = new Chart(membersCtx, {
            type: 'line',
            data: {
                labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
                datasets: [
                    {
                        label: '사용자',
                        data: [10, 75, 20, 100, 25, 20, 60, 40, 100, 40, 100, 10],
                        borderColor: mainColor,
                        tension: 0.1
                    },
                    {
                        label: '매장',
                        data: [75, 25, 90, 40, 50, 20, 90, 20, 80, 35, 35, 70],
                        borderColor: focusColor,
                        tension: 0.1
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                    }
                }
            }
        });

    });