package com.itwillbs.qtable.controller.email;

import java.io.Console;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.qtable.entity.EmailVerification;
import com.itwillbs.qtable.repository.EmailVerificationRepository;
import com.itwillbs.qtable.service.email.EmailService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EmailController {

	private final EmailService emailService;
	private final EmailVerificationRepository emailVerificationRepository;

	@PostMapping("/send")
	public String sendMail(@RequestParam("email") String email, HttpSession session) {
		String authCode = emailService.sendSimpleEmail(email);
		EmailVerification verification = new EmailVerification();
		verification.setEmail(email);
		verification.setVerification_code(authCode);
		verification.setCreateAt(LocalDateTime.now());
		verification.setExpiresAt(LocalDateTime.now().plusMinutes(5));
		emailVerificationRepository.save(verification);
		System.out.println("이메일 인증번호: " + authCode);
		System.out.println("받은 이메일: " + email);
		System.out.println("현재시간: " + LocalDateTime.now());
		System.out.println("만료시간: " + verification.getExpiresAt());
		return "메일 발송 완료!";
	}

	@PostMapping("/verify")
	public ResponseEntity<String> verify(@RequestParam("email") String email,
			@RequestParam("emailVerification") String emailVerification) {

		EmailVerification verification = emailVerificationRepository.findTopByEmailOrderByCreateAtDesc(email);
		
		// 만약 DB에 해당 이메일 레코드가 없으면
		if (verification == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("인증 정보가 없습니다.");
		}
		// 만약 DB에 해당 이메일 레코드가 있으면
		if (verification != null) {
			System.out.println("최근 인증번호: " + verification.getVerification_code());
		}
		// 만약 인증코드가 가져온 ExpiresAt가 현재보다 이전(isBefore)이면
		if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("인증 코드가 만료되었습니다.");
		}
		// 만약 작성한 인증코드가 Verification_code와 일치한다면
		if (verification.getVerification_code().equals(emailVerification)) {
			verification.setVerified(true);
			emailVerificationRepository.save(verification);
			return ResponseEntity.ok("인증 성공!");
		} else {
			return ResponseEntity.ok("인증 실패!");
		}

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
