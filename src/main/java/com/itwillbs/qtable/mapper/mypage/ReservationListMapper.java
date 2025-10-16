package com.itwillbs.qtable.mapper.mypage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReservationListMapper {

	// 예약현황 조회
	List<Map<String, Object>> getMyReservationList(Map<String, Object> params);








}