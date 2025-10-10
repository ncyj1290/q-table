package com.itwillbs.qtable.vo.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreListVO {

	private Integer member_idx;
	private Integer store_idx;
	private String store_name;
	private String member_id;
	private String email;
	private LocalDateTime signup_date;
	private String member_status;
	private String store_status;
	private String rejection_reason;
	private LocalDate processed_at;
	private LocalDate applied_at;
	

}
