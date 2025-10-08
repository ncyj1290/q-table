package com.itwillbs.qtable.mapper.storeDetail;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreDetailMapper {

	// 매장 기본 정보 조회
	Map<String, Object> getStoreBasicInfo(Integer storeIdx);
	
	// 매장 이미지 조회
	List<String> getStoreImage(Integer storeIdx);

	// 매장 카테고리 조회
	List<String> getStoreCategories(Integer storeIdx);

	// 매장 분위기 조회
	List<String> getStoreAtmosphere(Integer storeIdx);

	// 매장 편의시설 조회
	List<String> getStoreAmenities(Integer storeIdx);
	
	// 매장 메뉴 조회
	List<Map<String, Object>> getMenu(Integer storeIdx);
	
	// 메뉴판 이미지 조회
	List<String> getMenuBoardImages (Integer storeIdx);
	
	// 매장 리뷰 조회
	List<Map<String, Object>> getReview(Integer storeIdx);
	
	// 리뷰 개수 조회
	Map<String, String> getReviewScoreInfo(Integer storeIdx);
	
	// 리뷰 별점 분포 조회
	List<Map<String, Object>> getScoreDistribution(Integer storeIdx);
	
}
