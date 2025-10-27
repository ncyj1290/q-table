package com.itwillbs.qtable.controller.storeManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.service.storeManagement.StoreDataService;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;


/* 매장 통계 관련 컨트롤러 */
@Controller
public class StoreStatistiController {
	
	@Autowired
	StoreDataService storeDataService;

	/* 매장ㅇ 그 그그그그그그 통계 페이지 이동 */
	@GetMapping("store_statistic")
	public String storeStatistic(Model model, @AuthenticationPrincipal QtableUserDetails user) {
		
		/* 프로필 정보 가져와서 집어넣는건데 spData란 이름으로 모델에 집어넣게 될거임 */
		storeDataService.injectStoreProfileByOwnerIdx(model, user.getMember().getMemberIdx());
		StoreVO sData = (StoreVO) model.getAttribute("spData");
		
		return "storeManagement/storeStatistic";
	}
	
	
	
	
	
	
	
}
