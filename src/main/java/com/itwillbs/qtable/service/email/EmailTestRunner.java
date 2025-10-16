package com.itwillbs.qtable.service.email;

import org.springframework.boot.CommandLineRunner;

public class EmailTestRunner implements CommandLineRunner{
	
    private final EmailService emailService;

    public EmailTestRunner(EmailService emailService) {
        this.emailService = emailService;
    }
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		 emailService.sendSimpleEmail(
		            "tmddyd6004@gmail.com"
		        );	
	}

}
