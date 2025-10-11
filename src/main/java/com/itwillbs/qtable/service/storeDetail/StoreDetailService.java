package com.itwillbs.qtable.service.storeDetail;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.storeDetail.StoreDetailMapper;
import com.itwillbs.qtable.vo.PageResponse;

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

		// 매장 존재 여부 체크
		if (result == null) {
			return new HashMap<>();
		}

		// 매장 이미지 조회 (null 방어)
		List<String> images = storeMapper.getStoreImage(storeIdx);
		result.put("store_images", images != null && !images.isEmpty()
			? images : List.of("/img/logo.png"));

		// 카테고리 조회 (null 방어)
		List<String> categories = storeMapper.getStoreCategories(storeIdx);
		result.put("categories", categories != null ? categories : List.of());

		// 매장 휴일 조회 (null 방어)
		List<String> holiday = storeMapper.getStoreHoliday(storeIdx);
		result.put("holiday", holiday != null ? holiday : List.of());

		// 분위기 조회 (null 방어)
		List<String> atmosphere = storeMapper.getStoreAtmosphere(storeIdx);
		result.put("atmosphere", atmosphere != null ? atmosphere : List.of());

		// 편의시설 조회 (null 방어)
		List<Map<String, Object>> amenities = storeMapper.getStoreAmenities(storeIdx);
		result.put("amenities", amenities != null ? amenities : List.of());

		return result;
	}
	
	// 매장 메뉴 섹션 조회
	public Map<String, Object> getMenuInfo(Integer storeIdx) {
		Map<String, Object> result = new HashMap<>();

		// 메뉴 리스트 조회 (현재 ajax에서 랜더링중이라 필요 x, 추후에 전체메뉴 조회 할 일 있을까봐 주석처리) 
//		List<Map<String, Object>> menuList = storeMapper.getMenu(storeIdx, null, null);
//		result.put("menuList", menuList != null ? menuList : List.of());

		// 메뉴판 이미지 조회 (null 방어)
		List<String> menuBoardImages = storeMapper.getMenuBoardImages(storeIdx);
		result.put("menuBoardImages", menuBoardImages != null ? menuBoardImages : List.of());

		// 식자재 조회 (null 방어)
		List<Map<String, Object>> ingredientList = storeMapper.getIngredients(storeIdx);
		result.put("ingredientList", ingredientList != null ? ingredientList : List.of());

		return result;
	}
	
	// 리뷰 정렬 옵션 공통코드 조회
	public List<Map<String, Object>> getReviewSortOptions() {
		List<Map<String, Object>> sortOptions = storeMapper.getReviewSortOptions();
		return sortOptions != null ? sortOptions : List.of();
	}
	
	// 매장 별점 정보 조회
	public Map<String, Object> getReviewScoreInfo(Integer storeIdx) {
		Map<String, Object> scoreInfo = storeMapper.getReviewScoreInfo(storeIdx);

		// null 방어 + 기본값 설정
		if (scoreInfo == null) {
			Map<String, Object> result = new HashMap<>();
			result.put("avgScore", 0.0);
			result.put("reviewCount", 0);
			return result;
		}

		// avgScore가 null이면 0.0으로 대체
		if (scoreInfo.get("avgScore") == null) {
			scoreInfo.put("avgScore", 0.0);
		}

		return scoreInfo;
	}
	
	// 리뷰 별점 분포 조회
	public Map<String, Object> getScoreDistribution(Integer storeIdx) {
	    List<Map<String, Object>> scoreDistribution = storeMapper.getScoreDistribution(storeIdx);

	    Map<String, Object> result = new LinkedHashMap<>();

	    // null 방어
	    if (scoreDistribution == null || scoreDistribution.isEmpty()) {
	        // 기본값 설정 (모든 별점 0으로)
	        for (int i = 5; i >= 1; i--) {
	            result.put("score" + i + "Count", 0);
	            result.put("score" + i + "Percent", 0.0);
	        }
	        return result;
	    }

	    for (Map<String, Object> item : scoreDistribution) {
	        Integer score = ((Number) item.get("score")).intValue();
	        Integer count = ((Number) item.get("count")).intValue();
	        Double percent = ((Number) item.get("percent")).doubleValue();

	        // scoreCount=1, score1Percent=66.7 형태로 map에 저장
	        result.put("score" + score + "Count", count);
	        result.put("score" + score + "Percent", percent);
	    }

	    return result;
	}
	
	
	// 예약 가능 시간 조회
	public List<String> getAvailableReservationTimes(Integer storeIdx) {
		Map<String, Object> storeInfo = storeMapper.getStoreBasicInfo(storeIdx);

		// storeInfo null 체크
		if (storeInfo == null) {
			return List.of();
		}

		// 24시간 영업 여부 true면 모든시간 반환
		Boolean is24Hour = (Boolean) storeInfo.get("is_24hour");
		if (Boolean.TRUE.equals(is24Hour)) {
			List<String> allTimes = storeMapper.getAllTimeCodes();
			return allTimes != null ? allTimes : List.of();
		}

		String openTime = (String) storeInfo.get("open_time");
		String closeTime = (String) storeInfo.get("close_time");

		// 영업 시간 null 체크
		if (openTime == null || closeTime == null) {
			return List.of();
		}

		// 24시간 영업 여부 false면 영업시작시간 ~ 영업종료시간 사이의 시간들 리턴
		List<String> times = storeMapper.getTimeCodesBetween(openTime, closeTime);
		return times != null ? times : List.of();
	}

	// 리뷰 페이지네이션 조회
	public PageResponse<Map<String, Object>> getReviewsPaged(Integer storeIdx, String sortType, int page, int size) {
		int offset = PageResponse.getOffset(page, size); // offset 계산
		List<Map<String, Object>> reviews = storeMapper.getReview(storeIdx, sortType, offset, size); // 리뷰 데이터 조회
		int totalCount = storeMapper.getReviewCount(storeIdx, sortType); // 전체 개수 조회

		return new PageResponse<>(reviews, page, size, totalCount);
	}
	
	// 메뉴 페이지네이션 조회
	public PageResponse<Map<String, Object>> getMenuPaged(Integer storeIdx, Integer page, Integer size) {
		int offset = PageResponse.getOffset(page, size); // offset 계산
		List<Map<String, Object>> menu = storeMapper.getMenu(storeIdx, offset, size); // 메뉴 데이터 조회
		int totalCount = storeMapper.getMenuCount(storeIdx); // 전체 개수 조회

		return new PageResponse<>(menu, page, size, totalCount);
	}

}
