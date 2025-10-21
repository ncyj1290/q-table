package com.itwillbs.qtable.vo.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.itwillbs.qtable.config.MaskingUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreListVO {

	private Integer member_idx;
	private Integer store_idx;
	private String store_name;
	private String member_id;
	private String email;
	private LocalDateTime signup_date;
    private String member_status;      // 코드 값
    private String member_status_name; // 코드 라벨
	private String store_status;
	private String rejection_reason;
	private LocalDate processed_at;
	private LocalDate applied_at;
	private LocalDateTime leave_at;
	
	public void applyMasking() {
        // 탈퇴 상태이고, 탈퇴일이 존재하며, 3개월 이내일 때
        if ("mstat_02".equals(this.member_status) &&
            this.leave_at != null &&
            this.leave_at.isAfter(LocalDateTime.now().minusMonths(3))) 
        {
            this.email = MaskingUtils.maskEmail(this.email); // 별도 MaskingUtils 유틸리티 클래스로 분리
        }
	}
	

}
