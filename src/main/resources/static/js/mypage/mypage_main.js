let currentStat = "rsrt_05";  // 기본 예약상태 초기값 할당

function showTab(tabId, element, reserveResult) {
	// 모든 탭 내용 숨기기
	document.querySelectorAll(".tab-content").forEach(c => c.style.display = "none");

	// 선택한 탭만 보이기
	document.getElementById(tabId).style.display = "block";

	// 모든 탭 라벨에서 active 제거
	document.querySelectorAll(".reserve-label").forEach(l => l.classList.remove("active"));

	// 클릭한 라벨 활성화
	element.classList.add("active");

	currentStat = reserveResult;  // 현재 상태 갱신

	//예약&취소 조회 
	$.ajax({
		url: "/reservation_list",
		type: "GET",
		data: { reserveResult: currentStat },
		headers: { "X-Requested-With": "XMLHttpRequest" },
		success: function(html) {
			document.getElementById("reservationContent").innerHTML = html;

			// 예약현황 o -> 취소 select
			const tabContentElements = document.querySelectorAll(".tab-content");
			tabContentElements.forEach(c => c.style.display = "none");

			//예약현황 o -> 예약 & 취소 select
			const currentTabContent = document.getElementById(tabId);
			(currentTabContent ? currentTabContent : document.getElementById("reservationContent")).style.display = "block";

			// 메인에서 취소 누를 때 메세지 삭제 & 방문예정 클릭 시 메세지 띄우기
			const emptyMsg = document.querySelector('.reservation-empty-content');
			if (!emptyMsg) return;  // 요소가 없으면 종료
			emptyMsg.style.display = (tabId === "cancel" && html.trim() !== "") ? "none" : "block";
		},
		error: function() {
			alert("[translate:데이터 조회가 실패하였습니다.]");
		}
	});
}

// 예약취소
$(document).on('click', 'button[data-type="cancelBtn"]', function() {
	console.log('예약 취소 버튼 클릭됨');
	const reserveIdx = $(this).data('reserveIdx');
	console.log(reserveIdx);  // 값이 찍히는지 확인
	if (!reserveIdx) {
		alert('예약 ID가 없습니다.');
		return;
	}
	console.log('예약 ID:', reserveIdx);
	$.ajax({
		url: '/reservation_cancel',
		type: 'POST',
		data: { reserveIdx },
		success: function(resp) {
			console.log(reserveIdx);
			if (resp.success) {
				showTab('cancel', document.querySelector('#cancelTabLabel'), 'rsrt_03');
				fetchCanceledReservations();
				console.log('응답:', resp);
			} else {
				alert('예약 취소에 실패했습니다.');
			}
		},
		error: function() {
			alert('네트워크 오류가 발생했습니다.');
		}
	});
});

//스크랩 
$(document).on('click', '.scrap-icon', function() {
	const $icon = $(this);
    const storeIdx = $(this).data('storeIdx'); // 아이콘에 data-store-id 속성으로 가게 ID 저장 필요
	console.log('data-store-idx value:', $(this).attr('data-store-idx'));

  if (!storeIdx) {
    alert('가게 정보를 찾을 수 없습니다.');
    return;
  }

  $.ajax({
    url: '/scrap/save',
    type: 'POST',
    data: { storeIdx: storeIdx },
    success: function(resp) {
      if (resp.success) {
		const currentSrc = $icon.attr('src');
        const isScrapped = currentSrc.includes('scrap_full');
        $icon.attr('src', isScrapped ? '/img/scrap.png' : '/img/scrap_full.png');
        alert('스크랩이 저장되었습니다.');
      } else {
        alert('스크랩 저장에 실패했습니다.');
      }
    },
    error: function() {
      alert('네트워크 오류가 발생했습니다.');
    }
  });
});

$(document).ready(function() {
  $.ajax({
    url: '/mypageScrap/list',
    type: 'GET',
    success: function(scrapList) {
		console.log('스크랩 리스트 응답:', scrapList);
      // scrapList는 스크랩된 가게의 ID 배열이라고 가정
      scrapList.forEach(function(storeIdx) {
        let $icon = $('.scrap-icon[data-store-idx="' + storeIdx + '"]');
        if ($icon.length > 0) {
          $icon.attr('src', '/img/scrap_full.png');
        }
      });
    },
    error: function() {
      console.log('스크랩 상태 로드 실패');
    }
  });
});








