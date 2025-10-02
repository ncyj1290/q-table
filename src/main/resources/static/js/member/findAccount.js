$(function(){
	// id/pw 탭 전환 기능
	$('.tab').on('click', function(){
		$('.tab').removeClass('active');
		$(this).addClass('active');
	});
});