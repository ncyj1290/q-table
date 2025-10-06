package com.itwillbs.qtable.mapper.storeManagementMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;

@Mapper
public interface StoreWrite {
	
	/* 매장 등록 */
	int insertNewStore(StoreVO storeVO);
	
	/* 휴일 등록 */
	int insertNewHoliday(@Param("store_idx") int store_idx, @Param("store_holiday") String store_holiday);
	
	/* 편의 시설 등록 */
	int insertNewAmenity(@Param("store_idx") int store_idx, @Param("store_amenity") String store_amenity);
	
	/* 분위기 등록 */
	int insertNewAtmosphere(@Param("store_idx") int store_idx, @Param("store_atmosphere") String store_atmosphere);
	
	/* 카테고리 등록 */
	int insertNewCategory(@Param("store_idx") int store_idx, @Param("store_category") String store_category);
	
	
}
