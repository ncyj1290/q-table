package com.itwillbs.qtable.mapper.mypage;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PasswordMapper {

	// 회원 비밀번호 가져오기
	String selectPasswordByMemberIdx(int memberIdx);

	// 회원 비밀번호 변경
	int updatePassword(@Param("memberIdx") int memberIdx, @Param("newPassword") String newPassword);

}
