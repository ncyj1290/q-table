$(function() {
    const comcodeColumns = [
		{ name: 'common_idx', hidden: true },
		{ name: 'No.', width: '5%' },
		{ name: '코드그룹 ID', width: '8%' },
		{ name: '코드그룹 명', width: '15%' },
		{ name: '공통코드 ID', width: '7%' },
		{ name: '공통코드 명', width: '7%' },
		{ name: '공통코드 설명', width: '20%' },
        {
            name: '관리', width: '25%',
            width: '150px',
            formatter: (cell, row) => {
				
				const common_idx = row.cells[0].data;
				
                const detailButton = `<button class="management-button status-change-btn" data-idx=${common_idx}>상세보기</button>`;
                const deleteButton = `<button class="management-button delete-btn" data-idx=${common_idx}>삭제</button>`;
                return gridjs.html(detailButton + deleteButton);
            }
        }
    ];

	// AJAX 회원 목록 데이터 요청
	$.ajax({
		url: '/api/commoncode',
		type: 'GET',
		success: function(response) {
			// Grid.js 형식
			const formatted_data = response.map((commoncode, index) => {
				return [
					commoncode.common_idx,
					index + 1,
					commoncode.group_code,
					commoncode.group_desc,
					commoncode.code,
					commoncode.code_label,
					commoncode.code_desc,
					null
				];
			});

			// 공통 함수 createGrid
			createGrid({
				targetId: '#comcode-table', // 테이블을 표시할 div ID
				columns: comcodeColumns,
				data: formatted_data,
				pagination: { limit: 10 }
			});
		},
	});
	
	$('#comcode-table').on('click', '.status-change-btn', function() {
		const common_idx = $(this).data('idx');

		$.ajax({
			url: `/api/commoncode/${common_idx}`,
			type: 'GET',
			success: function(CommonCodeVO) {

				console.log(CommonCodeVO);

				const body_html = `
	                <div class="form-group">
	                    <label>코드그룹 ID</label>
	                    <input type="text" class="component-write" value="${CommonCodeVO.group_code}" readonly>
	                </div>
					<div class="form-group">
					    <label>코드그룹 명</label>
					    <input type="text" class="component-write" value="${CommonCodeVO.group_desc}" readonly>
					</div>
					<div class="form-group">
					    <label>공통코드 ID</label>
					    <input type="text" class="component-write" id="modal-code" value="${CommonCodeVO.code}">
					</div>
					<div class="form-group">
					    <label>공통코드 명</label>
					    <input type="text" class="component-write" id="modal-code-label" value="${CommonCodeVO.code_label}">
					</div>
					<div class="form-group">
					    <label>공통코드 설명</label>
					    <input type="text" class="component-write" id="modal-code-desc" value="${CommonCodeVO.code_desc}">
					</div>
	            `;

				const save_function = function() {
					const update_data = {
						code: $('#modal-code').val(),
						code_label: $('#modal-code-label').val(),
						code_desc: $('#modal-code-desc').val()
					};

					$.ajax({
						url: `/api/commoncode/${common_idx}/update`,
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
					title: '공통코드 수정',
					bodyHtml: body_html,
					saveButtonText: '저장',
					onSave: save_function
				});

			}
		});
	});
	
	// 삭제 버튼 클릭 이벤트
	$('#comcode-table').on('click', '.delete-btn', function() {

		const common_idx = $(this).data('idx');

		if (confirm(`정말로 삭제하시겠습니까?`)) {

			// 확인을 눌렀을 때 AJAX 코드 실행
			$.ajax({
				url: `/api/commoncode/${common_idx}`,
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