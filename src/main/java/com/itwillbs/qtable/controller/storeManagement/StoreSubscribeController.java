package com.itwillbs.qtable.controller.storeManagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.mapper.storeManagementMapper.StoreCommonCode;
import com.itwillbs.qtable.service.storeManagement.StoreDataService;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;

/* 맴버쉽 구매 페이지 관련 컨트롤러 */
@Controller
public class StoreSubscribeController {
	
	@Autowired
	StoreDataService storeDataService;

	@Autowired
	StoreCommonCode storeCommonCode;
	
	/* ================================================= */
	/* 구독권 구매 페이지 */
	@GetMapping("/purchase_subscribe")
	public String purchaseSubscribe(Model model, @AuthenticationPrincipal QtableUserDetails user) {
		
		/* 프로필 정보 가져와서 집어넣는건데 spData란 이름으로 모델에 집어넣게 될거임 */
		storeDataService.injectStoreProfileByOwnerIdx(model, user.getMember().getMemberIdx());
		
		/* 구독권 단위 공통코드 목록 */
		List<CommonCodeVO> mUnitList = storeCommonCode.selectCommonCodeForStore("membership_dateunit", null);
		model.addAttribute("mUnitList", mUnitList);

		return "storeManagement/purchaseSubscribe";
	}
	
}
