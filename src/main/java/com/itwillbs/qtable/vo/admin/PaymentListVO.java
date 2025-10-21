package com.itwillbs.qtable.vo.admin;

import java.time.LocalDateTime;

import com.itwillbs.qtable.config.MaskingUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentListVO {

	private int payment_idx;
	private int member_idx;
	private int store_idx;
    private String store_name;
	private String member_name;
	private String member_id;
	private LocalDateTime payment_date;
	private int payment_amount;
	private String pay_status;
	private String pay_way;
	private String pay_type;
	private String pay_reference;
	private int reference_idx;
	private String external_transaction_idx;
	private String member_status;
	private LocalDateTime leave_at;
	
	public void applyMasking() {
        // 탈퇴 상태이고, 탈퇴일이 존재하며, 3개월 이내일 때
        if ("mstat_02".equals(this.member_status) &&
            this.leave_at != null &&
            this.leave_at.isAfter(LocalDateTime.now().minusMonths(3))) 
        {
            this.member_name = MaskingUtils.maskName(this.member_name); // 별도 MaskingUtils 유틸리티 클래스로 분리
        }
	}
	
}
