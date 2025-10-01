package com.itwillbs.qtable.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberJoinController {
	@GetMapping("/member_join")
	public String member_join(){
		return "memberJoin";
	}
}
