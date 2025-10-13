package com.itwillbs.qtable.service.member;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository repo;
	@Value("${kakao.unlink-uri}")
	private String kakaoUnlink; 
    private final RestTemplate restTemplate = new RestTemplate(); 
    
	@Transactional 
	public void updateMemStatus(String accessToken) {
		
		// 카카오 api 호출 
		// 필요한것 : 엑세스 토큰 
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		headers.set("Authorization", "Bearer " + accessToken);
	    HttpEntity<Void> entity = new HttpEntity<>(headers);
	    ResponseEntity<Map> res = restTemplate.exchange(kakaoUnlink, HttpMethod.POST, entity, Map.class);
	    
		// 회원 상태 변경하는 로직 - 완료 
		String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = repo.findByMemberId(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원을 찾을 수 없습니다."));
		member.setMemberStatus("mstat_02");

	}
	
	@Transactional 
	public void restoreMemStat(String userId) {
		Member member = repo.findByMemberId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 회원을 찾을 수 없습니다."));
		member.setMemberStatus("mstat_01");
	}
	
}
