package com.itwillbs.qtable.vo.admin;

import java.time.LocalDateTime;

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
	private LocalDateTime requested_at;
	private LocalDateTime processed_at;
	private String rejection_reason;
	
	public JeongsanListVO(Jeongsan entity) {
		this.jeongsan_idx = entity.getJeongsanIdx();
		this.member_idx = entity.getMemberIdx();
		this.jeongsan_amount = entity.getJeongsanAmount();
		this.calculate_result = entity.getCalculateResult();
		this.requested_at = entity.getRequestedAt();
		this.processed_at = entity.getProcessedAt();
		this.rejection_reason = entity.getRejectionReason();
	}
	
	
}
