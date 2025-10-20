let currentStat = "rsrt_05";  // 기본 예약상태 초기값 할당
document.addEventListener('DOMContentLoaded', () => {
	const historyLink = document.querySelector('a#nav-history');
	if (!historyLink) return;

	historyLink.addEventListener('click', (e) => {

		showTab('rsrt', historyLink, 'rsrt_01', () => {
			console.log("클릭!");
			// Ajax 완료 콜백 함수 (showTab 호출 시 콜백 전달 필요)
//			window.location.href = historyLink.href;
		});
	});
});
function showTab(tabId, element, reserveResult) {	
	console.log("showTab 실행중!", tabId, reserveResult);
	
	// 모든 탭 내용 숨기기
	document.querySelectorAll(".tab-content").forEach(c => c.style.display = "none");

	// 선택한 탭만 보이기
	document.getElementById(tabId).style.display = "block";

	// 모든 탭 라벨에서 active 제거
	document.querySelectorAll(".reserve-label").forEach(l => l.classList.remove("active"));

	// 클릭한 라벨 활성화
	element.classList.add("active");

	currentStat = reserveResult;  // 현재 상태 갱신
	console.log("보낼 상태:", currentStat);
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

