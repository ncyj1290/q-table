package com.itwillbs.qtable.controller.mypage;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.service.pay.KakaoPayService;
import com.itwillbs.qtable.service.pay.PortOneService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class PortOnePaymentController {

	private final KakaoPayService kakaoPayService;
	private final PortOneService portOneService;

	// 결제 검증
	@PostMapping("/portone/verify/{imp_uid}")
	@ResponseBody
	public Map<String, Object> verifyPayment( @PathVariable("imp_uid") String imp_uid, @RequestBody Map<String, Object> payload,
			@AuthenticationPrincipal QtableUserDetails qtable) {

		String merchantUid = (String) payload.get("merchant_uid");
		Object amountObj = payload.get("amount");
		
		long amount = 0;
		if (amountObj != null) {
		    try {
		        amount = Long.parseLong(amountObj.toString());
		    } catch(NumberFormatException e) {
		        return Map.of("status", "fail", "msg", "amount 변환 오류");
		    }
		}

		System.out.println("==== verifyPayment start ====");
		System.out.println("impUid=" + imp_uid + ", merchantUid=" + merchantUid + ", amount=" + amount);
		
		boolean ok = portOneService.verifyPayment(imp_uid, merchantUid, amount, qtable);

		if (ok) {
			return Map.of("status", "success");
		} else {
			return Map.of("status", "fail", "msg", "검증 실패");
		}
	}
}