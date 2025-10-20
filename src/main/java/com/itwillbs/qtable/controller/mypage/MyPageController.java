package com.itwillbs.qtable.controller.mypage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
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
import com.itwillbs.qtable.service.mypage.ScrapService;
import com.itwillbs.qtable.service.pay.KakaoPayService;
import com.itwillbs.qtable.service.storeManagement.StoreDataService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class MyPageController {

	private final KakaoPayService kakaoPayService;
	private final ReservationListService reservationService;
	private final ScrapService scrapService;

	@GetMapping("/mypage_main")
	public String mypageMain() {

		return "mypage/mypageMain";
	}

	@GetMapping("/mypage_review")
	public String mypageReview() {

		return "mypage/mypageReview";
	}

	@GetMapping("/mypage_scrap")
	public String mypageScrap(@AuthenticationPrincipal QtableUserDetails qtable, Model model) {
		int memberIdx = qtable.getMember().getMemberIdx();
		List<Map<String, Object>> list = scrapService.getScrapList(memberIdx);
		System.out.println(list);
		model.addAttribute("upcomingList", list);
		return "mypage/mypageScrap";
	}

	// 스크랩 추가/해제
	@PostMapping("/scrap/toggle")
	@ResponseBody
	public Map<String, Object> toggleScrap(@RequestParam("storeIdx") int storeIdx,
			@AuthenticationPrincipal QtableUserDetails qtable) {
		int memberIdx = qtable.getMember().getMemberIdx();
		scrapService.toggleScrap(memberIdx, storeIdx);
		return Map.of("status", "success");
	}

	@GetMapping("/mypage_history")
	public String mypageHistory(@RequestParam(value="reserveResult", required=false) String reserveResult, Model model) {
		  System.out.println("reserveResult: " + reserveResult);

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

	private String getMemberIdx(QtableUserDetails userDetails) {
		Member member = userDetails.getMember();
		return String.valueOf(member.getMemberIdx());
		// String memberIdx = getMemberIdx(userDetails); 이거 복사해서 넣으면 됨
	}

	// 예약&취소 조회
	@GetMapping("reservation_list")
	public String reservationList(Model model, HttpServletRequest request,
			@AuthenticationPrincipal QtableUserDetails userDetails,
			@RequestParam(value = "reserveResult", required = false) String reserveResult) {

		String memberIdx = getMemberIdx(userDetails);
		// null이거나 빈 문자열이면 기본값 할당
		reserveResult = (reserveResult == null || reserveResult.isEmpty()) ? "rsrt_05" : reserveResult;

		List<Map<String, Object>> upcomingList = reservationService.getUpcomingList(memberIdx, reserveResult);
		model.addAttribute("upcomingList", upcomingList);

		// AJAX 요청인지 확인
		String requestedWith = request.getHeader("X-Requested-With");
		boolean isAjax = "XMLHttpRequest".equals(requestedWith);

		if (isAjax) {
			return "mypage/reservationList :: listFragment"; // AJAX 요청: fragment 반환
		} // isAjax x-> upcomingList에 따라 뷰 반환
		return (upcomingList != null && !upcomingList.isEmpty()) ? "mypage/reservationList" : "mypage/mypageMain";
	}

	// 예약취소
	@PostMapping("/reservation_cancel")
	@ResponseBody
	public Map<String, Object> reservationCancel(@RequestParam("reserveIdx") int reserveIdx,
			@AuthenticationPrincipal QtableUserDetails userDetails) {

		String memberIdx = getMemberIdx(userDetails);

		boolean success = false;
		try {
			// 서비스 호출로 예약 상태 취소로 변경
			success = reservationService.cancelReservation(memberIdx, reserveIdx);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		return Collections.singletonMap("success", success);
	}

	// q-money 금액 불러오기
	@GetMapping("/mypage/qmoneyBalance")
	@ResponseBody
	public Map<String, Object> getQmoneyBalance(@AuthenticationPrincipal QtableUserDetails userDetails) {
		int memberIdx = userDetails.getMember().getMemberIdx();

		// 서비스에서 총 Q-money 계산
		int totalQmoney = kakaoPayService.getTotalQmoney(memberIdx);

		Map<String, Object> result = Map.of("balance", totalQmoney);
		return result;
	}

}
