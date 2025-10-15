package com.itwillbs.qtable.service.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.controller.EmailController.AuthUtil;

@Service
public class EmailService {
	private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(String to, String subject, String text) {
    	String authCode = AuthUtil.generateAuthCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText("인증번호" + authCode);

        mailSender.send(message);
    }
}
