package com.itwillbs.qtable.mapper.storeManagementMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/* 매장 통계 데이터 쿼리 매퍼 */
@Mapper
public interface StoreStatistic {
	
	/* 통계 데이터 받는 쿼리문, 최근 6개월 단위 고정 */
	List<Map<String, Object>> selectStatisticDate(@Param("store_idx") int store_idx, @Param("code") String code);

}
