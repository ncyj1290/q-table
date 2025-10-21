package com.itwillbs.qtable.service.storeManagement;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.itwillbs.qtable.mapper.storeManagementMapper.StoreData;
import com.itwillbs.qtable.mapper.storeManagementMapper.StoreSubscribe;
import com.itwillbs.qtable.vo.storeManagement.StoreIngredient;
import com.itwillbs.qtable.vo.storeManagement.StoreMenu;
import com.itwillbs.qtable.vo.storeManagement.StorePicture;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;
import com.itwillbs.qtable.vo.storeManagement.SubscribeVO;

/* 매장 정보 관련 데이터 불러오거나 관리하는 뭐 그런 종류의 서비스 */
@Service
public class StoreDataService {
	
	@Autowired
	StoreData storeData;

	@Autowired
	StoreSubscribeService storeSubscribeService;
	
	/* 매장 프로필에 사용될 기본 정보같은거 불러와서 모델에 처박는 서비스 */
	public void injectStoreProfileByOwnerIdx(Model model, int member_idx) {
		
		SubscribeVO subscribe = storeSubscribeService.selectSubscribe(member_idx);
		if(subscribe.getSubscribe_end() != null) model.addAttribute("subscribe", subscribe);
		
		/* 스토어 기본(프로필) 정보고 spData라는 이름으로 들어감 */
		StoreVO spData = storeData.selectStoreProfileByOwnerIdx(member_idx);
		System.out.println("Check SPDATA: " + spData.toString());

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
		sData.setAddress(ad[1].trim().trim());
		sData.setAddress_detail(ad[2].trim());

		/* 매장 프로필 이미지 VO에 삽입 */
		sData.setStore_profile_path(storeData.selectProfilePathByStoreIdx(store_idx));

		/* 매장 사진 들고와서 VO에 삽입 -> 없으면 처리 안함 */
		List<StorePicture> pList = storeData.selectStoreImgPathByStoreIdx(store_idx);
		if (!pList.isEmpty()) sData.setStorePictureList(pList);

		/* 식자재 불러와서 VO에 삽입 */
		List<StoreIngredient> iList = storeData.selectIngredientByStoreIdx(store_idx);
		if(!iList.isEmpty()) sData.setIngredientList(iList);

		/* 메뉴 불러와서 VO에 삽입 */
		List<StoreMenu> mList = storeData.selectMenuByStoreIdx(store_idx);
		if(!mList.isEmpty()) sData.setMenuList(mList);

		/* 휴일 불러와서 VO 삽입 */
		List<String> hList = storeData.selectHolidayByStoreIdx(store_idx);
		if(!hList.isEmpty()) sData.setHoliday_list(hList);

		/* 편의 시설 불러와서 VO 삽입 */
		List<String> aList = storeData.selectAmenityByStoreIdx(store_idx);
		if(!aList.isEmpty()) sData.setAmenity_list(aList);

		/* 매장 카테고리 VO 삽입 */
		List<String> cList = storeData.selectCategoryByStoreIdx(store_idx);
		if(!cList.isEmpty()) sData.setStore_category(cList);
		
		/* 매장 분위기 VO 삽입 */
		List<String> atList = storeData.selectAtmosphereByStoreIdx(store_idx);
		if(!atList.isEmpty()) sData.setStore_atmosphere(atList);

		/* 메장 메뉴판 */
		String menuBoardPath = storeData.selectBoardImgPathByStoreIdx(store_idx);
		if(menuBoardPath != null && !menuBoardPath.isBlank()) sData.setMenu_board_url(menuBoardPath);

		System.out.println("Check Modify Store Store Data VO: " + sData.toString());
		
		return sData;
	}

}
