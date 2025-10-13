package com.itwillbs.qtable.controller.search;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.qtable.service.search.searchService;
import com.itwillbs.qtable.service.storeManagement.StoreWriteService;

import lombok.RequiredArgsConstructor;

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
		return "search/search";
	}
	
}
