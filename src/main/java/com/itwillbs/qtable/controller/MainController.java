package com.itwillbs.qtable.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	@GetMapping("/main")
	public String main(){
		return "main";
	}
	@GetMapping("/login")
	public String login(){
		return "member/login";
	}
	
}
