package com.itwillbs.qtable.service.pay;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.itwillbs.qtable.config.KakaoPayProperties;
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

	private final KakaoPayProperties KakaoayProperties;
	private RestTemplate restTemplate = new RestTemplate();
	private KakaoReadyResponse KakaoReady;
	
	private org.springframework.http.HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK " + KakaoayProperties.getSecretKey());
		headers.set("Content-Type", "application/json");

		
		return headers;
	}
	
	// 결제 완료 요청 
	public KakaoReadyResponse KakaoPayReady(String payMethod, String amount) { 
		Map<String, Object> parameters = new HashMap<>(); 
		parameters.put("cid", KakaoayProperties.getCid()); 
		parameters.put("partner_order_id", "ORDER1234"); // 주문 번호 
		parameters.put("partner_user_id", "user_kim"); // 사용자 id
		parameters.put("item_name", "q-money"); // 상품명
		parameters.put("quantity", "1"); // 수량, 숫자 
		parameters.put("total_amount", "amount"); // 총 금액
		parameters.put("vat_amount", "1"); // 부가세
		parameters.put("tax_free_amount", "0"); // 비가세
		parameters.put("approval_url", "http://localhost:8080/pay/success"); // 등록한 url
		parameters.put("fail_url", "http://localhost:8080/pay/cancel"); // 등록한 fail
		parameters.put("cancel_url", "http://localhost:8080/pay/fail"); // 등록한 cancel
		
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		
		// 외부에 보낼 URL
		RestTemplate restTemplate = new RestTemplate();
		
		KakaoReady = restTemplate.postForObject(
				"https://open-api.kakaopay.com/online/v1/payment/ready",	
				requestEntity, 
				KakaoReadyResponse.class);
		
		return KakaoReady;
		}
	
	// 결제 완료 승인
	public KakaoApproveResponse approveResponse (String pgToken) {
		
		//카카오 요청
		Map<String, String>parameters = new HashMap<>();
		parameters.put("cid", KakaoayProperties.getCid());
		parameters.put("tid", KakaoReady.getTid());
		parameters.put("partner_order_id", "ORDER_ID");
		parameters.put("partner_user_id", "USER_ID");
		parameters.put("pg_token", pgToken);
		
		//파라미터 헤더
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(requestEntity);
		System.out.println();
		System.out.println();
		System.out.println();
		
		//외부에 보낼 url
		RestTemplate restTemplate = new RestTemplate();
		
		KakaoApproveResponse approveResponse = restTemplate.postForObject(
				"https://open-api.kakaopay.com/online/v1/payment/approve", 
				requestEntity, 
				KakaoApproveResponse.class);
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
