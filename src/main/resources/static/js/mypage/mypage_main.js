let currentStat = "rsrt_05";  // 기본 예약상태 초기값 할당

// 페이지 로드 시 기본 예약상태 탭 보여주기
window.onload = function() {
	const defaultTabId = "visit";
	  // 방문예정 탭에만 showTab 호출
	  if (defaultLabel) {
	    showTab(defaultTabId, defaultLabel, currentStat);
	  }
};

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
	
	console.log('[reserveResult 전달값]', reserveResult);
	// 서버에서 예약 리스트 조회 및 화면에 렌더링
	$.ajax({
	  url: "/reservation_list",
	  type: "GET",
	  data: { reserveResult: currentStat },
	  headers: {"X-Requested-With": "XMLHttpRequest"},
	  success: function(html) {
	    document.getElementById("reservationContent").innerHTML = html;
	  },
	  error: function() {
	    alert("[translate:데이터 조회가 실패하였습니다.]");
	  }
	});
}




