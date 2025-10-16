package com.itwillbs.qtable.controller.storeManagement;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.qtable.service.storeManagement.StoreWriteService;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;

@Controller
public class StoreManagementController {
	

	/* ================================================= */
	/* 매장 관리자가 이용하는 매장 관리 메인 페이지 */
	@GetMapping("/store_management_main")
	public String storeManagement() {
		return "storeManagement/store_management_main";
	}

	
	
	/* ================================================= */
	/* 구독권 구매 페이지 */
	@GetMapping("/purchase_subscribe")
	public String purchaseSubscribe() {
		return "storeManagement/purchaseSubscribe";
	}
	
	/* ================================================= */
	/* 정산 목록 페이지 */
	@GetMapping("/store_calculate_list")
	public String calculateList() {
		return "storeManagement/storeCalculateList";
	}
	
	/* 정산 상세 페이지 */
	@GetMapping("/store_calculate_detail")
	public String calculateDetail() {
		return "storeManagement/storeCalculateDetail";
	}
	
	
}
