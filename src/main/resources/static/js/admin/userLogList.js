$(function() {
	const userLogColumns = [
		{ name: 'log_idx', hidden: true },
		{ name: 'No.', width: '5%' },
		{ name: '회원 아이디', width: '10%' },
		{ name: '로그인 시간', width: '10%' },
		{ name: '회원 IP', width: '10%' },
		{
			name: '관리', width: '10%',
			width: '50px',
			formatter: (cell, row) => {

				// row.cells 배열에서 필요한 데이터를 먼저 꺼내서 변수에 담아야 합니다.
				const log_idx = row.cells[0].data;

				const delete_button = `<button class="management-button delete-btn" data-idx="${log_idx}">삭제</button>`;
				return gridjs.html(delete_button);
			}
		}
	];
	// AJAX 회원 목록 데이터 요청
	$.ajax({
		url: '/api/members/log',
		type: 'GET',
		success: function(response) {
			// Grid.js 형식
			const formatted_data = response.map((log, index) => {
				return [
					log.log_idx,
					index + 1,
					log.member_id,
					log.login_time.replace('T', ' '),
					log.ip_address,
					null
				];
			});

			// 공통 함수 createGrid
			createGrid({
				targetId: '#userlog-table', // 테이블을 표시할 div ID
				columns: userLogColumns,
				data: formatted_data,
				pagination: { limit: 10 }
			});
		},
	});
});

// 삭제 버튼 클릭 이벤트
$('#userlog-table').on('click', '.delete-btn', function() {

	const log_idx = $(this).data('idx');
	
	if (confirm(`정말로 삭제하시겠습니까?`)) {
		// 확인을 눌렀을 때 AJAX 코드 실행
		$.ajax({
			url: `/api/log/${log_idx}`,
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