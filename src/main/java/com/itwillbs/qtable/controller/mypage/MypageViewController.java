package com.itwillbs.qtable.controller.mypage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.service.pay.KakaoPayService;
import com.itwillbs.qtable.service.pay.PaymentService;
import com.itwillbs.qtable.vo.myPage.PaymentVO;

@Controller
@RequestMapping("/mypage")
public class MypageViewController {

    private final KakaoPayService kakaoPayService;
    private final PaymentService paymentService;

    public MypageViewController(KakaoPayService kakaoPayService, PaymentService paymentService) {
        this.kakaoPayService = kakaoPayService;
        this.paymentService = paymentService;
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
    public String paymentCompletePage(@AuthenticationPrincipal QtableUserDetails qtable, Model model, 
    		@RequestParam(value = "merchant_uid", required = false) String merchantUid) {
    	
        // 로그인한 사용자 정보
        int memberIdx = qtable.getMember().getMemberIdx();
        
        // DB에서 결제 내역 조회
        // 카카오페이
        var kakaoPayments = kakaoPayService.getPaymentsByMember(memberIdx);
        if(kakaoPayments == null) kakaoPayments = new ArrayList<>();
        
        // 포트원 결제 내역
        List<PaymentVO> portOnePayments = new ArrayList<>();
        if(merchantUid != null && !merchantUid.isEmpty()) {
            portOnePayments = paymentService.findByMerchantUid(merchantUid);
        }

        // 모델에 담기
        if(!portOnePayments.isEmpty()) {
            model.addAttribute("payments", portOnePayments);
        } else {
            model.addAttribute("payments", kakaoPayments);
        }
    	
        return "mypage/paymentcomplete";
    }
	
}
