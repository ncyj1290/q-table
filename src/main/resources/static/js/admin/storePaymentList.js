$(function() {
    const paymentColumns = [
		{ name: 'No.', width: '5%' },
		{ name: '매장명', width: '15%' },
		{ name: '결제자 이름', width: '15%' },
		{ name: '회원 아이디', width: '15%' },
		{ name: '결제 금액', width: '15%' },
		{ name: '결제 일시', width: '15%' },
		{
		    name: '결제상태', width: '10%',
		    formatter: (cell) => {
		        let badgeClass = cell === '정상' ? 'status-badge-active' : 'status-badge-inactive';
		        return gridjs.html(`<span class="status-badge ${badgeClass}">${cell}</span>`);
		    }
		},
        {
            name: '관리', width: '10%',
            width: '100px',
            formatter: (cell, row) => {
                const deleteButton = `<button class="management-button">삭제</button>`;
                return gridjs.html(deleteButton);
            }
        }
    ];

	const paymentData = [
	    ["1", "Q-Table 레스토랑", "김민준", "user_kim", "25,000원", "2025-09-30 13:45:22", "정상"],
	    ["2", "맛있는 파스타", "이서연", "user_lee", "18,500원", "2025-09-30 11:10:05", "정상"],
	    ["3", "행복한 베이커리", "박도현", "user_park", "8,000원", "2025-09-29 17:30:10", "실패"],
	    ["4", "든든한 국밥집", "최지아", "user_choi", "19,000원", "2025-09-29 12:05:40", "정상"],
	    ["5", "신선한 샐러드", "정은우", "user_jung", "12,500원", "2025-09-28 20:15:00", "정상"],
	    ["6", "Q-Table 레스토랑", "강하윤", "user_kang", "45,000원", "2025-09-28 19:00:30", "정상"],
	    ["7", "최고의 스테이크 하우스", "김민준", "user_kim", "78,000원", "2025-09-27 18:22:15", "정상"],
	    ["8", "맛있는 파스타", "윤서준", "user_yoon", "21,000원", "2025-09-27 14:00:55", "실패"],
	    ["9", "아늑한 심야식당", "임지민", "user_lim", "32,000원", "2025-09-26 23:10:00", "정상"],
	    ["10", "든든한 국밥집", "이서연", "user_lee", "9,500원", "2025-09-26 13:25:41", "정상"]
	];

    createGrid({
        targetId: '#store-payment-table',
        columns: paymentColumns,
        data: paymentData,
        pagination: { limit: 10 }
    });
});