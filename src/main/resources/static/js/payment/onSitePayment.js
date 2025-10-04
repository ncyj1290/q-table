// ===================================
// 이벤트 리스너 & 실행 코드
// ===================================
$(function() {
	// 금액 입력 시 계산
	$amountInput.on('input', calculatePayment);

	// 결제하기 버튼 클릭
	$paymentBtn.on('click', processPayment);
});

// ===================================
// 변수 선언
// ===================================
// 서버에서 가져올 데이터 (TODO 현재 하드코딩)
let currentQMoney = 195000;
let depositAmount = 3000;

// 상수
const DISCOUNT_RATE = 0.05;

// DOM 캐싱
const $amountInput = $('.amount-input-section input');
const $paymentBtn = $('.payment-btn');
const $inputAmountDisplay = $('#inputAmountDisplay');
const $discountDisplay = $('#discountDisplay');
const $finalAmountDisplay = $('#finalAmountDisplay');
const $afterQMoneyDisplay = $('#afterQMoneyDisplay');

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
