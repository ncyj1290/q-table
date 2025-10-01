$(function() {
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
			name: '관리', width: '45%',
			width: '250px',
			formatter: (cell, row) => {
				const detailButton = `<a href="/admin_detail" class="management-button">상세보기</a>`;
				const statusButton = `<button class="management-button status-change-btn">회원 상태변경</button>`;
				const deleteButton = `<button class="management-button delete-btn">삭제</button>`;
				return gridjs.html(detailButton + statusButton + deleteButton);
			}
		}
	];

	const memberData = [
		["1", "김민준", "user_kim", "kim.minjun@example.com", "2025-09-28", "정상"],
		["2", "이서연", "user_lee", "lee.seoyun@example.com", "2025-09-27", "정상"],
		["3", "박도현", "user_park", "park.dohyun@example.com", "2025-09-25", "탈퇴"],
		["4", "최지아", "user_choi", "choi.jia@example.com", "2025-09-22", "정상"],
		["5", "정은우", "user_jung", "jung.eunwoo@example.com", "2025-09-21", "정상"],
		["1", "김민준", "user_kim", "kim.minjun@example.com", "2025-09-28", "정상"],
		["2", "이서연", "user_lee", "lee.seoyun@example.com", "2025-09-27", "정상"],
		["3", "박도현", "user_park", "park.dohyun@example.com", "2025-09-25", "탈퇴"],
		["4", "최지아", "user_choi", "choi.jia@example.com", "2025-09-22", "정상"],
		["5", "정은우", "user_jung", "jung.eunwoo@example.com", "2025-09-21", "정상"],
		["1", "김민준", "user_kim", "kim.minjun@example.com", "2025-09-28", "정상"],
		["2", "이서연", "user_lee", "lee.seoyun@example.com", "2025-09-27", "정상"],
		["3", "박도현", "user_park", "park.dohyun@example.com", "2025-09-25", "탈퇴"],
		["4", "최지아", "user_choi", "choi.jia@example.com", "2025-09-22", "정상"],
		["5", "정은우", "user_jung", "jung.eunwoo@example.com", "2025-09-21", "정상"]
	];

	createGrid({
		targetId: '#member-table',
		columns: memberColumns,
		data: memberData,
		pagination: { limit: 10 }
	});
});