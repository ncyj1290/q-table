package com.itwillbs.qtable.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

	@GetMapping("/mypageMain")
	public String mypageMain() {
		
		return "mypage/mypageMain";
	}
	
	@GetMapping("/mypageReview")
	public String mypageReview() {
		
		return "mypage/mypageReview";
	}
	
}
