function showTab(tabId, element) {
  // 모든 탭 내용 숨기기
  document.querySelectorAll(".tab-content").forEach(c => c.style.display = "none");

  // 선택한 탭만 보이기
  document.getElementById(tabId).style.display = "block";

  // 모든 탭 라벨에서 active 제거
  document.querySelectorAll(".reserve-label").forEach(l => l.classList.remove("active"));

  // 클릭한 라벨 활성화
  element.classList.add("active");
}
