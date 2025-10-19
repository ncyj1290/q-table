package com.itwillbs.qtable.service.mypage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.mypage.ReservationListMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationListService {

	private final ReservationListMapper reservationListMapper;

    /*방문예정 예약 리스트 조회*/
	public List<Map<String, Object>> getUpcomingList(String memberIdx, String reserveResult) {
		 Map<String, Object> params = new HashMap<>();
	     params.put("member_idx", memberIdx);          // 쿼리 변수명과 동일하게
	     params.put("reserve_result", reserveResult);  // 쿼리 변수명 그대로
	     List<Map<String, Object>> result = reservationListMapper.getMyReservationList(params);
	     return result;
	}

	public boolean cancelReservation(String memberIdx, int  reserveIdx) {
	    // 예약 상태 취소로 변경 쿼리 수행 예시
	    int updateCount = reservationListMapper.updateReservationStatus(reserveIdx, memberIdx, "rsrt_03");
	    return updateCount > 0;
	}
	
	public boolean existsByUserAndStore(String memberIdx, int storeIdx) {
	    return reservationListMapper.existsScrap(memberIdx, storeIdx) > 0;
	}

	public void saveScrap(String memberIdx, int storeIdx, LocalDateTime createAt) {
		reservationListMapper.insertScrap(memberIdx, storeIdx, createAt);
	}

	public void deleteScrap(String memberIdx, int storeIdx) {
		reservationListMapper.deleteScrap(memberIdx, storeIdx);
	}

	public List<Integer> getScrapStoreIdsByUser(String memberIdx) {
		return reservationListMapper.getScrapStoreIdsByUser(memberIdx);
	}
	
	
}
