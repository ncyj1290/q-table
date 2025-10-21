package com.itwillbs.qtable.vo.admin;

import java.time.LocalDateTime;

import com.itwillbs.qtable.config.MaskingUtils;
import com.itwillbs.qtable.entity.Jeongsan;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JeongsanListVO {
	
	private int jeongsan_idx;
	private int member_idx;
	private String store_name;
	private String member_id;
	private int jeongsan_amount;
	private String calculate_result;
	private String calculate_result_name;
	private LocalDateTime requested_at;
	private LocalDateTime processed_at;
	private String rejection_reason;
	private String account_number;
	private String bank_code;
	private String member_status;
	private LocalDateTime leave_at;
	
	public JeongsanListVO(Jeongsan entity) {
		this.jeongsan_idx = entity.getJeongsanIdx();
		this.member_idx = entity.getMemberIdx();
		this.jeongsan_amount = entity.getJeongsanAmount();
		this.calculate_result = entity.getCalculateResult();
		this.requested_at = entity.getRequestedAt();
		this.processed_at = entity.getProcessedAt();
		this.rejection_reason = entity.getRejectionReason();
	}
	
	public void applyMasking() {
        // 탈퇴 상태이고, 탈퇴일이 존재하며, 3개월 이내일 때
        if ("mstat_02".equals(this.member_status) &&
            this.leave_at != null &&
            this.leave_at.isAfter(LocalDateTime.now().minusMonths(3))) 
        {
            this.account_number = ("*****");
            this.bank_code = ("*****");
        }
	}
	
	
}
