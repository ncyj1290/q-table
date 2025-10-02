$(function() {

	//모달 열기
	$('.keywords').on('click', '.dashed-box', showModal);
	// 모달닫기
	$(".close-btn").on("click",hideModal);
	$(window).on("click", function(e) {
	    if (e.target == $("#myModal")[0]) hideModal();
	});	
	// 3. 내부 "확인" 버튼 클릭 이벤트: 닫기 버튼과 동일한 기능
	$("#apply-btn").on("click", function() {
	    hideModal();
	});

});

// 함수 선언
function showModal() {
	$("#myModal").fadeIn(200); 
}

function hideModal () {
	$("#myModal").fadeOut(200);
}