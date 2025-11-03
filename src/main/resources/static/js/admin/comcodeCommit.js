$(function() {
	
	let groupCodeOptionsHtml = ""; // 그룹 목록 option 태그 HTML을 저장할 변수

	    // 공통 코드 그룹 목록 가져오기 ajax
	    $.ajax({
	        url: '/api/common-code-groups',
	        type: 'GET',
	        async: false,
	        success: function(groups) {
	            if (groups && groups.length > 0) {
	                groups.forEach(group => {
	                    groupCodeOptionsHtml += `<option value="${group.group_code}">${group.group_code} : ${group.group_desc}</option>`;
	                });
	            }
	        },
	    });
	    
	    // --- 테이블 초기 행(첫 번째 행)에 셀렉트 박스 적용 ---
	    const firstRowFirstCell = $('.editable-table tbody tr:first-child td:first-child');
	    if (firstRowFirstCell.length) {
	        const initialSelectHtml = `
	            <select class="group-code-select" style="width: 100%; padding: 5px;">
	                <option value="">-- 그룹 선택 --</option>
	                ${groupCodeOptionsHtml}
	            </select>`;
	        firstRowFirstCell.html(initialSelectHtml); // 셀 내용을 select 태그로 교체
	    }


	    // --- 행 추가 버튼 클릭 이벤트 ---
	    $('#add-row-btn').on('click', function() {
	        const newRowHtml = `
	            <tr>
	                <td>
	                    <select class="group-code-select" style="width: 100%; padding: 5px;">
	                        <option value="">-- 그룹 선택 --</option>
	                        ${groupCodeOptionsHtml}
	                    </select>
	                </td>
	                <td><input type="text" placeholder="코드 명"></td>
	                <td><input type="text" placeholder="코드 라벨"></td>
					<td><input type="text" placeholder="코드 설명"></td>
	                <td><input type="text" placeholder="부모코드 ID"></td>
					<td><input type="text" placeholder="순서"></td>
	                <td><button class="negative-button">삭제</button></td> </tr>
	        `;
	        $('.editable-table tbody').append(newRowHtml);
	    });

    // 삭제 버튼 클릭 시
    $('.editable-table').on('click', '.negative-button', function() {
        $(this).closest('tr').remove();
    });
	
	// 공통코드 그룹 저장 버튼 클릭
	$('#save-group-btn').on('click', function() {
	        const group_data = {
	            group_code: $('#group-code').val(),
	            group_desc: $('#group-desc').val()
	        };

	        // 유효성 
	        if (!group_data.group_code || !group_data.group_desc) {
	            alert("그룹 이름과 그룹설명을 모두 입력해주세요.");
	            return;
	        }

			// 공통 코드 그룹 저장 ajax
	        $.ajax({
	            url: '/api/common-code-groups',
	            type: 'POST',
	            contentType: 'application/json',
	            data: JSON.stringify(group_data),
	            success: function(response) {
	                alert("그룹이 성공적으로 저장되었습니다.");
	                location.reload();
	            },
	            error: function(error) {
	                alert("그룹 저장에 실패했습니다.");
	                console.error(error.responseText);
	            }
	        });
	    });
		
		// --- 코드 저장 버튼 클릭 이벤트 ---
			$('#save-codes-btn').on('click', function() {
			    const codes_data = [];

			    $('.editable-table tbody tr').each(function() {
			        const $row = $(this);
			        const inputs = $row.find('input[type="text"]'); // 현재 행의 모든 텍스트 입력창
			        const row_data = {
			            group_code: $row.find('.group-code-select').val(), // 그룹 코드
			            code: inputs.eq(0).val(),              // 코드 명
			            code_label: inputs.eq(1).val(),        // 코드 라벨
						code_desc: inputs.eq(2).val(),        // 코드 설명
			            parent_code: inputs.eq(3).val() || null, // 부모코드 ID
						code_index: inputs.eq(4).val()
			        };
			        
			        // 유효성 검사
			        if (row_data.group_code && row_data.code && row_data.code_label) {
			            codes_data.push(row_data);
			        }
			    });

			    // 저장할 데이터가 없으면 사용자에게 알림
			    if (codes_data.length === 0) {
			        alert("저장할 코드가 없습니다. 내용을 입력해주세요.");
			        return;
			    }

			    // 공통 코드 저장 AJAX 요청
			    $.ajax({
			        url: '/api/comcode/common_codes',
			        type: 'POST',
			        contentType: 'application/json',
			        data: JSON.stringify(codes_data),
			        success: function(response) {
			            alert("코드가 성공적으로 저장되었습니다.");
			            location.reload(); // 성공 시 페이지 새로고침
			        },
			        error: function(error) {
			            alert("코드 저장에 실패했습니다.");
			            console.error(error.responseText);
			        }
			    });
			});
		
});