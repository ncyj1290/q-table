package com.itwillbs.qtable.controller.store_management;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StoreManagementController {

	
	/* 매장 관리자가 이용하는 매장 관리 메인 페이지 */
	@GetMapping("/store_management_main")
	public String storeManagement() {
		return "storeManagement/store_management_main";
	}
	
	/* 매장 등록/수정 페이지 */
	@GetMapping("/write_store")
	public String wirteStore() {
		return "storeManagement/writeStore";
	}
	
	
}
