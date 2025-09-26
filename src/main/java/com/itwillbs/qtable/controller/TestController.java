package com.itwillbs.qtable.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.qtable.entity.Member;
//import com.itwillbs.qtable.repository.MemberRepository;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
//    private MemberRepository memberRepository;
    
    @GetMapping("/test1")
    public String test1() {
        return "test/test1";  
    }
    
    @GetMapping("/test1-1")
    public String test1_1() {
        return "test/test1-1";  
    }
    
    @GetMapping("/test2")
    public String test2() {
        return "test/test2";  
    }
    
    @GetMapping("/test3")
    public String test3() {
        return "test/test3";  
    }
    
    @GetMapping("/test4")
    public String test4() {
        return "test/test4";  
    }
    
//    @PostMapping("testInsert")
//    public String testInsert(@RequestParam("name") String name, @RequestParam("pw") String password) {
//        Member member = new Member();
//        member.setName(name);
//        member.setPassword(password);
//
//        memberRepository.save(member);
//
//        return "redirect:test2";
//    }
}
