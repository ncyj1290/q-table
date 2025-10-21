package com.itwillbs.qtable.vo.search;

import java.util.List;


import lombok.Data;

@Data
public class searchVO {
	private List<String> loc;
	private List<String> food;
	private List<String> atmosphere;
	private List<String> facility;
	private String personCnt;
	private String sort;
	private List<String> price;
	private String day;
	private String time;
	private String query;
	private Integer member_idx;
}
