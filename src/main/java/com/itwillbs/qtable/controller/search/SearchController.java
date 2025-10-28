package com.itwillbs.qtable.controller.search;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.service.search.searchService;
import com.itwillbs.qtable.service.storeManagement.StoreWriteService;
import com.itwillbs.qtable.vo.search.searchVO;

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
			Model model,
			searchVO vo
			,@AuthenticationPrincipal QtableUserDetails details) {
		
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
		
		log.info(vo.toString());
		if(vo.isEmpty()) {
			model.addAttribute("storeList", new searchVO());
			model.addAttribute("isFirst", true);
			return "search/search";
		}
		Integer memberIdx = Optional.ofNullable(details)
								.map(d -> d.getMember())       
								.map(m -> m.getMemberIdx())    
								.orElse(null);                 
		if (memberIdx == null) log.info("비회원입니다");
		vo.setMember_idx(memberIdx);
		Map<String,Object> result = searchService.getResult(vo);
		model.addAttribute("storeList", result.get("storeList"));
		model.addAttribute("hasNext", result.get("hasNext"));
		return "search/search";
	}
	
	@GetMapping("/api/search")
	public String getResult(searchVO vo
			, Model model
			, @AuthenticationPrincipal QtableUserDetails details) {
		
		log.info("입력값");
		log.info("지역" + vo.getLoc());
		log.info("음식" + vo.getFood());
		log.info("분위기" + vo.getAtmosphere());
		log.info("편의시설" + vo.getFacility());
		log.info("좌석수" + vo.getPersonCnt());
		log.info("가격" + vo.getPrice());
		log.info("정렬" + vo.getSort());
		log.info("날짜" + vo.getDay());
		log.info("시간" + vo.getTime());
		log.info("입력값" + vo.getQuery());
		log.info("가격cs " + vo.getPriceCs());
		log.info("별점cs " + vo.getScoreCs());
		log.info("리뷰수cs " + vo.getReviewCs());
		log.info("커ㅏ서 " + vo.getCursor());
		
		Integer memberIdx = Optional.ofNullable(details)
					                .map(d -> d.getMember())       
					                .map(m -> m.getMemberIdx())    
					                .orElse(null);                 
		if (memberIdx == null) log.info("비회원입니다");
		vo.setMember_idx(memberIdx);
		Map<String,Object> result = searchService.getResult(vo);
		model.addAttribute("storeList", result.get("storeList"));
		model.addAttribute("hasNext", result.get("hasNext"));
		return "search/searchResult :: searchResult";
	}
	
	
}
