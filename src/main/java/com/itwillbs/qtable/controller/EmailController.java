package com.itwillbs.qtable.controller;

import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.qtable.service.email.EmailService;

@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send")
    public String sendMail(@RequestParam("email") String email) {
        emailService.sendSimpleEmail(email, "테스트 메일", "스프링 부트 이메일 전송 성공!");
        return "메일 발송 완료!";
    }
    
    public class AuthUtil {
        public static String generateAuthCode() {
            Random random = new Random();
            int code = 100000 + random.nextInt(900000); // 100000~999999
            return String.valueOf(code);
        }
    }
}
