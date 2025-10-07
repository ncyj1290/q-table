// 공통 모달
function openCommonModal(options) {
	$('#modal-title').text(options.title);
	$('#modal-body-content').html(options.bodyHtml);
	$('#modal-save-btn').text(options.saveButtonText || '저장');

	$('#modal-save-btn').off('click').on('click', options.onSave);

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
});