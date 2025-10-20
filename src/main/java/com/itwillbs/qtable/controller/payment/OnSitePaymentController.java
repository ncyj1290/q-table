package com.itwillbs.qtable.controller.payment;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.service.pay.OnSitePaymentService;
import com.itwillbs.qtable.vo.storeManagement.ReservationVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class OnSitePaymentController {

	private final OnSitePaymentService onSitePaymentService;

	// 현장 결제 페이지
	@GetMapping("/on_site_payment")
	public String onSitePayment(
			@RequestParam("store_idx") Integer storeIdx,
			@AuthenticationPrincipal QtableUserDetails userDetails,
			Model model) {

		// 로그인한 회원 정보 가져오기
		Member member = userDetails.getMember();
		Integer memberIdx = member.getMemberIdx();

		// 매장 정보 조회
		Map<String, Object> store = onSitePaymentService.getStoreInfo(storeIdx);
		model.addAttribute("store", store);

		// 매장 이미지 조회
		String storeImage = onSitePaymentService.getStoreImage(storeIdx);
		model.addAttribute("storeImage", storeImage);

		// 예약 정보 조회 (회원 + 매장 + 오늘 날짜)
		ReservationVO reservation = onSitePaymentService.getReservationInfo(memberIdx, storeIdx);
//		log.info("=======================" + reservation.toString());
		model.addAttribute("reservation", reservation);

		int qMoney = onSitePaymentService.getMemberQMoney(memberIdx);
		model.addAttribute("qMoney", qMoney);

		return "payment/onSitePayment";
	}

	// 현장 결제 처리 API
	@PostMapping("/api/payment/onsite")
	@ResponseBody
	public Map<String, Object> processPayment(
			  @RequestParam("store_idx") Integer storeIdx,
	          @RequestParam("amount") Integer amount,
	          @AuthenticationPrincipal QtableUserDetails userDetails) {
		
		Map<String, Object> result = new HashMap<>();
		
		
		try {
			Member member = userDetails.getMember();
			
			// 결제 처리 (고객/매장 qMoney 차감/증가 + 결제 기록)
			onSitePaymentService.processOnSitePayment(member.getMemberIdx(), storeIdx, amount);
			
			result.put("success", true);
			result.put("message", "결제가 완료되었습니다.");
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", "결제 처리 중 오류가 발생하였습니다.");
		}
		
		return result; 
	}
}
