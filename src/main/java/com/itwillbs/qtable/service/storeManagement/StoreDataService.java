package com.itwillbs.qtable.service.storeManagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.itwillbs.qtable.mapper.storeManagementMapper.StoreData;
import com.itwillbs.qtable.vo.storeManagement.StoreIngredient;
import com.itwillbs.qtable.vo.storeManagement.StoreMenu;
import com.itwillbs.qtable.vo.storeManagement.StorePicture;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;

/* 매장 정보 관련 데이터 불러오거나 관리하는 뭐 그런 종류의 서비스 */
@Service
public class StoreDataService {
	
	@Autowired
	StoreData storeData;

	/* 매장 프로필에 사용될 기본 정보같은거 불러와서 모델에 처박는 서비스 */
	public void injectStoreProfileByOwnerIdx(Model model, int member_idx) {
		/* 스토어 기본(프로필) 정보고 spData라는 이름으로 들어감 */
		StoreVO spData = storeData.selectStoreProfileByOwnerIdx(member_idx);
		model.addAttribute("spData", spData);
	}
	
	/* 예약 받기 상태 토글 후 결과 반환 서비스 */
	@Transactional
	public StoreVO toggleAndGetAcceptStatus(int store_idx) {
		/* 일단 먼저 토글 */
		storeData.toggleAcceptStatus(store_idx);
		/* 그리고 토글 된 결과 리턴 */
		return storeData.selectAcceptStatus(store_idx);
	}
	
	/* 매장의 모든 정보 (메뉴, 식자재 등) 긁어오는거 */
	@Transactional
	public StoreVO selectAllStoreData(int member_idx){
		
		/* 매장 idx부터 일단 찾아오기 */
		int store_idx = storeData.selectStoreIdxByOwnerIdx(member_idx);
		
		/* Store idx 로 기본 매장 정보 다 가져오기 */
		StoreVO sData = storeData.selectStoreBasicData(store_idx);
		
		/* 주소 재가공 */
		String[] ad = sData.getFull_address().split(",");
		sData.setAddress(ad[1]);
		sData.setAddress_detail(ad[2]);
		
		System.out.println("Check Modify Store Store Data VO: " + sData.toString());
		
		/* 매장 프로필 이미지 VO에 삽입 */
		sData.setStore_profile_path(storeData.selectProfilePathByStoreIdx(store_idx));
		System.out.println(sData.getStore_profile_path().toString());
		
		/* 매장 사진 들고와서 VO에 삽입 -> 없으면 처리 안함 */
		List<StorePicture> pList = storeData.selectStoreImgPathByStoreIdx(store_idx);
		if (!pList.isEmpty()) sData.setStorePictureList(pList);
		
		System.out.println(sData.getStorePictureList().toString());
		
		/* 식자재 불러와서 VO에 삽입 */
		List<StoreIngredient> iList = storeData.selectIngredientByStoreIdx(store_idx);
		if(!iList.isEmpty()) sData.setIngredientList(iList);
		
		System.out.println(sData.getIngredientList().toString());
		
		/* 메뉴 불러와서 VO에 삽입 */
		List<StoreMenu> mList = storeData.selectMenuByStoreIdx(store_idx);
		if(!mList.isEmpty()) sData.setMenuList(mList);
		
		System.out.println(sData.getMenuList().toString());
		
		/* 휴일 불러와서 VO 삽입 */
		List<String> hList = storeData.selectHolidayByStoreIdx(store_idx);
		if(!hList.isEmpty()) sData.setHoliday_list(hList);
		
		System.out.println(sData.getHoliday_list().toString());
		
		/* 편의 시설 불러와서 VO 삽입 */
		List<String> aList = storeData.selectAmenityByStoreIdx(store_idx);
		if(!aList.isEmpty()) sData.setAmenity_list(aList);
		
		System.out.println(sData.getAmenity_list().toString());
		
		/* 매장 카테고리 VO 삽입 */
		List<String> cList = storeData.selectCategoryByStoreIdx(store_idx);
		if(!cList.isEmpty()) sData.setStore_category(cList);
		
		System.out.println(sData.getStore_category().toString());
		
		/* 매장 분위기 VO 삽입 */
		List<String> atList = storeData.selectAtmosphereByStoreIdx(store_idx);
		if(!atList.isEmpty()) sData.setStore_atmosphere(atList);
		
		System.out.println(sData.getStore_atmosphere().toString());
		
		
		/* 메장 메뉴판 */
		String menuBoardPath = storeData.selectBoardImgPathByStoreIdx(store_idx);
		if(menuBoardPath != null && !menuBoardPath.isBlank()) sData.setMenu_board_url(menuBoardPath);
		
		System.out.println(sData.getMenu_board_url());
		
		
		return sData;
	}
	
	/* 매장 정보 수정 서비스 */
	@Transactional
	public void modifyStore() {
		
		
		
		
		
		
		
		
	}
	
	
	

}
