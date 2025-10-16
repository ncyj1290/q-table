package com.itwillbs.qtable.controller.storeManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.service.storeManagement.StoreDataService;


@Controller
public class StoreManagementController {
	
	@Autowired
	StoreDataService storeDataService;
	

	/* ================================================= */
	/* 매장 관리자가 이용하는 매장 관리 메인 페이지 */
	@GetMapping("/store_management_main")
	public String storeManagement(Model model, @AuthenticationPrincipal QtableUserDetails user) {
		
		/* 프로필 정보 가져와서 집어넣는건데 spData란 이름으로 모델에 집어넣게 될거임 */
		storeDataService.injectStoreProfileByOwnerIdx(model, user.getMember().getMemberIdx());
		
		return "storeManagement/storeManagementMain";
	}

	
	/* ================================================= */
	/* 구독권 구매 페이지 */
	@GetMapping("/purchase_subscribe")
	public String purchaseSubscribe(Model model, @AuthenticationPrincipal QtableUserDetails user) {
		
		/* 프로필 정보 가져와서 집어넣는건데 spData란 이름으로 모델에 집어넣게 될거임 */
		storeDataService.injectStoreProfileByOwnerIdx(model, user.getMember().getMemberIdx());
		
		return "storeManagement/purchaseSubscribe";
	}
	
	/* ================================================= */
	/* 정산 목록 페이지 */
	@GetMapping("/store_calculate_list")
	public String calculateList(Model model, @AuthenticationPrincipal QtableUserDetails user) {
		
		/* 프로필 정보 가져와서 집어넣는건데 spData란 이름으로 모델에 집어넣게 될거임 */
		storeDataService.injectStoreProfileByOwnerIdx(model, user.getMember().getMemberIdx());
		
		return "storeManagement/storeCalculateList";
	}
	
	/* 정산 상세 페이지 */
	@GetMapping("/store_calculate_detail")
	public String calculateDetail(Model model, @AuthenticationPrincipal QtableUserDetails user) {
		
		/* 프로필 정보 가져와서 집어넣는건데 spData란 이름으로 모델에 집어넣게 될거임 */
		storeDataService.injectStoreProfileByOwnerIdx(model, user.getMember().getMemberIdx());
		
		return "storeManagement/storeCalculateDetail";
	}
	
	
}
