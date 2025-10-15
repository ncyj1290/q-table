$(document).ready(function() {
	const amountButtons = $('.charge-amount-group .box');
	const payButtons = $('.payment-method-section .img-button, .payment-method-section .card');

	let selectedAmount = null;
	let selectedPayMethod = null;

	// 금액 선택
	amountButtons.click(function() {
		amountButtons.removeClass('selected');
		$(this).addClass('selected');

		selectedAmount = $(this).text().replace(/[^0-9]/g, '');

		$('#selectedAmountDisplay').text(selectedAmount.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
		$('#totalPaymentAmount').text(selectedAmount.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
	});

	// 결제 수단 선택
	payButtons.click(function() {
		payButtons.removeClass('selected');
		$(this).addClass('selected');

		if ($(this).hasClass('card')) {
			selectedPayMethod = 'card';
		} else if ($(this).find('.kakao_pay').length > 0) {
			selectedPayMethod = 'kakao';
		} else if ($(this).find('.naver_pay').length > 0) {
			selectedPayMethod = 'naver';
		} else if ($(this).find('.toss_pay').length > 0) {
			selectedPayMethod = 'toss';
		}
	});

	// 결제하기 버튼 클릭 시
	$('.positive-button.paylast').click(function() {
		if (!selectedAmount) {
			alert('충전할 금액을 선택해 주세요.');
			return;
		}
		if (!selectedPayMethod) {
			alert('결제 수단을 선택해 주세요.');
			return;
		}
		if (!$('.component-checkbox').is(':checked')) {
			alert('결제 및 환불 정책에 동의해 주세요.');
			return;
		}

		let token = $("meta[name='_csrf']").attr('content');
		let header = $("meta[name='_csrf_header']").attr('content');

		$.ajax({
			url: '/mypage/qmoneyCharge',
			type: 'POST',
			contentType: 'application/json',
			dataType: 'json',
			data: JSON.stringify({
				payment_idx: selectedPayMethod,
				amount: selectedAmount
			}),
			beforeSend: function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			success: function(data) {
				if (data && data.next_redirect_pc_url) {
					window.location.href = data.next_redirect_pc_url;
				} else if (data && data.updatedBalance) {
					$('.Qmoney-balance span').text(data.updatedBalance + '원');
				} else {
					alert('결제 요청 실패');
				}
			},
			error: function(xhr, status, error) {
				console.error('결제 중 오류:', error);
				alert('결제 중 오류가 발생했습니다.');
			}
		});
	});

	// 카카오페이 결제 완료 후 승인 처리 & 완료 페이지 이동
	$(window).on('load', function() {
		const urlParams = new URLSearchParams(window.location.search);
		const pgToken = urlParams.get('pg_token');

		if (pgToken) {
			// 결제 완료 후 돌아온 경우
			$.ajax({
				url: `/mypage/paymentcomplete?pg_token=${pgToken}`,
				type: 'GET',
				dataType: 'json',
				success: function(data) {
					console.log('결제 승인 성공:', data);

					// 결제 완료 페이지로 이동
					window.location.href = `/mypage/paymentcomplete?tid=${data.tid || ''}`;
				},
				error: function(xhr, status, error) {
					console.error('결제 승인 오류:', error);
					window.location.href = '/pay/fail';
				}
			});
		}
	});
});

