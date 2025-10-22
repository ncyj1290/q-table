package com.itwillbs.qtable.controller.chat;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.service.chat.ChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	@GetMapping("chat")
	public String chatMain(
			@AuthenticationPrincipal QtableUserDetails userDetails,
			Model model) {

		Member member = userDetails.getMember();
		Integer memberIdx = member.getMemberIdx();

		// 사용자의 모든 채팅방 목록 조회
		List<Map<String, Object>> chatRoomList = chatService.getChatRoomList(memberIdx);
		model.addAttribute("chatRoomList", chatRoomList);

		return "chat/chat";
	}
}
