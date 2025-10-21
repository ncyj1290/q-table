package com.itwillbs.qtable.service.storeManagement;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class PaymentVO {
	private int payment_idx;
	private int member_idx;
	private Timestamp payment_date;
	private int payment_amount;
	private String pay_status;
	private String pay_way;
	private String pay_type;
	private String pay_reference;
	private int reference_idx;
	private String external_transaction_idx;
}
