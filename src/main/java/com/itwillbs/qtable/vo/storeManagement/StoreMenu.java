package com.itwillbs.qtable.vo.storeManagement;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/* 매장 -> 원재료 데이터 담는 Class */
@Data
public class StoreMenu {
	/* 메뉴 이름 */
	private String menu_name;
	/* 메뉴 가격 */
	private String price;
	/* 메뉴 설명 */
	private String menu_content;
	/* 메뉴 중량 */
	private String menu_gram;
	
	/* 메뉴 사진 */
	private MultipartFile menu_picture;
	/* 사진 경로 */
	private String menu_picture_path;
}
