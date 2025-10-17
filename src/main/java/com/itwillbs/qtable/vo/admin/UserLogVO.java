package com.itwillbs.qtable.vo.admin;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLogVO {
	private int log_idx;
	private int member_idx;
	private LocalDateTime login_time;
	private String ip_address;
	private String member_id;

}
