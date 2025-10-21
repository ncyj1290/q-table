package com.itwillbs.qtable.vo.admin;

import java.time.LocalDateTime;

import com.itwillbs.qtable.config.MaskingUtils;
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
	private LocalDateTime leave_at;

	public MemberListVO(Member entity, int rowNum) {
		this.member_idx = rowNum;
		this.member_name = entity.getMemberName();
		this.member_id = entity.getMemberId();
		this.email = entity.getEmail();
		this.signup_date = entity.getSignupDate();
		this.member_status = convertStatusCode(entity.getMemberStatus());
		this.member_type = convertTypeCode(entity.getMemberType());
		this.leave_at = entity.getLeaveAt();
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
	
	public void applyMasking() {
	    // --- 마스킹 로직 ---
	    // 탈퇴 상태이고, 탈퇴일 존재하며, 3개월 이내일 때
	    if ("탈퇴".equals(this.member_status) &&
	        this.leave_at != null &&
	        this.leave_at.isAfter(LocalDateTime.now().minusMonths(3)))
	    {
            this.member_name = MaskingUtils.maskName(this.member_name);
            this.email = MaskingUtils.maskEmail(this.email);
        }
	    // --- 마스킹 로직 끝 ---
	}


}
