$(function() {
	console.log('dateModal js 연동');
	
	$('.search-box').on('click', '.date-area', showDateModal);
	// 모달닫기
	$(".close-btn").on("click", function() {
		hideDateModal();
	});
	//모달 외에 부분 눌렀을때
	$(window).on("click", function(e) {
	    if (e.target == $("#dateModal")[0]) {
			hideDateModal();
		}
	});	
});

function showDateModal() {
	$("#dateModal").fadeIn(200); 
}

function hideDateModal () {
	$("#dateModal").fadeOut(200);
}