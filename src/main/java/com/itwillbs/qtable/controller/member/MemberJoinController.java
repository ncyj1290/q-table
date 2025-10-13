package com.itwillbs.qtable.controller.member;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.service.MemberJoinService;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Controller
@RequiredArgsConstructor
public class MemberJoinController {
	private final MemberJoinService memberJoinService;
	
	@GetMapping("/member_join")
	public String member_join(Model model) {
	    List<CommonCodeVO> genderCodes = memberJoinService.getCodesByGroup("GENDER");
	    model.addAttribute("genderCodes", genderCodes);
				
		return "member/memberJoin";
	}

	@PostMapping("/member_join_Pro")
	public String member_join_Pro(Member member) {
		
		memberJoinService.save(member);
		
		return "redirect:/login";
	}
}
