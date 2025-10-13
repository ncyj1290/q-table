package com.itwillbs.qtable.mapper.storeManagementMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.vo.storeManagement.StoreIngredient;
import com.itwillbs.qtable.vo.storeManagement.StoreMenu;
import com.itwillbs.qtable.vo.storeManagement.StorePicture;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;

/* 매장 관련 데이터들 불러오는 쿼리 */
@Mapper
public interface StoreData {

	/* 매장 프로필 정보들 들고오는 쿼리문 */
	StoreVO selectStoreProfileByOwnerIdx(@Param("member_idx") int member_idx);
	
	/* 회원 idx로 매장 idx 찾는 쿼리문 */
	int selectStoreIdxByOwnerIdx(@Param("member_idx") int member_idx);
	
	/* 예약 받기 상태 셀렉트 쿼리문 */
	StoreVO selectAcceptStatus(@Param("store_idx") int store_idx);
	
	/* 예약 받기 상태 토글 쿼리문 */
	int toggleAcceptStatus(@Param("store_idx") int store_idx);
	
	/* 매장 기본 정보 다 들고오는거 */
	StoreVO selectStoreBasicData(@Param("store_idx") int store_idx);
	
	/* 매장 프로필 이미지 불러오는 쿼리문 */
	String selectProfilePathByStoreIdx(@Param("store_idx") int store_idx);
	
	/* 매장 이미지 불러오는 쿼리문 */
	List<StorePicture> selectStoreImgPathByStoreIdx(@Param("store_idx") int store_idx);
	
	/* 매장 식자재 불러오는 쿼리문 */
	List<StoreIngredient> selectIngredientByStoreIdx(@Param("store_idx") int store_idx);
	
	/* 매장 메뉴 불러오는 쿼리문 */
	List<StoreMenu> selectMenuByStoreIdx(@Param("store_idx") int store_idx);
	
	/* 휴일 불러오는 쿼리문 */
	List<String> selectHolidayByStoreIdx(@Param("store_idx") int store_idx);
	
	/* 편의 시설 불러오는 쿼리문 */
	List<String> selectAmenityByStoreIdx(@Param("store_idx") int store_idx);
	
	/* 매장 카테고리 */
	List<String> selectCategoryByStoreIdx(@Param("store_idx") int store_idx);
	
	/* 매장 분위기 */
	List<String> selectAtmosphereByStoreIdx(@Param("store_idx") int store_idx);
	
	/* 메뉴판 이미지 */
	String selectBoardImgPathByStoreIdx(@Param("store_idx") int store_idx);
	
	
	
}
