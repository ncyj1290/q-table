package com.itwillbs.qtable.service.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.itwillbs.qtable.mapper.search.SearchKeywordListMapper;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;
import com.itwillbs.qtable.vo.search.searchVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class searchService {
	
	private final SearchKeywordListMapper mapper;
//	private static Map<String, String> DAY_MAPPING_MAP = new HashMap<>();
//
//	static {
//	     DAY_MAPPING_MAP = Map.of(
//	         "토,일", "평일",
//	         "토", "평일, 일",
//	         "월", "화~일",
//	         "화", "화 휴무",
//	         "수", "수 휴무",
//	         "목", "목 휴무",
//	         "금", "금 휴무",
//	         "", "매일"
//	     );
//	}
	
	
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
	public List<Map<String, Object>> getResult(searchVO vo) {
		
		String rawSort = vo.getSort();
        String safeSort; // 검증된 값을 담을 변수

        // sql injection 방지용 화이트 리스트 로직 
        switch (rawSort) {
            case "price asc":
                safeSort = "price asc";
                break;
            case "price desc":
                safeSort = "price desc";
                break;
            case "score desc":
                safeSort = "score desc";
                break;
            case "reviewCnt desc":
                safeSort = "reviewCnt desc";
                break;
            default:
                safeSort = "score desc"; 
                break;
        }
        // 검증된 값으로 덮어쓰기
        vo.setSort(safeSort);
		return mapper.getResult(vo);
	}
	
}
