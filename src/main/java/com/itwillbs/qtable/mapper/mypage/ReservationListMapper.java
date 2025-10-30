package com.itwillbs.qtable.mapper.mypage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReservationListMapper {

	// 예약현황 조회
	List<Map<String, Object>> getMyReservationList(Map<String, Object> params);

	//예약상태 업데이트
	int updateReservationStatus(@Param("reserve_idx") int reserve_idx,
					            @Param("member_idx") String member_idx,
					            @Param("reserve_result") String reserve_result);

	// 예약 결제정보 한 건 조회
	Map<String, Object> findPaymentByMemberIdx(@Param("reserve_idx") int reserve_idx,
											   @Param("member_idx") String member_idx);

	//멤버테이블 Q머니 환불
	int refundQmoney(Map<String, Object> params);
	
	//결제 상태 업데이트
	int updatePaymentType(Map<String, Object> params);

	int selectQmoneyByMemberIdx(@Param("member_idx") String member_idx);

	//pick 랜덤 값 받기
	List<Map<String, Object>> selectRandomStores();




}