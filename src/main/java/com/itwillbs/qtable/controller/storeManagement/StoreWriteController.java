package com.itwillbs.qtable.controller.storeManagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.itwillbs.qtable.service.FileUploadService;
import com.itwillbs.qtable.service.storeManagement.StoreWriteService;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;

@Controller
public class StoreWriteController {
	
	@Autowired
	StoreWriteService storeWriteService;
	
	@Autowired
	FileUploadService fileUploadService;
	
	
	/* ================================================= */
	/* 매장 등록/수정 페이지 */
	@GetMapping("/write_store")
	public String wrtieStore(Model model) {

		/* 매장 관련 공통코드 모두 불러와서 Model에 담는 서비스 함수 */
		storeWriteService.selectAllCommonCodeForStore(model);

		return "storeManagement/writeStore";
	}
	
	
	/* 매장 등록 Post */
	@PostMapping("/write_store")
	public String inserNewStore(@ModelAttribute  StoreVO storeVO) throws Exception {
		
		System.out.println("Check Write Store Submit -> Done");
		System.out.println("Store Write - VO CHECK: " + storeVO.toString());
		
		
		
		String savePath = fileUploadService.saveFileAndGetPath(storeVO.getStore_profile_file());
		
		
		
		
		/* 매장 주소 가공 */
		storeVO.setFull_address(storeVO.getPost_code() + " " + storeVO.getAddress() + " " + storeVO.getAddress_detail());
		
		
		
		
		System.out.println("File Save Path: " + savePath);

		return "redirect:store_reservation_list";
	}

}
