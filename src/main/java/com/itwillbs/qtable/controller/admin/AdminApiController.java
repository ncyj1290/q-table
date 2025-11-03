package com.itwillbs.qtable.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.qtable.service.admin.AdminService;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;
import com.itwillbs.qtable.vo.commonCode.CommonCodeGroupVO;
import com.itwillbs.qtable.vo.admin.JeongsanListVO;
import com.itwillbs.qtable.vo.admin.MemberDetailVO;
import com.itwillbs.qtable.vo.admin.MemberListVO;
import com.itwillbs.qtable.vo.admin.MemberUpdateVO;
import com.itwillbs.qtable.vo.admin.PaymentListVO;
import com.itwillbs.qtable.vo.admin.StoreDetailVO;
import com.itwillbs.qtable.vo.admin.StoreListVO;
import com.itwillbs.qtable.vo.admin.StoreUpdateVO;
import com.itwillbs.qtable.vo.admin.SubscribeListVO;
import com.itwillbs.qtable.vo.admin.UserLogVO;
import com.itwillbs.qtable.vo.admin.AdminCommonCodeVO;
import com.itwillbs.qtable.vo.admin.AdminCommonCodeGroupVO;
import com.itwillbs.qtable.vo.admin.JeongsanUpdateVO;

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
    @PostMapping("/api/stores/{member_idx}")
    public void deleteStoreMember(@PathVariable("member_idx") Integer member_idx) {
    	adminService.deleteStoreMember(member_idx);
    }
    
    // -------------------------- 결제 -----------------------
    
    // 회원 결제 목록 리스트 조회
	@GetMapping("/api/payments/members")
	public List<PaymentListVO> findPaymentListMembers() {

		return adminService.findPaymentListMembers();
	}
	
    // 매장 결제 목록 리스트 조회
	@GetMapping("/api/payments/stores")
	public List<PaymentListVO> findPaymentListStores() {

		return adminService.findPaymentListStores();
	}
	
    // 결제 목록 삭제 이벤트
    @PostMapping("/api/{paymentIdx}")
    public void deletePayment(@PathVariable("paymentIdx") Integer paymentIdx) {
    	adminService.deletePayment(paymentIdx);
    }
    
    // ----------------------- 정산 --------------------------
    
    // 매장 정산 목록 리스트 조회
	@GetMapping("/api/jeongsan")
	public List<JeongsanListVO> findJeongsanList() {

		return adminService.findJeongsanList();
	}
	
    // 정산 상세 정보 조회
    @GetMapping("/api/jeongsan/{jeongsanIdx}")
    public JeongsanListVO findJeongsanDetail(@PathVariable("jeongsanIdx") Integer jeongsanIdx) {
        return adminService.findJeongsanDetail(jeongsanIdx);
    }
    
    // 정산 상태 변경
    @PostMapping("/api/jeongsan/{jeongsanIdx}/status")
    public void updateJeongsanStatus(
            @PathVariable("jeongsanIdx") Integer jeongsanIdx,
            @RequestBody JeongsanUpdateVO JeongsanUpdateVO) {
    	
        adminService.updateJeongsanStatus(jeongsanIdx, JeongsanUpdateVO);
    }
    
    // 정산 목록 삭제 이벤트
    @PostMapping("/api/jeongsan/{jeongsanIdx}")
    public void deleteJeongsan(@PathVariable("jeongsanIdx") Integer jeongsanIdx) {
    	adminService.deleteJeongsan(jeongsanIdx);
    }
    
    // ------------------------- 구독 ----------------------
    
    // 매장 구독 목록 리스트 조회
	@GetMapping("/api/subscribe")
	public List<SubscribeListVO> findSubscribeList() {

		return adminService.findSubscribeList();
	}
	
    // 정산 구독 삭제 이벤트
    @PostMapping("/api/subscribe/{subscribeIdx}")
    public void deleteSubscribe(@PathVariable("subscribeIdx") Integer subscribeIdx) {
    	adminService.deleteSubscribe(subscribeIdx);
    }
    
    // ------------------------ 공통코드 --------------------
    
    // 공통 코드 목록 리스트 조회
	@GetMapping("/api/commoncode")
	public List<AdminCommonCodeVO>findCommonCodeList() {

		return adminService.findCommonCodeList();
	}
	
    // 공통 코드 상세 정보 조회
    @GetMapping("/api/commoncode/{commoncodeIdx}")
    public AdminCommonCodeVO findCommoncodeDetail(@PathVariable("commoncodeIdx") Integer commoncodeIdx) {
        return adminService.findCommoncodeDetail(commoncodeIdx);
    }
    
    // 공통 코드 수정
    @PostMapping("/api/commoncode/{commoncodeIdx}/update")
    public void updateCommonCode(@PathVariable("commoncodeIdx") Integer commoncodeIdx,
            					 @RequestBody AdminCommonCodeVO CommonCodeVO) {
        
    	adminService.updateCommonCode(commoncodeIdx, CommonCodeVO);
    }
	
    // 공통코드 삭제 이벤트
    @PostMapping("/api/commoncode/{commoncodeIdx}")
    public void deleteCommonCodeById(@PathVariable("commoncodeIdx") Integer commoncodeIdx) {
    	adminService.deleteCommonCodeById(commoncodeIdx);
    }
    
    // 공통코드 그룹 리스트
    @GetMapping("/api/common-code-groups")
    public List<AdminCommonCodeGroupVO> findAllGroups() {
        return adminService.findAllGroups();
    }
    
    // 공통코드 그룹 추가
    @PostMapping("/api/common-code-groups")
    public void saveCommonCodeGroup(@RequestBody AdminCommonCodeGroupVO CommonCodeGroupVO) {

    	adminService.saveCommonCodeGroup(CommonCodeGroupVO);
    }
    
    // 공통코드 추가
    @PostMapping("/api/comcode/common_codes")
    public void saveCommonCode(@RequestBody List<AdminCommonCodeVO> CommonCodeVO) {

    	adminService.saveCommonCode(CommonCodeVO);
    }
    
    // --------------------------- 어드민 -----------------------
    
	// 어드민 회원 목록 리스트 조회
	@GetMapping("/api/members/admin")
	public List<MemberListVO> adminMemberFindAll() {

		return adminService.adminMemberFindAll();
	}
	
	// -------------------------- 로그 --------------------------
	
	// 회원 로그인 로그 리스트 조회
	@GetMapping("/api/members/log")
	public List<UserLogVO> findUserLogList() {

		return adminService.findUserLogList();
	}
	
    // 로그 삭제 이벤트
    @PostMapping("/api/log/{LogIdx}")
    public void deleteUserLog(@PathVariable("LogIdx") Integer LogIdx) {
    	adminService.deleteUserLog(LogIdx);
    }
    
    
}
