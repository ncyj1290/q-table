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
	     params.put("member_idx", memberIdx);        
	     params.put("reserve_result", reserveResult);  
	     List<Map<String, Object>> result = reservationListMapper.getMyReservationList(params);
	     return result;
	}

	public boolean cancelReservation(String memberIdx, int  reserveIdx) {
	    // 예약 상태 취소로 변경 쿼리 수행 예시
	    int updateCount = reservationListMapper.updateReservationStatus(reserveIdx, memberIdx, "rsrt_03");
	    return updateCount > 0;
	}
	
	
	
}
