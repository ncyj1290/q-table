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
			reserveResult: currentStat
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
				const cancelTabLabel = document.querySelector('.reserve-label[data-tab="cancel"]');
				    showTab('cancel', cancelTabLabel, 'rsrt_03');
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

// 예약변경 모달을 연 후, 프래그먼트 html이 동적으로 삽입된 이후 실행!
document.body.addEventListener('click', async (e) => {
	const btn = e.target.closest('.positive-button');
	if (!btn) return;
	const type = btn.getAttribute('data-type');
	const storeIdx = btn.getAttribute('data-store-idx');
	console.log('storeIdx:', storeIdx, 'type:', type);
	if (type === 'changeBtn' || type === 'revisitBtn' || type === 'rebookBtn') {
		const response = await fetch(`/reserv_change?store_idx=${storeIdx}`);
		if (response.ok) {
			const html = await response.text();
			document.getElementById('mypage-calendar-wrapper').innerHTML = html;
			initCalendar('#mypage-calendarModal');
			document.getElementById('mypage-calendarModal').style.display = 'block';

		}
	}
});

//모달 열고 닫기
document.getElementById('mypage-closeCalendarModal').addEventListener('click', () => {
  document.getElementById('mypage-calendarModal').style.display = 'none';
});









