package com.itwillbs.qtable.controller.mypage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.service.mypage.ReservationListService;
import com.itwillbs.qtable.service.storeManagement.StoreDataService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class MyPageController {


	private final ReservationListService reservationService;
	
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

	@GetMapping("/mypage_payment")
	public String mypagePayment() {

		return "mypage/mypagePayment";
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

	// 예약현황
//	@GetMapping("/reservation_list")
//	public String reservationList(Model model, HttpServletRequest request,
//									@AuthenticationPrincipal QtableUserDetails userDetails,
//									@RequestParam(value = "reserveResult", required = false) String reserveResult) {
//		Member member = userDetails.getMember();
//		String memberIdx = String.valueOf(member.getMemberIdx());
//		
//		// 예약상태 넘김
//		List<Map<String, Object>> upcomingList = reservationService.getUpcomingList(memberIdx, reserveResult);
//		System.out.println("upcomingList size: " + (upcomingList != null ? upcomingList.size() : "null"));
//		System.out.println("upcomingList contents: " + upcomingList);
//		model.addAttribute("upcomingList", upcomingList);
//		
//		
//		if (upcomingList != null && !upcomingList.isEmpty()) {
//			// 예약현황이 있을 때 보이는 화면
//			 System.out.println(upcomingList.get(0).keySet());
//			 System.out.println("upcomingList size: "+ upcomingList.size());
//			 System.out.println("Keys: " + upcomingList.get(0).keySet());
//			return "mypage/reservationList";
//		} else {
//			// 예약 현황이 없을 때 보이는 화면
//			return "mypage/mypageMain";
//		}
//	}
//	
//	//ajax
//	@GetMapping("/reservation_list/fragment")
//	public String reservationListFragment(Model model,
//	        @AuthenticationPrincipal QtableUserDetails userDetails,
//	        @RequestParam(value = "reserveResult", required = false) String reserveResult) {
//		
//		 String memberIdx = String.valueOf(userDetails.getMember().getMemberIdx());
//
//	    List<Map<String, Object>> upcomingList = reservationService.getUpcomingList(memberIdx, reserveResult);
//	    model.addAttribute("upcomingList", upcomingList);
//
//	    if (upcomingList == null || upcomingList.isEmpty()) {
//	    	 System.out.println(upcomingList.get(0).keySet());
//	        return "mypage/reservationList :: listFragment"; // 필요하면 메인 화면 일부 fragment 반환
//	    }
//	    return "mypage/reservationList :: listFragment"; // 예약 현황 fragment 반환
//	}
	
	@GetMapping("reservation_list")
	public String reservationList(Model model,
	                                      HttpServletRequest request,
	                                      @AuthenticationPrincipal QtableUserDetails userDetails,
	                                      @RequestParam(value = "reserveResult", required = false) String reserveResult) {
	    Member member = userDetails.getMember();
	    String memberIdx = String.valueOf(member.getMemberIdx());

	    if (reserveResult == null || reserveResult.isEmpty()) {
	        reserveResult = "rsrt_05";  // 기본 예약상태 설정
	    }

	    List<Map<String, Object>> upcomingList = reservationService.getUpcomingList(memberIdx, reserveResult);
	    model.addAttribute("upcomingList", upcomingList);
	    System.out.println("memberIdx=" + memberIdx + ", reserveResult=" + reserveResult);

	    // AJAX 요청인지 확인
	    String requestedWith = request.getHeader("X-Requested-With");
	    boolean isAjax = "XMLHttpRequest".equals(requestedWith);

	    if (isAjax) {
	        // AJAX이면 fragment 반환
	        return "mypage/reservationList :: listFragment";
	    } else {
	        // 일반 요청일 때 페이지 뷰 반환
	        if (upcomingList != null && !upcomingList.isEmpty()) {
	            return "mypage/reservationList";
	        } else {
	            return "mypage/mypageMain";
	        }
	    }
	}


}
