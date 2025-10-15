package com.itwillbs.qtable.vo.admin;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubscribeListVO {
	
	private int subscribe_idx;
	private int store_idx;
	private int member_idx;
	private LocalDate subscribe_start;
	private LocalDate subscribe_end;
	private int remaining_days; // 남은 구독일수
	private String store_name;
	private String member_id;

	
	
}
