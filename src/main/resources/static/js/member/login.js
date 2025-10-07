$(function() {
	console.log('로그인 js 연동');
	
	// 회원 유형 탭 전환
	$('.login-tabs').on('click', '.tab', typeChange);
	moveUnderline($('.active'));//기본 선택 
	
	// 로그인 버튼 클릭시 
	$('form').on('submit', login);
	
});
	

// ==================================
// ========  함수  선언 ================
// ==================================

//로그인 이벤트 처리 
function login(event) {
//	event.preventDefault(); 
	console.log(this);
	const id = $(this).find('#id').val();
	const passwd = $(this).find('#passwd').val();
	const memType = $(this).find('#memType').val();
	if (id == '' || passwd == '') alert('아이디와 비밀번호를 입력해주세요');
	// 아래에는 ajax 호출 로직 로그인 처리 해야함 	
}

// 회원 유형 탭 전환 함수 
function typeChange() {
	$('.tab').removeClass('active');
	$(this).toggleClass('active');
	moveUnderline($(this));
	if ($(this).text() == '매장 회원') {
		$('.social-login').hide();
		$('#memType').val('mtype_03');	
	}
	if ($(this).text() == '개인 회원') {
		$('.social-login').show();
		$('#memType').val('mtype_02');
	}
}

function moveUnderline(target) {
   if (!target || target.length === 0) return;
   // 타겟의 너비 계산
   const targetWidth = target.outerWidth();
   // 타겟의 위치 계산 (부모(.tabs) 기준)
   const targetLeft = target.position().left;
   // .underline 요소의 CSS를 변경
   $('.underline').css({
     'width': targetWidth + 'px',
     'transform': 'translateX(' + targetLeft + 'px)'
   });
 }
