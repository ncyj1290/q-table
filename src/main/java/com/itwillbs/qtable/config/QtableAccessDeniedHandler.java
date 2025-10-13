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
		
	    log.info("접근 거부 에러 발생:" + accessDeniedException.getMessage());
        log.info("요청 URI:" + request.getRequestURI());
        log.info("예외 타입:" + accessDeniedException.getClass().getName());
        
		response.sendRedirect("/error/denied");
	}

}
