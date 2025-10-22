$(function() {
	let isUserIdAvailable = false;
	let debounceTimer;
	//아이디 유효성검사
	const idRegex = /^[a-zA-Z0-9]{4,20}$/; // 영문, 숫자 4~20자
	const msg = $("#userIdMsg");

	$("#userId").on("keyup", function() {
		clearTimeout(debounceTimer);
		debounceTimer = setTimeout(() => {
			const userId = $(this).val().trim();

			if (userId === "") {
				msg.text("아이디를 입력해주세요.").removeClass("success").addClass("error");
				return;
			}

			if (!idRegex.test(userId)) {
				msg.text("4~20자의 영문 또는 숫자만 가능합니다.").removeClass("success").addClass("error");
				return;
			}


			$.ajax({
				url: "/checkMemberId",
				type: "GET",
				data: { memberId: userId },
				success: function(isAvailable) {
					if (isAvailable === true || isAvailable === "true") {
						msg.text("사용 가능한 아이디입니다").removeClass("error").addClass("success");
						isUserIdAvailable = true;
					} else {
						msg.text("이미 사용중인 아이디입니다").removeClass("success").addClass("error");
						isUserIdAvailable = false;
					}
				}
			});
		}, 400);
	});






	//비밀번호 8~16자리 영문 숫자 특수문자 포함 해야함
	const passwordRegex = /^(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,16}$/;
	const userPwMsg = $("#userPwMsg");
	const confirmPasswordInput = $("#confirmPassword");
	let isPasswordValid = false;
	let isPasswordMatch = false;

	// 비밀번호 유효성검사
	$("#password").on("keyup", function() {
		const password = $(this).val().trim()

		if (password === "") {
			userPwMsg.text("비밀번호를 입력해주세요").removeClass("success").addClass("error");
			isPasswordValid = false;
			return;
		}
		if (!passwordRegex.test(password)) {
			userPwMsg.text("8~16자, 영문/숫자/특수문자 포함").removeClass("success").addClass("error");
			isPasswordValid = false;
			return;
		}
		userPwMsg.text("사용 가능한 비밀번호입니다").removeClass("error").addClass("success");
		isPasswordValid = true;
	});
	//비밀번호 일치 유효성검사
	confirmPasswordInput.on("keyup", function() {
		const confirmPw = $(this).val();
		const password = $("#password").val(); // 여기서 실시간 값 가져오기

		if (confirmPw === "") {
			$("#confirmPwMsg").text("비밀번호 확인을 입력해주세요").removeClass("success").addClass("error");
			isPasswordMatch = false;
			return;
		}

		if (password === confirmPw) {
			$("#confirmPwMsg").text("비밀번호가 일치합니다.").removeClass("error").addClass("success");
			isPasswordMatch = true;
		} else {
			$("#confirmPwMsg").text("비밀번호가 일치하지 않습니다.").removeClass("success").addClass("error");
			isPasswordMatch = false;
		}
	});



	// 회원가입 폼 제출
	$('#signupForm').on('submit', function(e) {
		e.preventDefault();
		const userId = $('#userId').val().trim();
		const password = $('#password').val().trim();
		const confirmPassword = $('#confirmPassword').val().trim();
		const userName = $('#userName').val().trim();
		const email = $('#email').val().trim();
		const emailVerification = $('#emailVerification').val().trim();
		// 기존 필드 체크


		if (!userId) { alert('아이디를 입력해주세요.'); return; }
		if (!password) { alert('비밀번호를 입력해주세요.'); return; }
		if (!confirmPassword) { alert('비밀번호 확인을 입력해주세요.'); return; }
		if (password !== confirmPassword) { alert('비밀번호가 일치하지 않습니다.'); return; }
		if (!userName) { alert('이름을 입력해주세요.'); return; }
		if (!email) { alert('이메일을 입력해주세요.'); return; }
		if (!emailVerification) { alert('이메일 인증번호를 입력해주세요.'); return; }
		if (!isUserIdAvailable) {
			alert("사용할 수 없는 아이디입니다. 다른 아이디를 입력해주세요.");
			return;
		}
		// 비밀번호 유효성여부 
		if (!isPasswordValid) {
			alert("시용할 수 없는 비밀번호입니다. 다른 비밀번호를 입력해주세요.")
			return;
		}
		//비밀번호 일치여부
		if (!isPasswordMatch) {
			alert("비밀번호가 일치하지 않습니다. 확인해주세요.");
			return;
		}
		if (!emailVerified) {
			alert("이메일 인증이 완료되어야 회원가입이 가능합니다.");
			return;
		}

		this.submit();
	});
});

$(function() {
	$("#emailsand").click(function() {
		var $btn = $(this);
		if ($btn.prop("disabled")) return;

		$btn.prop("disabled", true);

		$.ajax({
			type: "POST",
			url: "/send",
			data: { email: $("#email").val() },
			success: function(result) {
				$("#userEmailMsg")
					.text(result)
					.removeClass("error")
					.addClass("success");

				// 버튼 10초 후 다시 클릭 가능 (서버에서 호출하는중에 중복클릭 방지)
				setTimeout(function() {
					$btn.prop("disabled", false);
				}, 1 * 10 * 1000);
			},
			error: function(xhr) {
				$("#userEmailMsg")
					.text(xhr.responseText) // 서버에서 반환한 에러 메시지 표시
					.removeClass("success")
					.addClass("error");

				$btn.prop("disabled", false);
			}
		});
	});
});
let emailVerified = false; // 인증 여부 저장

$(function() {
	$("#verifyBtn").click(function() {
		const email = $("#email").val();
		$.ajax({
			type: "POST",
			url: "/verify",
			data: {
				email: email,
				emailVerification: $("#emailVerification").val()
			},
			success: function(result) {
				alert(result); // 인증 성공/실패 메시지
				if (result.includes("인증 성공!")) {
					emailVerified = true; // ✅ 인증 성공 시 플래그 true
					$("#mailAuthStatus").val(true);
				} else {
					emailVerified = false;
					$("#mailAuthStatus").val(false);
				}
			},
			error: function(xhr) {
				alert("인증 실패! (" + xhr.status + "): " + xhr.responseText);
				emailVerified = false;
			}
		});
	});
});