package com.itwillbs.qtable.mapper.mypage;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PasswordMapper {

	// 회원 비밀번호 가져오기
	String selectPasswordByMemberIdx(int memberIdx);

	// 회원 비밀번호 변경
	int updatePassword(@Param("memberIdx") int memberIdx, @Param("newPassword") String newPassword);

	// 닉네임 중복 개수 조회
	int countNickname(String nickname);

	// 닉네임 업데이트
	int updateNickname(@Param("memberIdx") int memberIdx, @Param("newNickname") String newNickname);

	// 음식 취향
    void deleteByMemberIdx(int memberIdx);
    void insert(@Param("memberIdx") int memberIdx, @Param("pref") String pref);
    List<String> selectByMemberIdx(int memberIdx);

    // 닉네임 빈 회원 idx 조회
    List<Integer> selectMembersWithEmptyNickname();
    
    // 이미지 업로드
    int updateProfileImage(@Param("memberIdx") int memberIdx, @Param("profileImgUrl") String profileImgUrl);

    // 회원 상태 업데이트
    int updateMemberStatus(@Param("member_idx") int member_idx, @Param("member_status") String member_status);


    
}
