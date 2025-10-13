package com.itwillbs.qtable.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.qtable.exception.AccountRestoreRequiredException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginFailHandler extends SimpleUrlAuthenticationFailureHandler{
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	// 로그인 실패 처리 세부 구현 메서드 
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, 
		HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		
		// 에러메시지 선언 
		String msg;
		
		// 에러 타입별 메시지 달리 설정하기 
		if(exception instanceof BadCredentialsException) {
			msg = "아이디 또는 비밀번호가 틀립니다";
		} else if (exception instanceof AccountRestoreRequiredException) {
			msg = "탈퇴한 이력이 있습니다. 다시 복구하시겠습니까?";
		} else {
			msg = "로그인에 실패하였습니다. 관리자에게 문의하세요";
		}
		
		
		// 응답 데이터 생성
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("status", "error");
        responseData.put("message", msg);
        
        // JSON 응답 설정 (HTTP 상태 코드: 401 Unauthorized)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseData));
		
		//메시지를 json 형태로 담아서 ajax 응답 값으로 주기 
		
//		//메시지를 url로 받기 때문에 인코딩 필요 
//		msg = URLEncoder.encode(msg, "UTF-8");
//		//실패시 이동할 경로 지정 
//		setDefaultFailureUrl("/login?error=true&msg=" + msg);
//		super.onAuthenticationFailure(request, response, exception);
	}
}
