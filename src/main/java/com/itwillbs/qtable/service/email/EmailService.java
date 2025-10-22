package com.itwillbs.qtable.service.email;


import java.io.UnsupportedEncodingException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.controller.email.EmailController.AuthUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender mailSender;

	public void sendHtmlEmail(String to,String authCode) throws MessagingException{
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		try {
		    helper.setFrom("tmddyd6004@gmail.com", "Q-Table");
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		    helper.setFrom("tmddyd6004@gmail.com"); // 이름 없이 기본 발신자로 설정
		}
		helper.setTo(to);
		helper.setSubject("[Q-Table] 가입인증 메일");
		String htmlContent = "<div style=\"width: 400px; margin: 0 auto; padding: 20px; "
        + "border: 1px solid #FABFBF; border-radius: 10px; sans-serif;\">"
        + "<h2 style=\"color: #333; text-align: center;\">[Q-Table] 이메일 인증</h2>"
        + "<p style=\"font-size: 16px; color: #555;\">아래 인증번호를 입력해주세요.</p>"
        + "<p style=\"font-size: 24px; font-weight: bold; text-align: center; "
        + "color: #FFFFFF; background-color: #FF6B6B; padding: 10px 0; border-radius: 5px;\">"
        + authCode + "</p>"
        + "<p style=\"font-size: 14px; color: #999; text-align: center;\">인증 유효기간: 5분</p>"
        + "</div>";
		helper.setText(htmlContent, true);
		mailSender.send(message);
		
	}
	
    
}
