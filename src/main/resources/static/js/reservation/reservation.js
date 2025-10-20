// ===================================
// 변수 선언
// ===================================
// 이메일 정규식
const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

$(function() {
	// ===================================
	// DOM 캐싱
	// ===================================
	const $reservationForm = $('#reservationForm');
	const $customerName = $('#customerName');
	const $customerEmail = $('#customerEmail');
	const $specialRequest = $('#specialRequest');
	const $allergies = $('#allergies');
	const $agreePolicy = $('#agreePolicy');
	const $reservationDate = $('#reservationDate');
	const $reservationTime = $('#reservationTime');
	const $personCount = $('#personCount');
	const $storeIdx = $('#storeIdx');
	const $deposit = $('#deposit');
	const $userQMoney = $('#userQMoney');
	const $afterPaymentQMoney = $('#afterPaymentQMoney');

	// ===================================
	// 초기화
	// ===================================
	updateAfterPaymentBalanceClass();

	// ===================================
	// 이벤트 리스너
	// ===================================

	// 폼 전송 시 검증 및 예약 제출
	$reservationForm.on('submit', function(e) {
		e.preventDefault();

		// 입력값 검증
		if (!validateForm()) {
			return false;
		}

		// 큐머니 잔액 검증
		if (!validateQMoney()) {
			return false;
		}

		// 예약 데이터 생성 및 제출
		const reservationData = createReservationData();
		submitReservation(reservationData);
	});

	// ===================================
	// 함수 정의
	// ===================================

	// 폼 입력값 검증
	function validateForm() {
		const customerName = $customerName.val().trim();
		const customerEmail = $customerEmail.val().trim();
		const agreePolicy = $agreePolicy.is(':checked');

		if (!customerName) {
			alert('이름을 입력해주세요.');
			$customerName.focus();
			return false;
		}

		if (!customerEmail) {
			alert('이메일을 입력해주세요.');
			$customerEmail.focus();
			return false;
		}

		if (!EMAIL_REGEX.test(customerEmail)) {
			alert('올바른 이메일 형식을 입력해주세요.');
			$customerEmail.focus();
			return false;
		}

		if (!agreePolicy) {
			alert('예약 정책에 동의해주세요.');
			return false;
		}

		return true;
	}

	// 큐머니 잔액 검증
	function validateQMoney() {
		const deposit = parseInt($deposit.val());
		const personCount = parseInt($personCount.text());
		const totalDeposit = deposit * personCount;

		const qMoneyText = $userQMoney.text().replace(/[^0-9]/g, '');
		const userQMoney = parseInt(qMoneyText);

		if (userQMoney < totalDeposit) {
			const confirmed = confirm(
				'큐머니 잔액이 부족합니다.\n' +
				'현재 잔액: ' + userQMoney.toLocaleString() + ' Q-money\n' +
				'필요 금액: ' + totalDeposit.toLocaleString() + ' Q-money\n\n' +
				'큐머니 충전 페이지로 이동하시겠습니까?'
			);

			if (confirmed) {
				location.href = '/qmoney_charge';
			}
			return false;
		}

		return true;
	}

	// 예약 데이터 생성
	function createReservationData() {
		return {
			store_idx: parseInt($storeIdx.val()),
			reserve_name: $customerName.val().trim(),
			reserve_email: $customerEmail.val().trim(),
			reserve_date: $reservationDate.text(),
			reserve_time: $reservationTime.text(),
			person_count: parseInt($personCount.text()),
			requirement: $specialRequest.val().trim(),
			allergy: $allergies.val().trim()
		};
	}

	// 예약 제출
	function submitReservation(reservationData) {
		$.ajax({
			url: '/api/reservation/submit',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(reservationData),
			success: function(response) {
				handleAjaxSuccess(response, function() {
					alert('예약이 완료되었습니다');
					location.href = '/reservation_list';
				}, '예약에 실패하였습니다.');
			},
			error: function(xhr) {
				handleAjaxError(xhr, '예약 처리 중 오류가 발생했습니다.');
			}
		});
	}

	// 결제 후 잔액 부족 시 빨간색 표시
	function updateAfterPaymentBalanceClass() {
		const afterPaymentText = $afterPaymentQMoney.text().replace(/[^0-9-]/g, '');
		const afterPayment = parseInt(afterPaymentText);

		if (afterPayment < 0) {
			$afterPaymentQMoney.addClass('insufficient');
		} else {
			$afterPaymentQMoney.removeClass('insufficient');
		}
	}
});
