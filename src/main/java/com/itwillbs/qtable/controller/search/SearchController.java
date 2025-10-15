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
	public String search(Model model) {
		model.addAttribute("isSearch", true);
		storeWriteService.selectAllCommonCodeForStore(model);
		searchService.selectSeatCntPriceRange(model);
		List<Map<String,String>> locLargeList = searchService.getRegionLargeCategory();
		model.addAttribute("locLargeList", locLargeList);
		List<Map<String,Object>> locSubList = searchService.getSubLocation();
		model.addAttribute("locSubList", locSubList);
		return "search/search";
	}
}
