package com.itwillbs.qtable.controller.chat;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.qtable.config.QtableUserDetails;
import com.itwillbs.qtable.entity.Member;
import com.itwillbs.qtable.mapper.storeDetail.StoreDetailMapper;
import com.itwillbs.qtable.service.FileUploadService;
import com.itwillbs.qtable.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Log
public class ChatApiController {

	private final ChatService chatService;
	private final StoreDetailMapper storeDetailMapper;
	private final FileUploadService fileUploadService;

	// 채팅방 생성 또는 기존 채팅방 반환
	@PostMapping("/room/insert")
	public Map<String, Object> insertChatRoom(
			@RequestParam("store_idx") Integer storeIdx,
			@AuthenticationPrincipal QtableUserDetails userDetails) {

		Member member = userDetails.getMember();
		Integer memberIdx = member.getMemberIdx();

		Map<String, Object> response = new HashMap<>();

		try {
			// 매장의 owner 조회
			Integer storeOwnerIdx = storeDetailMapper.getStoreMemberIdx(storeIdx);

			// 자신의 매장인 경우 차단
			if (storeOwnerIdx != null && storeOwnerIdx.equals(memberIdx)) {
				response.put("success", false);
				response.put("message", "자신의 매장에는 문의할 수 없습니다.");
				return response;
			}

			Map<String, Object> chatRoom = chatService.insertOrGetChatRoom(memberIdx, storeIdx);
			response.put("success", true);
			response.put("chatRoom", chatRoom);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "채팅방 생성에 실패했습니다.");
		}

		return response;
	}

	// 채팅 메시지 목록 조회
	@PostMapping("/messages")
	public Map<String, Object> getChatMessages(
			@RequestParam("room_idx") Integer roomIdx) {

		Map<String, Object> response = new HashMap<>();

		try {
			List<Map<String, Object>> messages = chatService.getChatMessages(roomIdx);
			response.put("success", true);
			response.put("messages", messages);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "메시지 조회에 실패했습니다.");
		}

		return response;
	}
	
	// 메시지 db 저장
	@PostMapping("/message/insert")
	public Map<String, Object> insertMessage(
			@AuthenticationPrincipal QtableUserDetails userDetails,
			@RequestParam("msg") String msg,
			@RequestParam("room_id") Integer roomId){

		Member member = userDetails.getMember();
		Integer memberIdx = member.getMemberIdx();

		Map<String, Object> response = new HashMap<>();
		try {
			chatService.insertChat(msg, memberIdx, roomId);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return response;
	}

	// 채팅방 메시지 읽음 처리
	@PostMapping("/mark-as-read")
	public Map<String, Object> markMessagesAsRead(
			@RequestParam("room_idx") Integer roomIdx,
			@AuthenticationPrincipal QtableUserDetails userDetails) {

		Member member = userDetails.getMember();
		Integer memberIdx = member.getMemberIdx();

		Map<String, Object> response = new HashMap<>();

		try {
			chatService.updateMessagesAsRead(memberIdx, roomIdx);
			response.put("success", true);
			response.put("message", "메시지 읽음 처리 완료");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "메시지 읽음 처리 실패");
		}

		return response;
	}

	// 채팅 이미지 업로드
	@PostMapping("/upload-image")
	public Map<String, Object> uploadChatImage(
			@RequestParam("image") MultipartFile image,
			@RequestParam("room_id") Integer roomId,
			@AuthenticationPrincipal QtableUserDetails userDetails) {

		Member member = userDetails.getMember();
		Integer memberIdx = member.getMemberIdx();

		Map<String, Object> response = new HashMap<>();

		try {
			String imageUrl = fileUploadService.saveFileAndGetPath(image);
			chatService.insertChat(imageUrl, memberIdx, roomId);
			response.put("success", true);
			response.put("imageUrl", imageUrl);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "이미지 업로드 실패");
		}

		return response;
	}

	// 전체 안읽은 메시지 수 조회
	@PostMapping("/unread/total")
	public Map<String, Object> getTotalUnreadCount(
			@AuthenticationPrincipal QtableUserDetails userDetails) {

		Member member = userDetails.getMember();
		Integer memberIdx = member.getMemberIdx();

		Map<String, Object> response = new HashMap<>();

		try {
			List<Map<String, Object>> unreadCounts = chatService.getUnreadCountByRoom(memberIdx);

			// 모든 채팅방의 안읽은 메시지 수를 합산
			int totalUnread = 0;
			for (Map<String, Object> unread : unreadCounts) {
				totalUnread += ((Number) unread.get("unread_count")).intValue();
			}

			response.put("success", true);
			response.put("totalUnread", totalUnread);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "안읽은 메시지 수 조회 실패");
		}

		return response;
	}

}
