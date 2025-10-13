package com.itwillbs.qtable.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.itwillbs.qtable.service.admin.AdminService;
import com.itwillbs.qtable.vo.admin.MemberDetailVO;
import com.itwillbs.qtable.vo.admin.StoreDetailVO;


@Controller
public class AdminController {
	
	@Autowired
	AdminService adminService;
	
//	어드민 메인페이지
	@GetMapping("/admin_main")
	public String adminMain() {
		return "admin/adminMain";
	}
	
//	사용자 회원관리 페이지
	@GetMapping("/admin_member")
	public String adminMember() {
		return "admin/adminMember";
	}
	
//	매장 회원관리 페이지
	@GetMapping("/admin_store")
	public String adminStore() {
		return "admin/adminStore";
	}
	
//	매장 회원관리 페이지
    @GetMapping("/admin_detail/{member_idx}")
    public String adminDetail(@PathVariable("member_idx") Integer member_idx, Model model) {
    	
    	MemberDetailVO memberDetailVO = adminService.findMemberDetail(member_idx);
    	
    	model.addAttribute("member", memberDetailVO);
    	
		return "admin/adminDetail";
	}
	
//	입점 신청관리 페이지
	@GetMapping("/store_entry")
	public String storeEntry() {
		return "admin/storeEntry";
	}
	
//	회원 결제 목록 페이지
	@GetMapping("/member_payment")
	public String memberPayment() {
		return "admin/memberPayment";
	}
	
//	매장 결제 목록 페이지
	@GetMapping("/store_payment")
	public String storePayment() {
		return "admin/storePayment";
	}

//	매장 정산 페이지
	@GetMapping("/store_refund")
	public String adminRefund() {
		return "admin/adminRefund";
	}
	
//	매장 구독 페이지
	@GetMapping("/admin_subscribe")
	public String adminSubscribe() {
		return "admin/adminSubscribe";
	}
	
//	공통 코드 관리 페이지
	@GetMapping("/comcode_list")
	public String comCodeList() {
		return "admin/comCodeList";
	}
	
//	공통 코드 추가 페이지
	@GetMapping("/comcode_commit")
	public String comcodeCommit() {
		return "admin/comcodeCommit";
	}
	
//	관리자 계정 목록 페이지
	@GetMapping("/admin_account")
	public String adminAccount() {
		return "admin/adminAccount";
	}
	
//	관리자 계정 추가 페이지
	@GetMapping("/admin_commit")
	public String adminCommit() {
		return "admin/adminCommit";
	}
	
	
	


}
