package com.itwillbs.qtable.vo.storeManagement;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CalculateVO {
	private int jeongsan_idx;
	private	int member_idx;
	private int jeongsan_amount;
	private String calculate_result;
	private Timestamp requested_at;
	private Timestamp processed_at;
	private String rejection_reason;
	
	/* 그 외 Select 항목들 */
	private String member_id;
	/* 공통 코드 Labeal */
	private String result_label;

	/* 디테일에 넣을 추가 정보들 */
	private String full_address;
	private String account_number;
	private String store_name;
}
