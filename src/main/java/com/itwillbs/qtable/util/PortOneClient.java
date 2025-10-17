package com.itwillbs.qtable.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.itwillbs.qtable.vo.myPage.PortOneVO;

@Component
public class PortOneClient {

    @Value("${portone.api-key}")
    private String apiKey;

    @Value("${portone.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public PortOneVO getPaymentByImpUid(String impUid) {

        // 1. 토큰 발급 요청
        String tokenUrl = "https://api.iamport.kr/users/getToken";
        Map<String, String> body = Map.of(
                "imp_key", apiKey,
                "imp_secret", secretKey
        );
        HttpEntity<Map<String, String>> tokenEntity = new HttpEntity<>(body, new HttpHeaders());
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, tokenEntity, Map.class);

        String accessToken = (String) ((Map) tokenResponse.getBody().get("response")).get("access_token");

        // 2. 결제 조회 요청
        String url = "https://api.iamport.kr/payments/" + impUid;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<PortOneVO> resp = restTemplate.exchange(
                url, HttpMethod.GET, entity, PortOneVO.class
        );

        return resp.getBody();
    }
}
