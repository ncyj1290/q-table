$(function() {
	const paymentColumns = [
		{ name: 'member_idx', hidden: true },
		{ name: 'No.', width: '2%' },
		{ name: '결제자 이름', width: '5%' },
		{ name: '회원 아이디', width: '5%' },
		{ name: '결제 금액', width: '3%' },
		{ name: '결제 타입', width: '5%' },
		{ name: '결제 일시', width: '5%' },
		{
			name: '결제상태', width: '5%',
			formatter: (cell) => {
				let badgeClass = cell === '결제 성공' ? 'status-badge-active' : 'status-badge-inactive';
				return gridjs.html(`<span class="status-badge ${badgeClass}">${cell}</span>`);
			}
		},
		{
			name: '관리', width: '5%',
			width: '10px',
			formatter: (cell, row) => {

				// row.cells 배열에서 필요한 데이터를 먼저 꺼내서 변수에 담아야 합니다.
				const member_idx = row.cells[0].data; // 'No.' 컬럼
				const member_id = row.cells[3].data;  // '매장 아이디' 컬럼

				const delete_button = `<button class="management-button delete-btn" data-idx="${member_idx}" data-id="${member_id}">삭제</button>`;
				return gridjs.html(delete_button);
			}
		}
	];
	// AJAX 회원 목록 데이터 요청
	$.ajax({
		url: '/api/payments/members',
		type: 'GET',
		success: function(response) {
			// Grid.js 형식
			const formatted_data = response.map((payment, index) => {
				return [
					payment.member_idx,
					index + 1,
					payment.member_name,
					payment.member_id,
					payment.payment_amount + " 원",
					payment.pay_type,
					payment.payment_date ? payment.payment_date.substring(0, 10) : '-',
					payment.pay_status,
					null
				];
			});

			// 공통 함수 createGrid
			createGrid({
				targetId: '#member-payment-table', // 테이블을 표시할 div ID
				columns: paymentColumns,
				data: formatted_data,
				pagination: { limit: 10 }
			});
		},
	});
});

// 삭제 버튼 클릭 이벤트
$('#member-payment-table').on('click', '.delete-btn', function() {

	const member_idx = $(this).data('idx');

	if (confirm(`정말로 삭제하시겠습니까?`)) {

		// 확인을 눌렀을 때 AJAX 코드 실행
		$.ajax({
			url: `/api/members/${member_idx}`,
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