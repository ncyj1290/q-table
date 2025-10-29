package com.itwillbs.qtable.service.mypage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.mypage.ReservationListMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationListService {

	private final ReservationListMapper reservationListMapper;

	/* 방문예정 예약 리스트 조회 */
	public List<Map<String, Object>> getUpcomingList(String memberIdx, String reserveResult) {
		Map<String, Object> params = new HashMap<>();
		params.put("member_idx", memberIdx);
		params.put("reserve_result", reserveResult);
		List<Map<String, Object>> result = reservationListMapper.getMyReservationList(params);
		return result;
	}

//	public boolean cancelReservation(String memberIdx, int  reserveIdx) {
//	    // 예약 상태 취소로 변경 쿼리 수행 예시
//	    int updateCount = reservationListMapper.updateReservationStatus(reserveIdx, memberIdx, "rsrt_03");
//	    return updateCount > 0;
//	}

	@Transactional
	public boolean cancelReservation(String memberIdx, int reserveIdx) {
		// 1. 예약 상태를 취소 변경
		int updateCount = reservationListMapper.updateReservationStatus(reserveIdx, memberIdx, "rsrt_03");
		if (updateCount == 0)
			return false; // 예약정보가 없거나 실패 시 종료

		// 2. 취소한 예약의 결제정보 한 건 조회 (예: reference_idx 기반)
		Map<String, Object> payment = reservationListMapper.findPaymentByMemberIdx(memberIdx);
		System.out.println("payment = " + payment);
		// 3. 결제정보가 있고 Q머니 결제라면
		if (payment != null && "pyus_02".equals(payment.get("pay_type"))) {
			Object amountObj = payment.get("payment_amount");
			int amount = (amountObj instanceof Number) ? ((Number) amountObj).intValue()
					: Integer.parseInt(String.valueOf(amountObj));

			// 4. member 테이블 qmoney 칼럼에 결제금액 환불
			Map<String, Object> moneyParams = new HashMap<>();
			moneyParams.put("memberIdx", memberIdx);
			moneyParams.put("amount", amount);
			reservationListMapper.refundQmoney(moneyParams);
			System.out.println("refundQmoney 파라미터: " + moneyParams);
			int refundCount = reservationListMapper.refundQmoney(moneyParams);
			if (refundCount == 0) {
	            System.out.println("Qmoney 환불 업데이트 실패");
	            return false;
	        }
	        System.out.println("환불 반영 결과: " + moneyParams);

			// 5. 결제 row의 pay_type을 환불 상태(pyus_04)로 변경
			Map<String, Object> updateTypeParams = new HashMap<>();
			updateTypeParams.put("paymentIdx", payment.get("payment_idx"));
			updateTypeParams.put("payType", "pyus_04");
			reservationListMapper.updatePaymentType(updateTypeParams);
			 int updatePayCount = reservationListMapper.updatePaymentType(updateTypeParams);
		        if (updatePayCount == 0) {
		            System.out.println("결제 상태 업데이트 실패");
		            return false;
		        }

		}
		return true; 
	}

	// pick 랜덤 값 받기
	public List<Map<String, Object>> getRandomStores() {
		return reservationListMapper.selectRandomStores();
	}

}
