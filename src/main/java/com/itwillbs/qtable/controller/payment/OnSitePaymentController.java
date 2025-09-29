package com.itwillbs.qtable.controller.payment;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OnSitePaymentController {
	
	@GetMapping("/on_site_payment")
	public String onSitePayment() {
		
		return "payment/onSitePayment";
	}
}
