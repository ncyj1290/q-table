$(function() {
	console.log('search js 연동');
	// .content는 페이지 초기에 렌더링 되므로 여기에 이벤트 위임 
	$('.content').on('click', '.scrap', function() {
		// ajax 호출
		scrapAjax(this); 
		// 비로그인이면 아래 이벤트 막아야함 
		scrapToggle(this);
	});
});

function scrapAjax(el) {
	const onScrap = '/img/scrap_full.png';
	const offScrap = '/img/scrap.png';
	const storeIdx = {
		storeIdx : $(el).data('idx')
	}
	
	$.ajax({
		url:"/scrap/toggle",
		type:'post',
		data: storeIdx,
		success: function () {
			//스크랩 취소
			if($(el).attr('src') == onScrap) {
				$(el).attr('src', offScrap)
				showNotification("스크랩 취소 되었습니다");
				return;
			}
			//스크랩
			if($(el).attr('src') == offScrap) {
				$(el).attr('src', onScrap);
				showNotification("스크랩 되었습니다");
				return;
			}
		},
		error: function(xhr, status, error) {
			const responseData = xhr.responseJSON;
	        // 서버가 401 에러를 보냈는지 확인
	        if (xhr.status === 401) {
	            // 3. 로그인 처리
	            if (confirm(responseData.message)) {
	                location.href = responseData.redirectUrl;
	            }
	        } 
	        // 403 (권한 없음) 등 다른 에러 처리
	        else if (xhr.status === 403) {
	             alert("이 게시물을 스크랩할 권한이 없습니다.");
	        }
	        // 5. 그 외 서버 에러
	        else {
	            alert("스크랩 처리 중 오류가 발생했습니다: 관리자에게 문의바랍니다");
	        }
	    }
	});
	
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

