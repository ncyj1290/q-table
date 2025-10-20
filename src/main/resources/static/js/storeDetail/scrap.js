$(function() {
	// ===================================
	// DOM 캐싱
	// ===================================
	const $scrapBtn = $('#scrapBtn');
	const $scrapIcon = $('#scrapIcon');

	// ===================================
	// 이벤트 리스너
	// ===================================

	// 스크랩 버튼 클릭
	$scrapBtn.on('click', function() {
		const storeIdx = $(this).data('store-idx');
		const isScrapped = $(this).data('scrapped');

		toggleScrap(storeIdx, isScrapped);
	});

	// ===================================
	// 함수 정의
	// ===================================

	// 스크랩 토글
	function toggleScrap(storeIdx, isScrapped){
		$.ajax({
			url: '/api/storeDetail/scrap/toggle',
			type: 'POST',
			data: {store_idx : storeIdx},
			success: function(response){
				handleAjaxSuccess(response, function() {
					const newScrapped = !isScrapped;
					updateScrapUI(newScrapped);
					$scrapBtn.data('scrapped', newScrapped);

					const message = newScrapped ? '스크랩에 추가되었습니다.' : '스크랩이 해제되었습니다.';
					showNotification(message);
				}, '스크랩 처리에 실패했습니다.');
			},
			error: function(xhr){
				handleAjaxError(xhr, '스크랩 처리 중 오류가 발생했습니다.');
			}
		});

	}

	// 스크랩 UI 업데이트
	function updateScrapUI(isScrapped) {
		if (isScrapped) {
			// 스크랩 활성화
			$scrapIcon.attr('src', '/img/scrap_full.png');
			$scrapBtn.addClass('active');
		} else {
			// 스크랩 비활성화
			$scrapIcon.attr('src', '/img/scrap.png');
			$scrapBtn.removeClass('active');
		}
	}

	// 알림 표시
	function showNotification(message) {
		// 기존 알림 제거
		$('.scrap-notification').remove();

		// 알림 생성
		const $notification = $('<div class="scrap-notification">' + message + '</div>');
		$('body').append($notification);

		// 페이드인
		setTimeout(function() {
			$notification.addClass('show');
		}, 10);

		// 3초 후 페이드아웃 후 제거
		setTimeout(function() {
			$notification.removeClass('show');
			setTimeout(function() {
				$notification.remove();
			}, 300);
		}, 3000);
	}
});
