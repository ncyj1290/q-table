package com.itwillbs.qtable.vo.myPage;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ScrapVO {

	private int scrap_idx;
	private int member_idx;
	private int store_idx;
	private Timestamp create_at;
	
    // 추가
    private String store_name;
    private String store_content;
    private String store_phone;
    private String full_address;
    private String image_url; 
    private Double avgScore;   
}
