package com.itwillbs.qtable.service.member;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.controller.email.EmailController.AuthUtil;
import com.itwillbs.qtable.entity.EmailVerification;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.repository.EmailVerificationRepository;
import com.itwillbs.qtable.repository.MemberRepository;
import com.itwillbs.qtable.service.email.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class FindAccountService {
	
	private final EmailService emailService;
	
	private final MemberRepository memberRepository;
	private final EmailVerificationRepository emailVerificationRepository;
	
	public void sendVerificationEmail(String userId, String userEmail) {
		Member member = 
				memberRepository.findByMemberNameAndEmail(userId, userEmail)
				.orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다"));
		
		String authCode = AuthUtil.generateAuthCode();
		
		// 인증번호 생성 및 이메일 발송
        try {
             emailService.sendHtmlEmail(userEmail, authCode);
        } catch (Exception e) {
            
        }
        
        // 인증번호 DB 저장
        EmailVerification verification = new EmailVerification();
        verification.setEmail(userEmail);
        verification.setVerification_code(authCode);
        verification.setCreateAt(LocalDateTime.now());
        verification.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        emailVerificationRepository.save(verification);
		
	}
	
	// 인증번호 확인(id)
	public boolean checkVerificationCode(String email, String code) {
		
		EmailVerification verification = emailVerificationRepository.findTopByEmailOrderByCreateAtDesc(email);
		
		if (verification.getVerification_code().equals(code)) {
			Member member = memberRepository.findByEmail(email)
					.orElseThrow(() -> new RuntimeException("해당 이메일의 회원 정보가 존재하지 않습니다."));
		} 
		
		return verification.getVerification_code().equals(code);
	}
	
	
	// 랜덤 코드 생성
	public class AuthUtil {
		public static String generateAuthCode() {
			Random random = new Random();
			int code = 100000 + random.nextInt(900000); // 100000~999999
			return String.valueOf(code);
		}
	}
}
