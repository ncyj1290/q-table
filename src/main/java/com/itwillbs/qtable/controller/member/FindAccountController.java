package com.itwillbs.qtable.controller.member;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.qtable.service.member.FindAccountService;
import com.itwillbs.qtable.service.mypage.RandomNickname;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
@Log
public class FindAccountController {

	private final FindAccountService findAccountService;

	@GetMapping("find_account")
	public String findAccount() {
		return "member/findAccount";
	}
	
	// 인증번호 발송(id 찾기)
	@ResponseBody
	@PostMapping("/api/findId/email/send")
	public Map<String, Object> sendIdVerificationEmail(
			@RequestParam("first_input") String userName,
			@RequestParam("user_email") String userEmail){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			// 인증번호 발송
			findAccountService.sendIdVerificationEmail(userName, userEmail);
			
			response.put("success", true);
			
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
		}
		
		return response;
	}
	
	// 인증번호 확인(id, pw 둘다)
	@ResponseBody
	@PostMapping("/api/findIdPw/email/verify")
	public Map<String, Object> checkVerificationCode(
			@RequestParam("user_email") String email,
			@RequestParam("verification_code") String code) {

		Map<String, Object> response = new HashMap<>();

		try {
			// 인증번호 검증 및 아이디 조회
			Map<String, Object> result = findAccountService.checkVerificationCode(email, code);

			// Service에서 반환한 결과를 그대로 전달
			response.putAll(result);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", e.getMessage());
		}

		return response;
	}
	
	// 인증번호 발송(pw 찾기)
	@PostMapping("/api/findPw/email/send")
	@ResponseBody
	public Map<String, Object> sendPwVerificationEmail(
			@RequestParam("first_input") String userId,
			@RequestParam("user_email") String userEamil) {
		
			Map<String, Object> response = new HashMap<>();
			try {
				findAccountService.sendPwVerificationEmail(userId, userEamil);
				
				response.put("success", true);
			} catch (Exception e) {
				response.put("success", false);
				response.put("message", e.getMessage());
			}
			
		return response;
	}
	
	// 새 비밀번호 생성
	@PostMapping("/api/findPw/reset")
	@ResponseBody
	public Map<String, Object> resetPassword(
			@RequestParam("new_password") String newPw,
			@RequestParam("new_password_confirm") String confirmPw,
			@RequestParam("user_id") String userId) {
		
		Map<String, Object> response = new HashMap<>();
		
		// 비밀번호 일치 검증 (프론트, 서버 2중 검증)
		if (!newPw.equals(confirmPw)) {
	        response.put("success", false);
	        response.put("message", "비밀번호가 일치하지 않습니다111.");
	        return response;
	    }
		
		try {
			findAccountService.resetPassword(userId, newPw);
			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
		}
		
		return response;
	}
	
}
