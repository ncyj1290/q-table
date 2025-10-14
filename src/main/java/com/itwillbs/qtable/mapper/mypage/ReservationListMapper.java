package com.itwillbs.qtable.mapper.mypage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReservationListMapper {
	/* 마이페이지 예약현황 목록 조회 */
    List<Map<String, Object>> getMyReservationList(@Param("member_idx") String memberIdx, @Param("reserve_result") String reserve_result);
    
//    List<Map<String, Object>> getCanceledList(@Param("reserve_result") String reserve_result, @Param("member_idx") String member_idx);


}