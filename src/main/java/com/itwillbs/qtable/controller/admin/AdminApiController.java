package com.itwillbs.qtable.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.qtable.service.AdminService;
import com.itwillbs.qtable.vo.admin.MemberListVO;

@RestController
public class AdminApiController {
	
	@Autowired
	private AdminService adminService;
	
	@GetMapping("/api/members")
	public List<MemberListVO> memberFindAll() {
		
		return adminService.memberFindAll();
	}
}
