package com.itwillbs.qtable.controller.mypage;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.service.pay.KakaoPayService;
import com.itwillbs.qtable.vo.myPage.KakaoApproveResponse;
import com.itwillbs.qtable.vo.myPage.KakaoReadyResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class KakaoPayController {

	private final KakaoPayService kakaoPayService;

	// 결제 요청
	@PostMapping("/qmoneyCharge")
	public KakaoReadyResponse readyToKakaoPay(@AuthenticationPrincipal QtableUserDetails qtable,
			@RequestBody Map<String, String> request) {

		String payment_idx = request.get("payment_idx");
		String amount = request.get("amount");

		return kakaoPayService.KakaoPayReady(qtable, payment_idx, amount);
	}

	// 결제 성공
	@GetMapping("/paymentcomplete")
	public ResponseEntity<KakaoApproveResponse> afterPayRequest(@AuthenticationPrincipal QtableUserDetails qtable,
			@RequestParam("pg_token") String pgToken) {

		KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(qtable, pgToken);

		return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
	}


}
