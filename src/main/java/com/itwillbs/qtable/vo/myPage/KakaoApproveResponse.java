package com.itwillbs.qtable.vo.myPage;

import lombok.Data;

@Data
public class KakaoApproveResponse {

	private String aid; //요청 고유 번호
	private String tid; //결제 고유 번호
	private String cid; //가맹점 코드
	private String sid; // 정기 결제용
	private String partner_order_id; // 가맹점 주문번호
	private String partner_user_id; // 가맹점 회원 id
	private String payment_method_type; // 결제 수단
	private String payment_amount; // 결제 금액 정보
	private String item_name; // 상품명
	private String item_code; //상품 코드
	private int qunatity; // 상품 수량
	private String created_at; // 결제 요청 시간
	private String approved_at; // 결제 승인 시간
	private String payload; // 요정 전달 내용
}
