package com.itwillbs.qtable.service.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.itwillbs.qtable.mapper.chat.chatMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class ChatService {

	private final chatMapper chatMapper;

	// 채팅방 생성 또는 채팅방 조회
	public Map<String, Object> insertOrGetChatRoom(Integer memberIdx, Integer storeIdx) {

		// 기존 채팅방 확인
		Map<String, Object> existingRoom = chatMapper.getChatRoomByMemberAndStore(memberIdx, storeIdx);

		// 기존 채팅방 존재하면 리턴
		if (existingRoom != null) {
			return existingRoom;
		}

		// 새 채팅방 생성
		Map<String, Object> params = new HashMap<>();
		params.put("roomType", "더미");
		params.put("memberIdx", memberIdx);
		params.put("storeIdx", storeIdx);

		chatMapper.insertChatRoom(params);

		return chatMapper.getChatRoomByMemberAndStore(memberIdx, storeIdx);
	}

	// 사용자의 모든 채팅방 목록 조회
	public List<Map<String, Object>> getChatRoomList(Integer memberIdx) {
		return chatMapper.getChatRoomListByMember(memberIdx);
	}

	// 특정 채팅방의 메시지 목록 조회
	public List<Map<String, Object>> getChatMessages(Integer roomIdx) {
		return chatMapper.getChatMessagesByRoomIdx(roomIdx);
	}
	
	// 마지막 대화내용 조회
	public String getLastMessage(Integer chatRoomIdx) {
		return chatMapper.getLastMessage(chatRoomIdx);
	}

	// 메시지 저장
	public void insertChat(String message, Integer senderIdx, Integer roomIdx) {
		Map<String, Object> params = new HashMap<>();
		params.put("messageContent", message);
		params.put("senderIdx", senderIdx);
		params.put("roomIdx", roomIdx);

		chatMapper.insertChat(params);
	}

}
