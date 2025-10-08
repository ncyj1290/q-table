package com.itwillbs.qtable.service.storeDetail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.storeDetail.StoreDetailMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class StoreDetailService {

	private final StoreDetailMapper storeMapper;

	// 매장 정보 섹션 조회
	public Map<String, Object> getStoreInfo(Integer storeIdx) {

		// 매장 기본 정보 조회
		Map<String, Object> result = storeMapper.getStoreBasicInfo(storeIdx);
		
		// 매장 이미지 조회
		List<String> images = storeMapper.getStoreImage(storeIdx);
		result.put("store_images", images);

		// 카테고리 조회
		List<String> categories = storeMapper.getStoreCategories(storeIdx);
		result.put("categories", categories);

		// 분위기 조회
		List<String> atmosphere = storeMapper.getStoreAtmosphere(storeIdx);
		result.put("atmosphere", atmosphere);

		// 편의시설 조회
		List<String> amenities = storeMapper.getStoreAmenities(storeIdx);
		result.put("amenities", amenities);

		return result;
	}
	
	// 매장 메뉴 섹션 조회
	public Map<String, Object> getMenuInfo(Integer storeIdx) {
		Map<String, Object> result = new HashMap<>();

		// 메뉴 리스트 조회
		List<Map<String, Object>> menuList = storeMapper.getMenu(storeIdx);
		result.put("menuList", menuList);

		// 메뉴판 이미지 조회
		List<String> menuBoardImages = storeMapper.getMenuBoardImages(storeIdx);
		result.put("menuBoardImages", menuBoardImages);

		return result;
	}
	
	// 매장 리뷰 섹션 조회
	public List<Map<String, Object>> getStoreReview(Integer storeIdx) {
		return storeMapper.getReview(storeIdx);
	}
	
	// 매장 별점 정보 조회
	public Map<String, String> getReviewScoreInfo(Integer storeIdx) {
		return storeMapper.getReviewScoreInfo(storeIdx);
	}
	
	// 리뷰 별점 분포 조회
	public Map<String, Object> getScoreDistribution(Integer storeIdx) {
	    List<Map<String, Object>> scoreDistribution = storeMapper.getScoreDistribution(storeIdx);
	    
	    Map<String, Object> result = new LinkedHashMap<>();
	    
	    for (Map<String, Object> item : scoreDistribution) {
	        Integer score = ((Number) item.get("score")).intValue();
	        Integer count = ((Number) item.get("count")).intValue();
	        Double percent = ((Number) item.get("percent")).doubleValue();
	        
	        // scoreCount=1, score1Percent=66.7 형태로 map에 저장
	        result.put("score" + score + "Count", count);
	        result.put("score" + score + "Percent", percent);
	    }
	    
//	    log.info(scoreDistribution.toString());
//	    log.info(result.toString());
	    
	    return result;
	}
}
