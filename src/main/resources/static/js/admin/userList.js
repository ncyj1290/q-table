$(function() {
    const userColumns = [
		{ name: 'No.', width: '5%' },
		{ name: '회원 이름', width: '10%' },
		{ name: '회원 아이디', width: '10%' },
		{ name: '회원 이메일', width: '15%' },
		{ name: '가입일', width: '10%' },
        {
            name: '회원상태', width: '5%',
            formatter: (cell) => {
                // cell 값(예: "정상")에 따라 다른 클래스를 가진 <span>을 반환
                let badgeClass = cell === '정상' ? 'status-badge-active' : 'status-badge-inactive';
                return gridjs.html(`<span class="status-badge ${badgeClass}">${cell}</span>`);
            }
        },
        {
            name: '관리', width: '45%',
            width: '250px',
            formatter: (cell, row) => {
                // common.css의 버튼과 유사하지만, 크기가 작은 버튼을 생성
                const detailButton = `<button class="positive-button">상세보기</button>`;
                const statusButton = `<button class="positive-button">회원상태변경</button>`;
                const deleteButton = `<button class="positive-button">삭제</button>`;
                return gridjs.html(detailButton + statusButton + deleteButton);
            }
        }
    ];

    const userData = [
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
        targetId: '#user-table',
        columns: userColumns,
        data: userData,
        pagination: { limit: 10 }
    });
});