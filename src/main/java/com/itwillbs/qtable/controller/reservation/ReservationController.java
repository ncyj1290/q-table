package com.itwillbs.qtable.controller.reservation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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
	public String reservation() {
		return "reservation/reservation";
	}

	// 매장 정보 조회 API (예약 페이지용)
	@GetMapping("/api/reservation/store_info")
	@ResponseBody
	public Map<String, Object> getStoreInfo(@RequestParam("store_idx") Integer storeIdx) {
		Map<String, Object> response = new HashMap<>();

		try {
			Map<String, Object> storeInfo = reservationService.getStoreInfo(storeIdx);
			response.put("success", true);
			response.put("data", storeInfo);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
		}

		return response;
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

//			log.info("매장 번호: " + reservationData.get("store_idx"));
//			log.info("예약자명: " + reservationData.get("reserve_name"));
//			log.info("이메일: " + reservationData.get("reserve_email"));
//			log.info("예약일: " + reservationData.get("reserve_date"));
//			log.info("예약시간: " + reservationData.get("reserve_time"));
//			log.info("인원수: " + reservationData.get("person_count"));

			Map<String, Object> result = reservationService.createReservation(reservationData, memberIdx);

			response.put("success", true);
			response.put("message", "예약이 완료되었습니다.");
			response.put("data", result);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", e.getMessage());
		}

		return response;
	}

}
