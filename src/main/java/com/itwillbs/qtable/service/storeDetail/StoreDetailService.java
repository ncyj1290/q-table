package com.itwillbs.qtable.service.storeDetail;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.storeDetail.StoreDetailMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreDetailService {

	private final StoreDetailMapper storeMapper;

	// 매장 정보 섹션 조회
	public Map<String, Object> getStoreInfo(Integer storeIdx) {

		// 매장 기본 정보 조회
		Map<String, Object> result = storeMapper.getStoreBasicInfo(storeIdx);

		// 카테고리 조회
		List<Map<String, Object>> categories = storeMapper.getStoreCategories(storeIdx);
		result.put("categories", categories);

		// 분위기 조회
		List<Map<String, Object>> atmosphere = storeMapper.getStoreAtmosphere(storeIdx);
		result.put("atmosphere", atmosphere);

		// 편의시설 조회
		List<Map<String, Object>> amenities = storeMapper.getStoreAmenities(storeIdx);
		result.put("amenities", amenities);

		return result;
	}
	
	// 매장 메뉴 섹션 조회
	public List<Map<String, Object>> getStoreMenu(Integer storeIdx) {

		return storeMapper.getMenu(storeIdx);
	}
	
	// 매장 리뷰 섹션 조회
	public List<Map<String, Object>> getStoreReview(Integer storeIdx) {
		return storeMapper.getReview(storeIdx);
	}
	
	// 매장 별점 정보 조회
	public Map<String, String> getReviewScoreInfo(Integer storeIdx) {
		return storeMapper.getReviewScoreInfo(storeIdx);
	}
}
