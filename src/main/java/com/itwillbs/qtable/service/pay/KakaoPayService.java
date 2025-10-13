package com.itwillbs.qtable.service.pay;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

	private final KakaoPayProperties kakaoPayProperties;
	private RestTemplate restTemplate = new RestTemplate();
	private KakaoReadyResponse KakaoReady;
	
	private org.springframework.http.HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		
		log.info("Authorization : KakaoAK {}", kakaoPayProperties.getSecretkey());

		
		headers.set("Authorization", "KakaoAK " + kakaoPayProperties.getSecretkey());
		headers.set("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");


		
		return headers;
	}
	
	// 결제 완료 요청 
	public KakaoReadyResponse KakaoPayReady(String payMethod, String amount) { 
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
		parameters.add("cid", kakaoPayProperties.getCid()); 
		parameters.add("partner_order_id", "ORDER1234"); // 주문 번호 
		parameters.add("partner_user_id", "user_kim"); // 사용자 id
		parameters.add("item_name", "q-money"); // 상품명
		parameters.add("quantity", "1"); // 수량, 숫자 
		parameters.add("total_amount", amount); // 총 금액
		parameters.add("vat_amount", "1"); // 부가세
		parameters.add("tax_free_amount", "0"); // 비가세
		parameters.add("approval_url", "http://localhost:8080/mypage/qmoneyCharge"); // 등록한 url
		parameters.add("fail_url", "http://localhost:8080/pay/cancel"); // 등록한 fail
		parameters.add("cancel_url", "http://localhost:8080/pay/fail"); // 등록한 cancel
		
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		
		// 외부에 보낼 URL
	    return restTemplate.postForObject(
	            "https://kapi.kakao.com/v1/payment/ready",
	            requestEntity,
	            KakaoReadyResponse.class);
		}
	
	// 결제 완료 승인
	public KakaoApproveResponse approveResponse (String pgToken) {
		
		//카카오 요청
		Map<String, String>parameters = new HashMap<>();
		parameters.put("cid", kakaoPayProperties.getCid());
		parameters.put("tid", KakaoReady.getTid());
		parameters.put("partner_order_id", "ORDER1234");
		parameters.put("partner_user_id", "user_kim");
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
				"https://kapi.kakao.com/v1/payment/approve", 
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
