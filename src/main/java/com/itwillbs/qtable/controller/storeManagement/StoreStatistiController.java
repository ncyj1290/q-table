package com.itwillbs.qtable.controller.storeManagement;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.mapper.storeManagementMapper.StoreCommonCode;
import com.itwillbs.qtable.service.storeManagement.StoreDataService;
import com.itwillbs.qtable.service.storeManagement.StoreStatisticService;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;


/* 매장 통계 관련 컨트롤러 */
@Controller
public class StoreStatistiController {
	
	@Autowired
	StoreDataService storeDataService;

	@Autowired
	StoreCommonCode storeCommonCode;
	
	@Autowired
	StoreStatisticService storeStatisticService;
	
	
	/* 매장ㅇ 그 그그그그그그 통계 페이지 이동 */
	@GetMapping("store_statistic")
	public String storeStatistic(Model model, @AuthenticationPrincipal QtableUserDetails user) {
		
		int memberIdx = user.getMember().getMemberIdx();
		int storeIdx = storeDataService.selectStoreIdxByMemberIdx(memberIdx);
		
		/* 프로필 정보 가져와서 집어넣는건데 spData란 이름으로 모델에 집어넣게 될거임 */
		storeDataService.injectStoreProfileByOwnerIdx(model, memberIdx);
		StoreVO sData = (StoreVO) model.getAttribute("spData");
		
		List<CommonCodeVO> stList = storeCommonCode.selectCommonCodeForStore("store_statistic", null);
		model.addAttribute("stList", stList);
		
		List<Map<String, Object>> initStatistic = storeStatisticService.selectStatisticDate(storeIdx, null);
		model.addAttribute("initStatistic", initStatistic);
		
		return "storeManagement/storeStatistic";
	}
	
	/* AJAX로 매장 통계 정보 들고오는거 */
	@GetMapping("select_store_statistic_data")
	@ResponseBody
	public List<Map<String, Object>> selectStoreStatisticData(@RequestParam(name = "st_cat") String st_cat, @AuthenticationPrincipal QtableUserDetails user) {
		
		/* 매장 idx 가져오는거 */
		int store_idx = storeDataService.selectStoreIdxByMemberIdx(user.getMember().getMemberIdx());
		/* TODO: 리턴타입 어케하지? */
		return storeStatisticService.selectStatisticDate(store_idx, st_cat);

	}

}
