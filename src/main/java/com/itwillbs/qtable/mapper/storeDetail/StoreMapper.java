package com.itwillbs.qtable.mapper.storeDetail;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMapper {

	// 매장 기본 정보 조회  
	Map<String, Object> getStoreDetail(Integer storeIdx);

	// 매장 카테고리 조회
	List<Map<String, Object>> getStoreCategories(Integer storeIdx);
	
	// 매장 분위기 조회
	List<Map<String, Object>> getStoreAtmosphere(Integer storeIdx);
	
}
