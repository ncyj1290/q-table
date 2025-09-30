$(function() {
    const storeColumns = [
		{ name: 'No.', width: '5%' },
		{ name: '매장 이름', width: '13%' },
		{ name: '매장 아이디', width: '10%' },
		{ name: '매장 이메일', width: '15%' },
		{ name: '신청일', width: '7%' },
		{
		    name: '회원상태',
		    width: '10%',
		    formatter: (cell) => {
		        let badgeClass = '';
		        switch (cell) {
		            case '승인':
		                badgeClass = 'status-badge-active';
		                break;
		            case '반려':
		                badgeClass = 'status-badge-inactive';
		                break;
		            case '심사대기': // 새로운 주황색 상태 추가
		                badgeClass = 'status-badge-dormant';
		                break;
		            default: // 그 외의 값이 들어올 경우
		                badgeClass = 'status-badge-default';
		        }

		        return gridjs.html(`<span class="status-badge ${badgeClass}">${cell}</span>`);
		    }
		},
        {
            name: '관리', width: '42%',
            width: '300px',
            formatter: (cell, row) => {
                const detailButton = `<a href="/admin_detail" class="management-button">상세보기</a>`;
                const statusButton = `<button class="management-button">입점 신청 관리</button>`;
                const deleteButton = `<button class="management-button">삭제</button>`;
                return gridjs.html(detailButton + statusButton + deleteButton);
            }
        }
    ];

    const storeData = [
		["1", "Q-Table 레스토랑", "store_qtable", "contact@qtable.com", "2025-09-28", "승인"],
		["2", "맛있는 파스타", "store_pasta", "pasta_love@example.com", "2025-09-27", "승인"],
		["3", "행복한 베이커리", "store_bakery", "happy@bakery.com", "2025-09-26", "승인"],
		["4", "든든한 국밥집", "store_gukbap", "master@gukbap.co.kr", "2025-09-25", "승인"],
		["5", "신선한 샐러드", "store_salad", "fresh_salad@example.com", "2025-09-24", "승인"],
		["6", "달콤한 디저트 카페", "store_dessert", "sweet@dessert.com", "2025-09-23", "승인"],
		["7", "오래된 노포식당", "store_old", "old@restaurant.com", "2025-09-22", "반려"],
		["8", "최고의 스테이크 하우스", "store_steak", "steakhouse@example.com", "2025-09-21", "심사대기"],
		["9", "건강한 주스 전문점", "store_juice", "juice_bar@example.com", "2025-09-20", "심사대기"],
		["10", "아늑한 심야식당", "store_night", "night_food@example.com", "2025-09-19", "심사대기"]
    ];

    createGrid({
        targetId: '#store-entry-table',
        columns: storeColumns,
        data: storeData,
        pagination: { limit: 10 }
    });
});