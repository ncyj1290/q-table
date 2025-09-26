package com.itwillbs.qtable.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminController {
	
	@GetMapping("/adminMain")
	public String adminMain() {
		return "admin/adminMain";
	}

}
