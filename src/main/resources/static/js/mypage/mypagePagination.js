let reservationPagination = null;

$(function() {
  // 실제 회원번호는 세션이나 서버 템플릿에서 얻어야 합니다.
  const memberIdx = $('#memberIdx').val() || 1; // 예: input hidden에서 가져오기
  let reserveResult = 'rsrt_05'; // 기본 방문예정 상태, 필요에 따라 탭 클릭 시 변경

  reservationPagination = new Pagination({
    dataContainer: '#reservation-list',          // 예약 목록이 표시될 영역 ID
    paginationContainer: '#reservation-pagination',  // 페이징 버튼이 표시될 영역 ID
    url: '/api/mypage/reservationList',          // 예약현황 페이징용 REST API
    params: {
      member_idx: memberIdx,
      reserve_result: reserveResult
    },
    pageSize: 10,
    renderItem: function(reservation) {
      // 예약 객체의 필드에 맞게 HTML 생성 (백틱 사용)
      return `
        <div class="reservation-item">
          <div class="store-name">${reservation.store_name}</div>
          <div class="reserve-info">${reservation.reserve_dt} / ${reservation.person_count}명</div>
          <div class="store-phone">${reservation.store_phone}</div>
          <div class="store-img">
            <img src="${reservation.store_img || '/img/noimg.png'}" alt="가게 이미지">
          </div>
        </div>
      `;
    },
    emptyMessage: `
      <div class="empty-state">
        <p>예약 내역이 없습니다.</p>
      </div>
    `
  });

  // 페이지 첫 로드: 1페이지 로드
  reservationPagination.loadPage(1);

  // (선택) 탭 별 예약상태 변경 시 호출하는 함수
  window.changeReserveStatus = function(newStatus) {
    reserveResult = newStatus;
    reservationPagination.params.reserve_result = reserveResult;
    reservationPagination.loadPage(1);
  }
});
