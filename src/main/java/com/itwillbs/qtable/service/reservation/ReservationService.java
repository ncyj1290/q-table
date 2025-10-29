package com.itwillbs.qtable.service.reservation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.mapper.reservation.ReservationMapper;
import com.itwillbs.qtable.repository.MemberRepository;
import com.itwillbs.qtable.service.pay.PaymentHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class ReservationService {

	private final ReservationMapper reservationMapper;
	private final PaymentHistoryService paymentHistoryService;
	private final MemberRepository memberRepository;

	// 매장 정보 조회
	public Map<String, Object> getStoreInfo(Integer storeIdx) {
		Map<String, Object> storeInfo = reservationMapper.getStoreInfo(storeIdx);
		String storeImg = reservationMapper.getStoreProfileImage(storeIdx);

		// 이미지 URL이 있으면 추가
		if (storeImg != null && !storeImg.isEmpty()) {
			storeInfo.put("store_img", storeImg);
		}

		return storeInfo;
	}

	// 회원 큐머니 조회 (DB에서 최신 값)
	public Integer getMemberQMoney(Integer memberIdx) {
		return memberRepository.findById(memberIdx)
			.map(member -> member.getQMoney())
			.orElse(0);
	}

	// 예약 처리 (등록 또는 수정) + 큐머니 차감 + 결제내역
	@Transactional
	public Map<String, Object> processReservation(Map<String, Object> reservationData, Integer memberIdx) {
		Integer reserveIdx = convertToInteger(reservationData.get("reserve_idx"));

		// reserve_idx가 있으면 수정, 없으면 등록
		if (reserveIdx != null) {
			return updateReservation(reservationData, memberIdx);
		} else {
			return insertReservation(reservationData, memberIdx);
		}
	}

	// 예약 등록 + 큐머니 차감 + 결제내역
	@Transactional
	public Map<String, Object> insertReservation(Map<String, Object> reservationData, Integer memberIdx) {
		// 1. 매장 정보 조회
		Integer storeIdx = convertToInteger(reservationData.get("store_idx"));
		Map<String, Object> storeInfo = reservationMapper.getStoreInfo(storeIdx);

		// 예약금 계산
		Integer deposit = convertToInteger(storeInfo.get("deposit"));
		Integer personCount = convertToInteger(reservationData.get("person_count"));
		Integer totalAmount = deposit * personCount;

		// 2. 중복 예약 확인
		String reserveDate = String.valueOf(reservationData.get("reserve_date"));
		int duplicateCount = reservationMapper.checkDuplicateReservation(memberIdx, storeIdx, reserveDate);

		if (duplicateCount > 0) {
			throw new RuntimeException("해당 날짜에 이미 예약이 존재합니다.");
		}

		Integer reserveIdx = null;

		try {
			// 3. 예약 등록
			reservationData.put("member_idx", memberIdx);
			int result = reservationMapper.insertReservation(reservationData);

			reserveIdx = convertToInteger(reservationData.get("reserve_idx"));

			// 4. 큐머니 차감
			result = reservationMapper.updateMemberQMoney(memberIdx, totalAmount);

			if (result == 0) {
				throw new RuntimeException("큐머니 잔액이 부족합니다. 충전 후 다시 시도해주세요.");
			}

			// 5. 결제 성공 기록 (별도 트랜잭션)
			paymentHistoryService.recordReservationPayment(memberIdx, totalAmount, reserveIdx, "pyst_01");

			// 6. 결과 반환
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("reserve_idx", reserveIdx);
			resultMap.put("total_amount", totalAmount);

			return resultMap;

		} catch (Exception e) {
			// 7. 결제 실패 기록 (별도 트랜잭션)
			paymentHistoryService.recordReservationPayment(
				memberIdx, totalAmount, null, "pyst_02"
			);
			throw e; // 예외 다시 던져서 예약/큐머니 차감은 롤백
		}
	}

	// 예약 수정 (날짜/시간/인원/요청사항만 수정, 큐머니 추가 차감 없음)
	// 예약 환불하고 다시결제 되는 로직 짜야함
	// + 결제 기록남기기까지 
	@Transactional
	public Map<String, Object> updateReservation(Map<String, Object> reservationData, Integer memberIdx) {
		try {
			// 노쇼 횟수 확인
			Member member = memberRepository.findById(memberIdx)
				.orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));
			
			if (member.getNoShowCount() > 2) {
				throw new RuntimeException("노쇼 횟수 초과로 예약 변경이 불가능합니다. 관리자에게 문의해주세요.");
			}

			reservationData.put("member_idx", memberIdx);
			int result = reservationMapper.updateReservation(reservationData);

			if (result == 0) {
				throw new RuntimeException("예약 정보 수정에 실패했습니다. 예약 정보를 확인해주세요.");
			}

			Integer reserveIdx = convertToInteger(reservationData.get("reserve_idx"));
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("reserve_idx", reserveIdx);
			resultMap.put("message", "예약이 성공적으로 변경되었습니다.");

			return resultMap;

		} catch (Exception e) {
			throw new RuntimeException("예약 변경 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	// Object를 Integer로 안전하게 변환 (Number, String 모두 처리)
	private Integer convertToInteger(Object value) {
		if (value == null) return null;	

		if (value instanceof Number) return ((Number) value).intValue();

		if (value instanceof String) return Integer.parseInt((String) value);

		throw new IllegalArgumentException("Cannot convert " + value.getClass() + " to Integer");
	}
}
