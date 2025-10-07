package com.itwillbs.qtable.controller.storeDetail;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.qtable.service.storeDetail.StoreDetailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class StoreDetailController {
	
	private final StoreDetailService storeService;
	
	// 식당 상세 페이지 이동
	@GetMapping("store_detail_main")
	public String storeDeatilMain(Model model) {
		
		Map<String, Object> storeData = storeService.getStoreInfo(1);
		List<Map<String, Object>> menuData = storeService.getStoreMenu(1);
		List<Map<String, Object>> reviewData = storeService.getStoreReview(1);
		Map<String, String> scoreData = storeService.getReviewScoreInfo(1);
		
		model.addAllAttributes(storeData);
		model.addAttribute("menu", menuData);
		model.addAttribute("review", reviewData);
		model.addAttribute("score", scoreData);
		
		// 로그모음
//		log.info("storeData: " + storeData.toString()); // 식당 정보 섹션 
//		log.info("categories: " + storeData.get("categories")); // 음식카테고리
//		log.info("atmosphere: " + storeData.get("atmosphere")); // 분위기 
//		log.info("amenities: " + storeData.get("amenities")); // 편의시설
//		log.info("menu: " + menuData.toString()); // 메뉴 섹션
//		log.info("review: " + reviewData.toString()); // 리뷰 섹션

		return "storeDetail/storeDetailMain";
	}
}
 