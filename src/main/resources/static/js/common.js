$(function(){
	// meta 태그에서 토큰과 헤더 이름 가져오기
	const token = $("meta[name='_csrf']").attr("content");
	const header = $("meta[name='_csrf_header']").attr("content");

	// jQuery의 모든 AJAX 요청에 자동으로 CSRF 헤더 추가
	$.ajaxSetup({
	    beforeSend: function(xhr) {
	        xhr.setRequestHeader(header, token);
	    }
	});
});

// ===================================
// AJAX 공통 에러 처리 함수
// ===================================

/**
 * AJAX 성공 응답 처리
 * @param {Object} response - 서버 응답 객체 {success: boolean, message: string, data: any}
 * @param {Function} onSuccess - 성공 시 실행할 콜백
 * @param {string} defaultErrorMsg - 기본 에러 메시지
 */
function handleAjaxSuccess(response, onSuccess, defaultErrorMsg = '요청에 실패했습니다.') {
	if (response && response.success) {
		onSuccess(response);
	} else {
		const errorMsg = response && response.message
			? response.message
			: defaultErrorMsg;
		alert(errorMsg);
	}
}

/**
 * AJAX 에러 처리
 * @param {Object} xhr - XMLHttpRequest 객체
 * @param {string} defaultErrorMsg - 기본 에러 메시지
 */
function handleAjaxError(xhr, defaultErrorMsg = '요청 처리 중 오류가 발생했습니다.') {
	console.error('AJAX Error:', xhr);

	// 서버에서 보낸 에러 메시지 파싱
	const errorMsg = xhr.responseJSON && xhr.responseJSON.message
		? xhr.responseJSON.message
		: defaultErrorMsg;

	alert(errorMsg);
}
