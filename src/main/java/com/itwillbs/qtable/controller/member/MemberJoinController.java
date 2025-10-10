package com.itwillbs.qtable.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.service.MemberJoinService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Controller
@RequiredArgsConstructor
public class MemberJoinController {
	private final MemberJoinService memberJoinService;

	@GetMapping("/member_join")
	public String member_join() {
		return "member/memberJoin";
	}

	@PostMapping("/member_join_Pro")
	public String member_join_Pro(Member member) {
		System.out.println("@@##@");
		
		memberJoinService.save(member);
		
		return "redirect:/login";
	}
}
