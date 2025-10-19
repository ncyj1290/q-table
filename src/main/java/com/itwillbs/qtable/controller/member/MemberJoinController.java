package com.itwillbs.qtable.controller.member;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.repository.UserRepository;
import com.itwillbs.qtable.service.member.MemberJoinService;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Controller
@RequiredArgsConstructor
public class MemberJoinController {
	private final MemberJoinService memberJoinService;
	 private final UserRepository userRepository; 
	@GetMapping("/member_join")
	public String member_join(Model model) {
	    List<CommonCodeVO> genderCodes = memberJoinService.getCodesByGroup("GENDER");
	    model.addAttribute("genderCodes", genderCodes);
	    List<CommonCodeVO> statusCodes = memberJoinService.getCodesByGroup("member_status");
	    model.addAttribute("statusCodes", statusCodes);
	    List<CommonCodeVO> memberType = memberJoinService.getCodesByGroup("member_type");
	    model.addAttribute("memberType", memberType);
		return "member/memberJoin";
	}

	@PostMapping("/member_join_Pro")
	public String member_join_Pro(Member member) {
		
		memberJoinService.save(member);
		
		return "redirect:/login";
	}
	
    @GetMapping("/checkMemberId")
    public boolean checkMemberId(@RequestParam String memberId) {
        boolean exists = userRepository.existsByMemberId(memberId);
        return !exists;
    }
	
}
