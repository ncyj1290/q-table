$(function() {
    const storeColumns = [
		{ name: 'No.', width: '5%' },
		{ name: '매장 이름', width: '13%' },
		{ name: '매장 아이디', width: '10%' },
		{ name: '매장 이메일', width: '15%' },
		{ name: '가입일', width: '7%' },
        {
            name: '회원상태', width: '8%',
            formatter: (cell) => {
                let badgeClass = cell === '정상' ? 'status-badge-active' : 'status-badge-inactive';
                return gridjs.html(`<span class="status-badge ${badgeClass}">${cell}</span>`);
            }
        },
        {
            name: '관리', width: '42%',
            width: '300px',
            formatter: (cell, row) => {
                const detailButton = `<a href="/admin_detail" class="management-button">상세보기</a>`;
                const statusButton = `<button class="management-button">회원상태변경</button>`;
				const reviewButton = `<a href="/store_detail_main" class="management-button">리뷰보기</a>`;
                const deleteButton = `<button class="management-button">삭제</button>`;
                return gridjs.html(detailButton + statusButton + reviewButton + deleteButton);
            }
        }
    ];

    const storeData = [
		["1", "Q-Table 레스토랑", "store_qtable", "contact@qtable.com", "2025-09-28", "정상"],
		["2", "맛있는 파스타", "store_pasta", "pasta_love@example.com", "2025-09-27", "정상"],
		["3", "행복한 베이커리", "store_bakery", "happy@bakery.com", "2025-09-26", "탈퇴"],
		["4", "든든한 국밥집", "store_gukbap", "master@gukbap.co.kr", "2025-09-25", "정상"],
		["5", "신선한 샐러드", "store_salad", "fresh_salad@example.com", "2025-09-24", "정상"],
		["6", "달콤한 디저트 카페", "store_dessert", "sweet@dessert.com", "2025-09-23", "정상"],
		["7", "오래된 노포식당", "store_old", "old@restaurant.com", "2025-09-22", "탈퇴"],
		["8", "최고의 스테이크 하우스", "store_steak", "steakhouse@example.com", "2025-09-21", "정상"],
		["9", "건강한 주스 전문점", "store_juice", "juice_bar@example.com", "2025-09-20", "정상"],
		["10", "아늑한 심야식당", "store_night", "night_food@example.com", "2025-09-19", "정상"]
    ];

    createGrid({
        targetId: '#store-table',
        columns: storeColumns,
        data: storeData,
        pagination: { limit: 10 }
    });
});