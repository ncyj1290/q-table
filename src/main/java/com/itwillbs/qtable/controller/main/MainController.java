package com.itwillbs.qtable.controller.main;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.qtable.entity.Image;
import com.itwillbs.qtable.entity.Store;
import com.itwillbs.qtable.service.StoreService;
import com.itwillbs.qtable.service.image.ImageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final StoreService storeService;
	private final ImageService imageService;
	
	
	@GetMapping("/")
	public String home(Model model) {
	    List<Store> stores = storeService.getAllStores();

	    // 1. 스토어 idx 리스트 수집
	    List<Integer> storeIdx = stores.stream()
	                                   .map(Store::getStoreIdx)
	                                   .toList();

	    // 2. 한 번의 쿼리로 모든 메인 이미지 조회
	    List<Image> mainImages = imageService.getMainImages("imguse_01", storeIdx);

	    // 3. StoreIdx 기준 Map 생성 (value 타입 Image로)
	    Map<Integer, Image> imageMap = mainImages.stream()
	                                             .collect(Collectors.toMap(Image::getTargetIdx, img -> img));

	    // 4. Store에 이미지 세팅
	    stores.forEach(store -> store.setMainImage(imageMap.get(store.getStoreIdx())));

	    model.addAttribute("stores", stores);
	    return "index"; 
	}
}
