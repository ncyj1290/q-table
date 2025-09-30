/* ====================================================== */
/* 매장 정보 작성/ 수정 페이지 JS */
/* ====================================================== */

$(function() {
	
	/* ===================================== */
	/* 매장 이미지 관련 변수들 */
	const MAX_PICTURE = 8;
	
	const $spList = $(".store_picture_layout");
	const $sp = $("#store_picture_input");
	
	
	
	
	/* ==================================== */
	/* 주소 입력 */
	/* ==================================== */
	/* DAUM ADDRESS API 호출 */
	$("#address_button").on("click", function(){
		new daum.Postcode({
			oncomplete: function(data) {
				/* 우편번호, 주소(1)에 자동 입력 */
				$("#post_code").val(data.zonecode);
				$("#address").val(data.roadAddress);
			}
		}).open();
	});
	
	/* ==================================== */
	/* 매장 운영 시간 */
	/* ==================================== */
	/* 24시간 체크 박스 -> 체크 시 Open, Close 시간 선택 불가 */
	$("#is_24hour").on("change", function(){
		
		let isChecked = $(this).is(":checked");
		
		/* 체크 박스 상태에 따라 Open, Close Select Box 제어 */
		$("#open_time").prop("disabled", isChecked);
		$("#close_time").prop("disabled", isChecked);
	});
	
	/* ==================================== */
	/* 휴일 선택 */
	/* ==================================== */
	/* 휴일 선택 버튼 */
	$(".holiday-button").on("click", function() {
		
		$(this).toggleClass("active"); 
		
		let selected = $(".holiday-button.active").map(function() {
			return $(this).val();
	    }).get();
		
		console.log(selected);
		
		$("#holidays").val(selected.join(",")); 
	});
	
	
	/* ==================================== */
	/* 매장 이미지 */
	/* ==================================== */
	/* 매장 이미지 업로드 버튼 */
	$("#sp_upload").on("click", function(){
		
		const totalPictures = $spList.find(".store_picture_element").length;
		
		/* 매장 이미지 갯수가 8개 이상 -> 업로드 거부 */
		if(totalPictures >= MAX_PICTURE){
			alert("매장 사진은 8개를 초과할 수 없습니다.");
			return;
		}
		
		/* 매장 이미지 담는 태그 코드 */
		const htmlCode = $(`
			<div class="store_picture_element">
				<i class="bi bi-trash fs-4 sp-remove-bt"></i>
				<label><b>Picture Names - UUID 12345678 - ${totalPictures + 1}번 사진</b></label>
			</div>
      	`);
		
  		$spList.append(htmlCode);
	});
	
	
	/* 매장 이미지 삭제 */
	$spList.on("click", ".sp-remove-bt", function(){
		const $element = $(this).closest(".store_picture_element");
		$element.remove();
	});
	
	
	/* ==================================== */
	/* 편의 시설 */
	/* ==================================== */
	/* 매장 편의 시설 선택 버튼 */
	$(".facility-button").on("click", function() {
		
		$(this).toggleClass("active"); 
		
		let selected = $(".facility-button.active").map(function() {
			return $(this).val();
	    }).get();
		
		console.log(selected);
		
		$("#store_facilities").val(selected.join(",")); 
	});
	
	
	
	

});