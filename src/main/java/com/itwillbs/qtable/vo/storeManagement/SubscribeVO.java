package com.itwillbs.qtable.vo.storeManagement;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class SubscribeVO {
	private int subscribe_idx;
	private int member_idx;
	private Timestamp subscribe_start;
	private Timestamp subscribe_end;
}
