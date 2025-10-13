package com.itwillbs.qtable.controller.storeManagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import com.itwillbs.qtable.config.QtableAccessDeniedHandler;
import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.mapper.storeManagementMapper.StoreData;
import com.itwillbs.qtable.mapper.storeManagementMapper.StoreWrite;
import com.itwillbs.qtable.service.FileUploadService;
import com.itwillbs.qtable.service.storeManagement.StoreDataService;
import com.itwillbs.qtable.service.storeManagement.StoreWriteService;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;
import com.itwillbs.qtable.vo.storeManagement.StoreIngredient;
import com.itwillbs.qtable.vo.storeManagement.StoreMenu;
import com.itwillbs.qtable.vo.storeManagement.StorePicture;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class StoreWriteController {

    private final QtableAccessDeniedHandler qtableAccessDeniedHandler;
	
	@Autowired
	StoreWriteService storeWriteService;
	
	@Autowired
	StoreWrite storeWrite;
	
	@Autowired
	StoreDataService storeDataService;
	
	@Autowired
	FileUploadService fileUploadService;


    StoreWriteController(QtableAccessDeniedHandler qtableAccessDeniedHandler) {
        this.qtableAccessDeniedHandler = qtableAccessDeniedHandler;
    }
	
	
	/* ================================================= */
	/* 매장 등록 관련 */
	@GetMapping("/write_store")
	public String wrtieStore(Model model) {

		/* 매장 관련 공통코드 모두 불러와서 Model에 담는 서비스 함수 */
		storeWriteService.selectAllCommonCodeForStore(model);

		return "storeManagement/writeStore";
	}
	
	/* 매장 등록 Post */
	@PostMapping("/write_store")
	public String inserNewStore(@ModelAttribute  StoreVO storeVO, @AuthenticationPrincipal QtableUserDetails user) throws Exception {
		
		storeVO.setMember_idx(user.getMember().getMemberIdx());
		
		System.out.println("Store Write - VO CHECK: " + storeVO.toString());
		System.out.println("Check Member IDX: " + storeVO.getMember_idx());
		
		/* 새로운 매장 및 매장 부가 요소 DB에 추가 */
		storeWriteService.insertNewStore(storeVO);
		
		return "redirect:store_reservation_list";
	}
	
	
	/* ================================================= */
	/* 매장 수정 관련 */
	
	@GetMapping("/modify_store")
	public String modifyStore(@AuthenticationPrincipal QtableUserDetails user, Model model) {
		
		/* 매장 관련 공통코드 모두 불러와서 Model에 담는 서비스 함수 */
		storeWriteService.selectAllCommonCodeForStore(model);
		
		StoreVO sData = storeDataService.selectAllStoreData(user.getMember().getMemberIdx());
		model.addAttribute("sData", sData);
		
		return "storeManagement/modifyStore";
	}
	
	
	
	
	
}
