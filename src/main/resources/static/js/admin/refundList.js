$(function() {
    const refundColumns = [
		{ name: 'No.', width: '5%' },
		{ name: '매장명', width: '15%' },
		{ name: '회원 이름', width: '15%' },
		{ name: '회원 아이디', width: '15%' },
		{ name: '정산 금액', width: '10%' },
		{ name: '결제 일시', width: '15%' },
		{
		    name: '결제상태', width: '10%',
		    formatter: (cell) => {
		        let badgeClass = cell === '정산' ? 'status-badge-active' : 'status-badge-inactive';
		        return gridjs.html(`<span class="status-badge ${badgeClass}">${cell}</span>`);
		    }
		},
        {
            name: '관리', width: '10%',
            width: '200px',
            formatter: (cell, row) => {
				const detailButton = `<button class="management-button status-change-btn">정산</button>`;
                const deleteButton = `<button class="management-button delete-btn">삭제</button>`;
                return gridjs.html(detailButton + deleteButton);
            }
        }
    ];

	const refundData = [
	    ["1", "Q-Table 레스토랑", "김민준", "user_kim", "250,000원", "2025-09-30 15:00:00", "정산"],
	    ["2", "맛있는 파스타", "이서연", "user_lee", "185,000원", "2025-09-30 15:00:00", "정산"],
	    ["3", "행복한 베이커리", "박도현", "user_park", "80,000원", "2025-09-30 15:00:00", "미정산"],
	    ["4", "든든한 국밥집", "최지아", "user_choi", "190,000원", "2025-09-29 15:00:00", "정산"],
	    ["5", "신선한 샐러드", "정은우", "user_jung", "125,000원", "2025-09-29 15:00:00", "정산"],
	    ["6", "Q-Table 레스토랑", "강하윤", "user_kang", "450,000원", "2025-09-29 15:00:00", "미정산"],
	    ["7", "최고의 스테이크 하우스", "김민준", "user_kim", "780,000원", "2025-09-28 15:00:00", "정산"],
	    ["8", "맛있는 파스타", "윤서준", "user_yoon", "210,000원", "2025-09-28 15:00:00", "정산"],
	    ["9", "아늑한 심야식당", "임지민", "user_lim", "320,000원", "2025-09-27 15:00:00", "정산"],
	    ["10", "든든한 국밥집", "이서연", "user_lee", "95,000원", "2025-09-27 15:00:00", "미정산"]
	];

    createGrid({
        targetId: '#refund-table',
        columns: refundColumns,
        data: refundData,
        pagination: { limit: 10 }
    });
});