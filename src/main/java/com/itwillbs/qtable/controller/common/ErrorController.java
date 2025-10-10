package com.itwillbs.qtable.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
	
	@GetMapping("error/denied")
	public String accessDenied() {
		return "common/error/denied";
	}
	
	
	
	
}
