package com.itwillbs.qtable.mapper.storeManagementMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.vo.storeManagement.ReservationVO;

@Mapper
public interface StoreReservation {
	
	/* 예약 갯수 계산 쿼리문 -> 페이지네이션에 필요 */
	int countReservationByStoreIdx(@Param("store_idx") int store_idx, @Param("filter") String filter);
	
	/* 해당 매장 idx로 예약 목록 가져오는 쿼리문*/
	List<ReservationVO> selectReservationByStoreIdx(@Param("store_idx") int store_idx, @Param("start_row") int start_row, @Param("list_limit") int list_limit, @Param("filter") String filter);
	
	/* 예약 정보 디테일 가져오는 쿼리문 -> 근데 헛짓거리 하면 안되니까 매장 idx도 같이 */
	ReservationVO selectReservationDetail(@Param("reserve_idx") int reserve_idx, @Param("store_idx") int store_idx);

}
