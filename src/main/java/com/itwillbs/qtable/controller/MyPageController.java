package com.itwillbs.qtable.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

	@GetMapping("/mypage_main")
	public String mypageMain() {
		
		return "mypage/mypageMain";
	}
	
	@GetMapping("/mypage_review")
	public String mypageReview() {
		
		return "mypage/mypageReview";
	}
	
	@GetMapping("/mypage_scrap")
	public String mypageScrap() {
		
		return "mypage/mypageScrap";
	}
	
	@GetMapping("/mypage_history")
	public String mypageHistory() {
		
		return "mypage/mypageHistory";
	}
	
	@GetMapping("/reservation_list")
	public String reservationList() {
		
		return "mypage/reservationList";
	}	
}
