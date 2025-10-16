package com.itwillbs.qtable.service.mypage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.mypage.ReservationListMapper;
import com.itwillbs.qtable.mapper.storeDetail.StoreDetailMapper;

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
	    
	     System.out.println("서비스 reserveResult 나오나: " + reserveResult);  // 값 확인용
	     return result;
	}

	
	
}
