document.addEventListener('DOMContentLoaded', () => {
  const amountButtons = document.querySelectorAll('.charge-amount-group .box');
  const payButtons = document.querySelectorAll('.payment-method-section .img-button, .payment-method-section .card');

  const selectedAmountDisplay = document.getElementById('selectedAmountDisplay');
  const totalPaymentAmount = document.getElementById('totalPaymentAmount');
  
  let selectedAmount = null;
  let selectedPayMethod = null;

  // 금액 선택
  amountButtons.forEach(btn => {
    btn.addEventListener('click', () => {
      amountButtons.forEach(b => b.classList.remove('selected'));
      btn.classList.add('selected');

      selectedAmount = btn.textContent.replace(/[^0-9]/g, '');

      // 금액 표시
      selectedAmountDisplay.textContent = selectedAmount.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
      totalPaymentAmount.textContent = selectedAmount.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
    });
  });

  // 결제 수단 선택
  payButtons.forEach(btn => {
    btn.addEventListener('click', () => {
      payButtons.forEach(b => b.classList.remove('selected'));
      btn.classList.add('selected');

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

    const agreeCheckbox = document.querySelector('.component-checkbox');
    if (!agreeCheckbox.checked) {
      alert('결제 및 환불 정책에 동의해 주세요.');
      return;
    }

    fetch('/pay/ready', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({ 
        payMethod: selectedPayMethod, 
        amount: selectedAmount 
      })
    })
    .then(response => response.json())
    .then(data => {
      if (data && data.next_redirect_pc_url) {
        window.location.href = data.next_redirect_pc_url;
      } else {
        alert('결제 요청 실패');
      }
    })
    .catch(e => {
      console.error(e);
      alert('결제 중 오류가 발생했습니다.');
    });
  });
});
