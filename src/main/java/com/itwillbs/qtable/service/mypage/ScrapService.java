package com.itwillbs.qtable.service.mypage;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.mypage.ScrapMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapService {

	private final ScrapMapper scrapMapper;

	public boolean toggleScrap(int memberIdx, int storeIdx) {
	    if (scrapMapper.existsScrap(memberIdx, storeIdx) > 0) {
	        scrapMapper.deleteScrap(memberIdx, storeIdx);
	        return false; 
	    } else {
	        scrapMapper.insertScrap(memberIdx, storeIdx);
	        return true; 
	    }
	}


    // 스크랩 목록 조회
    public List<Map<String, Object>> getScrapList(int memberIdx) {
        return scrapMapper.selectScrapList(memberIdx);
    }
    
	
}
