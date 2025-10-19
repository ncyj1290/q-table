package com.itwillbs.qtable.controller.storeManagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.service.storeManagement.StoreCalculateService;
import com.itwillbs.qtable.service.storeManagement.StoreDataService;
import com.itwillbs.qtable.util.PagingHandler;
import com.itwillbs.qtable.vo.PageVO;
import com.itwillbs.qtable.vo.storeManagement.CalculateVO;

@Controller
public class StoreCalculateController {

	@Autowired
	StoreDataService storeDataService;
	
	@Autowired
	StoreCalculateService storeCalculateService;
	
	/* ================================================= */
	/* 정산 목록 페이지 */
	@GetMapping("/store_calculate_list")
	public String calculateList(Model model, @AuthenticationPrincipal QtableUserDetails user, @RequestParam(defaultValue = "1", name = "pageNum") int pageNum) {
		
		int memberIdx = user.getMember().getMemberIdx();
		/* 프로필 정보 가져와서 집어넣는건데 spData란 이름으로 모델에 집어넣게 될거임 */
		storeDataService.injectStoreProfileByOwnerIdx(model, memberIdx);
		
		PageVO pageVo = PagingHandler.pageHandler(pageNum, () -> storeCalculateService.countCalculateByMemberidx(memberIdx));
		List<CalculateVO> cList = storeCalculateService.selectCaculateListByMemberidx(memberIdx, pageVo.getStartRow(), pageVo.getListLimit());
		
		/* PageVO 정보랑 정산 리스트 모델에 담아넣기 */
		model.addAttribute("pageVo", pageVo);
		model.addAttribute("cList", cList);

		return "storeManagement/storeCalculateList";
	}
	
	/* 정산 상세 페이지 */
	@GetMapping("/store_calculate_detail")
	public String calculateDetail(Model model, @AuthenticationPrincipal QtableUserDetails user, @RequestParam("jeongsan_idx") int jeongsan_idx) {
		
		int memberIdx = user.getMember().getMemberIdx();
		/* 프로필 정보 가져와서 집어넣는건데 spData란 이름으로 모델에 집어넣게 될거임 */
		storeDataService.injectStoreProfileByOwnerIdx(model, memberIdx);
		
		/* 정산 세부내용 들고와서 model에 박기 */
		CalculateVO cal = storeCalculateService.selectCalculateDetail(jeongsan_idx, memberIdx);
		model.addAttribute("cal", cal);

		return "storeManagement/storeCalculateDetail";
	}
}
