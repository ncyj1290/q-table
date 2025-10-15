package com.itwillbs.qtable.vo.storeManagement;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import lombok.Data;

/* 예약 정보 VO */
@Data
public class ReservationVO {
	private int reserve_idx;
	private int store_idx;
	private int member_idx;
	private String reserve_name;
	private String reserve_email;
	private Date reserve_date;
	private Time reserve_time;
	private Timestamp created_at;
	private String reserve_result;
	private Timestamp updated_at;
	private int person_count;
	private String requirement;
	private String allergy;
	
	/* 결과 공통코드 라벨 담는 변수 */
	private String result_label;
	/* 멤버 아이디 조인해서 같이 출력하려고 추가함 */
	private String member_id;
	/* 예약 거부 이유 */
	private String deny_reason;
}
