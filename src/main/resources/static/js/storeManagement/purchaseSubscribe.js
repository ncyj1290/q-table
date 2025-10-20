$(function(){
	
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
		
		/* 결제 전 마지막 알림 */
		let choiceDate = $(".active").find(".subscribe-date").text();
		confirm(choiceDate + "을 구매하시겠습니까?");
		
		$('#app-loader').addClass('show').attr('aria-hidden', 'false');
		$('body').addClass('no-scroll').attr('aria-busy','true');
		
//		$ajax({
//			url: "",
//			type: "post",
//			data:{
//				
//				
//				
//			}
//		});
		
		
		
	});
	
});