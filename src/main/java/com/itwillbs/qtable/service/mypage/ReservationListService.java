package com.itwillbs.qtable.service.mypage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.mypage.ReservationListMapper;
import com.itwillbs.qtable.vo.PageResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationListService {

	private final ReservationListMapper reservationListMapper;

	// 방문예정 예약 리스트 조회
	public List<Map<String, Object>> getUpcomingList(String memberIdx, String reserveResult
															) {
		Map<String, Object> params = new HashMap<>();
		params.put("member_idx", memberIdx);
		params.put("reserve_result", reserveResult);
		List<Map<String, Object>> result = reservationListMapper.getMyReservationList(params);
		return result;
	}

	// 예약취소
	@Transactional
	public boolean cancelReservation(String memberIdx, int reserveIdx) {
		// 예약 상태를 취소 변경
		int updateCount = reservationListMapper.updateReservationStatus(reserveIdx, memberIdx, "rsrt_03");
		if (updateCount == 0)
			return false; 

		// 취소한 예약의 결제정보 한 건 조회
		Map<String, Object> payment = reservationListMapper.findPaymentByMemberIdx(reserveIdx, memberIdx);
		
		if (payment != null && "pyus_02".equals(payment.get("pay_type"))) {
			Object amountObj = payment.get("payment_amount");
			int amount = (amountObj instanceof Number) ? ((Number) amountObj).intValue()
					: Integer.parseInt(String.valueOf(amountObj));

			// 결제금액 환불
			Map<String, Object> moneyParams = new HashMap<>();
			moneyParams.put("reserve_idx", reserveIdx);
			moneyParams.put("member_idx", memberIdx);
			moneyParams.put("amount", amount);
			
			if (reservationListMapper.refundQmoney(moneyParams) == 0) {
	            return false;
	        }

			// 환불 상태 변경
			Map<String, Object> updateTypeParams = new HashMap<>();
			updateTypeParams.put("payment_idx", payment.get("payment_idx"));
			updateTypeParams.put("pay_type", "pyus_04");
			updateTypeParams.put("payment_date", LocalDateTime.now());

	        if (reservationListMapper.updatePaymentType(updateTypeParams) == 0) {
	            return false;
	        }
		}
		return true; 
	}
	
	// 큐머니 반영
	public int getQmoney(String memberIdx) {
	    Integer qmoney = reservationListMapper.selectQmoneyByMemberIdx(memberIdx);
	    return qmoney != null ? qmoney : 0;
	}
	// pick 랜덤 값 받기
	public List<Map<String, Object>> getRandomStores() {
		return reservationListMapper.selectRandomStores();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
