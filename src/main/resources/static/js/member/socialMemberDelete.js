$(function() {
	console.log('회원탈퇴 js 연동');
	
	$('#delete-btn').on('click', function(event){
		const isChecked = $('#agree').is(':checked');
		
		if(!isChecked) {
			event.preventDefault();
			alert('탈퇴 정책에 동의가 필요합니다.');
			return;
		}
		
		$.ajax({
			url:"/api/member_delete_social",
			type:"post",
			success: function(res) {
				alert(res);
				window.location.href = "/";
			},
			error: function(error) {
				alert('서버와 통신중 오류가 발생했습니다. 관리자에게 문의바랍니다.')
				console.log(error);
			}
			
		})
		
		
	});
});