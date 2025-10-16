package com.itwillbs.qtable.controller.storeManagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.mapper.storeManagementMapper.StoreCommonCode;
import com.itwillbs.qtable.mapper.storeManagementMapper.StoreReservation;
import com.itwillbs.qtable.service.storeManagement.StoreDataService;
import com.itwillbs.qtable.service.storeManagement.StoreReservationService;
import com.itwillbs.qtable.util.PagingHandler;
import com.itwillbs.qtable.vo.PageVO;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;
import com.itwillbs.qtable.vo.storeManagement.ReservationVO;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;

@Controller
public class StoreReservationController {
	
	@Autowired
	StoreDataService storeDataService;
	
	@Autowired
	StoreReservation storeReservation;
	
	@Autowired
	StoreReservationService storeReservationService;
	
	@Autowired
	StoreCommonCode storeCommonCode;

	/* 예약 목록 페이지 */
	@GetMapping("/store_reservation_list")
	public String storeReservationList(@AuthenticationPrincipal QtableUserDetails user, Model model,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, @RequestParam(required = false, name ="filter") String filter) {
		
		/* 프로필 정보 가져와서 집어넣는건데 spData란 이름으로 모델에 집어넣게 될거임 */
		storeDataService.injectStoreProfileByOwnerIdx(model, user.getMember().getMemberIdx());
		StoreVO sData = (StoreVO) model.getAttribute("spData");
		
		int storeIdx = sData.getStore_idx();

		/* 예약 리스트 가져오는 부분 + 페이지네이션*/
		PageVO pageVo = PagingHandler.pageHandler(pageNum, () -> storeReservationService.countReservationByStoreIdx(storeIdx, filter));
		List<ReservationVO> reservationList = storeReservationService.selectReservationByStoreIdx(storeIdx, pageVo.getStartRow(), pageVo.getListLimit(), filter);
		
		/* 예약 결과 공통 코드 */
		List<CommonCodeVO> resvList = storeCommonCode.selectCommonCodeForStore("reservation_result", null);
		
		System.out.println("ResvList Check: " + resvList);
		
		/* 모델에 다 때려박기 */
		model.addAttribute("reservationList", reservationList);
		model.addAttribute("pageVo", pageVo);
		model.addAttribute("resvList", resvList);
		model.addAttribute("filter", filter);
		
		return "storeManagement/storeReservationList";
	}
	
	/* 예약 상세 페이지 */
	@GetMapping("/store_reservation_detail")
	public String storeReservationDetail(@AuthenticationPrincipal QtableUserDetails user, Model model, @RequestParam("reserve_idx") int reserve_idx) {
		
		/* 프로필 정보 가져와서 집어넣는건데 spData란 이름으로 모델에 집어넣게 될거임 */
		storeDataService.injectStoreProfileByOwnerIdx(model, user.getMember().getMemberIdx());
		StoreVO sData = (StoreVO) model.getAttribute("spData");
		
		ReservationVO resData = storeReservationService.selectReservationDetail(reserve_idx, sData.getStore_idx());
		model.addAttribute("resData", resData);
		
		System.out.println("Check Reservation Data: " + resData.toString());
		
		return "storeManagement/storeReservationDetail";
	}
	

}
