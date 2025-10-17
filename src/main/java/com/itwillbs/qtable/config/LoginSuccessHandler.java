package com.itwillbs.qtable.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.qtable.entity.UserLog;
import com.itwillbs.qtable.repository.UserLogRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final RequestCache requestCache = new HttpSessionRequestCache();
	private final UserLogRepository repo;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		
		
		
		// 리다이렉트 url 설정 
		boolean isAdmin = authentication.getAuthorities().stream()
			    .anyMatch(authority -> authority.getAuthority().equals("ROLE_mtype_01"));
		boolean isStore = authentication.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("ROLE_mtype_03"));
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		String redirectUrl;
		
        if (savedRequest != null) {
            redirectUrl = savedRequest.getRedirectUrl();
        } else if (isAdmin) {
        	redirectUrl = "/admin_main";
        } else if (isStore) {
        	redirectUrl = "/store_management_main";
        } else {
            redirectUrl = "/";
        }
        requestCache.removeRequest(request, response);
        
        // 일반 회원 요청인지 소셜 로그인 회원 요청인지 분기 
        String acceptHeader = request.getHeader("Accept");
		boolean isAjax = acceptHeader != null && acceptHeader.contains("application/json");
		UserLog userLog = new UserLog();
		String userIp = request.getRemoteAddr();
		QtableUserDetails userDetails = (QtableUserDetails)authentication.getPrincipal();
		
		// 멤버 idx, ip 
		log.info("사용자 idx "+ userDetails.getMember().getMemberIdx());
		log.info("사용자 ip "+request.getRemoteAddr());
		userLog.setMember_idx(userDetails.getMember().getMemberIdx());
		userLog.setIp_address(userIp);
		repo.save(userLog);// 로그 디비 저장
		
        if(isAjax) { //일반 호출이면 ajax 응답 
        	 // 응답 데이터 생성
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", "success");
            responseData.put("redirectUrl", redirectUrl); // 동적으로 결정된 URL을 담아줌

            // JSON 응답 설정
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(responseData));
            
        } else { // 소셜 회원이면 바로 리다이렉트 요청보내기 
        	getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
       
	}
}
