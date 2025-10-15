package com.itwillbs.qtable.controller.main;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.qtable.entity.Store;
import com.itwillbs.qtable.service.StoreService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final StoreService storeService;
    @GetMapping("/")
    public String home(Model model) {
    	 List<Store> stores = storeService.getAllStores();	
         model.addAttribute("stores", stores);
    	
        return "index"; 
    }
}
