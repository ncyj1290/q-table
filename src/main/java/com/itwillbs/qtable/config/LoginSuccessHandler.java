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

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final RequestCache requestCache = new HttpSessionRequestCache();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		
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
        // 응답 데이터 생성
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("redirectUrl", redirectUrl); // 동적으로 결정된 URL을 담아줌

        // JSON 응답 설정
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
	}
}
