package com.itwillbs.qtable.vo.myPage;

import lombok.Data;

@Data
public class Amount {

	private Integer total; // 총 결제 금액
	private Integer tax_free; // 비과세 금액
//	private int tax; // 부가세 금액
	private Integer point; // 사용한 포인트
	private Integer discount; // 할인 금액
}