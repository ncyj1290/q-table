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
	
	private String store_seat;
	private String deposite;
	private String store_content;
	
	/* 매장 테이블 -> 24시간인지 확인하는 Flag Column 추가 필요하다고 말해야함. */
	private String open_time;
	private String close_time;
//	private String is_24hour;
	
	/* 휴일 목록 -> split(",") */
	private String holidays;
	/* 편의 시설 목록  -> split(",") 필요 */
	private String store_facilities;
	/* 매장 카테고리 */
	private String store_category;
	/* 매장 분위기 */
	private String store_atmosphere;
	
	/* 주소 */
	private String post_code;
	private String address;
	private String address_detail;
	private String full_address;
	
	/* 매장 이미지 사진 리스트 */
	 private List<StorePicture> storePictureList;
	/* 매장 원재료 리스트 */
	private List<StoreIngredient> ingredientList;
	/* 매장 메뉴 리스트 */
	private List<StoreMenu> menuList;
	
	/* 메뉴판 사진 */
	private MultipartFile menu_board_picture;
	/* 메뉴판 사진 경로 */
	private String menu_board;
}
