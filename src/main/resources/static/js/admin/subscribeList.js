$(function() {
    const subscribeColumns = [
		{ name: 'No.', width: '5%' },
		{ name: '매장명', width: '15%' },
		{ name: '결제자 이름', width: '10%' },
		{ name: '회원 아이디', width: '15%' },
		{ name: '구독 시작날짜', width: '15%' },
		{ name: '구독 종료날짜', width: '15%' },
        {
            name: '관리', width: '20%',
            width: '200px',
            formatter: (cell, row) => {
				const detailButton = `<a href="/admin_detail" class="management-button">상세보기</a>`;;
                const deleteButton = `<button class="management-button">삭제</button>`;
                return gridjs.html(detailButton + deleteButton);
            }
        }
    ];

	const subscribeData = [
	    ["1", "Q-Table 레스토랑", "김민준", "user_kim", "2025-08-01", "2025-11-01", ""],
	    ["2", "맛있는 파스타", "이서연", "user_lee", "2025-05-15", "2025-11-15", ""],
	    ["3", "행복한 베이커리", "박도현", "user_park", "2024-11-20", "2025-11-20", ""],
	    ["4", "든든한 국밥집", "최지아", "user_choi", "2025-09-05", "2025-12-05", ""],
	    ["5", "신선한 샐러드", "정은우", "user_jung", "2025-07-10", "2026-01-10", ""],
	    ["6", "달콤한 디저트 카페", "강하윤", "user_kang", "2025-01-01", "2026-01-01", ""],
	    ["7", "최고의 스테이크 하우스", "윤서준", "user_yoon", "2025-09-18", "2025-12-18", ""],
	    ["8", "아늑한 심야식당", "임지민", "user_lim", "2025-04-30", "2025-10-30", ""],
	    ["9", "Q-Table 레스토랑", "박도현", "user_park", "2024-10-10", "2025-10-10", ""],
	    ["10", "맛있는 파스타", "김민준", "user_kim", "2025-06-25", "2025-09-25", ""]
	];

    createGrid({
        targetId: '#subscribe-table',
        columns: subscribeColumns,
        data: subscribeData,
        pagination: { limit: 10 }
    });
});