package com.itwillbs.qtable.controller.storeManagement;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.zxing.WriterException;
import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.service.storeManagement.StoreDataService;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;


@Controller
public class StoreManagementController {
	
	@Autowired
	StoreDataService storeDataService;
	

	/* ================================================= */
	/* 매장 관리자가 이용하는 매장 관리 메인 페이지 */
	@GetMapping("/store_management_main")
	public String storeManagement(Model model, @AuthenticationPrincipal QtableUserDetails user) {
		
		/* 프로필 정보 가져와서 집어넣는건데 spData란 이름으로 모델에 집어넣게 될거임 */
		storeDataService.injectStoreProfileByOwnerIdx(model, user.getMember().getMemberIdx());
		
		return "storeManagement/storeManagementMain";
	}
	
	
	/* ================================================= */
	/* QR 코드날라간거 아니면 쓰지마시오. */
	/* 전 매장에 QR 삽입 */
//	@GetMapping("/inser_store_qr_code")
//	public String inserStoreQrCode(Model model, @AuthenticationPrincipal QtableUserDetails user) throws WriterException, IOException {
//		storeDataService.updateQrCodeForNullStore();
//		return "storeManagement/storeManagementMain";
//	}
	
}
