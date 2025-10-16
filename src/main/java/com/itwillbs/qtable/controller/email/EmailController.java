package com.itwillbs.qtable.controller.email;

import java.io.Console;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.qtable.service.email.EmailService;

import jakarta.servlet.http.HttpSession;

@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public String sendMail(@RequestParam("email") String email,HttpSession session) {
        String authCode = emailService.sendSimpleEmail(email);
        session.setAttribute("authCode", authCode);
        System.out.println("이메일 인증번호: " + authCode);
        return "메일 발송 완료!";
    }
    @PostMapping("/verify")
    public ResponseEntity<String> verify(
    		@RequestParam("emailVerification") String emailVerification, HttpSession session){
    	String sessionCode = (String) session.getAttribute("authCode");
    	if(sessionCode != null && sessionCode.equals(emailVerification)) {
    		return ResponseEntity.ok("인증성공!");
    	}else {
    		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("인증실패");
    	}
    	
    }
       
    public class AuthUtil {
        public static String generateAuthCode() {
            Random random = new Random();
            int code = 100000 + random.nextInt(900000); // 100000~999999
            return String.valueOf(code);
        }
    }
}
