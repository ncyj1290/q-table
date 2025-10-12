package com.itwillbs.qtable.vo.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KakaoMemberVO {
	private String userId;          // 아이디
    private String name;            // 이름
    private String email;           // 이메일
    private String gender;          // 성별
    private String profileImage;    // 프로필 이미지
    private String baseAddress;     // 기본주소 (도로명 등)
    private String detailAddress;   // 세부주소 (건물명 등)
    private String zoneNo;          // 우편번호
    private String birthday;        // 생년월일 (YYYY-MM-DD)
    private String socialId;		// 소셜 아이디 : 회원 탈퇴시 사용
}
