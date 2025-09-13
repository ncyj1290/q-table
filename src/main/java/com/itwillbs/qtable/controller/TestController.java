package com.itwillbs.qtable.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {
    
    @GetMapping("/test1")
    public String test1() {
        return "test/test1";  
    }
    
    @GetMapping("/test2")
    public String test2() {
        return "test/test2";  
    }
    
    @GetMapping("/test3")
    public String test3() {
        return "test/test3";  
    }
}