package com.itwillbs.qtable.service.storeDetail;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.storeDetail.StoreMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreDetailService {

	private final StoreMapper storeMapper;

	// 매장 전체 정보 조회
	public Map<String, Object> getStoreInfo(Integer storeIdx) {
		
		// 매장 기본 정보 조회  
		Map<String, Object> result = storeMapper.getStoreDetail(storeIdx);

		// 카테고리 조회
		List<Map<String, Object>> categories = storeMapper.getStoreCategories(storeIdx);
		result.put("categories", categories);
		
		List<Map<String, Object>> atmosphere = storeMapper.getStoreAtmosphere(storeIdx);
		result.put("atmosphere", atmosphere);

		return result;
	}
}
