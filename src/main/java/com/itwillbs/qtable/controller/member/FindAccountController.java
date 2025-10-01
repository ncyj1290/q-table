package com.itwillbs.qtable.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FindAccountController {

	@GetMapping("find_account")
	public String findAccount() {
		return "member/findAccount";
	}
}
