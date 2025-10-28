package com.itwillbs.qtable.vo.myPage;

import lombok.Data;

@Data
public class KakaoCancelResponse {

	private String tid;
	private String cid;
	private String status;
	private String partner_order_id;
	private String partner_user_id;
	private String payment_method_type;
	private String item_name;
	private int quantity;

	private Amount amount;
	private Amount approved_cancel_amount;
	private Amount canceled_amount;
	private Amount cancel_available_amount;

	private String created_at;
	private String approved_at;
	private String canceled_at;

}
