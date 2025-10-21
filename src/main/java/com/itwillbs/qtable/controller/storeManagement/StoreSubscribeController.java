package com.itwillbs.qtable.controller.storeManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.mapper.storeManagementMapper.StoreCommonCode;
import com.itwillbs.qtable.service.storeManagement.StoreDataService;
import com.itwillbs.qtable.service.storeManagement.StoreSubscribeService;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;

/* 맴버쉽 구매 페이지 관련 컨트롤러 */
@Controller
public class StoreSubscribeController {
	
	@Autowired
	StoreDataService storeDataService;

	@Autowired
	StoreCommonCode storeCommonCode;
	
	@Autowired
	StoreSubscribeService storeSubscribeService;
	
	/* ================================================= */
	/* 구독권 구매 페이지 */
	@GetMapping("/purchase_subscribe")
	public String purchaseSubscribe(Model model, @AuthenticationPrincipal QtableUserDetails user) {
		
		int memberIdx = user.getMember().getMemberIdx();
		/* 프로필 정보 가져와서 집어넣는건데 spData란 이름으로 모델에 집어넣게 될거임 */
		storeDataService.injectStoreProfileByOwnerIdx(model, memberIdx);
		
		/* 구독권 단위 공통코드 목록 */
		List<CommonCodeVO> mUnitList = storeCommonCode.selectCommonCodeForStore("membership_dateunit", null);
		model.addAttribute("mUnitList", mUnitList);
		
		return "storeManagement/purchaseSubscribe";
	}
	
	/* 구독권 구매 동작 */
	@PostMapping("/purchase_subscribe_processing")
	@ResponseBody
	public Map<String, Object> purchaseSubscribeProcessing(@RequestParam Map<String, String> purchaseData, @AuthenticationPrincipal QtableUserDetails user) {
		
		/* 구매에 필요한 정보들 */
		int memberIdx = user.getMember().getMemberIdx();
		int qMoney = storeSubscribeService.selectQmoneyByMemberidx(memberIdx);
		int cost = Integer.parseInt(purchaseData.get("cost")); 
		int plusDate = Integer.parseInt(purchaseData.get("plus_date")); 
		
		/* 결과에 넣을 초기 값들 */
		String msg = "구독권 구매가 완료되었습니다!";
		boolean bool = true;
		
		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println("Check MemberIDX: " + memberIdx + " QMONEY: " + qMoney);

		/* 큐머니 부족하면 부정 결과 */
		if(qMoney < cost) {
			msg = "큐머니가 부족합니다. 먼저 충전 후 이용해주세요!";
			bool = false;
			
			result.put("msg", msg);
			result.put("bool", bool);
			
			return result;
		}
		
		/* 구독권 구매 서비스 */
		bool = storeSubscribeService.purchaseSubscribe(memberIdx, cost, plusDate);
		
		/* 결과 전송 */
		result.put("msg", msg);
		result.put("bool", bool);
		return result;
	}
	
	
	
	
	
	
	
	
	
}
