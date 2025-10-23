package com.itwillbs.qtable.vo.storeManagement;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class StoreVO {

	private int store_idx;
	private int member_idx;
	
	private MultipartFile store_profile_file;
	private String store_profile_path;
	
	private String store_name;
	private String store_phone;
	private String account_number;
	private String bank_code;
	
	/* QR CODE 주소 */
	private String qr_code;
	
	private String store_seat;
	private String deposit;
	private String store_content;
	
	/* 닫는시간, 여는시간, 24시간 플래그 */
	private String open_time;
	private String close_time;
	private boolean is_24hour;
	
	private boolean is_accept;
	
	/* 매장 등록 페이지에서 사용 */
	private String flag_24hour;
	
	/* 평균 식사 비용 */
	private String price_avg;
	
	/* 휴일 목록 -> split(",") */
	private String holidays;
	/* 휴일 select 해서 뿌릴 때 */
	private List<String> holiday_list;
	
	/* 편의 시설 목록  -> split(",") 필요 */
	private String store_amenity;
	/* 편의 시설 select 해서 뿌릴 때 */
	private List<String> amenity_list;
	
	/* 매장 카테고리 */
	private List<String> store_category;
	/* 매장 분위기 */
	private List<String> store_atmosphere;
	
	/* 주소 */
	private String post_code;
	private String address;
	private String address_detail;
	private String full_address;
	
	private String sido;
	private String sigungu;
	
	
	/* 매장 이미지 사진 리스트 */
	 private List<StorePicture> storePictureList;
	/* 매장 원재료 리스트 */
	private List<StoreIngredient> ingredientList;
	/* 매장 메뉴 리스트 */
	private List<StoreMenu> menuList;
	
	/* 메뉴판 사진 */
	private MultipartFile menu_board_picture;
	/* 메뉴판 사진 경로 */
	private String menu_board_url;
	
	/* ================================= */
	/* 부가사항 - 큐머니 */
	private int q_money;
	
	
}
