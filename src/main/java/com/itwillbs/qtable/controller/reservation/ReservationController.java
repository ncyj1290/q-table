package com.itwillbs.qtable.controller.reservation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.service.reservation.ReservationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class ReservationController {

	private final ReservationService reservationService;
	
	// 예약 페이지 이동
	@GetMapping("reservation")
	public String reservation(
			@AuthenticationPrincipal QtableUserDetails userDetails,
			@RequestParam(value = "store_idx", required = false) Integer storeIdx,
			@RequestParam(value = "reserve_date", required = false) String reserveDate,
			@RequestParam(value = "reserve_time", required = false) String reserveTime,
			@RequestParam(value = "person_count", required = false) Integer personCount,
			Model model) {

		Member member = userDetails.getMember();

		// 매장 정보 조회
		Map<String, Object> storeInfo = reservationService.getStoreInfo(storeIdx);

		// Model에 담기
		model.addAttribute("storeInfo", storeInfo);
		model.addAttribute("userQMoney", member.getQMoney());
		model.addAttribute("storeIdx", storeIdx);
		model.addAttribute("reserveDate", reserveDate);
		model.addAttribute("reserveTime", reserveTime);
		model.addAttribute("personCount", personCount);

		return "reservation/reservation";
	}

	// 예약 전송 API
	@PostMapping("/api/reservation/submit")
	@ResponseBody
	public Map<String, Object> submitReservation(@RequestBody Map<String, Object> reservationData,
												@AuthenticationPrincipal QtableUserDetails userdetail) {
		
		Member member = userdetail.getMember();
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			Integer memberIdx = member.getMemberIdx();

			Map<String, Object> result = reservationService.insertReservation(reservationData, memberIdx);

			response.put("success", true);
			response.put("message", "예약이 완료되었습니다.");
			response.put("data", result);
		} catch (Exception e) {
			e.printStackTrace();
			// service 에서 던진 에러 처리 
			response.put("success", false);
			response.put("message", e.getMessage());
		}

		return response;
	}

}
