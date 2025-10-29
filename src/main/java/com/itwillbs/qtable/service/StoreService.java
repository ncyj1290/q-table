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
	    
	    public List<Store> getPopularRegions() {
	        List<Object[]> results = storeRepository.findPopularRegionsByReservationCountNative();

	        return results.stream().map(obj -> {
	            Store store = new Store();

	            String region = (String) obj[0];
	            Long reservationCount = ((Number) obj[1]).longValue();

	            store.setSigungu(region);
	            store.setStoreCount(reservationCount);
	            // 이미지 매핑
	            String imageUrl = switch (region) {
	                case "중구" -> "/img/local/Myeongdong.png";
	                case "해운대구" -> "/img/local/Haeundae.png";
	                case "마포구" -> "/img/local/Hongdae.png";
	                case "수영구" -> "/img/local/Gwangalli.png";
	                case "제주시" -> "/img/local/jeju.png";
	                case "부산진구" -> "/img/local/Busanjin.png";
	                case "강서구" -> "/img/local/Gangseo.png";
	                case "강동구" -> "/img/local/Gangdong.png";
	                case "익산시" -> "/img/local/Iksan.png";
	                case "여수시" -> "/img/local/Yeosu.png";
	                case "경주시" -> "/img/local/Gyeongju.png";
	                case "김해시" -> "/img/local/Gimhae.png";
	                case "창녕군" -> "/img/local/Changnyeong.png";
	                case "산청군" -> "/img/local/Sancheong.png";
	                case "군위군" -> "/img/local/Gunwi.png";
	                case "갈매로" -> "/img/local/Galmae.png";
	                
	                
	                default -> "/img/local/default.png";
	            };
	            store.setImageUrl(imageUrl);

	            return store;
	        }).collect(Collectors.toList());
	    }
	    
	    
	    
}
