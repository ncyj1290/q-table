package com.itwillbs.qtable.service.mypage;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.mypage.ScrapMapper;
import com.itwillbs.qtable.vo.myPage.ScrapVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapMapper scrapMapper;

    public void toggleScrap(int memberIdx, int storeIdx) {
        if (scrapMapper.existsScrap(memberIdx, storeIdx) > 0) {
            scrapMapper.deleteScrap(memberIdx, storeIdx);
        } else {
            scrapMapper.insertScrap(memberIdx, storeIdx);
        }
    }

    public List<ScrapVO> getScrapList(int memberIdx) {
        return scrapMapper.selectScrapList(memberIdx);
    }
	
}
