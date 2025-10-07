package com.itwillbs.qtable.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.qtable.service.AdminService;
import com.itwillbs.qtable.vo.admin.MemberDetailVO;
import com.itwillbs.qtable.vo.admin.MemberListVO;
import com.itwillbs.qtable.vo.admin.MemberUpdateVO;
import com.itwillbs.qtable.vo.admin.StoreListVO;

@RestController
public class AdminApiController {

	@Autowired
	private AdminService adminService;

	// 회원 목록 리스트 조회
	@GetMapping("/api/members")
	public List<MemberListVO> memberFindAll() {

		return adminService.memberFindAll();
	}

	// 회원 상세 정보 조회
	@GetMapping("/api/members/{memberIdx}")
	public MemberDetailVO getMemberDetail(@PathVariable("memberIdx") Integer memberIdx) {

		return adminService.findMemberDetailById(memberIdx);
	}

	// 회원 정보 수정
	@PostMapping("/api/members/{memberIdx}/status")
	public void memberUpdate(@PathVariable("memberIdx") Integer memberIdx, @RequestBody MemberUpdateVO MemberUpdateVO) {

		adminService.memberUpdate(memberIdx, MemberUpdateVO);
	}
	
    // 회원 삭제 이벤트
    @PostMapping("/api/members/{memberIdx}")
    public void deleteMember(@PathVariable("memberIdx") Integer memberIdx) {
    	adminService.deleteMember(memberIdx);
    }
    
	// 매장 회원 목록 리스트 조회
	@GetMapping("/api/stores")
	public List<StoreListVO> findStoreMembers() {

		return adminService.findStoreMembers();
	}
}
