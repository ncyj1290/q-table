$(function() {
    const adminColumns = [
		{ name: 'No.', width: '5%' },
		{ name: '관리자 이름', width: '10%' },
		{ name: '관리자 아이디', width: '10%' },
		{ name: '관리자 이메일', width: '15%' },
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

	const adminData = [
	    ["1", "김관리", "admin_kim", "admin_kim@example.com", "2025-01-15", "정상"],
	    ["2", "이총괄", "admin_lee", "admin_lee@example.com", "2025-02-20", "정상"],
	    ["3", "박시스템", "admin_park", "admin_park@example.com", "2025-03-01", "탈퇴"],
	    ["4", "최운영", "admin_choi", "admin_choi@example.com", "2025-04-10", "정상"],
	    ["5", "정마스터", "admin_master", "master@example.com", "2025-05-01", "정상"]
	];

    createGrid({
        targetId: '#admin-table',
        columns: adminColumns,
        data: adminData,
        pagination: { limit: 10 }
    });
});