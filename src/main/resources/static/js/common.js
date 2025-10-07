$(function(){
	console.log("제이쿼리 연결확인");	
	
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
