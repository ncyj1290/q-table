$(function() {
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
		// 값이 ''일경우  값을 null로 바꾸기 db저장할떄 유니크 속성이있을떄 ''허용 불가
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
		this.submit();
	});
});
$(document).ready(function() {
	// 초기 상태: 개인회원가입 기준
	$(".form-group.business").hide();  // 사업자 등록번호 숨김
	$(".addresshide").show();          // 주소 영역 보임
	$("#personalBtn").addClass("active"); // 초기 선택된 버튼 표시
	$("#memberType").val("mtype_02"); //초기 맴버 타입 값
	// 개인회원가입 클릭
	$("#personalBtn").click(function() {
		if (!$(this).hasClass("active")) {
			$(this).addClass("active");
			$("#storeBtn").removeClass("active");

			// 영역 전환 (토글)
			$(".form-group.business").hide();
			$(".addresshide").show();
			//개인 회원가입 탭일때 맴버 타입 값
			$("#memberType").val("mtype_02");
		}
	});

	// 매장회원가입 클릭
	$("#storeBtn").click(function() {
		if (!$(this).hasClass("active")) {
			$(this).addClass("active");
			$("#personalBtn").removeClass("active");

			// 영역 전환 (토글)
			$(".form-group.business").show();
			//매장 회원가입 탭일때 맴버 타입 값
			$("#memberType").val("mtype_03");
		}
	});
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