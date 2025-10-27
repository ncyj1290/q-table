// ===================================
// 변수 선언
// ===================================

$(function(){
	// ===================================
	// DOM 캐싱
	// ===================================
	const userId = $("#user-name");
	const userEmail = $("#user-email");
	const verificationCode = $("#verification-code");
	const emailBtn = $("#email-send-btn");		
	const checkBtn = $("#check-btn");
	
	
	// ===================================
	// 이벤트 리스너
	// ===================================
	
	// id/pw 탭 전환 기능
	$('.tab').on('click', function(){
		$('.tab').removeClass('active');
		$(this).addClass('active');
	});
	
	emailBtn.on('click', function(){
		$.ajax({
			url: '/api/findId/email/send',
			type: 'POST',
			data: {user_id: userId.val(), user_email: userEmail.val()},
			success: function(res){
				
				if (res.success) {
					alert("이메일이 성공적으로 전송되었습니다.");
				} else {
					alert(res.message)
				}
				
			}
		});	
	});
	
	
	checkBtn.on('click', function(){
		
		
		
		$.ajax({
			url: '/api/findId/email/verify',
			type: 'POST',
			data: {
				verification_code: verificationCode.val(),
				user_email: userEmail.val()								
			},
			success: function(res){
				if (res.success){
					console.log("호출성공");
				}else{
					alert(res.message);					
				}
			},
			error: function(res){
				
			}
				
		});
	});
});