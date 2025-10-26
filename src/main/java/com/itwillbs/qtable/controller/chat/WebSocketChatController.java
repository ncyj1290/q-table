package com.itwillbs.qtable.controller.chat;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.vo.chat.ChatMessageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * WebSocket 채팅 메시지 처리 컨트롤러
 * STOMP 프로토콜을 사용한 실시간 채팅 메시지 송수신
 */
@Controller
@RequiredArgsConstructor
public class WebSocketChatController {

	// 메시지 전송을 위한 SimpMessageSendingOperations
	private final SimpMessageSendingOperations messagingTemplate;

	/**
	 * 채팅 메시지 전송 처리
	 * 클라이언트가 /app/chat/send 로 메시지를 보내면 이 메서드가 처리
	 * 처리 후 해당 채팅방을 구독한 모든 클라이언트에게 브로드캐스트
	 *
	 * @param message 전송된 채팅 메시지
	 */
	@MessageMapping("/chat/send")
	public void sendMessage(@Payload ChatMessageVO message, Authentication authentication) {

		QtableUserDetails userDetails = (QtableUserDetails) authentication.getPrincipal();
		Member member = userDetails.getMember();
		Integer senderIdx = member.getMemberIdx();
		message.setSenderIdx(senderIdx);
		message.setSenderName(member.getMemberName());

		// 메시지 전송 시간 설정
		message.setTimestamp(LocalDateTime.now());

		// 해당 채팅방을 구독한 모든 클라이언트에게 메시지 전송
		// /topic/chat/{roomIdx} 를 구독한 클라이언트들이 받음
		messagingTemplate.convertAndSend("/topic/chat/" + message.getRoomIdx(), message);

		// 모든 사용자의 개인 채널에도 전송 (읽지 않은 메시지 카운트 업데이트용)
		messagingTemplate.convertAndSend("/topic/chat-broadcast", message);
	}
}
