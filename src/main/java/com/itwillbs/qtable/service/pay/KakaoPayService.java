package com.itwillbs.qtable.service.pay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.itwillbs.qtable.config.KakaoPayProperties;
import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.mapper.pay.PaymentMapper;
import com.itwillbs.qtable.util.SessionUtils;
import com.itwillbs.qtable.vo.myPage.KakaoApproveResponse;
import com.itwillbs.qtable.vo.myPage.KakaoCancelResponse;
import com.itwillbs.qtable.vo.myPage.KakaoReadyResponse;
import com.itwillbs.qtable.vo.myPage.PaymentVO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {

	private final PaymentMapper paymentMapper;
	private final PaymentService paymentService;
	private final KakaoPayProperties kakaoPayProperties;
	private RestTemplate restTemplate = new RestTemplate();
	private KakaoReadyResponse KakaoReady;

	private org.springframework.http.HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "SECRET_KEY " + kakaoPayProperties.getSecretkey());
		headers.set("Content-Type", "application/json");

		return headers;
	}

	// 결제 승인 요청
	public KakaoReadyResponse KakaoPayReady(@AuthenticationPrincipal QtableUserDetails qtable, String payMethod, String amount) {
		Map<String, Object> parameters = new HashMap<>();
		
		Member member = qtable.getMember();
		Integer member_idx = member.getMemberIdx();
		
		parameters.put("cid", kakaoPayProperties.getCid());
		parameters.put("partner_order_id", "order1234"); // 주문 번호
		parameters.put("partner_user_id", member_idx); // 사용자 id
		parameters.put("item_name", "q-money"); // 상품명
		parameters.put("quantity", 1); // 수량, 필수 값
		parameters.put("total_amount", amount); // 총 금액
//		parameters.put("vat_amount", "1"); // 부가세
		parameters.put("tax_free_amount", 0); // 비가세, 필수 값
		parameters.put("approval_url", "http://c2d2505t1p2.itwillbs.com/mypage/paymentSuccess"); // 등록한 url
		parameters.put("fail_url", "http://c2d2505t1p2.itwillbs.com/pay/fail"); // 등록한 fail
		parameters.put("cancel_url", "http://c2d2505t1p2.itwillbs.com/pay/cancel"); // 등록한 cancel

		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

		// 외부에 보낼 URL
		RestTemplate restTemplate = new RestTemplate();

		KakaoReady = restTemplate.postForObject("https://open-api.kakaopay.com/online/v1/payment/ready", requestEntity,
				KakaoReadyResponse.class);

		SessionUtils.addAttribute("tid", KakaoReady.getTid());

		return KakaoReady;
	}

	// 결제 완료 승인
	public KakaoApproveResponse approveResponse(@AuthenticationPrincipal QtableUserDetails qtable, String pgToken) {

		Member member = qtable.getMember();
		Integer member_idx = member.getMemberIdx();
		
		// 카카오 요청
		Map<String, String> parameters = new HashMap<>();
		parameters.put("cid", kakaoPayProperties.getCid());
		parameters.put("tid", KakaoReady.getTid());
		parameters.put("partner_order_id", "order1234");
		parameters.put("partner_user_id", String.valueOf(member_idx));
		parameters.put("pg_token", pgToken);

		// 파라미터 헤더
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(requestEntity);
		System.out.println();
		System.out.println();
		System.out.println();

		// 외부에 보낼 url
		RestTemplate restTemplate = new RestTemplate();

		KakaoApproveResponse approveResponse = restTemplate.postForObject("https://open-api.kakaopay.com/online/v1/payment/approve",
				requestEntity, KakaoApproveResponse.class);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(approveResponse);
		System.out.println();
		System.out.println();
		System.out.println();
		
        // DB에 저장할 payment 객체 생성
		PaymentVO pay = new PaymentVO();
		pay.setMember_idx(member_idx);
		pay.setPayment_amount(approveResponse.getAmount().getTotal()); 
		pay.setPay_status("pyst_01");  // 상태 코드
		pay.setPay_way("pywy_02 ");
		pay.setPay_type("pyus_01");  // 결제 유형
//		pay.setPay_reference();
		pay.setExternal_transaction_idx(approveResponse.getTid());
		pay.setItem_name(approveResponse.getItem_name());

        // DB 저장
        paymentService.savePayment(pay);
        
        // member.qmoney 컬럼에 결제 금액 누적
        paymentMapper.increaseQmoney(member_idx, approveResponse.getAmount().getTotal());

		return approveResponse;
	}

    // 결제 내역 조회
    public List<PaymentVO> getPaymentsByMember(int memberIdx) {
        return paymentMapper.selectPaymentsByMember(memberIdx);
    }
    
    public int getQmoneyBalance(int memberIdx) {
        return paymentMapper.selectQmoneyByMemberIdx(memberIdx);
    }

    
    // q-money 합산해서 가져오기
    public int getTotalQmoney(int memberIdx) {
        // 결제 내역 조회
        List<PaymentVO> payments = paymentMapper.selectPaymentsByMember(memberIdx);

        // 결제 금액 합산
        int total = 0;
        if (payments != null) {
            for (PaymentVO p : payments) {
                total += p.getPayment_amount();
            }
        }

        return total;
    }
    
    
    // 결제 취소(환불)
    public KakaoCancelResponse cancelPayment(String tid, int cancelAmount, int taxFreeAmount) {
        HttpHeaders headers = getHeaders();

        Map<String, Object> params = new HashMap<>();
        params.put("cid", kakaoPayProperties.getCid());
        params.put("tid", tid);
        params.put("cancel_amount", cancelAmount);
        params.put("cancel_tax_free_amount", taxFreeAmount);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForObject(
            "https://open-api.kakaopay.com/online/v1/payment/cancel",
            requestEntity,
            KakaoCancelResponse.class
        );
    }

    public KakaoCancelResponse cancelPaymentAndSave(Map<String, Object> params) {
        String tid = (String) params.get("tid");
        System.out.println("cancelPaymentAndSave tid = " + tid);
        int cancelAmount = (int) params.get("cancelAmount");
        int cancelTaxFreeAmount = (int) params.get("cancelTaxFreeAmount");

        KakaoCancelResponse response = cancelPayment(tid, cancelAmount, cancelTaxFreeAmount);

        if (response != null 
            && response.getApproved_cancel_amount() != null
            && response.getApproved_cancel_amount().getTotal() > 0) {

            PaymentVO pay = new PaymentVO();
            pay.setExternal_transaction_idx(tid);
            pay.setPay_status("pyst_02"); // 환불 완료 상태 코드
            pay.setPayment_amount(response.getApproved_cancel_amount().getTotal());

            paymentService.updatePaymentStatus(pay);
        }

        return response;
    }

    
    



	
}
