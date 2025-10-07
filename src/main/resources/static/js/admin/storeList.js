$(function() {
	// Grid.js 컬럼
    const storeColumns = [
		{ name: 'No.', width: '5%' },
		{ name: '매장 이름', width: '13%' },
		{ name: '매장 아이디', width: '10%' },
		{ name: '매장 이메일', width: '15%' },
		{ name: '가입일', width: '7%' },
        {
            name: '회원상태', width: '8%',
            formatter: (cell) => {
                let badgeClass = cell === '정상' ? 'status-badge-active' : 'status-badge-inactive';
                return gridjs.html(`<span class="status-badge ${badgeClass}">${cell}</span>`);
            }
        },
        {
            name: '관리', width: '42%',
            width: '300px',
            formatter: (cell, row) => {
				
				// row.cells 배열에서 필요한 데이터를 먼저 꺼내서 변수에 담아야 합니다.
				const member_idx = row.cells[0].data; // 'No.' 컬럼
				const member_id = row.cells[2].data;  // '매장 아이디' 컬럼
				
				const detail_button = `<a href="/admin_detail/${member_idx}" class="management-button">상세보기</a>`;
				const status_button = `<button class="management-button status-change-btn" data-idx="${member_idx}" data-id="${member_id}">상태변경</button>`;
				const review_button = `<a href="/store_detail_main" class="management-button">리뷰보기</a>`;
				const delete_button = `<button class="management-button delete-btn" data-idx="${member_idx}" data-id="${member_id}">삭제</button>`;
                return gridjs.html(detail_button + status_button + review_button + delete_button);
            }
        }
    ];

	// AJAX 회원 목록 데이터 요청
	$.ajax({
		url: '/api/stores',
		type: 'GET',
		success: function(response) {
			// Grid.js 형식
			const formatted_data = response.map(store => {
				return [
					store.member_idx,
					store.store_name,
					store.member_id,
					store.email,
					store.signup_date ? store.signup_date.substring(0, 10) : '-',
					store.member_status,
					null
				];
			});

			// 공통 함수 createGrid
			createGrid({
				targetId: '#store-table', // 테이블을 표시할 div ID
				columns: storeColumns,
				data: formatted_data,
				pagination: { limit: 10 }
			});
		},
	});
	
	$('.grid-wrapper').on('click', '.status-change-btn', function() {
		const member_idx = $(this).data('idx');
		const member_id = $(this).data('id');

		// AJAX 현재 회원 정보를 가져오기
		$.ajax({
			url: `/api/members/${member_idx}`,
			type: 'GET',
			success: function(memberVO) {

				const body_html = `
	                <div class="form-group">
	                    <label>변경 대상 아이디</label>
	                    <div class="readonly-input">${member_id}</div>
	                </div>
	                <div class="form-group">
	                    <label>회원 상태</label>
	                    <div class="select-box" style="width: 100%;">
	                        <select id="modal-status-select">
	                            <option value="mstat_01">정상</option>
	                            <option value="mstat_02">탈퇴</option>
	                        </select>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label>탈퇴 사유</label>
	                    <textarea id="modal-reason-textarea" class="reason-textarea" placeholder="탈퇴 처리 시 사유를 입력하세요."></textarea>
	                </div>
	            `;

				// 수정 버튼을 눌렀을 때 실행될 기능
				const save_function = function() {
					const update_data = {
						member_status: $('#modal-status-select').val(),
						leave_reason: $('#modal-reason-textarea').val()
					};

					$.ajax({
						url: `/api/members/${member_idx}/status`,
						type: 'POST',
						contentType: 'application/json',
						data: JSON.stringify(update_data),
						success: function(response) {
							alert("상태 변경에 성공했습니다.");
							$('#common-modal').removeClass('active');
							location.reload();
						},
						error: function(error) { alert("상태 변경에 실패했습니다."); }
					});
				};

				// 공통 모달 함수
				openCommonModal({
					title: '매장 회원 상태 변경',
					bodyHtml: body_html,
					saveButtonText: '수정',
					onSave: save_function
				});

				$('#modal-status-select').val(memberVO.member_status);
				$('#modal-reason-textarea').val(memberVO.leave_reason);
			},
			error: function() {
				alert("회원 정보를 불러오는 데 실패했습니다.");
			}
		});
	});
});

// 삭제 버튼 클릭 이벤트
$('#store-table').on('click', '.delete-btn', function() {

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

