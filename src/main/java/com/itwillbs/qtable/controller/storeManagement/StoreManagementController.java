package com.itwillbs.qtable.controller.storeManagement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StoreManagementController {

	/* ================================================= */
	/* 매장 관리자가 이용하는 매장 관리 메인 페이지 */
	@GetMapping("/store_management_main")
	public String storeManagement() {
		return "storeManagement/store_management_main";
	}
	
	/* ================================================= */
	/* 매장 등록/수정 페이지 */
	@GetMapping("/write_store")
	public String wirteStore() {
		return "storeManagement/writeStore";
	}

	/* ================================================= */
	/* 예약 목록 페이지 */
	@GetMapping("/store_reservation_list")
	public String storeReservationList() {
		return "storeManagement/storeReservationList";
	}
	
	/* 예약 상세 페이지 */
	@GetMapping("/store_reservation_detail")
	public String storeReservationDetail() {
		return "storeManagement/storeReservationDetail";
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
