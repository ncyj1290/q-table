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

}
