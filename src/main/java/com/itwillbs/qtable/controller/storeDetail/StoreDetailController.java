package com.itwillbs.qtable.controller.storeDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.service.FileUploadService;
import com.itwillbs.qtable.service.storeDetail.StoreDetailService;
import com.itwillbs.qtable.vo.PageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Controller
@RequiredArgsConstructor
@Log
public class StoreDetailController {

	private final StoreDetailService storeService;
	private final FileUploadService fileUploadService;
	
	// 식당 상세 페이지 이동
	@GetMapping("store_detail_main")
	public String storeDetailMain(
			@AuthenticationPrincipal QtableUserDetails userDetails,
			@RequestParam("store_idx") Integer storeIdx,
			Model model) {
		
		// 매장 기본 정보 조회
		Map<String, Object> storeData = storeService.getStoreInfo(storeIdx);
		model.addAllAttributes(storeData);

		// 스크랩 여부 조회 (로그인 한 경우만)
		boolean isScrapped = false;
		if (userDetails != null) {
			Member member = userDetails.getMember();
			Integer memberIdx = member.getMemberIdx();
			isScrapped = storeService.isStoreScrapped(storeIdx, memberIdx);
		}
		model.addAttribute("isScrapped", isScrapped);

		// 매장 메뉴 섹션 조회 
		Map<String, Object> menuData = storeService.getMenuInfo(storeIdx);
		model.addAllAttributes(menuData);

		// 리뷰 정렬 옵션 공통코드 조회
		List<Map<String, Object>> sortOptions = storeService.getReviewSortOptions();
		model.addAttribute("sortOptions", sortOptions);

		// 매장 별점 정보 조회
		Map<String, Object> scoreData = storeService.getReviewScoreInfo(storeIdx);
		model.addAttribute("score", scoreData);

		// 리뷰 별점 분포 조회
		Map<String, Object> scoreDistribution = storeService.getScoreDistribution(storeIdx);
		model.addAllAttributes(scoreDistribution);

		// 예약 가능 시간 조회
		List<String> reservationTimeData = storeService.getAvailableReservationTimes(storeIdx);
		model.addAttribute("availableTimes", reservationTimeData);
		
//		log.info("storeData: " + storeData.toString()); // 식당 정보 섹션
//		log.info("categories: " + storeData.get("categories")); // 음식카테고리
//		log.info("atmosphere: " + storeData.get("atmosphere")); // 분위기
//		log.info("amenities: " + storeData.get("amenities")); // 편의시설
//		log.info("menuData: " + menuData.toString()); // 메뉴 섹션
//		log.info("review: " + reviewData.toString()); // 리뷰 섹션
//		log.info("scoreDistribution: " + scoreDistribution.toString()); // 리뷰 별점 분포
//		log.info("availableTimes: " + reservationTimeData.toString()); // 예약 가능 시간

		return "storeDetail/storeDetailMain";
	}

	// 리뷰 페이지네이션, 정렬 AJAX API
	@GetMapping("/api/storeDetail/reviews")
	@ResponseBody
	public PageResponse<Map<String, Object>> getReviewsPaged(
			@RequestParam("store_idx") Integer storeIdx,
			@RequestParam(value = "sort_type", defaultValue = "rvs_01") String sortType,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "5") int size,
			@AuthenticationPrincipal QtableUserDetails userDetails) {

		// 로그인한 사용자의 memberIdx 가져오기
		Integer memberIdx = null;
		if (userDetails != null) {
			memberIdx = userDetails.getMember().getMemberIdx();
		}

		return storeService.getReviewsPaged(storeIdx, sortType, page, size, memberIdx);
	}
	
	
	// 메뉴 페이지네이션 AJAX API
	@GetMapping("/api/storeDetail/menu")
	@ResponseBody
	public PageResponse<Map<String, Object>> getMenuPaged(
			@RequestParam("store_idx") Integer storeIdx,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "6") int size) {

		
		return storeService.getMenuPaged(storeIdx, page, size);
	}
	
	// 리뷰 작성 api
	@PostMapping("/api/storeDetail/reviews")
	@ResponseBody
	public Map<String, Object> writeReview(
			@AuthenticationPrincipal QtableUserDetails userDetails,
			@RequestParam("store_idx") Integer storeIdx,
			@RequestParam("score") Integer score,
			@RequestParam("content") String content,
			@RequestParam(value = "images", required = false) List<MultipartFile> images) {

		Map<String, Object> result = new HashMap<>();

		try {
			Member member = userDetails.getMember();
			Integer memberIdx = member.getMemberIdx();

			// 이미지 업로드하고 경로 뽑아내는 작업
			List<String> imagePaths = new ArrayList<>();
			if (images != null && !images.isEmpty()) {
				for (MultipartFile image : images) {
					if (!image.isEmpty()) {
						String path = fileUploadService.saveFileAndGetPath(image);
						imagePaths.add(path);
					}
				}
			}

			// 리뷰 데이터
			Map<String, Object> reviewData = new HashMap<>();
			reviewData.put("memberIdx", memberIdx);
			reviewData.put("storeIdx", storeIdx);
			reviewData.put("score", score);
			reviewData.put("content", content);
			reviewData.put("imagePaths", imagePaths);
			log.info(imagePaths.toString());

			storeService.insertReview(reviewData);

			result.put("success", true);
			result.put("message", "리뷰가 등록되었습니다.");

		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("message", e.getMessage());
		}

		return result;
	}

	// 스크랩 토글 API
	@PostMapping("/api/storeDetail/scrap/toggle")
	@ResponseBody
	public Map<String, Object> toggleScrap(
			@AuthenticationPrincipal QtableUserDetails userDetails,
			@RequestParam("store_idx") Integer storeIdx) {

		Map<String, Object> res = new HashMap<>();
		// 비로그인 시 처리
		if (userDetails == null) {
			res.put("success", false);
			res.put("message", "로그인이 필요한 서비스입니다.");
			return res;
		}

		Member member = userDetails.getMember();
		Integer memberIdx = member.getMemberIdx();

		try {
			// 토글 결과 여부
			int result = storeService.toggleScrap(storeIdx, memberIdx);

			if (result > 0) {
				res.put("success", true);
				res.put("message", "스크랩 처리가 완료되었습니다.");
			} else {
				res.put("success", false);
				res.put("message", "스크랩 처리중 오류가 발생했습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.put("success", false);
			res.put("message", e.getMessage());
		}

		return res;
	}
	
	// 리뷰 좋아요 토글
	@PostMapping("/api/storeDetail/reviews/{reviewIdx}/like")
	@ResponseBody
	public Map<String, Object> toggleReviewLike(
			@PathVariable("reviewIdx") Integer reviewIdx,
			@AuthenticationPrincipal QtableUserDetails userDetails) {

		Map<String, Object> res = new HashMap<>();

		// 비로그인 시 처리
		if (userDetails == null) {
			res.put("success", false);
			res.put("message", "로그인이 필요한 서비스입니다.");
			return res;
		}

		Member member = userDetails.getMember();
		Integer memberIdx = member.getMemberIdx();

		try {
			// 좋아요 토글 처리 (좋아요 수 반환)
			int likeCount = storeService.toggleReviewLike(reviewIdx, memberIdx);

			res.put("success", true);
			res.put("likeCount", likeCount);

		} catch (Exception e) {
			e.printStackTrace();
			res.put("success", false);
			res.put("message", "좋아요 처리 중 오류가 발생했습니다.");
		}

		return res;
	}
}
