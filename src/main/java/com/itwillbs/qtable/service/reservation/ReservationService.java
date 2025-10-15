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
	public Map<String, Object> createReservation(Map<String, Object> reservationData, Integer memberIdx) {
		// 1. 매장 정보 조회 (예약금 확인)
		Integer storeIdx = Integer.parseInt(String.valueOf(reservationData.get("store_idx")));
		Map<String, Object> storeInfo = reservationMapper.getStoreInfo(storeIdx);

		// 예약금 계산
		Number depositNum = (Number) storeInfo.get("deposit");
		Integer deposit = depositNum.intValue();
		
		Integer personCount = Integer.parseInt(String.valueOf(reservationData.get("person_count")));
		Integer totalAmount = deposit * personCount;


		// 2. 예약 등록 
		reservationData.put("member_idx", memberIdx);
		int result = reservationMapper.insertReservation(reservationData);

		Number reserveNum = (Number) reservationData.get("reserve_idx");
		Integer reserveIdx = reserveNum.intValue();

		// 3. 큐머니 차감
		result = reservationMapper.updateMemberQMoney(memberIdx, totalAmount);
		
		if (result == 0) {
			throw new RuntimeException("큐머니 차감에 실패했습니다.");
		}

		
		// 결제 내역 저장 짜야함 TODO 
		
		// 4. 결과 반환
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("reserve_idx", reserveIdx);
		resultMap.put("total_amount", totalAmount);

		return resultMap;
	}
}
