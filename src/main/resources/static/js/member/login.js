$(function() {
	console.log('로그인 js 연동');
	
	// 회원 유형 탭 전환
	$('.login-tabs').on('click', '.tab', typeChange);
	moveUnderline($('.active'));//기본 선택 
	
	// 로그인 버튼 클릭시 
	$('form').on('submit', login);
	
	//아이디 기억하기 
	// 최초 로그인 : 사용자가 기억하기 체크박스 클릭시, 쿠키를 셋팅 해주는 로직 
	// 재 로그인 : 사용자가 쿠기를 들고 있는 지 확인 한후 쿠키에서 아이디 들고오기 
	// 체크박스 해제 : 체크 박스 해제 된것을 보고 쿠기 지우기 
	const savedIdInCookie = Cookies.get('savedId');
	if(savedIdInCookie) {
		$(this).find('#id').val(savedIdInCookie);
		$('#save-id').prop('checked', true);
	}
});
	

// ==================================
// ========  함수  선언 ================
// ==================================


//쿠키 지우기 
function deleteCookie() {
	Cookies.remove('savedId');
}

//쿠키 셋팅 
function setCookie(id) {
	Cookies.set('savedId', id, { expires: 365 });
}

//로그인 이벤트 처리 
function login(event) {
	event.preventDefault(); 
	const id = $(this).find('#id').val();
	const passwd = $(this).find('#passwd').val();
	const memType = $(this).find('#memType').val();
	const isSavedId = $('#save-id').is(":checked");
	
	//아이디 기억하기 체크 유무에 따라 쿠키 생성및 삭제 
	if(isSavedId) setCookie(id);
	if(!isSavedId) deleteCookie();
	
	if (id == '' || passwd == '') alert('아이디와 비밀번호를 입력해주세요');
	// 아래에는 ajax 호출 로직 로그인 처리 해야함 
	
	$.ajax({
		url:"/loginPro",
		type: 'POST',
		data: { 
            id: id,
            passwd: passwd
        },
		dataType: "json",
		success : function(res) {
			window.location.href = res.redirectUrl;
		},
		error: function(jqXHR, textStatus, errorThrown) {
			if (jqXHR.responseJSON && jqXHR.responseJSON.message) {
               alert(jqXHR.responseJSON.message);
           	} else {
				alert('로그인 요청중 오류가 발생했습니다.');
				console.log(jqXHR);
           	}
		}
		
	})	
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
