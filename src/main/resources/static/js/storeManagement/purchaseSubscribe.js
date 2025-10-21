$(function(){
	
	/* 그 뭐냐 그..... ㅏㅏㅏㅏㅏ 구독권 선택하는 버튼들 */
	const $sButton = $(".subscribe-layout");
	
	/* 구독권 선택 버튼 리스너 */
	$sButton.on("click", function(){
		
		/* 버튼 Toggle */
		$sButton.not(this).removeClass('active');
		$(this).toggleClass('active');
		
		/* 선택한 날짜 안내 문구 생성 및 적용 */
		let $choiceDate = $(this).find(".subscribe-date").text();	
		let msg = (!$(".active").length==0)? $choiceDate + "을 선택하셨습니다." : "선택된 구독권이 존재하지 않습니다";
		$("#choice_info").text(msg);
	});
	
	/* 구매 버튼 */
	$(".purchase-button").on("click", function(){
		
		/* 선택한 구독권이 없으면 결제 거부 */
		if($(".active").length==0){
			alert("선택된 구독권이 존재하지 않습니다!"); 
			return;
		}
		
		/* 구독권 가격 */
		let cost = $(".active").find(".data-info").data("price");
		let plus_date = $(".active").find(".data-info").data("date");
		
		/* 결제 전 마지막 알림 */
		let choiceDate = $(".active").find(".subscribe-date").text();
		
		/* 사용자 마지막 의사 확인 */
		if(confirm(choiceDate + "을 구매하시겠습니까?")){
			
			/* 로딩 화면 출력 */
			showLoader();
			
			$.ajax({
				url: "/purchase_subscribe_processing",
				type: "post",
				dataType: "json",
				data:{
					cost: cost,
					plus_date: plus_date
				},
				
				success: function(res){
					hideLoader();
					alert(res.msg);
					location.href="store_management_main";
				},
				
				error: function(){
					hideLoader();
					alert("결제 도중 문제가 발생했습니다. 잠시 후 다시 이용해주세요.");
					localtion.reload(true);
				},
	
			});	
		}
	});
	
	/* 로딩 화면 출력 함수 */
	function showLoader(){
	  $('#app-loader').addClass('show').attr('aria-hidden', 'false');
	  $('body').addClass('no-scroll').attr('aria-busy','true');
	}
	
	/* 로딩 화면 끄는 함수 */
	function hideLoader(){
	  $('#app-loader').removeClass('show').attr('aria-hidden', 'true');
	  $('body').removeClass('no-scroll').removeAttr('aria-busy');
	}
	
});