package com.itwillbs.qtable.controller.search;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {
	
	@GetMapping("search")
	public String search(Model model) {
		model.addAttribute("isSearch", true);
		return "search/search";
	}
	
}
