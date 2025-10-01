/* ====================================================== */
/* 매장 정보 작성 & 수정 페이지 JS */
/* ====================================================== */

$(function() {
	
	/* ===================================== */
	/* 매장 이미지 관련 변수들 */
	const MAX_PICTURE = 8;
	
	const $spList = $(".store_picture_layout");
	const $sp = $("#store_picture_input");
	
	/* 재료 정보 담는 레이아웃 */
	const $ingList = $(".ingredient-layout");
	
	/* 재료 정보 담는 레이아웃 */
	const $menuList = $(".menu-layout");

	/* ==================================== */
	/* Handlers */
	/* ==================================== */
	/* 숫자가 아닌 문자 제거 함수 */
	function onlyNumHandler($el){
		let inputVal =  $el.val();
		$el.val(inputVal.replace(/[^-0-9]/g, ""));
	}
	

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
	/* 매장 전화번호 */
	/* ==================================== */
	/* 숫자 제외 입력 거부 */
	$("#store_phone").on("keyup", function(){
		onlyNumHandler($(this));
	});
	
	/* ==================================== */
	/* 매장 계좌번호 */
	/* ==================================== */
	/* 숫자 제외 입력 거부 */
	$("#store_account_number").on("keyup", function(){
		onlyNumHandler($(this));
	});
	
	/* ==================================== */
	/* 총 좌석 수 */
	/* ==================================== */
	/* 숫자 제외 입력 거부 */
	$("#total_seat")	.on("keyup", function(){
		onlyNumHandler($(this));
	});

	/* ==================================== */
	/* 예약금 */
	/* ==================================== */
	/* 숫자 제외 입력 거부 */
	$("#reservation_deposit").on("keyup", function(){
		onlyNumHandler($(this));
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
	
	
	/* ==================================== */
	/* 식자재 정보 */
	/* ==================================== */
	/* 식자재 추가 버튼 */
	$("#ing_upload").on("click", function(){
		
		const htmlCode = $(
		
			`
			<div class="ingredient-layout-row">			
				<div class="d-flex flex-row justify-content-center align-item-center">
					<i class="bi bi-trash fs-4 ing-remove-bt"></i>
				</div>
				
				<div class="ingredient-element">
					<div class="ingredient-element-row">
						<label class="ingredient-label"><b>재료명</b></label>
						<input type="text" class="component-write" placeholder="재료 명을 입력하세요.">	
					</div>
					
					<div class="ingredient-element-row">
						<label class="ingredient-label"><b>원산지</b></label>
						<input type="text" class="component-write" placeholder="재료의 원산지를 입력하세요.">	
					</div>
					
					<div class="ingredient-element-row">
						<label class="ingredient-label"><b>알레르기 정보</b></label>
						<input type="text" class="component-write" placeholder="해당 재료의 알레르기 정보를 입력하세요.">	
					</div>
				</div>
			</div>
		`)

		$ingList.append(htmlCode);
				
	});
	
	/* 식자재 삭제 */
	$ingList.on("click", ".ing-remove-bt", function(){
		const $element = $(this).closest(".ingredient-layout-row");
		$element.remove();
	});
	
	
	/* ==================================== */
	/* 매장 메뉴 */
	/* ==================================== */
	/* 매장 메뉴 업로드 버튼 */
	$("#menu_upload").on("click", function(){
			
		const htmlCode = $(
		
			`
			<div class="menu-layout-row">
									
				<div class="d-flex flex-row justify-content-center align-item-center">
					<i class="bi bi-trash fs-4 menu-remove-bt"></i>
				</div>
				
				<div class="d-flex flex-row align-items-center gap-3 menu-border">
					
					<div class="d-flex flex-column align-items-center gap-3">
						<img src="icons/icon_store_profile.png" class="menu-image-size">
						<button class="positive-button">사진 업로드</button>
					</div>
					
					<div class="menu-element">
						<div class="menu-element-row">
							<label class="menu-label"><b>음식명</b></label>
							<input type="text" class="component-write" placeholder="재료 명을 입력하세요.">	
						</div>
						
						<div class="menu-element-row">
							<label class="menu-label"><b>가격</b></label>
							<input type="text" class="component-write" placeholder="재료의 원산지를 입력하세요.">	
						</div>
						
						<div class="menu-element-row">
							<label class="menu-label"><b>설명</b></label>
							<textarea type="text" class="component-write menu-description-size"></textarea>
						</div>
						
						<div class="menu-element-row">
							<label class="menu-label"><b>중량</b></label>
							<input type="text" class="component-write menu-weight-size" placeholder="단위: (g)">
							<b>(g)</b>
						</div>
	
					</div>
	
				</div>
	
			</div>
		`)
	
		$menuList.append(htmlCode);	
	});
	
	/* 메뉴 정보 삭제 */
	$menuList.on("click", ".menu-remove-bt", function(){
		const $element = $(this).closest(".menu-layout-row");
		$element.remove();
	});
	
	
	/* ==================================== */
	/* 검사 로직 모음 */
	/* ==================================== */
	
	/* 임시 검사 버튼 */
	$("#test_bt").on("click", function(){
		checkBasicData();
	});
	
	
	/* 입력란 공백 검사 */
	function checkBasicData(){
		
		const $basicComponent = $(".component-write");
		console.log("Check Basic Components (Widget): " + $basicComponent);
		
		/* 각 입력 요소들 순회하며 Null 검사 */
		for(var element of $basicComponent){
			
			const $el = $(element);
			let isEmpty = $el.val() == null || $el.val() == "";
			
			/* 입력란 공백이면 Alert + Focus And Return */
			if(isEmpty){
				alert("빈칸 채워라");
				$el.focus();
				return;
			}
		}

	}
	

});