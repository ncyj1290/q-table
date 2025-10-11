package com.itwillbs.qtable.controller.storeDetail;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.qtable.service.storeDetail.StoreDetailService;
import com.itwillbs.qtable.vo.PageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class StoreDetailController {
	
	private final StoreDetailService storeService;
	
	// 식당 상세 페이지 이동
	@GetMapping("store_detail_main")
	public String storeDetailMain(@RequestParam("store_idx") Integer storeIdx, Model model) {

		// 매장 기본 정보 조회
		Map<String, Object> storeData = storeService.getStoreInfo(storeIdx);
		model.addAllAttributes(storeData);

		// 매장 메뉴 섹션 조회 
		Map<String, Object> menuData = storeService.getMenuInfo(storeIdx);
		model.addAllAttributes(menuData);

		// 리뷰 정렬 옵션 공통코드 조회
		List<Map<String, Object>> sortOptions = storeService.getReviewSortOptions();
		model.addAttribute("sortOptions", sortOptions);

		// 매장 별점 정보 조회
		Map<String, Object> scoreData = storeService.getReviewScoreInfo(storeIdx);
		model.addAttribute("score", scoreData);

		// 리뷰 별점 분포 조회
		Map<String, Object> scoreDistribution = storeService.getScoreDistribution(storeIdx);
		model.addAllAttributes(scoreDistribution);

		// 예약 가능 시간 조회
		List<String> reservationTimeData = storeService.getAvailableReservationTimes(storeIdx);
		model.addAttribute("availableTimes", reservationTimeData);
		

//		log.info("storeData: " + storeData.toString()); // 식당 정보 섹션
//		log.info("categories: " + storeData.get("categories")); // 음식카테고리
//		log.info("atmosphere: " + storeData.get("atmosphere")); // 분위기
//		log.info("amenities: " + storeData.get("amenities")); // 편의시설
//		log.info("menuData: " + menuData.toString()); // 메뉴 섹션
//		log.info("review: " + reviewData.toString()); // 리뷰 섹션
//		log.info("scoreDistribution: " + scoreDistribution.toString()); // 리뷰 별점 분포
//		log.info("availableTimes: " + reservationTimeData.toString()); // 예약 가능 시간

		return "storeDetail/storeDetailMain";
	}

	// 리뷰 페이지네이션, 정렬 AJAX API
	@GetMapping("/api/storeDetail/reviews")
	@ResponseBody
	public PageResponse<Map<String, Object>> getReviewsPaged(
			@RequestParam("store_idx") Integer storeIdx,
			@RequestParam(value = "sort_type", defaultValue = "rvs_01") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "3") int size) {

		
		return storeService.getReviewsPaged(storeIdx, sortType, page, size);
	}
	
	
	// 메뉴 페이지네이션 AJAX API
	@GetMapping("/api/storeDetail/menu")
	@ResponseBody
	public PageResponse<Map<String, Object>> getMenuPaged(
			@RequestParam("store_idx") Integer storeIdx,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "6") int size) {

		
		return storeService.getMenuPaged(storeIdx, page, size);
	}
}
