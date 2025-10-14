package com.itwillbs.qtable.config;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

@Log
@Component
public class QtableAccessDeniedHandler implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
	    // 1. 기본 접근 거부 로그 (InvalidCsrfTokenException 정보)
	    log.info("==================== 접근 거부 에러 상세 정보 ====================");
	    log.info("예외 타입:" + accessDeniedException.getClass().getName());
	    log.info("요청 URI:" + request.getRequestURI());

	    // 2. 근본 원인(Root Cause)까지 추적하여 로깅
	    Throwable rootCause = accessDeniedException;
	    while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
	        rootCause = rootCause.getCause();
	    }
	    log.info("근본 원인: " + rootCause.getClass().getName());
	    log.info("근본 원인: " + rootCause);
	    log.info("=========================================================");
	}

}
