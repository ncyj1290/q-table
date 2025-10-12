package com.itwillbs.qtable.controller.mypage;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.qtable.service.mypage.ReservationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Controller
@RequiredArgsConstructor
@Log
public class MyPageController {
	
	private final ReservationService reservationService;

   @GetMapping("/mypage_main")
   public String mypageMain() {
      
      return "mypage/mypageMain";
   }
   
   @GetMapping("/mypage_review")
   public String mypageReview() {
      
      return "mypage/mypageReview";
   }
   
   @GetMapping("/mypage_scrap")
   public String mypageScrap() {
      
      return "mypage/mypageScrap";
   }
   
   @GetMapping("/mypage_history")
   public String mypageHistory() {
	   
	   
      
      return "mypage/mypageHistory";
   }
   
   
   @GetMapping("/reservation_cancel")
   public String reservationCancel() {
      
      return "mypage/reservationCancel";
   }   
   
   @GetMapping("/qmoney_charge")
   public String qmoneycharge() {
      
      return "mypage/qmoneyCharge";
   }
   
   @GetMapping("/setting")
   public String setting() {
      return "mypage/setting";
   }
   
   @GetMapping("/card_edit")
   public String cardEdit() {
      return "mypage/cardEdit";
   }
   
   @GetMapping("/member_delete")
   public String memberDelete() {
      
      return "mypage/memberDelete";
   }
   
   @GetMapping("/profile_settings")
   public String profileSettings() {
      
      return "mypage/profileSettings";
   }
   
   @GetMapping("/reservation_list")
   public String reservationList(Model model) {
	   Long memberIdx = 1L; // 실제로는 로그인 회원의 번호로 대체하세요!
       // 방문예정 예약 리스트와 취소된 예약 리스트 각각 조회
       List<Map<String, Object>> upcomingList = reservationService.getUpcomingList(memberIdx);
       List<Map<String, Object>> canceledList = reservationService.getCanceledList(memberIdx);

       // 뷰에서 사용할 변수명으로 리스트 저장
       model.addAttribute("upcomingList", upcomingList);
       model.addAttribute("canceledList", canceledList);
       return "mypage/reservationList";
   }   

}


