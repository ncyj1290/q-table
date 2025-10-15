$(function() {
    const subscribeColumns = [
		{ name: 'subscribe_idx', hidden: true },
		{ name: 'member_idx', hidden: true },
		{ name: 'No.', width: '5%' },
		{ name: '매장명', width: '10%' },
		{ name: '회원 아이디', width: '10%' },
		{ name: '구독 시작날짜', width: '10%' },
		{ name: '구독 종료날짜', width: '10%' },
		{ name: '남은 구독일수', width: '8%' },
        {
            name: '관리', width: '20%',
            width: '200px',
            formatter: (cell, row) => {
				
				const subscribe_idx = row.cells[0].data;
				const member_idx = row.cells[1].data;
				
				const detail_button = `<a href="/admin_detail/${member_idx}" class="management-button">상세보기</a>`;
                const delete_button = `<button class="management-button delete-btn" data-idx="${subscribe_idx}">삭제</button>`;
                return gridjs.html(detail_button + delete_button);
            }
        }
    ];

	// AJAX 회원 목록 데이터 요청
	$.ajax({
		url: '/api/subscribe',
		type: 'GET',
		success: function(response) {
			// Grid.js 형식
			const formatted_data = response.map((subscribe, index) => {
				return [
					subscribe.subscribe_idx,
					subscribe.member_idx,
					index + 1,
					subscribe.store_name,
					subscribe.member_id,
					subscribe.subscribe_start,
					subscribe.subscribe_end,
					subscribe.remaining_days + "일",
					null
				];
			});

			// 공통 함수 createGrid
			createGrid({
				targetId: '#subscribe-table', // 테이블을 표시할 div ID
				columns: subscribeColumns,
				data: formatted_data,
				pagination: { limit: 10 }
			});
		},
	});
	
	// 삭제 버튼 클릭 이벤트
	$('#subscribe-table').on('click', '.delete-btn', function() {

		const subscribe_idx = $(this).data('idx');

		if (confirm(`정말로 삭제하시겠습니까?`)) {

			// 확인을 눌렀을 때 AJAX 코드 실행
			$.ajax({
				url: `/api/subscribe/${subscribe_idx}`,
				type: 'POST',
				success: function(response) {
					alert("삭제에 성공했습니다.");
					location.reload();
				},
				error: function(error) {
					alert("삭제에 실패했습니다.");
				}
			});

		} else {
			console.log("삭제가 취소되었습니다.");
		}
	});
	
});