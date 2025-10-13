package com.itwillbs.qtable.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.qtable.service.admin.AdminService;
import com.itwillbs.qtable.vo.admin.MemberDetailVO;
import com.itwillbs.qtable.vo.admin.MemberListVO;
import com.itwillbs.qtable.vo.admin.MemberUpdateVO;
import com.itwillbs.qtable.vo.admin.StoreDetailVO;
import com.itwillbs.qtable.vo.admin.StoreListVO;
import com.itwillbs.qtable.vo.admin.StoreUpdateVO;

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
	public void memberUpdate(
			@PathVariable("memberIdx") Integer memberIdx, 
			@RequestBody MemberUpdateVO MemberUpdateVO) {

		adminService.memberUpdate(memberIdx, MemberUpdateVO);
	}
	
    // 회원 삭제 이벤트
    @PostMapping("/api/members/{memberIdx}")
    public void deleteMember(@PathVariable("memberIdx") Integer memberIdx) {
    	adminService.deleteMember(memberIdx);
    }
    
    // ------------------------ 매장 ---------------------------
    
	// 매장 회원 목록 리스트 조회
	@GetMapping("/api/stores")
	public List<StoreListVO> findStoreMembers() {

		return adminService.findStoreMembers();
	}
	
	// 매장 입점 신청 목록 리스트 조회
	@GetMapping("/api/stores/entry")
	public List<StoreListVO> findEntryStores() {

		return adminService.findEntryStores();
	}
	
    // 매장 상세 정보 조회
    @GetMapping("/api/stores/{storeIdx}")
    public StoreDetailVO getStoreDetail(@PathVariable("storeIdx") Integer storeIdx) {
        return adminService.findStoreDetailById(storeIdx);
    }

    // 매장 상태 변경
    @PostMapping("/api/stores/{storeIdx}/status")
    public void updateStoreStatus(
            @PathVariable("storeIdx") Integer storeIdx,
            @RequestBody StoreUpdateVO StoreUpdateVO) {
    	
        adminService.updateStoreStatus(storeIdx, StoreUpdateVO);
    }
    
    // 매장 삭제 이벤트
    @PostMapping("/api/stores/{storeIdx}")
    public void deleteStore(@PathVariable("StoreIdx") Integer StoreIdx) {
    	adminService.deleteMember(StoreIdx);
    }
}
