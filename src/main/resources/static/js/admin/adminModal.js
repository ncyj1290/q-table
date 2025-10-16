// 공통 모달
function openCommonModal(options) {
	$('#modal-title').text(options.title);
	$('#modal-body-content').html(options.bodyHtml);
	$('#modal-save-btn').text(options.saveButtonText || '저장');

	$('#modal-save-btn').off('click').on('click', options.onSave);

	$('#common-modal').addClass('active');
}

function openCommonConfirmModal(options) {
	// 1. 전달받은 옵션으로 모달의 내용물을 채웁니다.
	$('#modal-title').text(options.title);
	$('#modal-body-content').html(options.bodyHtml);
	$('#modal-save-btn').text('삭제'); // 저장 버튼 텍스트를 '삭제'로 고정

	// 2. '삭제' 버튼의 기존 이벤트를 제거하고, 새로 받은 onConfirm 함수를 등록합니다.
	$('#modal-save-btn').off('click').on('click', options.onConfirm);

	// 3. 모달을 화면에 보여줍니다.
	$('#common-modal').addClass('active');
}


// --- 모달 공통 이벤트 핸들러 ---
$(function() {

	// 취소 버튼 클릭 시 모달 닫기
	$('#common-modal').on('click', '#modal-close-btn', function() {
		$('#common-modal').removeClass('active');
	});

	// 모달 바깥의 어두운 영역을 클릭했을 때 닫기
	$('#common-modal').on('click', function(e) {
		if (e.target === this) {
			$('#common-modal').removeClass('active');
		}
	});

	// --- 관리자 계정 생성 전용 모달 로직 ---

	// '관리자 계정 생성' 버튼(#status-change-btn2) 클릭 시
	$('#status-change-btn2').on('click', function() {
		// '관리자 계정 생성' 모달(#status-change-modal2)을 직접 엽니다.
		$('#status-change-modal2').addClass('active');
	});

	// '관리자 계정 생성' 모달의 '취소' 버튼(#modal-close-btn2) 클릭 시
	$('#modal-close-btn2').on('click', function() {
		$('#status-change-modal2').removeClass('active');
	});

	// '관리자 계정 생성' 모달의 바깥 영역 클릭 시
	$('#status-change-modal2').on('click', function(e) {
		if (e.target === this) {
			$(this).removeClass('active');
		}
	});
});