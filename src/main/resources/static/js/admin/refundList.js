$(function() {
    const refundColumns = [
		{ name: 'jeongsan_idx', hidden: true },
		{ name: 'No.', width: '5%' },
		{ name: '매장명', width: '15%' },
		{ name: '회원 아이디', width: '15%' },
		{ name: '정산 금액', width: '10%' },
		{ name: '정산 요청 일시', width: '15%' },
		{ name: '정산 처리 일시', width: '15%' },
		{
			name: '처리상태',
			width: '10%',
			formatter: (cell) => {
				let badgeClass = '';
				switch (cell) {
					case '완료':
						badgeClass = 'status-badge-active';
						break;
					case '반려':
						badgeClass = 'status-badge-inactive';
						break;
					case '대기': // 새로운 주황색 상태 추가
						badgeClass = 'status-badge-dormant';
						break;
					default: // 그 외의 값이 들어올 경우
						badgeClass = 'status-badge-default';
				}

				return gridjs.html(`<span class="status-badge ${badgeClass}">${cell}</span>`);
			}
		},
        {
            name: '관리', width: '10%',
            width: '200px',
            formatter: (cell, row) => {
				const jeongsan_idx = row.cells[0].data;
				
				const detailButton = `<button class="management-button status-change-btn" data-idx="${jeongsan_idx}">정산</button>`;
                const deleteButton = `<button class="management-button delete-btn">삭제</button>`;
                return gridjs.html(detailButton + deleteButton);
            }
        }
    ];

	// AJAX 회원 목록 데이터 요청
	$.ajax({
		url: '/api/jeongsan',
		type: 'GET',
		success: function(response) {
			// Grid.js 형식
			const formatted_data = response.map((jeongsan, index) => {
				return [
					jeongsan.jeongsan_idx,
					index + 1,
					jeongsan.store_name,
					jeongsan.member_id,
					jeongsan.jeongsan_amount,
					jeongsan.requested_at,
					jeongsan.processed_at,
					jeongsan.calculate_result,
					null
				];
			});

			// 공통 함수 createGrid
			createGrid({
				targetId: '#refund-table', // 테이블을 표시할 div ID
				columns: refundColumns,
				data: formatted_data,
				pagination: { limit: 10 }
			});
		},
	});
	
	$('#refund-table').on('click', '.status-change-btn', function() {
		const jeongsan_idx = $(this).data('idx');		

		// AJAX로 매장의 현재 정보
		$.ajax({
			url: `/api/jeongsan/${jeongsan_idx}`,
			type: 'GET',
			success: function(JeongsanListVO) {
				const body_html = `
	                <div class="form-group">
	                    <label>매장명</label>
	                    <div class="readonly-input">${JeongsanListVO.store_name}</div>
	                </div>
	                <div class="form-group">
	                    <label>상태 변경</label>
	                    <div class="select-box" style="width: 100%;">
	                        <select id="modal-jeongsan-status-select">
	                            <option value="ctrt_01">완료</option>
	                            <option value="ctrt_02">대기</option>
	                            <option value="ctrt_03">반려</option>
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
						calculate_result: $('#modal-jeongsan-status-select').val(),
						rejection_reason: $('#modal-rejection-reason').val()
					};

					$.ajax({
						url: `/api/jeongsan/${jeongsan_idx}/status`,
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
					title: '정산 신청 처리',
					bodyHtml: body_html,
					saveButtonText: '저장',
					onSave: save_function
				});

				$('#modal-jeongsan-status-select').val(JeongsanListVO.calculate_result);
				$('#modal-rejection-reason').val(JeongsanListVO.rejection_reason);
			}
		});
	});
	
});