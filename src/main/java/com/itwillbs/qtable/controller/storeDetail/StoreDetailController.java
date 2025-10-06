package com.itwillbs.qtable.controller.storeDetail;

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
		model.addAllAttributes(storeData);

		log.info("storeData: " + storeData.toString());
		log.info("categories: " + storeData.get("categories"));
		log.info("atmosphere: " + storeData.get("atmosphere"));

		return "storeDetail/storeDetailMain";
	}
}
 