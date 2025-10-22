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
}
