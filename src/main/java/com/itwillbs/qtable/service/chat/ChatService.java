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
		params.put("roomType", "room_01");
		params.put("memberIdx", memberIdx);
		params.put("storeIdx", storeIdx);

		chatMapper.insertChatRoom(params);

		return chatMapper.getChatRoomByMemberAndStore(memberIdx, storeIdx);
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
	
	// 사용자의 모든 채팅방별 읽지 않은 메시지 수 조회
	public List<Map<String, Object>> getUnreadCountByRoom(Integer memberIdx){
		return chatMapper.getUnreadCountByRoom(memberIdx);
	}

	// 채팅방의 읽지 않은 메시지 모두 읽음으로 수정
	public void updateMessagesAsRead(Integer memberIdx, Integer roomIdx) {
		chatMapper.updateMessagesAsRead(memberIdx, roomIdx);
	}

	// 채팅방 목록 + 읽지 않은 메시지 수까지 한 번에 조회
	public List<Map<String, Object>> getChatRoomList(Integer memberIdx) {
		List<Map<String, Object>> chatRoomList = chatMapper.getChatRoomListByMember(memberIdx);

		// 모든 채팅방의 읽지 않은 메시지 수 조회
		List<Map<String, Object>> unreadCounts = chatMapper.getUnreadCountByRoom(memberIdx);

		// unreadCounts를 Map으로 변환 (roomIdx -> unreadCount)
		Map<Integer, Integer> unreadMap = new HashMap<>();
		for (Map<String, Object> unread : unreadCounts) {
			Integer roomIdx = (Integer) unread.get("room_idx");
			Integer count = ((Number) unread.get("unread_count")).intValue();
			unreadMap.put(roomIdx, count);
		}

		// 각 채팅방에 마지막 대화 + 읽지 않은 수 추가
		for (Map<String, Object> room : chatRoomList) {
			Integer chatRoomIdx = (Integer) room.get("room_idx");

			// 마지막 대화
			String lastMsg = chatMapper.getLastMessage(chatRoomIdx);
			room.put("lastMsg", lastMsg);

			// 읽지 않은 수 (없으면 0)
			room.put("unreadCount", unreadMap.getOrDefault(chatRoomIdx, 0));
		}

		return chatRoomList;
	}

	// 관리자 채팅방 이미 있는지 조회
	public void ensureAdminChatRoomExists(Integer memberIdx) {
		
		Integer result = chatMapper.getAdminChatRoomByMember(memberIdx);
		log.info(result.toString());
		if (result == 0) {
			Map<String, Object> params = new HashMap<>();
			params.put("roomType", "room_02");
			params.put("memberIdx", memberIdx);
			params.put("storeIdx", null);
			
			chatMapper.insertChatRoom(params);
		}
		
	}
}
