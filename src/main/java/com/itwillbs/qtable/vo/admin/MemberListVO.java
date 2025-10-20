package com.itwillbs.qtable.vo.admin;

import java.time.LocalDateTime;

import com.itwillbs.qtable.entity.Member;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberListVO {

	private Integer member_idx;
	private String member_name;
	private String member_id;
	private String email;
	private LocalDateTime signup_date;
	private String member_status;
	private String member_type;

	public MemberListVO(Member entity, int rowNum) {
		this.member_idx = rowNum;
		this.member_name = entity.getMemberName();
		this.member_id = entity.getMemberId();
		this.email = entity.getEmail();
		this.signup_date = entity.getSignupDate();
		this.member_status = convertStatusCode(entity.getMemberStatus());
		this.member_type = convertTypeCode(entity.getMemberType());
	}
	
	// 공통코드 code -> code_label로 변환
	private String convertStatusCode(String statusCode) {
		if ("mstat_01".equals(statusCode)) {
			return "정상";
		} else {
			return "탈퇴";
		}
	}
	
	// 공통코드 code -> code_label로 변환
	private String convertTypeCode(String typeCode) {
		if ("mtype_02".equals(typeCode)) {
			return "일반회원";
		} else {
			return "매장관리자";
		}
	}

}
