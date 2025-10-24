package com.itwillbs.qtable.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.qtable.entity.Store;
import com.itwillbs.qtable.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {
	private final StoreRepository storeRepository;
	
	  public List<Store> getAllStores() {
	        return storeRepository.findAll();
	    }
//	    public List<Store> getStoresOrderByAvgRating() {
//	        return storeRepository.findAllOrderByAvgRatingDescNative();
//	    }
	    
	    public List<Store> getStoresOrderByAvgRating() {
	        List<Object[]> results = storeRepository.findStoresWithAvgScoreNative();
	        return results.stream().map(obj -> {
	            Store store = new Store();
	            store.setStoreIdx(((Number)obj[0]).intValue());
	            store.setStoreName((String)obj[1]);
	            store.setSido((String)obj[2]);
	            store.setDeposit(obj[3] != null ? ((Number)obj[3]).intValue() : null); // 예약금
	            store.setFullAddress((String)obj[4]);
	            store.setOpenTime((String)obj[5]);
	            store.setCloseTime((String)obj[6]);
	            store.setAvgScore(((Number)obj[7]).doubleValue()); // @Transient 필드
	            store.setReviewCount(((Number)obj[8]).intValue());
	            return store;
	        }).collect(Collectors.toList());
	    }
	    
	    
	    
	    
}
