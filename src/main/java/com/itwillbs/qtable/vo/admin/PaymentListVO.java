package com.itwillbs.qtable.vo.admin;

import java.time.LocalDateTime;


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
	
}
