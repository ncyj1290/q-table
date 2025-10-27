// ===================================
// 변수 선언
// ===================================
let currentTab = 'id'; // 현재 탭 (id or pw)
let resendTimerInterval = null; // 재발송 타이머
let verificationTimerInterval = null; // 인증번호 만료 타이머

$(function(){
	// ===================================
	// DOM 캐싱
	// ===================================
	const $firstInput = $("#first-input");
	const $userEmail = $("#user-email");
	const $verificationCode = $("#verification-code");
	const $emailBtn = $("#email-send-btn");
	const $checkBtn = $("#check-btn");
	const $verificationTimer = $("#verification-timer");

	const $idResultArea = $("#id-result-area");
	const $idResultText = $("#id-result-text");
	const $pwResultArea = $("#pw-result-area");

	const $newPassword = $("#new-password");
	const $newPasswordConfirm = $("#new-password-confirm");
	const $pwResetBtn = $("#pw-reset-btn");

	// ===================================
	// 이벤트 리스너
	// ===================================

	// 탭 전환
	$('.tab').on('click', switchTab);

	// 인증번호 발송
	$emailBtn.on('click', sendVerificationEmail);

	// 인증번호 확인
	$checkBtn.on('click', checkVerificationCode);

	// 비밀번호 재설정
	$pwResetBtn.on('click', resetPassword);

	// ===================================
	// 함수 정의
	// ===================================

	// 탭 전환
	function switchTab() {
		const targetTab = $(this).data('tab');

		// 같은 탭 클릭 시 무시
		if (currentTab === targetTab) {
			return;
		}

		// 작성중인 내용 확인
		if (hasInputData()) {
			if (!confirm('작성 중인 내용이 있습니다.\n탭을 전환하시겠습니까?')) {
				return;
			}
		}

		// 탭 전환 실행
		currentTab = targetTab;

		// 탭 활성화 상태 변경
		$('.tab').removeClass('active');
		$(this).addClass('active');

		// placeholder 변경
		if (targetTab === 'id') {
			$firstInput.attr('placeholder', '이름');
		} else {
			$firstInput.attr('placeholder', '아이디');
		}

		// 입력값 초기화
		clearAllInputs();

		// 결과 영역 숨김
		$idResultArea.hide();
		$pwResultArea.hide();

		// 타이머 초기화
		stopAllTimers();
	}

	// 작성중인 내용이 있는지 확인
	function hasInputData() {
		return $firstInput.val().trim() !== '' ||
		       $userEmail.val().trim() !== '' ||
		       $verificationCode.val().trim() !== '';
	}

	// 모든 입력값 초기화
	function clearAllInputs() {
		$firstInput.val('');
		$userEmail.val('');
		$verificationCode.val('');
		$newPassword.val('');
		$newPasswordConfirm.val('');
	}

	// 모든 타이머 중지
	function stopAllTimers() {
		// 재발송 타이머 중지
		if (resendTimerInterval) {
			clearInterval(resendTimerInterval);
			resendTimerInterval = null;
			$emailBtn.prop('disabled', false);
			$emailBtn.text('인증번호 발송');
		}

		// 인증번호 만료 타이머 중지
		if (verificationTimerInterval) {
			clearInterval(verificationTimerInterval);
			verificationTimerInterval = null;
			$verificationTimer.hide();
			$verificationTimer.text('');
			$verificationTimer.css('color', '');
		}
	}

	// 재발송 타이머 시작 (60초)
	function startResendTimer() {
		let remainingTime = 60;

		// 버튼 비활성화
		$emailBtn.prop('disabled', true);
		$emailBtn.text(`재발송 (${remainingTime}초)`);

		resendTimerInterval = setInterval(function() {
			remainingTime--;

			if (remainingTime <= 0) {
				clearInterval(resendTimerInterval);
				resendTimerInterval = null;
				$emailBtn.prop('disabled', false);
				$emailBtn.text('인증번호 발송');
			} else {
				$emailBtn.text(`재발송 (${remainingTime}초)`);
			}
		}, 1000);
	}

	// 인증번호 만료 타이머 시작 (5분 = 300초)
	function startVerificationTimer() {
		let remainingTime = 300; // 5분

		// 타이머 표시
		$verificationTimer.show();
		$verificationTimer.css('color', 'var(--color-main)');

		verificationTimerInterval = setInterval(function() {
			const minutes = Math.floor(remainingTime / 60);
			const seconds = remainingTime % 60;

			$verificationTimer.text(`${minutes}:${seconds.toString().padStart(2, '0')}`);

			remainingTime--;

			// 만료
			if (remainingTime < 0) {
				clearInterval(verificationTimerInterval);
				verificationTimerInterval = null;
				$verificationTimer.text('만료됨');
				$verificationTimer.css('color', 'red');
				$verificationCode.prop('disabled', true);
				$checkBtn.prop('disabled', true);
			}
		}, 1000);
	}

	// 인증번호 발송
	function sendVerificationEmail() {
		// 입력 검증
		const firstInputLabel = currentTab === 'id' ? '이름' : '아이디';

		if (!$firstInput.val().trim()) {
			alert(firstInputLabel + '을 입력해주세요.');
			return;
		}
		if (!$userEmail.val().trim()) {
			alert('이메일을 입력해주세요.');
			return;
		}

		// 기존 타이머 중지
		stopAllTimers();

		// 즉각 피드백: 버튼 텍스트 변경
		const originalText = $emailBtn.text();
		$emailBtn.prop('disabled', true);
		$emailBtn.text('발송 중...');

		// API 엔드포인트 선택
		const apiUrl = currentTab === 'id'
			? '/api/findId/email/send'
			: '/api/findPw/email/send';

		// 이메일 발송
		$.ajax({
			url: apiUrl,
			type: 'POST',
			data: {
				user_id: $firstInput.val(),
				user_email: $userEmail.val()
			},
			success: function(response){
				if (response && response.success) {
					// 성공: alert 표시
					alert("인증번호가 발송되었습니다.\n이메일 도착까지 최대 1분이 소요될 수 있습니다.");

					// 인증번호 입력칸 활성화
					$verificationCode.prop('disabled', false);
					$checkBtn.prop('disabled', false);

					// 타이머 시작
					startResendTimer();
					startVerificationTimer();
				} else {
					// 실패: 버튼 복구
					$emailBtn.prop('disabled', false);
					$emailBtn.text(originalText);

					// 에러 메시지 표시
					const errorMsg = response && response.message
						? response.message
						: '이메일 발송에 실패했습니다.';
					alert(errorMsg);
				}
			},
			error: function(xhr){
				// 네트워크 에러: 버튼 복구
				$emailBtn.prop('disabled', false);
				$emailBtn.text(originalText);

				handleAjaxError(xhr, '이메일 발송 중 오류가 발생했습니다.');
			}
		});
	}

	// 인증번호 확인
	function checkVerificationCode() {
		// 입력 검증
		if (!$verificationCode.val().trim()) {
			alert('인증번호를 입력해주세요.');
			return;
		}

		if (!$userEmail.val().trim()) {
			alert('이메일을 입력해주세요.');
			return;
		}

		// 만료 확인
		if (verificationTimerInterval === null && $verificationTimer.text() === '만료됨') {
			alert('인증번호가 만료되었습니다. 다시 발송해주세요.');
			return;
		}

		// API 엔드포인트 선택
		const apiUrl = currentTab === 'id'
			? '/api/findId/email/verify'
			: '/api/findPw/email/verify';

		$.ajax({
			url: apiUrl,
			type: 'POST',
			data: {
				verification_code: $verificationCode.val(),
				user_email: $userEmail.val()
			},
			success: function(response){
				handleAjaxSuccess(response, function(res) {
					// 타이머 중지
					stopAllTimers();

					if (currentTab === 'id') {
						// 아이디 찾기 - 아이디 표시
						$idResultArea.show();
						$idResultText.text(res.memberId + ' 입니다.');
					} else {
						// 비밀번호 찾기 - 비밀번호 재설정 폼 표시
						$pwResultArea.show();
					}
				}, '인증번호 확인에 실패했습니다.');
			},
			error: function(xhr){
				handleAjaxError(xhr, '인증번호 확인 중 오류가 발생했습니다.');
			}
		});
	}

	// 비밀번호 재설정
	function resetPassword() {
		const newPw = $newPassword.val();
		const confirmPw = $newPasswordConfirm.val();

		// 검증
		if (!newPw.trim()) {
			alert('새 비밀번호를 입력해주세요.');
			return;
		}
		if (newPw.length < 8) {
			alert('비밀번호는 8자 이상이어야 합니다.');
			return;
		}
		if (newPw !== confirmPw) {
			alert('비밀번호가 일치하지 않습니다.');
			return;
		}

		$.ajax({
			url: '/api/findPw/reset',
			type: 'POST',
			data: {
				user_id: $firstInput.val(),
				new_password: newPw
			},
			success: function(response){
				handleAjaxSuccess(response, function(res) {
					alert('비밀번호가 변경되었습니다.\n로그인 페이지로 이동합니다.');
					location.href = '/login';
				}, '비밀번호 변경에 실패했습니다.');
			},
			error: function(xhr){
				handleAjaxError(xhr, '비밀번호 변경 중 오류가 발생했습니다.');
			}
		});
	}
});
