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
public class ReservationLisService {

	private final ReservationListMapper reservationListMapper;

    /*방문예정 예약 리스트 조회*/
	public List<Map<String, Object>> getUpcomingList(String MemberIdx) {
		List<Map<String, Object>> result = reservationListMapper.getMyReservationList(MemberIdx, "rsrt_05");
	    return result;
	}

	/*취소 예약 리스트 조회*/
    public List<Map<String, Object>> getCanceledList(String MemberIdx) {
        return reservationListMapper.getMyReservationList(MemberIdx, "rsrt_03");
    }
}
