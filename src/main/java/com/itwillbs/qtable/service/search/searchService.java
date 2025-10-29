package com.itwillbs.qtable.service.search;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.itwillbs.qtable.mapper.search.SearchKeywordListMapper;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;
import com.itwillbs.qtable.vo.search.searchVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@Service
public class searchService {
	
	private final SearchKeywordListMapper mapper;
	
	
	public void selectSeatCntPriceRange(Model model) {
		List<CommonCodeVO> seatCntList = mapper.selectSeatCntPriceRange("seat_count");
		model.addAttribute("seatCntList", seatCntList);
		List<CommonCodeVO> priceRangeList = mapper.selectSeatCntPriceRange("price_range");
		model.addAttribute("priceRangeList", priceRangeList);
	}
	
	public List<Map<String,String>> getRegionLargeCategory() {
		return mapper.getRegionLargeCategory();
	}
	
	public List<Map<String, Object>> getSubLocation() {
		return mapper.getSubLocation();
	}
	public List<Map<String, Object>> getPerCnt() {
		return mapper.getPerCnt();
	}
	public List<Map<String, Object>> getTime() {
		return mapper.getTime();
	}
	
	// 검색결과 정보들 가져오기 
	public Map<String, Object> getResult(searchVO vo) {
		String query = vo.getQuery(); 
		if (query != null && !query.trim().isEmpty()) {
		    String[] words = query.split("\\s+"); 
		    List<String> keywords = Arrays.stream(words)
		            .filter(word -> !word.trim().isEmpty()) 
		            .collect(Collectors.toList());
		    vo.setKeywords(keywords);
		    vo.setQuery(null); 
		} else {
		    vo.setKeywords(null);
		    vo.setQuery(null);
		}
		
		vo.setLimit(vo.getLimit() + 1);
		
		Map<String, Object> hasNext = new HashMap<String,Object>();
		List<Map<String,Object>> storeList = mapper.getResult(vo);
		int listSize = storeList.size();
		if(listSize == 11) {
			storeList.removeLast();
			hasNext.put("hasNext", true);
		} else {
			hasNext.put("hasNext", false);
		}
		
		// 플레그와 데이터를 함께 가져가기 위함 
		Map<String, Object> resultList = new HashMap<>();
		resultList.put("storeList", storeList);
		resultList.put("hasNext", hasNext);
		
		return resultList;
	}
	
}
