$(function() {
	// 회원상태변경 버튼을 클릭했을 때
	$('.grid-wrapper').on('click', '.status-change-btn', function() {
		$('#status-change-modal').addClass('active');
	});

	// 취소 버튼을 클릭했을 때
	$('#modal-close-btn').on('click', function() {
		$('#status-change-modal').removeClass('active');
	});

	// 모달 바깥의 어두운 영역을 클릭했을 때
	$('#status-change-modal').on('click', function(e) {
		if (e.target === this) {
			$(this).removeClass('active');
		}
	});
	
	// 회원상태변경 버튼을 클릭했을 때
	$('.status-change-btn2').on('click', function() {
		$('#status-change-modal2').addClass('active');
	});

	// 취소 버튼을 클릭했을 때
	$('#modal-close-btn2').on('click', function() {
		$('#status-change-modal2').removeClass('active');
	});

	// 모달 바깥의 어두운 영역을 클릭했을 때
	$('#status-change-modal2').on('click', function(e) {
		if (e.target === this) {
			$(this).removeClass('active');
		}
	});
	
	// 삭제 버튼을 클릭했을 때
	$('.grid-wrapper').on('click', '.delete-btn', function() {
		if(confirm("삭제하시겠습니까?") == true){
			alert("삭제 완료");
		} else {
			return;
		}
	});
});