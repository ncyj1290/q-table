package com.itwillbs.qtable.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

	@GetMapping("/mypage_main")
	public String mypage_main() {
		
		return "mypage/mypage_main";
	}
	
}
