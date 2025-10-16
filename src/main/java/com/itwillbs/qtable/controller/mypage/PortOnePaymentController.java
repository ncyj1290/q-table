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

	// 결제 검증 엔드포인트
	@PostMapping("/portone/verify/{imp_uid}")
	@ResponseBody
	public Map<String, Object> verifyPayment(@PathVariable String imp_uid, @RequestBody Map<String, Object> payload,
			@AuthenticationPrincipal QtableUserDetails qtable) {

		String merchantUid = (String) payload.get("merchantUid");
		Number amount = (Number) payload.get("amount");

		boolean ok = portOneService.verifyPayment(imp_uid, merchantUid, amount.longValue(), qtable);

		if (ok) {
			return Map.of("status", "success");
		} else {
			return Map.of("status", "fail", "msg", "검증 실패");
		}
	}
}