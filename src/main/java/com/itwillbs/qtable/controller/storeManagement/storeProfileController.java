package com.itwillbs.qtable.controller.storeManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.mapper.storeManagementMapper.StoreData;
import com.itwillbs.qtable.service.storeManagement.StoreDataService;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;

@RestController
public class storeProfileController {

	@Autowired
	StoreData storeData;
	
	@Autowired
	StoreDataService storeDataService;
	
	/* 그뭐냐 그그그 예약 받는지 안 받는지 상태 갱신하고 현재 상태 주는거 */
	@PostMapping("toggle_accept_status")
	public boolean toggleAccept(@AuthenticationPrincipal QtableUserDetails user, @RequestParam("store_idx") int store_idx) {
		StoreVO res = storeDataService.toggleAndGetAcceptStatus(store_idx);
		return res.is_accept();
	}

}
