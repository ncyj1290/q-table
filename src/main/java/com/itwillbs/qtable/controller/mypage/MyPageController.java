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
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.itwillbs.qtable.service.member.MemberService;
import com.itwillbs.qtable.service.mypage.PasswordService;
import com.itwillbs.qtable.service.mypage.ReservationListService;
import com.itwillbs.qtable.service.mypage.ReviewService;
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

	private final ReviewService reviewService;

	private final MemberService memberService;

	private final PasswordEncoder passwordEncoder;

	private final PasswordService passwordService;

	@GetMapping("/mypage_main")
	public String mypageMain() {

		return "mypage/mypageMain";
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

	@GetMapping("/password")
	public String password() {

		return "mypage/password";
	}

	private String getMemberIdx(QtableUserDetails userDetails) {
		Member member = userDetails.getMember();
		return String.valueOf(member.getMemberIdx());
		// String memberIdx = getMemberIdx(userDetails); 이거 복사해서 넣으면 됨
	}

	// 리뷰 가져오기
	@GetMapping("/mypage_review")
	public String mypageReview(@AuthenticationPrincipal QtableUserDetails userDetails, Model model) {
		String memberIdx = getMemberIdx(userDetails);
		List<Map<String, Object>> myReviews = reviewService.getMyReviews(memberIdx);
		model.addAttribute("myReviews", myReviews);

		return "mypage/mypageReview";
	}

	// 스크랩 불러오기
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

	// 방문내역 불러오기
	@GetMapping("/mypage_history")
	public String mypageHistory(@AuthenticationPrincipal QtableUserDetails userDetails, Model model,
			@RequestParam(value = "reserveResult", required = false) String reserveResult) {
		System.out.println("reserveResult 파라미터 값: " + reserveResult);
		String memberIdx = getMemberIdx(userDetails);

		// 방문완료만 필터
		if (reserveResult == null || reserveResult.isEmpty()) {
			reserveResult = "rsrt_01";
		}

		List<Map<String, Object>> upcomingList = reservationService.getUpcomingList(memberIdx, reserveResult);
		model.addAttribute("upcomingList", upcomingList);

		return "mypage/mypageHistory";
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

	// 예약취소 업데이트
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

	// 현재 비밀번호
	@PostMapping("/CheckCurrentPassword")
	@ResponseBody
	public Map<String, Object> checkCurrentPassword(@RequestParam("current_pass") String currentPass,
			@AuthenticationPrincipal QtableUserDetails userDetails) {
		boolean isValid = false;

		// 저장된 해시 비밀번호 불러오기
		String storedHashedPassword = userDetails.getMember().getMemberPw();

		// 비밀번호 비교 (BCrypt 사용 시)
		isValid = passwordEncoder.matches(currentPass, storedHashedPassword);

		return Map.of("isValid", isValid);
	}

	// 새로운 비밀번호
	@PostMapping("/UpdatePassword")
	@ResponseBody
	public Map<String, Object> updatePassword(@RequestParam("current_pass") String currentPass,
			@RequestParam("new_pass") String newPass, @AuthenticationPrincipal QtableUserDetails userDetails) {
		try {
			String storedHashedPassword = userDetails.getMember().getMemberPw();

			if (!passwordEncoder.matches(currentPass, storedHashedPassword)) {
				return Map.of("success", false, "message", "현재 비밀번호가 올바르지 않습니다.");
			}

			// 새 비밀번호 해시화 후 저장
			passwordService.updatePassword(userDetails.getMember().getMemberIdx(), newPass);

			return Map.of("success", true);
		} catch (Exception e) {
			return Map.of("success", false, "message", "비밀번호 변경 중 오류가 발생했습니다.");
		}
	}
}
