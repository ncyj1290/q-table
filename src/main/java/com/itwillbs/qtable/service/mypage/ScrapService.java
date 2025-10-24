package com.itwillbs.qtable.service.mypage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.mypage.ScrapMapper;
import com.itwillbs.qtable.mapper.reservation.ReservationMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapService {

	private final ScrapMapper scrapMapper;
	private final ReservationMapper reservationMapper;

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

	// 매장 정보 조회
	// 서비스 클래스 내에 선언
	public Map<String, Object> getStoreInfo(Integer storeIdx) {
	    // 매장 정보 조회
	    Map<String, Object> storeInfo = reservationMapper.getStoreInfo(storeIdx);
	    if (storeInfo == null) {
	        // 데이터 없을 경우 빈 맵 또는 null 반환
	        return new HashMap<>();
	    }

	    // 프로필 이미지 URL 조회
	    String storeImg = reservationMapper.getStoreProfileImage(storeIdx);

	    // 이미지 URL이 있으면 정보에 포함
	    if (storeImg != null && !storeImg.trim().isEmpty()) {
	        storeInfo.put("store_img", storeImg);
	    }

	    return storeInfo;
	}


}
