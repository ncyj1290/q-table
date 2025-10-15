package com.itwillbs.qtable.service.pay;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.itwillbs.qtable.config.KakaoPayProperties;
import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.util.SessionUtils;
import com.itwillbs.qtable.vo.myPage.KakaoApproveResponse;
import com.itwillbs.qtable.vo.myPage.KakaoReadyResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {

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
		parameters.put("approval_url", "http://localhost:8080/mypage/paymentSuccess"); // 등록한 url
		parameters.put("fail_url", "http://localhost:8080/pay/fail"); // 등록한 fail
		parameters.put("cancel_url", "http://localhost:8080/pay/cancel"); // 등록한 cancel

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

		return approveResponse;
	}
	
}
