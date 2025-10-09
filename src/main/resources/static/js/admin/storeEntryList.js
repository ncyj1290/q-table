$(function() {
	// Grid.js 컬럼
	const entryColumns = [
		{ name: 'No.', width: '5%' },
		{ name: '매장 이름', width: '13%' },
		{ name: '매장 아이디', width: '10%' },
		{ name: '신청일', width: '7%' },
		{ name: '처리일', width: '7%' },
		{
			name: '처리상태',
			width: '10%',
			formatter: (cell) => {
				let badgeClass = '';
				switch (cell) {
					case '승인':
						badgeClass = 'status-badge-active';
						break;
					case '거부':
						badgeClass = 'status-badge-inactive';
						break;
					case '보류': // 새로운 주황색 상태 추가
						badgeClass = 'status-badge-dormant';
						break;
					default: // 그 외의 값이 들어올 경우
						badgeClass = 'status-badge-default';
				}

				return gridjs.html(`<span class="status-badge ${badgeClass}">${cell}</span>`);
			}
		},
		{
			name: '관리', width: '42%',
			width: '300px',
			formatter: (cell, row) => {

				// row.cells 배열에서 필요한 데이터를 먼저 꺼내서 변수에 담아야 합니다.
				const member_idx = row.cells[0].data; // 'No.' 컬럼
				const store_idx = row.cells[0].data;
				const member_id = row.cells[2].data;  // '매장 아이디' 컬럼

				const detail_button = `<a href="/admin_detail/${member_idx}" class="management-button">상세보기</a>`;
				const status_button = `<button class="management-button approve-btn" data-idx="${store_idx}" data-id="${member_id}">입점 신청 관리</button>`;
				const delete_button = `<button class="management-button delete-btn" data-idx="${member_idx}" data-id="${member_id}">삭제</button>`;
				return gridjs.html(detail_button + status_button + delete_button);
			}
		}
	];

	// AJAX 회원 목록 데이터 요청
	$.ajax({
		url: '/api/stores/entry',
		type: 'GET',
		success: function(response) {
			// Grid.js 형식
			const formatted_data = response.map(store => {
				return [
					store.store_idx,
					store.store_name,
					store.member_id,
					store.applied_at ? store.applied_at.substring(0, 10) : '-',
					store.processed_at ? store.processed_at.substring(0, 10) : '-',
					store.store_status,
					null
				];
			});

			// 공통 함수 createGrid
			createGrid({
				targetId: '#store-entry-table', // 테이블을 표시할 div ID
				columns: entryColumns,
				data: formatted_data,
				pagination: { limit: 10 }
			});
		},
	});

	$(document).on('click', '.approve-btn', function() {
		const store_idx = $(this).data('idx');

		// AJAX로 매장의 현재 정보
		$.ajax({
			url: `/api/stores/${store_idx}`,
			type: 'GET',
			success: function(storeVO) {
				const body_html = `
	                <div class="form-group">
	                    <label>매장명</label>
	                    <div class="readonly-input">${storeVO.store_name}</div>
	                </div>
	                <div class="form-group">
	                    <label>상태 변경</label>
	                    <div class="select-box" style="width: 100%;">
	                        <select id="modal-store-status-select">
	                            <option value="srst_01">승인</option>
	                            <option value="srst_02">보류</option>
	                            <option value="srst_03">거부</option>
	                        </select>
	                    </div>
	                </div>
	                <div class="form-group">
	                    <label>거부 사유</label>
	                    <textarea id="modal-rejection-reason" class="reason-textarea"></textarea>
	                </div>
	            `;

				const save_function = function() {
					const update_data = {
						store_status: $('#modal-store-status-select').val(),
						rejection_reason: $('#modal-rejection-reason').val()
					};

					$.ajax({
						url: `/api/stores/${store_idx}/status`,
						type: 'POST',
						contentType: 'application/json',
						data: JSON.stringify(update_data),
						success: function(response) {
							alert("상태 변경에 성공했습니다.");
							$('#common-modal').removeClass('active');
							location.reload();
						}
					});
				};

				// 공통 모달 함수
				openCommonModal({
					title: '입점 신청 처리',
					bodyHtml: body_html,
					saveButtonText: '저장',
					onSave: save_function
				});

				$('#modal-store-status-select').val(storeVO.store_status);
				$('#modal-rejection-reason').val(storeVO.rejection_reason);
			}
		});
	});

	// 삭제 버튼 클릭 이벤트
	$('#store-table').on('click', '.delete-btn', function() {

		const store_idx = $(this).data('idx');

		if (confirm(`정말로 삭제하시겠습니까?`)) {

			// 확인을 눌렀을 때 AJAX 코드 실행
			$.ajax({
				url: `/api/stores/${store_idx}`,
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

});