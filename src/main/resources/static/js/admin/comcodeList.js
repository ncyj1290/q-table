$(function() {
    const comcodeColumns = [
		{ name: 'No.', width: '5%' },
		{ name: '코드타입 ID', width: '10%' },
		{ name: '코드타입 명', width: '10%' },
		{ name: '공통코드 ID', width: '10%' },
		{ name: '공통코드 명', width: '10%' },
		{ name: '공통코드 설명', width: '20%' },
        {
            name: '관리', width: '25%',
            width: '150px',
            formatter: (cell, row) => {
                const detailButton = `<button class="management-button">상세보기</button>`;
                const deleteButton = `<button class="management-button">삭제</button>`;
                return gridjs.html(detailButton + deleteButton);
            }
        }
    ];

	const comcodeData = [
	    ["1", "UST", "회원상태", "UST01", "정상", "활성화된 정상 사용자"],
	    ["2", "UST", "회원상태", "UST02", "휴면", "장기 미접속으로 비활성화된 사용자"],
	    ["3", "UST", "회원상태", "UST03", "탈퇴", "서비스를 탈퇴한 사용자"],
	    ["4", "SST", "매장상태", "SST01", "영업중", "현재 정상 영업중인 매장"],
	    ["5", "SST", "매장상태", "SST02", "휴업", "임시 또는 정기 휴업중인 매장"],
	    ["6", "SST", "매장상태", "SST03", "폐업", "영업을 종료한 매장"],
	    ["7",- "PTY", "결제유형", "PTY01", "신용카드", "신용/체크카드를 통한 결제"],
	    ["8", "PTY", "결제유형", "PTY02", "계좌이체", "실시간 계좌이체를 통한 결제"],
	    ["9", "PTY", "결제유형", "PTY03", "포인트", "보유 포인트를 사용한 결제"],
	    ["10", "BTY", "게시판유형", "BTY01", "공지사항", "관리자가 작성하는 공지 게시판"]
	];

    createGrid({
        targetId: '#comcode-table',
        columns: comcodeColumns,
        data: comcodeData,
        pagination: { limit: 10 }
    });
});