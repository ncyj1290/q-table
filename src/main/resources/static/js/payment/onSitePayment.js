// ===================================
// 변수 선언
// ===================================
// 서버에서 가져올 데이터 (TODO 현재 하드코딩)
let currentQMoney = 195000;
let depositAmount = 3000;

// 상수
const DISCOUNT_RATE = 0.05;

$(function() {
	const $amountInput = $('.amount-input-section input');
	const $paymentBtn = $('.payment-btn');
	const $inputAmountDisplay = $('#inputAmountDisplay');
	const $discountDisplay = $('#discountDisplay');
	const $finalAmountDisplay = $('#finalAmountDisplay');
	const $afterQMoneyDisplay = $('#afterQMoneyDisplay');

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
			return;
		}

		// 계산
		let discount = Math.floor(inputAmount * DISCOUNT_RATE);
		let finalAmount = inputAmount - discount - depositAmount;
		let afterQMoney = currentQMoney - finalAmount;

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
		// TODO
	}
});
