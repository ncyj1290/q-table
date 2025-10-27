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
	private List<String> keywords;
	private Integer member_idx;
	private Integer cursor;
	private Integer limit;
	private String priceCs;
	private String reviewCs;
	private String scoreCs;

	public boolean isEmpty() {
		//하나라도 있으면 false
		if(this.loc == null 
			&& this.food == null
			&& this.atmosphere == null
			&& this.facility == null
			&& this.personCnt == null
			&& this.sort == null
			&& this.price == null
			&& this.day == null
			&& this.query == null
			&& this.keywords == null
		) return true ;
		else {
			return false;
		}  
	}
		//아무것도 없으면 true
}

