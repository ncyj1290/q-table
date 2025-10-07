$(function() {
	// Grid.js 컬럼
	const member_columns = [
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
			name: '관리',
			width: '250px',
			formatter: (cell, row) => {
				const member_idx = row.cells[0].data;
				const member_id = row.cells[2].data;

				const detail_button = `<a href="/admin_detail/${member_idx}" class="management-button">상세보기</a>`;
				const status_button = `<button class="management-button status-change-btn" data-idx="${member_idx}" data-id="${member_id}">상태변경</button>`;
				const delete_button = `<button class="management-button delete-btn" data-idx="${member_idx}" data-id="${member_id}">삭제</button>`;

				return gridjs.html(detail_button + status_button + delete_button);
			}
		}
	];

	// AJAX 회원 목록 데이터 요청
	$.ajax({
		url: '/api/members',
		type: 'GET',
		success: function(response) {
			// Grid.js 형식
			const formatted_data = response.map(member => {
				return [
					member.member_idx,
					member.member_name,
					member.member_id,
					member.email,
					member.signup_date ? member.signup_date.substring(0, 10) : '-',
					member.member_status,
					null
				];
			});

			// 공통 함수 createGrid
			createGrid({
				targetId: '#member-table', // 테이블을 표시할 div ID
				columns: member_columns,
				data: formatted_data,
				pagination: { limit: 10 }
			});
		},
	});


	// 공통 모달을 호출하는 역할
	$('#member-table').on('click', '.status-change-btn', function() {
		const member_idx = $(this).data('idx');
		const member_id = $(this).data('id');

		// 모달에 들어갈 HTML 내용 정의
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

		// 공통 모달 함수 호출
		openCommonModal({
			title: '회원 상태 변경',
			bodyHtml: body_html,
			saveButtonText: '수정',
			onSave: save_function
		});
	});
});