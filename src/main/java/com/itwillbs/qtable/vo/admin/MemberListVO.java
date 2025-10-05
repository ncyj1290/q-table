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

	public MemberListVO(Member entity, int rowNum) {
		this.member_idx = rowNum;
		this.member_name = entity.getMemberName();
		this.member_id = entity.getMemberId();
		this.email = entity.getEmail();
		this.signup_date = entity.getSignupDate();
		this.member_status = convertStatusCode(entity.getMemberStatus());;
	}

	private String convertStatusCode(String statusCode) {
		if ("mstat_01".equals(statusCode)) {
			return "정상";
		} else {
			return "탈퇴";
		}
	}

}
