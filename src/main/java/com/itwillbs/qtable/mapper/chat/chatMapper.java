package com.itwillbs.qtable.mapper.chat;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface chatMapper {

	// 채팅방 생성
	public int insertChatRoom(Map<String, Object> params);

	// 채팅방 조회
	public Map<String, Object> getChatRoomByMemberAndStore(@Param("memberIdx") Integer memberIdx,@Param("storeIdx") Integer storeIdx);

	// 사용자의 모든 채팅방 목록 조회
	public List<Map<String, Object>> getChatRoomListByMember(@Param("memberIdx") Integer memberIdx);

	// 특정 채팅방의 메시지 목록 조회 (채팅방 인덱스 기준)
	public List<Map<String, Object>> getChatMessagesByRoomIdx(@Param("roomIdx") Integer roomIdx);
	
	// 마지막 대화내용 조회
	public String getLastMessage(Integer chatRoomIdx);

	// 메시지 저장
	public int insertChat(Map<String, Object> params);
	
	// 사용자의 모든 채팅방별 읽지 않은 메시지 수 조회
	public List<Map<String, Object>> getUnreadCountByRoom(Integer memberIdx);
	
	// 채팅방의 읽지 않은 메시지 모두 읽음으로 수정 
	public void updateMessagesAsRead(@Param("memberIdx") Integer memberIdx, @Param("roomIdx") Integer roomIdx);
	
	// 관리자 채팅방이 이미 있는지 조회
	public Integer getAdminChatRoomByMember(Integer memberIdx);

	// 관리자 채팅방 조회 (room_idx 반환)
	public Map<String, Object> getAdminChatRoom(Integer memberIdx);

	// 채팅방 updated_at 갱신
	public void updateChatRoomTimestamp(@Param("roomIdx") Integer roomIdx);

}