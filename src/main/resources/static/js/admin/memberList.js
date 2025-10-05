$(function() {
	// Grid.js에 표시할 컬럼
	const memberColumns = [
		{ name: 'No.', width: '5%' },
		{ name: '회원 이름', width: '10%' },
		{ name: '회원 아이디', width: '10%' },
		{ name: '회원 이메일', width: '15%' },
		{ name: '가입일', width: '8%' },
		{
			name: '회원상태', width: '7%',
			formatter: (cell) => {
				let badgeClass = cell === '정상' ? 'status-badge-active' : 'status-badge-inactive';
				return gridjs.html(`<span class="status-badge ${badgeClass}">${cell}</span>`);
			}
		},
		{
			name: '관리', width: '250px',
			formatter: (cell, row) => {
				// 각 버튼에 해당 행의 고유 ID
				const memberId = row.cells[2].data;

				const detailButton = `<a href="/admin_detail/${memberId}" class="management-button">상세보기</a>`;
				const statusButton = `<button class="management-button status-change-btn" data-id="${memberId}">상태변경</button>`;
				const deleteButton = `<button class="management-button delete-btn" data-id="${memberId}">삭제</button>`;

				return gridjs.html(detailButton + statusButton + deleteButton);
			}
		}
	];

	// -------------- ajax 요청 -------------
	$.ajax({
		url: '/api/members',
		type: 'GET',
		success: function(response) {
			// 서버에서 받은 데이터를 grid.js 형식으로
			const formattedData = response.map(member => [
				member.no,
				member.memberName,
				member.memberId,
				member.email,
				member.joinDate ? member.joinDate.substring(0, 10) : '-',
				member.memberStatus,
				null
			]);

			// createGrid 함수를 호출
			createGrid({
				targetId: '#member-table',
				columns: memberColumns,
				data: formattedData,
				pagination: { limit: 10 }
			});
		}
	});
});