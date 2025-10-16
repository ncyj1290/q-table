package com.itwillbs.qtable.vo.myPage;

import lombok.Data;

@Data
public class PortOneVO {

	private int code;
	private String message;
	private PaymentResponse response;

	@Data
	public static class PaymentResponse {
		private String imp_uid;
		private String merchant_uid;
		private int amount;
		private String status;
		private String pay_method;
		private String item_name;

	}

}
