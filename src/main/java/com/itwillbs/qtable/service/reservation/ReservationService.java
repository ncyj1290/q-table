package com.itwillbs.qtable.service.reservation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.qtable.mapper.reservation.ReservationMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class ReservationService {

	private final ReservationMapper reservationMapper;

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

	// 예약 등록 + 큐머니 차감 + 결제내역(예정)
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

		// 3. 예약 등록
		reservationData.put("member_idx", memberIdx);
		int result = reservationMapper.insertReservation(reservationData);

		Integer reserveIdx = convertToInteger(reservationData.get("reserve_idx"));

		// 4. 큐머니 차감
		result = reservationMapper.updateMemberQMoney(memberIdx, totalAmount);

		if (result == 0) {
			throw new RuntimeException("큐머니 잔액이 부족합니다. 충전 후 다시 시도해주세요.");
		}

		
		// 결제 내역 저장 짜야함 TODO 
		
		// 4. 결과 반환
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("reserve_idx", reserveIdx);
		resultMap.put("total_amount", totalAmount);

		return resultMap;
	}

	// Object를 Integer로 안전하게 변환 (Number, String 모두 처리)
	private Integer convertToInteger(Object value) {
		if (value == null) return null;	

		if (value instanceof Number) return ((Number) value).intValue();

		if (value instanceof String) return Integer.parseInt((String) value);

		throw new IllegalArgumentException("Cannot convert " + value.getClass() + " to Integer");
	}
}
