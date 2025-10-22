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
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Log
public class ChatApiController {

	private final ChatService chatService;
	
	// 채팅방 생성 또는
	@PostMapping("/room/create")
	public Map<String, Object> createChatRoom(
			@RequestParam("store_idx") Integer storeIdx,
			@AuthenticationPrincipal QtableUserDetails userDetails) {
		
		Member member = userDetails.getMember();
		Integer memberIdx = member.getMemberIdx();
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			//  
			Map<String, Object> chatRoom = chatService.createOrGetChatRoom(memberIdx, storeIdx);
			response.put("success", true);
			response.put("chatRoom", chatRoom);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
		}
			
		return response;
	}
	
	
}
