package com.itwillbs.qtable.controller.storeDetail;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StoreDetailController {
	
	@GetMapping("store_detail_main")
	public String storeDeatilMain() {
		return "storeDetail/storeDetailMain";
	}
}
 