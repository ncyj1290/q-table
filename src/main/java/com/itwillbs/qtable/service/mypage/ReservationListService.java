package com.itwillbs.qtable.service.mypage;

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
	public List<Map<String, Object>> getUpcomingList(String memberIdx) {
		List<Map<String, Object>> result = reservationListMapper.getMyReservationList(memberIdx, "rsrt_05");
	    return result;
	}

//	/*취소 예약 리스트 조회*/
//    public  List<Map<String, Object>> getCanceledList(String memberIdx) {
//    	List<Map<String, Object>> result = reservationListMapper.getCanceledList(memberIdx, "rsrt_03");
//	    return result;
//    }
}
