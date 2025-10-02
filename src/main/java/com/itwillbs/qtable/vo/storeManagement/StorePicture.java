package com.itwillbs.qtable.vo.storeManagement;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/* 매장 -> 원재료 데이터 담는 Class */
@Data
public class StorePicture {
	/* 메뉴 사진 */
	private MultipartFile store_picture;
	/* 사진 경로 */
	private String image_url;
}
