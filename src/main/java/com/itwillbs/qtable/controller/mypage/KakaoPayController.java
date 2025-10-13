package com.itwillbs.qtable.controller.mypage;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	public KakaoReadyResponse readyToKakaoPay(@RequestBody Map<String, String> request) {

		String payMethod = request.get("payMethod");
		String amount = request.get("amount");

		return kakaoPayService.KakaoPayReady(payMethod, amount);
	}
	
	// 결제 성공
	@PostMapping("/success")
	public ResponseEntity<KakaoApproveResponse> afterPayRequest(@RequestParam("pg_token") String pgToken) {

		KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken);

		return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
	}

//	//결제 진행 중 취소
//	@GetMapping("/cancel")
//	public void cancel() {
//		
//		throw new BusinessLogicException(ExceptionCode.PAY_CANCEL);
//	}
//	
//	//결제 실패
//	@GetMapping("/fail")
//	public void fail() {
//		
//		throw new BusinessLogicException(ExceptionCode.FAILED);
//	}

}
