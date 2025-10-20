$(function() {
	let isUserIdAvailable = false;
	//아이디 유효성검사
	const idRegex = /^[a-zA-Z0-9]{4,20}$/; // 영문, 숫자 4~20자
	const msg = $("#userIdMsg");

	$("#userId").on("keyup", function() {
	  const userId = $(this).val().trim();
	
	  if (userId === "") {
	    msg.text("아이디를 입력해주세요.").removeClass("success").addClass("error");
	    return;
	  }

	  if (!idRegex.test(userId)) {
	    msg.text("4~20자의 영문 또는 숫자만 가능합니다.").removeClass("success").addClass("error");
	    return;
	  }

//	  msg.text("사용 가능한 형식입니다.").removeClass("error").addClass("success");
	  
	  $.ajax({
		url:"/checkMemberId",
		type:"GET",
		data:{memberId:userId},
		success:function(isAvailable){
			if (isAvailable === true || isAvailable === "true"){
				msg.text("사용 가능한 아이디입니다").removeClass("error").addClass("success");
				isUserIdAvailable = true;			
			} else {
				msg.text("이미 사용중인 아이디입니다").removeClass("success").addClass("error");
				isUserIdAvailable = false;
			}
		}
	  });	  
	});
		
	
	
	
	
	
	//비밀번호 8~16자리 영문 숫자 특수문자 포함 해야함
	const passwordRegex = /^(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,16}$/;
	const uerPwMsg = $("#uerPwMsg");
	const password = $("#password").val();
	const confirmPasswordInput = $("#confirmPassword");
	
	// 비밀번호 유효성검사
	$("#password").on("keyup",function(){
		const password = $(this).val().trim()
		
		if (password === ""){
			uerPwMsg.text("비밀번호를 입력해주세요").removeClass("success").addClass("error");
			return;
		}
		if (!passwordRegex.test(password)){
			uerPwMsg.text("8~16자, 영문/숫자/특수문자 포함").removeClass("success").addClass("error");
			return;
		}
		uerPwMsg.text("사용 가능한 비밀번호입니다").removeClass("error").addClass("success");
		
	});
	//비밀번호 일치 유효성검사
	confirmPasswordInput.on("keyup", function() {
	    const confirmPw = $(this).val();
	    const password = $("#password").val(); // 여기서 실시간 값 가져오기
	    
	    if (confirmPw === "") {
	        $("#confirmPwMsg").text("비밀번호 확인을 입력해주세요").removeClass("success").addClass("error");
	        return;
	    }

	    if (password === confirmPw) {
	        $("#confirmPwMsg").text("비밀번호가 일치합니다.").removeClass("error").addClass("success");
	    } else {
	        $("#confirmPwMsg").text("비밀번호가 일치하지 않습니다.").removeClass("success").addClass("error");
	    }
	});

	
	
	
	
	
	
	// 전체동의 체크 시 하위 체크박스 동기화
	$("#selectAll").change(function() {
		$(".component-checkbox").not(this).prop("checked", $(this).is(":checked"));
	});

	// 하위 체크박스 변화 시 전체동의 체크 반영
	$(".component-checkbox").not("#selectAll").change(function() {
		const allChecked = $(".component-checkbox").not("#selectAll").length === $(".component-checkbox.required:checked").length;
		$("#selectAll").prop("checked", allChecked && $(".component-checkbox").not("#selectAll").length === $(".component-checkbox").not("#selectAll:checked").length);
	});
	
	// 회원가입 폼 제출
	$('#signupForm').on('submit', function(e) {
		e.preventDefault();
		const memberType = $("#memberType").val();
		const businessRegNo = $('#businessRegNo').val().trim();
		const userId = $('#userId').val().trim();
		const password = $('#password').val().trim();
		const confirmPassword = $('#confirmPassword').val().trim();
		const userName = $('#userName').val().trim();
		const gender = $('#gender').val();
		const birthDate = $('#birthDate').val().trim();
		const addressPostcode = $('#addressPostcode').val().trim();
		const addressDetail = $('#addressDetail').val().trim();
		const email = $('#email').val().trim();
		const emailVerification = $('#emailVerification').val().trim();
		const businessInput = document.getElementById('businessRegNo');
		// 기존 필드 체크
		if (memberType == "mtype_03" && !businessRegNo) {
			alert('사업자등록번호를 입력해주세요.');
			return;
		}
		//일반회원일때 사업자번호등록 값 안들어감
		if(memberType === 'mtype_02') {
		    businessInput.value = null;
		    businessInput.removeAttribute('name');
		    businessInput.style.display = 'none';
		}
		if (!userId) { alert('아이디를 입력해주세요.'); return; }
		if (!password) { alert('비밀번호를 입력해주세요.'); return; }
		if (!confirmPassword) { alert('비밀번호 확인을 입력해주세요.'); return; }
		if (password !== confirmPassword) { alert('비밀번호가 일치하지 않습니다.'); return; }
		if (!userName) { alert('이름을 입력해주세요.'); return; }
		if (!gender) { alert('성별을 선택해주세요.'); return; }
		if (!birthDate) { alert('생년월일을 입력해주세요.'); return; }
		if (!addressPostcode) { alert('우편번호를 입력해주세요.'); return; }
		if (!addressDetail) { alert('상세주소를 입력해주세요.'); return; }
		if (!email) { alert('이메일을 입력해주세요.'); return; }
		if (!emailVerification) { alert('이메일 인증번호를 입력해주세요.'); return; }
		// 약관 체크 확인
		if (!isUserIdAvailable) {
		    alert("사용할 수 없는 아이디입니다. 다른 아이디를 입력해주세요.");
		    return;
		}
		
		let allRequiredChecked = true;
		$(".component-checkbox.required").each(function() {
			if (!$(this).is(":checked")) {
				allRequiredChecked = false;
			}
		});

		if (!allRequiredChecked) {
			alert("필수 약관에 모두 동의해주세요.");
			return;
		}
		if(!emailVerified) {
		    alert("이메일 인증이 완료되어야 회원가입이 가능합니다.");
		    return;
		}
		
		this.submit();
	});
});
$(document).ready(function() {
    // 초기 상태: 개인회원가입 기준
    const personalCode = (MEMBER_TYPE_CODES?.find(c => c.code.startsWith('mtype_02'))?.code) || 'mtype_02';
    const storeCode = (MEMBER_TYPE_CODES?.find(c => c.code.startsWith('mtype_03'))?.code) || 'mtype_03';

    $(".form-group.business").hide();  // 사업자 등록번호 숨김
    $(".addresshide").show();           // 주소 영역 보임
    $("#personalBtn").addClass("active"); // 초기 선택된 버튼 표시
    $("#memberType").val(personalCode);  // 초기 memberType 값 설정

    // 버튼 클릭 이벤트 공통 함수
    function toggleMemberType(isPersonal) {
        if (isPersonal) {
            $("#personalBtn").addClass("active");
            $("#storeBtn").removeClass("active");
            $(".form-group.business").hide();
            $("#memberType").val(personalCode);
        } else {
            $("#storeBtn").addClass("active");
            $("#personalBtn").removeClass("active");
            $(".form-group.business").show();
            $("#memberType").val(storeCode);
        }
    }

    // 이벤트 바인딩
    $("#personalBtn").click(() => toggleMemberType(true));
    $("#storeBtn").click(() => toggleMemberType(false));
});
// Daum주소 API로 불러오기
function execDaumPostcode() {
	new daum.Postcode({
		oncomplete: function(data) {
			// 우편번호
			$('#addressPostcode').val(data.zonecode);

			// 주소
			$('#address').val(data.roadAddress); // 도로명 주소
		}
	}).open();
}

function showTerms() {
    const terms = document.getElementById("terms");
    // 토글 기능: 보이면 숨기고, 숨겨져 있으면 보이게
    if (terms.style.display === "none") {
        terms.style.display = "block";
    } else {
        terms.style.display = "none";
    }
}
$(document).ready(function() {
    $(".view-details").click(function() {
        const agreeBox = $(this).parent().next(".agree");
        const btn = $(this);

        agreeBox.slideToggle(300, function() { // 300ms 슬라이드
            if (agreeBox.is(":visible")) {
                btn.text("간략히 보기");   // 열렸을 때
            } else {
                btn.text("자세히 보기"); // 닫혔을 때
            }
        });
    });
});
document.addEventListener("DOMContentLoaded", function() {
    const birthInput = document.getElementById("birthDate");
    const calendarIcon = document.querySelector(".birth-calendar-icon");

    const fp = flatpickr(birthInput, {
		locale: "ko",
        dateFormat: "Y-m-d",
        allowInput: true,
		maxDate: "today"
    });

    calendarIcon.addEventListener("click", () => {
        fp.open();
    });
});

$(function(){
  $("#emailsand").click(function(){
    $.ajax({
      type: "POST",  
      url: "/send",
      data: { email: $("#email").val() }, 
      success: function(result){
        alert(result);
      },
      error: function(){
        alert("메일 전송 실패!");
      }
    });
  });
});

let emailVerified = false; // 인증 여부 저장

$(function(){
    $("#verifyBtn").click(function(){
        $.ajax({
            type: "POST",
            url: "/verify",
            data: { emailVerification: $("#emailVerification").val() },
            success: function(result){
                alert(result); // 인증 성공/실패 메시지
                if(result.includes("인증성공")) {
                    emailVerified = true; // ✅ 인증 성공 시 플래그 true
                } else {
                    emailVerified = false;
                }
            },
            error: function(xhr){
                alert("인증 실패! (" + xhr.status + ")");
                emailVerified = false;
            }
        });
    });
});