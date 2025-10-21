package com.itwillbs.qtable.vo.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


// 채팅 메시지 VO 
// WebSocket을 통해 주고받을 채팅 메시지 데이터 구조 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageVO {

	// 메시지 타입
	private MessageType type;
	private Integer roomIdx; // 채팅방 Idx (chat_room.room_idx)
	private Integer senderIdx; // 발신자 Idx (member_idx)
	private String senderName; // 발신자 이름
	private String message; // 메시지 내용
	private LocalDateTime timestamp; // 전송 시간

	
	// 메시지 타입
	public enum MessageType {
		ENTER,   // 채팅방 입장
		TALK,    // 일반 대화
		LEAVE    // 채팅방 퇴장
	}
}
