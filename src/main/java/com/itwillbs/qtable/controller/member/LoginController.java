package com.itwillbs.qtable.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Controller
@RequiredArgsConstructor
public class LoginController {
	
	@GetMapping("/login") //로그인 실패시 넘겨줄 값 파라미터 포함 
	public String login(@RequestParam(value="error", required=false) String error
			,@RequestParam(value="msg", required=false) String msg
			,Model model){
		
		model.addAttribute("error", error);
		model.addAttribute("mag", msg);
		
		return "member/login";
	}
	
	

}
