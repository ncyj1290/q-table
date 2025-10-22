package com.itwillbs.qtable.controller.chat;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Log
public class ChatApiController {

	private final ChatService chatService;
	
	// 채팅방 생성 또는 기존 채팅방 반환
	@PostMapping("/room/create")
	public Map<String, Object> createChatRoom(
			@RequestParam("store_idx") Integer storeIdx,
			@AuthenticationPrincipal QtableUserDetails userDetails) {

		Member member = userDetails.getMember();
		Integer memberIdx = member.getMemberIdx();

		Map<String, Object> response = new HashMap<>();

		try {
			Map<String, Object> chatRoom = chatService.createOrGetChatRoom(memberIdx, storeIdx);
			response.put("success", true);
			response.put("chatRoom", chatRoom);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "채팅방 생성에 실패했습니다.");
		}

		return response;
	}

	// 채팅 메시지 목록 조회
	@PostMapping("/messages")
	public Map<String, Object> getChatMessages(
			@RequestParam("room_idx") Integer roomIdx) {

		Map<String, Object> response = new HashMap<>();

		try {
			List<Map<String, Object>> messages = chatService.getChatMessages(roomIdx);
			response.put("success", true);
			response.put("messages", messages);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "메시지 조회에 실패했습니다.");
		}

		return response;
	}

}
