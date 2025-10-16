package com.itwillbs.qtable.service.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.controller.email.EmailController.AuthUtil;

@Service
public class EmailService {
	private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String sendSimpleEmail(String to) {
        String authCode = AuthUtil.generateAuthCode(); // 인증코드 생성

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("인증번호 메일");
        message.setText("인증번호: " + authCode);

        mailSender.send(message);
		return authCode;
    }
}
