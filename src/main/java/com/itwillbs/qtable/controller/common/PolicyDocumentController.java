package com.itwillbs.qtable.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PolicyDocumentController {
	
		@GetMapping("/terms_of_use")
		public String terms_of_use(){
			return "termsOfUse";
		}
		@GetMapping("/privacy_policy")
		public String privacy_policy(){
			return "privacyPolicy";
		}
		@GetMapping("/error")
		public String error() {
			return "error";
		}

}
