package com.itwillbs.qtable.controller.storeManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.entity.Store;
import com.itwillbs.qtable.service.storeManagement.StoreDataService;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;

@Controller
public class StoreReservationController {
	
	@Autowired
	StoreDataService storeDataService;
	
	/* ================================================= */
	/* 예약 목록 페이지 */
	@GetMapping("/store_reservation_list")
	public String storeReservationList(@AuthenticationPrincipal QtableUserDetails user, Model model) {
		
//		StoreVO storeData = storeDataService.selectStoreProfileByOwnerIdx(user.getMember().getMemberIdx());
//		model.addAttribute("storeData", storeData);
		
		/* 프로필 정보 가져와서 집어넣는건데 spData란 이름으로 모델에 집어넣게 될거임 */
		storeDataService.injectStoreProfileByOwnerIdx(model, user.getMember().getMemberIdx());
		
		return "storeManagement/storeReservationList";
	}
	
	/* 예약 상세 페이지 */
	@GetMapping("/store_reservation_detail")
	public String storeReservationDetail() {
		return "storeManagement/storeReservationDetail";
	}
	

}
