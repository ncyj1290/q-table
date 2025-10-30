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
import com.itwillbs.qtable.service.member.MemberJoinService;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;
import com.itwillbs.qtable.vo.search.searchVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final StoreService storeService;
	private final ImageService imageService;
	private final MemberJoinService memberJoinService;

	@GetMapping("/")
	public String home(Model model) {
		List<Store> stores = storeService.getStoresOrderByAvgRating();

		// 1. 스토어 idx 리스트 수집
		List<Integer> storeIdx = stores.stream().map(Store::getStoreIdx).toList();

		// storeIdx에 해당하는 매장의 프로필 이미지(imguse_01)를 모두 조회
		List<Image> mainImages = imageService.getMainImages("imguse_01", storeIdx);
		// List<Image> localImages = imageService.getLocalImages("imguse_06", storeIdx);

		// 3. StoreIdx 기준 Map 생성 (value 타입 Image로)
		Map<Integer, Image> imageMap = mainImages.stream().collect(Collectors.toMap(Image::getTargetIdx, img -> img));

		// 4. Store에 이미지 세팅
		stores.forEach(store -> store.setMainImage(imageMap.get(store.getStoreIdx())));

		// 전체 stores 리스트에서 Sido가 "부산"인 매장만 필터링하여 busanStores 리스트에 저장
		List<Store> busanStores = stores.stream().filter(s -> "부산".equals(s.getSido())).toList();
		List<Store> seoulStores = stores.stream().filter(s -> "서울".equals(s.getSido())).toList();

		// 그룹 코드("time")를 기준으로 공통 코드를 조회하는 기능
		List<CommonCodeVO> timeCodes = memberJoinService.getCodesByGroup("time");
		// 인기 지역 조회
		
		model.addAttribute("regions", storeService.getPopularRegions());
        
		// 6. 모델에 추가
		model.addAttribute("stores", stores);
		model.addAttribute("busanStores", busanStores);
		model.addAttribute("seoulStores", seoulStores);

		// 공통 코드 모델에담아서
		model.addAttribute("timeCodes", timeCodes);
		return "index";
	}
}
