package com.itwillbs.qtable.service.mypage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.mypage.PaymentListMapper;
import com.itwillbs.qtable.mapper.mypage.ReservationListMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentListService {

	/* 방문예정 예약 리스트 조회 */
//	public List<Map<String, Object>> getPaymentList(String memberIdx, String reserveResult) {
//		Map<String, Object> params = new HashMap<>();
//		params.put("member_idx", memberIdx);
//		params.put("reserve_result", reserveResult);
//		List<Map<String, Object>> result = PaymentListMapper.getMyPaymentList(params);
//		return result;
//	}

}
