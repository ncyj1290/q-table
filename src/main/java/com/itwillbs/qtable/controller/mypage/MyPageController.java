package com.itwillbs.qtable.controller.mypage;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.service.mypage.ReservationLisService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class MyPageController {

	private final ReservationLisService reservationService;

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

	@GetMapping("/mypage_main")
	public String mypageMain() {

		return "mypage/mypageMain";
	}

	// 예약현황
	@GetMapping("/reservation_list")
	public String reservationList(Model model, HttpServletRequest request,
								  @AuthenticationPrincipal QtableUserDetails userDetails) {
		Member member = userDetails.getMember();
		String MemberIdx = String.valueOf(member.getMemberIdx());
		// 방문예정 예약 리스트와 취소된 예약 리스트 각각 조회
		List<Map<String, Object>> upcomingList = reservationService.getUpcomingList(MemberIdx);
		List<Map<String, Object>> canceledList = reservationService.getCanceledList(MemberIdx);

		// 뷰에서 사용할 변수명으로 리스트 저장
		model.addAttribute("upcomingList", upcomingList);
		model.addAttribute("canceledList", canceledList);

		return "mypage/reservationList";
	}
}
