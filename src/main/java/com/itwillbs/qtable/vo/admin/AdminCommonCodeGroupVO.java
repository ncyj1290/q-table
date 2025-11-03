package com.itwillbs.qtable.vo.admin;

import java.time.LocalDateTime;

import lombok.Data;


/* 공통 코드 그룹 VO */
@Data
public class AdminCommonCodeGroupVO {
	private int group_idx;
	private String group_code;
	private String group_desc;
	private boolean using_status;
	private LocalDateTime create_date;
	private int creater_idx;
	private LocalDateTime update_date;
	private int updater_idx;
}
