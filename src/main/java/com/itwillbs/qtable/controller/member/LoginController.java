package com.itwillbs.qtable.controller.member;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.qtable.service.member.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Controller
@RequiredArgsConstructor
public class LoginController {
	
	private final MemberService service;
	
	@GetMapping("/login") //로그인 실패시 넘겨줄 값 파라미터 포함 
	public String login(@RequestParam(value="error", required=false) String error
			,@RequestParam(value="msg", required=false) String msg
			,Model model){
		
		model.addAttribute("error", error);
		model.addAttribute("mag", msg);
		
		return "member/login";
	}
	
	@GetMapping("/member_delete_social")
	public String memberDeleteSocial() {
		return "member/socialMemberDelete";
	}
	
	@ResponseBody
	@PostMapping("/api/member_delete_social")
	public ResponseEntity<String> memberDelete(@RegisteredOAuth2AuthorizedClient("kakao") OAuth2AuthorizedClient authorizedClient
			,HttpServletRequest request, HttpServletResponse response) {
		// 서비스에게 api호출과 db 처리를 맡김 
		String accessToken = authorizedClient.getAccessToken().getTokenValue();
		service.updateMemStatus(accessToken);
		
		// 사이트 로그아웃 처리 
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
		
		return ResponseEntity.ok("회원 탈퇴가 성공적으로 처리되었습니다. 메인페이지로 이동합니다.");
	}
	
	@ResponseBody
	@PostMapping("/api/member_restore")
	public ResponseEntity<String> memberRestore(HttpSession session) {
		String userId = (String)session.getAttribute("userIdForRestore");
		service.restoreMemStat(userId);
		return ResponseEntity.ok("복구가 완료되었습니다. 다시 로그인바랍니다.");
	}
	
}
