package com.itwillbs.qtable.controller.chat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {
	
	@GetMapping("chat")
	public String chatMain() {
		return "chat/chat";
	}
}
