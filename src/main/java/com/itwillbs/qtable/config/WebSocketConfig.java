package com.itwillbs.qtable.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket 설정 클래스
 * 실시간 채팅을 위한 WebSocket + STOMP 프로토콜 설정
 */
@Configuration
@EnableWebSocketMessageBroker  // WebSocket 메시지 브로커 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	/**
	 * STOMP 엔드포인트 등록
	 * 클라이언트가 WebSocket에 접속할 때 사용할 주소 설정
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws-chat")  // WebSocket 접속 주소: ws://localhost:8080/ws-chat
				.setAllowedOriginPatterns("*")  // 모든 Origin 허용 (개발용, 배포 시 수정 필요)
				.withSockJS()  // SockJS 지원 (WebSocket 미지원 브라우저 대비)
				.setWebSocketEnabled(true)  // WebSocket 활성화
				.setSessionCookieNeeded(true);  // 세션 쿠키 사용 (인증된 사용자용)
	}

	/**
	 * 메시지 브로커 설정
	 * 메시지를 중계하는 브로커 구성
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 클라이언트가 구독할 목적지 prefix 설정
		// 예: /topic/chat/room1 을 구독하면, 해당 채팅방의 메시지를 받음
		registry.enableSimpleBroker("/topic");

		// 클라이언트가 메시지를 보낼 때 사용할 prefix 설정
		// 예: /app/chat/send 로 메시지를 보내면, @MessageMapping("/chat/send") 메서드가 처리
		registry.setApplicationDestinationPrefixes("/app");
	}
}
