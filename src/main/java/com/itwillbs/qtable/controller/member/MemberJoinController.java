package com.itwillbs.qtable.controller.member;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
		
		// 닉네임이 비어있으면 랜덤 닉네임 생성 및 세팅
	    if (member.getNickName() == null || member.getNickName().isEmpty()) {
	        String randomNick = memberJoinService.generateRandomNickname(); // 서비스에 생성 메서드 만들기
	        member.setNickName(randomNick);
	    }
		
		memberJoinService.save(member);
		
		return "redirect:/login";
	}
	@PostMapping("/member_join_Pro_admin")
	public String member_join_Pro_admin(Member member) {

		//랜덤 닉네임 생성
		if (member.getNickName() == null || member.getNickName().isEmpty()) {
			String randomNick = memberJoinService.generateRandomNickname();
			member.setNickName(randomNick);
		}

	    memberJoinService.save(member); 

	    return "redirect:/admin_account"; 
	}
	@ResponseBody
	@GetMapping("/checkMemberId")
	public boolean checkMemberId(@RequestParam("memberId") String id) {
	    boolean exists = userRepository.existsByMemberId(id);
	    return !exists;
	}	
	@ResponseBody
	@GetMapping("/checkBusinessNo")
	public boolean checkBusinessNo(@RequestParam("businessRegNo") String businessRegNo) {
		boolean exists = userRepository.existsByBusinessRegNo(businessRegNo);
		return !exists;
	}	
}
