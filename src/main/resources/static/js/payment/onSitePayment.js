// ===================================
// 변수 선언
// ===================================

// 상수
const DISCOUNT_RATE = 0.05;

$(function() {
	const urlParams = new URLSearchParams(location.search);
	const storeIdx = urlParams.get('store_idx');

	const $amountInput = $('.amount-input-section input');
	const $paymentBtn = $('.payment-btn');
	const $inputAmountDisplay = $('#inputAmountDisplay');
	const $discountDisplay = $('#discountDisplay');
	const $depositDisplay = $('#depositDisplay');
	const $currentQMoneyDisplay = $('#currentQMoneyDisplay');
	const $finalAmountDisplay = $('#finalAmountDisplay');
	const $afterQMoneyDisplay = $('#afterQMoneyDisplay');

	// 서버에서 렌더링된 값 읽기
	let depositText = $depositDisplay.text().replace(/[^0-9]/g, '');
	let depositAmount = parseInt(depositText) || 0;

	let qMoneyText = $currentQMoneyDisplay.text().replace(/[^0-9]/g, '');
	let currentQMoney = parseInt(qMoneyText) || 0;

	// 계산 결과 저장 객체
	let calculatedData = {
		inputAmount: 0,
		discount: 0,
		finalAmount: 0,
		afterQMoney: currentQMoney
	};

	// ===================================
	// 이벤트 리스너
	// ===================================

	// 숫자만 입력 가능
	$amountInput.on('input', function() {
		// 숫자가 아닌 문자 제거
		let value = $(this).val().replace(/[^0-9]/g, '');

		// 0으로 시작하는 경우 제거
		if (value.length > 1 && value.startsWith('0')) {
			value = value.replace(/^0+/, '');
		}

		$(this).val(value);
		calculatePayment();
	});

	// 결제하기 버튼 클릭
	$paymentBtn.on('click', processPayment);

	// ===================================
	// 함수 정의
	// ===================================

	// 결제 금액 계산
	function calculatePayment() {
		let inputValue = $amountInput.val().replace(/,/g, '');
		let inputAmount = parseInt(inputValue);

		// 유효성 검사
		if(!inputAmount || inputAmount <= 0 || isNaN(inputAmount)){
			resetDisplay();
			calculatedData = { inputAmount: 0, discount: 0, finalAmount: 0, afterQMoney: currentQMoney };
			return;
		}

		// 계산
		let discount = Math.floor(inputAmount * DISCOUNT_RATE);
		let finalAmount = inputAmount - discount - depositAmount;
		let afterQMoney = currentQMoney - finalAmount;

		// 계산 결과 저장
		calculatedData = {
			inputAmount: inputAmount,
			discount: discount,
			finalAmount: finalAmount,
			afterQMoney: afterQMoney
		};

		// 화면 업데이트
		updateDisplay(inputAmount, discount, finalAmount, afterQMoney);
	}

	// 화면에 금액 업데이트
	function updateDisplay(inputAmount, discount, finalAmount, afterQMoney) {
		$inputAmountDisplay.text(formatNumber(inputAmount) + ' 원');
		$discountDisplay.text('- ' + formatNumber(discount) + ' 원');
		$finalAmountDisplay.text(formatNumber(finalAmount) + ' 원');
		$afterQMoneyDisplay.text(formatNumber(afterQMoney) + ' 원');
	}

	// 금액 초기화
	function resetDisplay() {
		updateDisplay(0, 0, 0, currentQMoney);
	}

	// 숫자를 3자리마다 콤마 찍기
	function formatNumber(num) {
		return num.toLocaleString('ko-KR');
	}

	// 결제 처리
	function processPayment() {
		// 계산 결과 검증
		if (calculatedData.finalAmount <= 0) {
			alert('결제 금액을 입력해주세요');
			return;
		}

		// 큐머니 잔액 확인
		if (currentQMoney < calculatedData.finalAmount) {
			alert('큐머니 잔액이 부족합니다');
			return;
		}

		// 확인 메시지
		if (!confirm(`${formatNumber(calculatedData.finalAmount)}원을 결제하시겠습니까?`)) {
			return;
		}

		// AJAX 요청
		$.ajax({
			url: '/api/payment/onsite',
			type: 'POST',
			data: {
				store_idx: storeIdx,
				amount: calculatedData.finalAmount
			},
			success: function() {
				alert('결제가 완료되었습니다');
			},
			error: function(xhr) {
				console.error("실패:", xhr);
			}
		});
	}
});
