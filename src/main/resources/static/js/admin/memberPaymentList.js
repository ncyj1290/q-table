$(function() {
    const paymentColumns = [
		{ name: 'No.', width: '5%' },
		{ name: '결제자 이름', width: '15%' },
		{ name: '회원 아이디', width: '15%' },
		{ name: '결제 금액', width: '15%' },
		{ name: '충전/차감', width: '10%' },
		{ name: '결제 일시', width: '15%' },
		{
		    name: '결제상태', width: '10%',
		    formatter: (cell) => {
		        let badgeClass = cell === '정상' ? 'status-badge-active' : 'status-badge-inactive';
		        return gridjs.html(`<span class="status-badge ${badgeClass}">${cell}</span>`);
		    }
		},
        {
            name: '관리', width: '10%',
            width: '100px',
            formatter: (cell, row) => {
                const deleteButton = `<button class="management-button">삭제</button>`;
                return gridjs.html(deleteButton);
            }
        }
    ];

	const paymentData = [
	    ["1", "김민준", "user_kim", "50,000원", "충전", "2025-09-29 14:30:15", "정상"],
	    ["2", "이서연", "user_lee", "15,000원", "차감", "2025-09-28 11:20:45", "정상"],
	    ["3", "박도현", "user_park", "10,000원", "충전", "2025-09-28 09:05:30", "실패"],
	    ["4", "최지아", "user_choi", "30,000원", "충전", "2025-09-27 18:55:02", "정상"],
	    ["5", "정은우", "user_jung", "5,000원", "차감", "2025-09-27 16:40:11", "정상"],
	    ["6", "김민준", "user_kim", "20,000원", "차감", "2025-09-26 20:10:58", "정상"],
	    ["7", "강하윤", "user_kang", "100,000원", "충전", "2025-09-25 13:00:00", "정상"],
	    ["8", "윤서준", "user_yoon", "25,000원", "차감", "2025-09-24 10:15:23", "실패"],
	    ["9", "임지민", "user_lim", "50,000원", "충전", "2025-09-23 22:05:18", "정상"],
	    ["10", "이서연", "user_lee", "7,500원", "차감", "2025-09-22 15:30:00", "정상"]
	];

    createGrid({
        targetId: '#member-payment-table',
        columns: paymentColumns,
        data: paymentData,
        pagination: { limit: 10 }
    });
});