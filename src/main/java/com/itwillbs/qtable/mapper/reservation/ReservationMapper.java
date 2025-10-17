package com.itwillbs.qtable.mapper.reservation;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReservationMapper {

	// 매장 정보 조회 (예약 페이지용)
	Map<String, Object> getStoreInfo(@Param("storeIdx") Integer storeIdx);

	// 예약 등록
	int insertReservation(Map<String, Object> reservationData);

	// 예약금 차감 
	int updateMemberQMoney(@Param("memberIdx") Integer memberIdx, @Param("amount") Integer amount);

	// 매장 이미지 가져오기
	String getStoreProfileImage(Integer storeIdx);

	// 중복 예약 확인
	int checkDuplicateReservation(@Param("memberIdx") Integer memberIdx, @Param("storeIdx") Integer storeIdx, @Param("reserveDate") String reserveDate);
	
	// 결제 내역 등록
	int insertPaymentHistory(@Param("memberIdx") Integer memberIdx, @Param("amount") Integer amount, @Param("reserveIdx") Integer reserveIdx, @Param("status") String status);

}
