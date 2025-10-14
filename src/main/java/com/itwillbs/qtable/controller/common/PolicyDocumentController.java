package com.itwillbs.qtable.controller.common;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.qtable.service.storeDetail.StoreDetailService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PolicyDocumentController {
	
	private final StoreDetailService storeDetailService ;
	    @GetMapping("/")
	    public String home(Model model, Integer storeIdx) {
	    	Map<String, Object> storeInfo = storeDetailService.getStoreInfo(storeIdx);	    	    
	    	model.addAttribute("storeInfo", storeInfo);
	        return "index"; // templates/index.html과 일치해야 함
	    }
		@GetMapping("/terms_of_use")
		public String terms_of_use(){
			return "termsOfUse";
		}
		@GetMapping("/privacy_policy")
		public String privacy_policy(){
			return "privacyPolicy";
		}
		@GetMapping("/error")
		public String error() {
			return "error";
		}

}
