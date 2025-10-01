package com.itwillbs.qtable.vo.storeManagement;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class StoreVO {

	private MultipartFile store_profile_file;
	private String store_profile_path;
	
	private String store_name;
	private String store_phone;
	private String account_number;
	private String accoutn_bank;
	
	/* 매장 테이블 -> 24시간인지 확인하는 Flag Column 추가 필요하다고 말해야함. */
	private String open_time;
	private String close_time;
//	private String is_24hour;
	
	/* 휴일 목록 -> split(",") */
	private String holidays;
	/* 편의 시설 목록  -> split(",") 필요 */
	private String store_facilities;
	
	/* 주소 */
	private String post_code;
	private String address;
	private String address_detail;
	private String full_address;
	
	private String store_seat;
	private String deposite;
	private String store_content;
	
	/* 매장 이미지 사진 리스트 */
	// private List<MultipartFile> store_picture;
	
	/* 매장 원재료 리스트 */
	private List<StoreIngredient> ingredientList;
	/* 매장 메뉴 리스트 */
	private List<StoreMenu> menuList;
	
}
