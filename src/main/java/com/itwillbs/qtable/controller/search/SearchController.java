package com.itwillbs.qtable.controller.search;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.qtable.service.search.searchService;
import com.itwillbs.qtable.service.storeManagement.StoreWriteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@Controller
public class SearchController {
	
	private final StoreWriteService storeWriteService;
	private final searchService searchService;
	
	@GetMapping("search")
	public String search(
			Model model) {
		
		model.addAttribute("isSearch", true);
		storeWriteService.selectAllCommonCodeForStore(model);
		searchService.selectSeatCntPriceRange(model);
		List<Map<String,String>> locLargeList = searchService.getRegionLargeCategory();
		model.addAttribute("locLargeList", locLargeList);
		List<Map<String,Object>> locSubList = searchService.getSubLocation();
		model.addAttribute("locSubList", locSubList);
		List<Map<String,Object>> perCntList = searchService.getPerCnt();
		model.addAttribute("perCntList", perCntList);
		List<Map<String,Object>> timeList = searchService.getTime();
		model.addAttribute("timeList", timeList);
		return "search/search";
	}
	
	@GetMapping("/api/search")
	public String search(@RequestParam(value = "loc", required = false) List<String> locList,
			@RequestParam(value = "food", required = false) List<String> foodList,
			@RequestParam(value ="atmosphere", required = false) List<String> atmosphereList,
			@RequestParam(value ="facility", required = false) List<String> facility,
			@RequestParam(value ="personCnt", required = false) String personCnt,
			@RequestParam(value ="sort", required = false) String sort,
			@RequestParam(value ="price", required = false) List<String> price,
			@RequestParam(value ="day", required = false) String day,
			@RequestParam(value ="time", required = false) String time,
			@RequestParam(value ="query", required = false) String query
			) {
		
		log.info("입력값");
		log.info("지역" + locList);
		log.info("음식" + foodList);
		log.info("분위기" + atmosphereList);
		log.info("편의시설" + facility);
		log.info("좌석수" + personCnt);
		log.info("가격" + price);
		log.info("정렬" + sort);
		log.info("날짜" + day);
		log.info("시간" + time);
		log.info("입력값" + query);
		return "search/searchResult :: searchResult";
	}
	
	
}
