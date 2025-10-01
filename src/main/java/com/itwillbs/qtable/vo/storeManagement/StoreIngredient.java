package com.itwillbs.qtable.vo.storeManagement;

import lombok.Data;

/* 매장 -> 원재료 데이터 담는 Class */
@Data
public class StoreIngredient {
	/* 재료 이름 */
	private String ingredients_name;
	/* 재료 원산지 */
	private String ingredients_origin;
	/* 재료 알레르기 정보 */
	private String allergy;
}
