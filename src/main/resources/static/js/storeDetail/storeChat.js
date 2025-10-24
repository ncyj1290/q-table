// ===================================
// 변수 선언
// ===================================
// 일반 변수

$(function() {
	const $chatButton = $('#chat-btn');

	// URL에서 store_idx 가져오기
	const urlParams = new URLSearchParams(window.location.search);
	const storeIdx = urlParams.get('store_idx');

	// ===================================
	// 이벤트 리스너
	// ===================================
	$chatButton.on('click', function(){
		$.ajax({
			url : '/api/chat/room/insert',
			type : 'POST',
			data : {store_idx : storeIdx},
			success : function(res){
				console.log(res);
				location.href = '/chat';
			}
			
		})
	});
	// ===================================
	// 함수 정의
	// ===================================
});