document.addEventListener('DOMContentLoaded', () => {
  const amountButtons = document.querySelectorAll('.charge-amount-group .box');
  const payButtons = document.querySelectorAll('.payment-method-section .img-button, .payment-method-section .card');
  
  let selectedAmount = null;
  let selectedPayMethod = null;

  // 금액 선택
  amountButtons.forEach(btn => {
    btn.addEventListener('click', () => {
      // 선택 스타일 초기화
      amountButtons.forEach(b => b.classList.remove('selected'));
      
      // 클릭한 버튼 스타일 적용
      btn.classList.add('selected');
      
      // 선택 금액 저장 (숫자만)
      selectedAmount = btn.textContent.replace(/[^0-9]/g, '');
	  
	  // 선택 결제 금액
	  selectedAmountDisplay.textContent = selectedAmount.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
	  
	  // 총 결제 금액
	  totalPaymentAmount.textContent = selectedAmount.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
    });
  });

  // 결제 수단 선택
  payButtons.forEach(btn => {
    btn.addEventListener('click', () => {
      // 선택 스타일 초기화
      payButtons.forEach(b => b.classList.remove('selected'));
      
      // 클릭한 버튼 스타일 적용
      btn.classList.add('selected');

      // 결제 수단 결정
      if (btn.classList.contains('card')) {
        selectedPayMethod = 'card';
      } else if (btn.querySelector('.kakao_pay')) {
        selectedPayMethod = 'kakao';
      } else if (btn.querySelector('.naver_pay')) {
        selectedPayMethod = 'naver';
      } else if (btn.querySelector('.toss_pay')) {
        selectedPayMethod = 'toss';
      }
    });
  });

  // 결제하기 버튼 클릭 시
  const payButton = document.querySelector('.positive-button.paylast');
  payButton.addEventListener('click', () => {
    if (!selectedAmount) {
      alert('충전할 금액을 선택해 주세요.');
      return;
    }
    if (!selectedPayMethod) {
      alert('결제 수단을 선택해 주세요.');
      return;
    }
	
	// 결제 및 환불 정책 동의 체크 확인
	const agreeCheckbox = document.querySelector('.component-checkbox');
	if (!agreeCheckbox.checked) {
	  alert('결제 및 환불 정책에 동의해 주세요.');
	  return;
	}

    // 결제 준비 요청
	fetch('/payment/ready', {
	      method: 'POST',
	      headers: {'Content-Type': 'application/json'},
	      body: JSON.stringify({ 
	        payMethod: selectedPayMethod, 
	        amount: selectedAmount 
	      })
	    })
	    .then(response => response.json())
	    .then(data => {
	      // data 안에 결제 페이지 URL이 있어야 함 (KakaoPayReadyResponse 참고)
	      if (data && data.next_redirect_pc_url) {
	        window.location.href = data.next_redirect_pc_url; // 결제 페이지로 이동
	      } else {
	        alert('결제 요청 실패');
	      }
	    })
    }).catch(e => {
      console.error(e);
      alert('결제 중 오류가 발생했습니다.');
    });
});
