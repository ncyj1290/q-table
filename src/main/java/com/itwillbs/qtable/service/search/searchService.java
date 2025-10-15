package com.itwillbs.qtable.service.search;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.itwillbs.qtable.mapper.search.SearchKeywordListMapper;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;

import lombok.RequiredArgsConstructor;

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
	
}
