package com.itwillbs.qtable.mapper.storeDetail;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreDetailMapper {

	// 매장 기본 정보 조회
	Map<String, Object> getStoreBasicInfo(Integer storeIdx);

	// 매장 카테고리 조회
	List<Map<String, Object>> getStoreCategories(Integer storeIdx);

	// 매장 분위기 조회
	List<Map<String, Object>> getStoreAtmosphere(Integer storeIdx);

	// 매장 편의시설 조회
	List<Map<String, Object>> getStoreAmenities(Integer storeIdx);
	
	// 매장 메뉴 조회
	List<Map<String, Object>> getMenu(Integer storeIdx);
	
	// 매장 리뷰 조회
	List<Map<String, Object>> getReview(Integer storeIdx);
	
	// 리뷰 개수 조회
	Map<String, String> getReviewScoreInfo(Integer storeIdx);
	
}
