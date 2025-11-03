package com.itwillbs.qtable.vo.admin;

import java.sql.Timestamp;

import lombok.Data;


/* 공통 코드 VO */
@Data
public class AdminCommonCodeVO {
	private int common_idx;
	private String group_code;
	private String group_desc;
	private String code;
	private String code_label;
	private String code_desc;
	private String parent_code;
	private int code_index;
	private boolean using_status;
	private Timestamp create_date;
	private int creater_idx;
	private Timestamp update_date;
	private int updater_idx;
}
