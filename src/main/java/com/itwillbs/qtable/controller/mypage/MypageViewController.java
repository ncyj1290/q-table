package com.itwillbs.qtable.controller.mypage;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.service.pay.KakaoPayService;

@Controller
@RequestMapping("/mypage")
public class MypageViewController {

    private final KakaoPayService kakaoPayService;

    MypageViewController(KakaoPayService kakaoPayService) {
        this.kakaoPayService = kakaoPayService;
    }
	
    // 카카오페이 결제 완료 콜백 (카카오 서버가 redirect할 곳)
    // 여기서 결제 승인 처리만 하고, 결제 완료 페이지로 리다이렉트
    @GetMapping("/paymentSuccess")
    public String afterPayRequest(@AuthenticationPrincipal QtableUserDetails qtable,
                                  @RequestParam("pg_token") String pgToken) {

        kakaoPayService.approveResponse(qtable, pgToken);

        // 결제 완료 페이지로 리다이렉트
        return "redirect:/mypage/paymentcomplete";
    }
	
	// 결제 완료 화면
    @GetMapping("/paymentcomplete")
    public String paymentCompletePage() {
        return "mypage/paymentcomplete";
    }
	
}
