package com.itwillbs.qtable.controller.mypage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.service.FileUploadService;
import com.itwillbs.qtable.service.member.MemberService;
import com.itwillbs.qtable.service.mypage.PasswordService;
import com.itwillbs.qtable.service.mypage.PaymentListService;
import com.itwillbs.qtable.service.mypage.ReservationListService;
import com.itwillbs.qtable.service.mypage.ReviewService;
import com.itwillbs.qtable.service.mypage.ScrapService;
import com.itwillbs.qtable.service.pay.KakaoPayService;
import com.itwillbs.qtable.service.reservation.ReservationService;
import com.itwillbs.qtable.service.storeDetail.StoreDetailService;
import com.itwillbs.qtable.vo.PageResponse;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class MyPageController {

	private final KakaoPayService kakaoPayService;

	private final ReservationListService reservationListervice;

	private final ScrapService scrapService;

	private final ReviewService reviewService;

	private final PasswordEncoder passwordEncoder;

	private final PasswordService passwordService;

	private final StoreDetailService storeService;

	private final FileUploadService fileUploadService;

	private final ReservationService reservationService;
	
	private final MemberService memberService;
	
	private final PaymentListService paymentListService;
	

	@GetMapping("/mypage_main")
	public String mypageMain() {

		return "mypage/mypageMain";
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
	
	@GetMapping("/member_delete")
	public String memberDeletePage(
	    @AuthenticationPrincipal QtableUserDetails userDetails) {
	  
	  if (userDetails == null) {
	    return "redirect:/login";
	  }
	  
	  return "mypage/memberDelete";  // HTML 파일 반환
	}

	@PostMapping("/member_delete")
	  public ResponseEntity<Map<String, Object>> memberDelete(@AuthenticationPrincipal QtableUserDetails userDetails,
														      @RequestParam(value = "password") String password,
														      @RequestParam(value = "agree") boolean agree) {
	    Map<String, Object> response = new HashMap<>();

	    int memberIdx = userDetails.getMember().getMemberIdx();
	    
	    boolean result = passwordService.memberDelete(memberIdx, password, agree);
	    
	    if (result) {
	      response.put("success", true);
	      response.put("message", "회원탈퇴가 완료되었습니다");
	      return ResponseEntity.ok(response);
	    } else {
	      response.put("success", false);
	      response.put("message", "회원탈퇴 처리 중 오류가 발생했습니다");
	      return ResponseEntity.ok(response);
	    }
	  }
	
	// 리뷰 가져오기
	@GetMapping("/mypage_review")
	public String mypageReview(@AuthenticationPrincipal QtableUserDetails userDetails, Model model) throws Exception {
		String memberIdx = getMemberIdx(userDetails);
		List<Map<String, Object>> myReviews = reviewService.selectReviewsByMember(memberIdx);

		// 이미지 다중선택
		// JSON 문자열을 List로 변환하는 데 사용하는 Jackson 라이브러리의 객체
		ObjectMapper mapper = new ObjectMapper();

		// 각 리뷰를 순회하며 review_img(JSON 문자열)를 List<String>으로 파싱해서 imgList라는 키로 저장
		for (Map<String, Object> review : myReviews) {
			// review_img 컬럼: 이미지 여러 개가 JSON 배열(String)로 되어 있다
			String jsonArr = (String) review.get("review_img");
			// JSON이 null이 아니고 빈 값도 아니라면 파싱
			if (jsonArr != null && !jsonArr.isEmpty()) {
				// JSON 문자열을 실제 List<String>으로 변환
				List<String> imgList = mapper.readValue(jsonArr, new TypeReference<List<String>>() {
				});
				review.put("imgList", imgList);
			} else {
				// 이미지가 없는 경우 빈 리스트 추가
				review.put("imgList", new ArrayList<>());
			}
		}
		model.addAttribute("myReviews", myReviews);
		return "mypage/mypageReview";
	}

	// 리뷰 삭제
	@PostMapping("/delect_review")
	@ResponseBody
	public String deleteReview(@AuthenticationPrincipal QtableUserDetails userDetails,
			@RequestParam("reviewIdx") int reviewIdx) {
		reviewService.deleteReview(reviewIdx, getMemberIdx(userDetails));
		return "success";
	}

	// 리뷰 업데이트
	@PostMapping("/reviews_update")
	@ResponseBody
	public Map<String, Object> updateReview(@RequestBody Map<String, Object> updateData,
			@AuthenticationPrincipal QtableUserDetails userDetails) {
		String reviewIdx = String.valueOf(updateData.get("reviewIdx"));
		String content = (String) updateData.get("content");
		String memberIdx = String.valueOf(userDetails.getMember().getMemberIdx());

		reviewService.updateReviewContent(reviewIdx, content, memberIdx);

		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("message", "리뷰 수정 성공");
		return response;
	}

	// 스크랩 불러오기
	@GetMapping("/mypage_scrap")
	public String mypageScrap(@AuthenticationPrincipal QtableUserDetails qtable, Model model) {
		int memberIdx = qtable.getMember().getMemberIdx();
		List<Map<String, Object>> list = scrapService.getScrapList(memberIdx);
		model.addAttribute("upcomingList", list);
		return "mypage/mypageScrap";
	}

	// 스크랩 추가/해제
	@PostMapping("/scrap/toggle")
	@ResponseBody
	public Map<String, Object> toggleScrap(@RequestParam("storeIdx") int storeIdx,
			@AuthenticationPrincipal QtableUserDetails qtable) {
		int memberIdx = qtable.getMember().getMemberIdx();
		boolean isScrapped = scrapService.toggleScrap(memberIdx, storeIdx);
		return Map.of("status", "success", "isScrapped", isScrapped);
	}

	// 방문내역 불러오기
	@GetMapping("/mypage_history")
	public String mypageHistory(@AuthenticationPrincipal QtableUserDetails userDetails, Model model,
			@RequestParam(value = "reserveResult", required = false) String reserveResult) {
		String memberIdx = getMemberIdx(userDetails);

		// 방문완료만 필터
		if (reserveResult == null || reserveResult.isEmpty()) {
			reserveResult = "rsrt_01";
		}

		List<Map<String, Object>> upcomingList = reservationListervice.getUpcomingList(memberIdx, reserveResult);
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

		List<Map<String, Object>> upcomingList = reservationListervice.getUpcomingList(memberIdx, reserveResult);
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
			success = reservationListervice.cancelReservation(memberIdx, reserveIdx);
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		return Collections.singletonMap("success", success);
	}

	// 예약변경 모달
	@GetMapping("reserv_change")
	public String reservChange(@AuthenticationPrincipal QtableUserDetails userDetails,
							   @RequestParam("store_idx") Integer storeIdx,
							   @RequestParam("people") Integer people,
							   @RequestParam("reserve_time") String reserveTime,
							   Model model) {
		// 매장 기본 정보 조회
		Map<String, Object> storeData = storeService.getStoreInfo(storeIdx);
		List<String> reservationTimeData = storeService.getAvailableReservationTimes(storeIdx);

		model.addAttribute("holiday", storeData.getOrDefault("holiday", List.of()));
		model.addAttribute("availableTimes", reservationTimeData);
		model.addAttribute("reservedPeople", people);
		model.addAttribute("reservedTime", reserveTime);  
		
		return "storeDetail/fragments/reservationCalendar :: reservationCalendar";
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

	// 닉네임 중복 체크
	@GetMapping("/checkNickname")
	@ResponseBody
	public Map<String, Object> checkNicknameForProfile(@RequestParam("nickname") String nickname) {
		Map<String, Object> response = new HashMap<>();
		boolean isDuplicate = passwordService.isNicknameDuplicate(nickname);

		if (isDuplicate) {
			response.put("dupResult", true);
			response.put("text", "이미 사용 중인 닉네임입니다.");
			response.put("color", "red");
		} else {
			response.put("dupResult", false);
			response.put("text", "사용 가능한 닉네임입니다.");
			response.put("color", "green");
		}

		return response;
	}

	// 닉네임 변경
	@PostMapping("/updateNickname")
	@ResponseBody
	public Map<String, Object> updateNickname(@RequestParam("nickname") String newNickname,
			@AuthenticationPrincipal QtableUserDetails userDetails) {
		Map<String, Object> response = new HashMap<>();
		int memberIdx = userDetails.getMember().getMemberIdx();

		if (passwordService.isNicknameDuplicate(newNickname)) {
			response.put("success", false);
			response.put("message", "이미 사용 중인 닉네임입니다.");
			return response;
		}

		boolean result = passwordService.updateNickname(memberIdx, newNickname);
		response.put("success", result);

		if (result) {
			// 닉네임 변경 성공 시 인증 객체도 갱신
			userDetails.getMember().setNickName(newNickname);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,
					authentication.getCredentials(), authentication.getAuthorities()));
		} else {
			response.put("message", "닉네임 변경에 실패했습니다.");
		}

		return response;
	}

	// 닉네임 자동 주입
	@GetMapping("/admin/fillEmptyNicknames")
	@ResponseBody
	public String fillEmptyNicknamesManually() {
		passwordService.fillEmptyNicknames();
		return "빈 닉네임 자동 생성 완료";
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

	// 프로필 업로드
	@PostMapping("/uploadProfileImage")
	public ResponseEntity<String> uploadProfileImage(@AuthenticationPrincipal QtableUserDetails qtable,
			@RequestParam("profileImage") MultipartFile profileImage) {

		try {
			int memberIdx = qtable.getMember().getMemberIdx();

			// 파일 업로드 후 경로 반환
			String imageUrl = fileUploadService.saveFileAndGetPath(profileImage);

			// 반환된 경로(DB 저장)
			passwordService.updateProfileImage(memberIdx, imageUrl);

			// 세션의 프로필 이미지 즉시 갱신
			qtable.getMember().setProfileImgUrl(imageUrl);

			return ResponseEntity.ok("이미지가 성공적으로 업로드되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 중 오류가 발생했습니다.");
		}
	}

	// 기본 이미지로 변경
	@PostMapping("/resetProfileImage")
	public ResponseEntity<String> resetProfileImage(@AuthenticationPrincipal QtableUserDetails qtable) {

		try {
			int memberIdx = qtable.getMember().getMemberIdx();

			// 기본 이미지 경로 할당
			String defaultImageUrl = "/img/profile.png";

			// DB 반영
			passwordService.updateProfileImage(memberIdx, defaultImageUrl);

			// 세션 반영
			qtable.getMember().setProfileImgUrl(defaultImageUrl);

			return ResponseEntity.ok("기본 이미지로 변경되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("기본 이미지 변경 중 오류가 발생했습니다.");
		}
	}

	// 음식 취향
	@PostMapping("/mypage/foodPref")
	@ResponseBody
	public String saveFoodPreference(@AuthenticationPrincipal QtableUserDetails qtable,
			@RequestBody List<String> foodPrefs) {
		int memberIdx = qtable.getMember().getMemberIdx();
		passwordService.saveFoodPrefs(memberIdx, foodPrefs);
		return "success";
	}

	// 음식 취향
	@GetMapping("/mypage/foodPref")
	@ResponseBody
	public List<String> getFoodPreference(@AuthenticationPrincipal QtableUserDetails qtable) {
		int memberIdx = qtable.getMember().getMemberIdx();
		return passwordService.getFoodPrefs(memberIdx);
	}

	@GetMapping("/mypage_pick")
	public String mypageMain(@AuthenticationPrincipal QtableUserDetails userDetails, Model model,
			@RequestParam(value = "reserveResult", required = false) String reserveResult) {
		String memberIdx = getMemberIdx(userDetails);

		List<Map<String, Object>> upcomingList = reservationListervice.getUpcomingList(memberIdx, reserveResult);
		model.addAttribute("upcomingList", upcomingList);

		return "mypage_pick";
	}
	
	// 결제 내역
	   @GetMapping("/mypage_payment")
	   public String mypagePayment(@AuthenticationPrincipal QtableUserDetails userDetails, Model model,
	         @RequestParam(value = "reserveResult", required = false) String reserveResult) {

	      Member member = userDetails.getMember();
	      String memberIdx = getMemberIdx(userDetails);

	      // 결제완료만 필터
	      if (reserveResult == null || reserveResult.isEmpty()) {
	         reserveResult = "rsrt_01";
	      }

	      List<Map<String, Object>> upcomingList = paymentListService.getPaymentList(memberIdx, reserveResult);
	      model.addAttribute("upcomingList", upcomingList);
	//
	      return "mypage/mypagePayment";
	   }

}
