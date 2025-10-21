package com.itwillbs.qtable.controller.chat;

import com.itwillbs.qtable.vo.chat.ChatMessageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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
	public void sendMessage(@Payload ChatMessageVO message) {
		// 메시지 전송 시간 설정
		message.setTimestamp(LocalDateTime.now());

		// 메시지 타입에 따라 처리
		switch (message.getType()) {
			case ENTER:
				message.setMessage(message.getSenderName() + "님이 입장하셨습니다.");
				break;
			case LEAVE:
				message.setMessage(message.getSenderName() + "님이 퇴장하셨습니다.");
				break;
			case TALK:
				// 일반 메시지는 그대로 전송
				break;
		}

		// 해당 채팅방을 구독한 모든 클라이언트에게 메시지 전송
		// /topic/chat/{roomIdx} 를 구독한 클라이언트들이 받음
		messagingTemplate.convertAndSend("/topic/chat/" + message.getRoomIdx(), message);

		// TODO: 나중에 DB에 메시지 저장하는 로직 추가 예정
		// chatService.saveMessage(message);
	}
}
