/* ====================================================== */
/* 매장 정보 작성 & 수정 페이지 JS */
/* ====================================================== */

$(function() {
	
	/* 프로필 이미지(?) 기본 경로 */
	const DEFAULT_PROFILE = 'icons/icon_store_profile.png';
	
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
//		$el.val(inputVal.replace(/[^-0-9]/g, ""));
		$el.val(inputVal.replace(/\D/g, ""));
	}

	
	/* 매장 프로필 이미지 표시 */
	$(document).on('change', '.store-profile-layout input[type="file"]', function () {
		
		const file = this.files && this.files[0];
		const $row = $(this).closest('.store-profile-layout');
		const $img = $row.find('img.store-profile');
		const $name = $row.find('.store-profile-name');
		
		if (!file) { 
			$img.attr('src', DEFAULT_IMG);
			return;
		}
		 const url = URL.createObjectURL(file);
			$img.attr('src', url).one('load', function () {
			URL.revokeObjectURL(url);
		});
		
		if ($name.length) $name.text(file.name);
	});
	
	
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
	$("#account_number").on("keyup", function(){
		onlyNumHandler($(this));
	});
	
	/* ==================================== */
	/* 총 좌석 수 */
	/* ==================================== */
	/* 숫자 제외 입력 거부 */
	$("#store_seat").on("keyup", function(){
		onlyNumHandler($(this));
	});

	/* ==================================== */
	/* 예약금 */
	/* ==================================== */
	/* 숫자 제외 입력 거부 */
	$("#deposit").on("keyup", function(){
		onlyNumHandler($(this));
	});
	
	/* ==================================== */
	/* 예약금 */
	/* ==================================== */
	/* 숫자 제외 입력 거부 */
	$("#price_avg").on("keyup", function(){
		onlyNumHandler($(this));
	});
	
	/* ==================================== */
	/* 매장 운영 시간 */
	/* ==================================== */
	
	/* 매장이 24시간이여서 그 뭐시기냐 체크 되어있으면 open, close 선택 불가하게 설정 -> 초기값 설정ㅇ */
	const is24hourCheck = $("#is_24hour").is(":checked") 
	$("#open_time").prop("disabled", is24hourCheck);
	$("#close_time").prop("disabled", is24hourCheck);
	
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
	/* 초기 렌더링 후 값 */
	let previousHoliday = $(".holiday-button.active").map(function() {
		return $(this).val();
    }).get();
	
	$("#holidays").val(previousHoliday.join(","));
	
	/* 휴일 선택 버튼 */
	$(".holiday-button").on("click", function() {
		
		$(this).toggleClass("active"); 
		
		let selected = $(".holiday-button.active").map(function() {
			return $(this).val();
	    }).get();
		
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
				<label class="positive-button">매장 사진 추가<input type="file" data-field="store_picture" accept=".jpg, .jpeg, .png" hidden></label>
				<span class="sp-name">선택된 파일 없음</span>
			</div>
      	`);
  		$spList.append(htmlCode);
		reindexSP();
	});
	
	
	/* 매장 사진 이름 표시 */
	$(document).on('change', '.store_picture_element input[type="file"]', function () {
		
		const file = this.files && this.files[0];
		const $row = $(this).closest('.store_picture_element');
//		const $img = $row.find('img.store-profile');
		const $name = $row.find('.sp-name');
		
//		if (!file) { 
//			$img.attr('src', DEFAULT_IMG);
//			return;
//		}
//		 const url = URL.createObjectURL(file);
//			$img.attr('src', url).one('load', function () {
//			URL.revokeObjectURL(url);
//		});
		
		if ($name.length) $name.text(file.name);
	});
	
	
	/* 매장 이미지 삭제 */
	$spList.on("click", ".sp-remove-bt", function(){
		const $element = $(this).closest(".store_picture_element");
		$element.remove();
		reindexSP();
	});
	
	
	/* 매장 사진 리인덱싱 */
	function reindexSP(){
		$spList.find(".store_picture_element").each(function(i){
			$(this).find("[data-field]").each(function(){
				const field = $(this).data("field");
				$(this).attr("name", `storePictureList[${i}].${field}`);
			});
		});
	}
		
	/* ==================================== */
	/* 편의 시설 (amenity) */
	/* ==================================== */
	let previousFacility = $(".facility-button.active").map(function() {
		return $(this).val();
    }).get();
			
	$("#store_facilities").val(previousFacility.join(",")); 
	
	/* 매장 편의 시설 선택 버튼 */
	$(".facility-button").on("click", function() {
		
		$(this).toggleClass("active"); 
		
		let selected = $(".facility-button.active").map(function() {
			return $(this).val();
	    }).get();
		
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
						<input type="text" data-field="ingredients_name" class="component-write" placeholder="재료 명을 입력하세요.">	
					</div>
					
					<div class="ingredient-element-row">
						<label class="ingredient-label"><b>원산지</b></label>
						<input type="text" data-field="ingredients_origin" class="component-write" placeholder="재료의 원산지를 입력하세요.">	
					</div>
					
					<div class="ingredient-element-row">
						<label class="ingredient-label"><b>알레르기 정보</b></label>
						<input type="text" data-field="allergy" class="component-write" placeholder="해당 재료의 알레르기 정보를 입력하세요.">	
					</div>
				</div>
			</div>
		`)

		$ingList.append(htmlCode);
		reindexIng();				
	});
	
	/* 식자재 삭제 */
	$ingList.on("click", ".ing-remove-bt", function(){
		const $element = $(this).closest(".ingredient-layout-row");
		$element.remove();
		reindexIng();
	});
	
	/* 매장 메뉴 목록 리인덱싱 */
	function reindexIng(){
		
		$ingList.find(".ingredient-layout-row").each(function(i){
			$(this).find("[data-field]").each(function(){
				const field = $(this).data("field");
				$(this).attr("name", `ingredientList[${i}].${field}`);
			});
		});
	}
	
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
						<img src="icons/icon_menu_profile.png" class="menu-image-size">
						<label class="positive-button">메뉴 사진 추가<input type="file" data-field="menu_picture" accept=".jpg, .jpeg, .png" hidden></label>
					</div>
					
					<div class="menu-element">
						<div class="menu-element-row">
							<label class="menu-label"><b>음식명</b></label>
							<input type="text" data-field="menu_name" class="component-write" placeholder="메뉴 명을 입력하세요.">	
						</div>
						
						<div class="menu-element-row">
							<label class="menu-label"><b>가격</b></label>
							<input type="text" data-field="price" class="component-write" placeholder="메뉴 가격을 작성하세요..">	
						</div>
						
						<div class="menu-element-row">
							<label class="menu-label"><b>설명</b></label>
							<textarea type="text" data-field="menu_content" class="component-write menu-description-size"></textarea>
						</div>
						
						<div class="menu-element-row">
							<label class="menu-label"><b>중량</b></label>
							<input type="text" data-field="menu_gram" class="component-write menu-weight-size" placeholder="단위: (g)">
							<b>(g)</b>
						</div>
					</div>
	
				</div>
	
			</div>
		`)
	
		$menuList.append(htmlCode);	
		reindexMenu()
	});
	
	/* 메뉴 이미지 업로드 시 => 표시 */
	$(document).on('change', '.menu-layout-row input[type="file"][data-field="menu_picture"]', function () {
		
		const file = this.files && this.files[0];
		const $row = $(this).closest('.menu-layout-row');
		const $img = $row.find('img.menu-image-size');
		
		if (!file) { 
			$img.attr('src', DEFAULT_IMG);
			return;
		}
		 const url = URL.createObjectURL(file);
			$img.attr('src', url).one('load', function () {
			URL.revokeObjectURL(url);
		});
	});
		

	/* 메뉴 정보 삭제 */
	$menuList.on("click", ".menu-remove-bt", function(){
		const $element = $(this).closest(".menu-layout-row");
		$element.remove();
		reindexMenu()
	});
	
	/* 매장 메뉴 목록 리인덱싱 */
	function reindexMenu(){
		
		$menuList.find(".menu-layout-row").each(function(i){
			$(this).find("[data-field]").each(function(){
				const field = $(this).data("field");
				$(this).attr("name", `menuList[${i}].${field}`);
			});
		});
	}
	
	
	/* 매장 메뉴판 이미지 이름 표시 */
	$(document).on('change', '.menu-board-layout input[type="file"]', function () {
		const file = this.files && this.files[0];
		const $row = $(this).closest('.menu-board-layout');
		const $name = $row.find('.menu-board-name');

		if ($name.length) $name.text(file.name);
	});
	
	
	
	/* ==================================== */
	/* Submit */
	/* ==================================== */
	/* Submit */
	$("#submit_bt").on("click", function(){

		if(!checkInputNull() || !checkPhoneNum()) return false;
		
		/* 빈 칸 없으면 저장 진행 */
		reindexSP();
		reindexMenu();
		reindexIng();
		
		const form = $(".write-store-layout");
		form.submit();
	});
	
	/* ==================================== */
	/* 검사 로직 모음 */
	/* ==================================== */
	/* 임시 검사 버튼 */
//	$("#test_bt").on("click", function(){
//		checkBasicData();
//	});
	
	/* 입력란 공백 검사 */
	function checkInputNull(){
		
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
				return false;
			}
		}
		
		/* 검사 통과 시 true 반환 */
		return true
	}
	
	/* 휴대전화 번호 글자 길이 */
	function checkPhoneNum(){
		
		const phoneRegex =  /^01[016-9]\d{8}$/;
		
		let $phone = $("#store_phone");
		let phoneNum = $phone.val()
		let result = true;
		
		if(!phoneRegex.test(phoneNum)){
			$phone.focus();
			alert("휴대폰 번호 11자리를 올바르게 작성했는지 확인하세요!");
			result = false;
		}	
		
		console.log("Check Phone Num Regex Result: " + result);
		return result;
	}

	/* ==================================== */

});