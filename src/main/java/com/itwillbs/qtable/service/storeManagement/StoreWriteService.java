package com.itwillbs.qtable.service.storeManagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionSynchronizationManager;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.qtable.mapper.storeManagementMapper.StoreCommonCode;
import com.itwillbs.qtable.mapper.storeManagementMapper.StoreData;
import com.itwillbs.qtable.mapper.storeManagementMapper.StoreWrite;
import com.itwillbs.qtable.service.FileUploadService;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;
import com.itwillbs.qtable.vo.storeManagement.StoreIngredient;
import com.itwillbs.qtable.vo.storeManagement.StoreMenu;
import com.itwillbs.qtable.vo.storeManagement.StorePicture;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;

@Service
public class StoreWriteService {
	
	@Autowired
	StoreCommonCode storeCommonCode;
	
	@Autowired
	StoreWrite storeWrite;
	
	@Autowired
	StoreData storeData;
	
	@Autowired
	FileUploadService fileUploadService;
	
	/* 매장 관련 공통코드 모두 불러와서 모델에 넣는 함수 -> 그냥 여기 다 모아버리겠음. */
	public void selectAllCommonCodeForStore(Model model) {
		
		/* 은행 목록 */
		List<CommonCodeVO> bankList = storeCommonCode.selectCommonCodeForStore("bank_code", null);
		model.addAttribute("bankList", bankList);
		
		/* 매장 운영 시간 */
		List<CommonCodeVO> operationTimeList = storeCommonCode.selectCommonCodeForStore("time", null);
		model.addAttribute("operationTimeList", operationTimeList);
		
		/* 매장 휴일(?) */
		List<CommonCodeVO> holidayList = storeCommonCode.selectCommonCodeForStore("store_holiday", null);
		model.addAttribute("holidayList", holidayList);
		
		/* =========================================== */
		/* 매장 카테고리 (전체) */
		List<CommonCodeVO> storeCategoryList = storeCommonCode.selectCommonCodeForStore("store_category", null);
		model.addAttribute("storeCategoryList", storeCategoryList);
		
		/* 카테고리 국가 */
		List<CommonCodeVO> ctCountryList = storeCommonCode.selectCommonCodeForStore("store_category", "srct_m01");
		model.addAttribute("ctCountryList", ctCountryList);
		
		/* 카테고리 육류 */
		List<CommonCodeVO> ctMeatList = storeCommonCode.selectCommonCodeForStore("store_category", "srct_m02");
		model.addAttribute("ctMeatList", ctMeatList);
		
		/* 카테고리 해산물 */
		List<CommonCodeVO> ctSeafoodList = storeCommonCode.selectCommonCodeForStore("store_category", "srct_m03");
		model.addAttribute("ctSeafoodList", ctSeafoodList);
		
		/* 카테고리 술 */
		List<CommonCodeVO> ctDrinkList = storeCommonCode.selectCommonCodeForStore("store_category", "srct_m04");
		model.addAttribute("ctDrinkList", ctDrinkList);
		
		/* 카테고리 카페 */
		List<CommonCodeVO> ctCafeList = storeCommonCode.selectCommonCodeForStore("store_category", "srct_m05");
		model.addAttribute("ctCafeList", ctCafeList);
		/* =========================================== */
		
		/* 편의 시설 */
		List<CommonCodeVO> facilityList = storeCommonCode.selectCommonCodeForStore("convenient_facilities", null);
		model.addAttribute("facilityList", facilityList);
		
		/* 분위기 */
		List<CommonCodeVO> atmosphereList = storeCommonCode.selectCommonCodeForStore("atmosphere", null);
		model.addAttribute("atmosphereList", atmosphereList);
	}
	
	/* 새로운 매장 삽입 */
	@Transactional
	public void insertNewStore(StoreVO storeVO) throws Exception {
		
		/* ------------------------------------------------------------------------------------ */
		/* 기본 매장 정보 insert */

		/* 운영 시간  -> 만약 24시간이면 Open, Close 시간 null, 다른 값 필요하면 그걸로 넣어주겠음. */
		boolean timeFlag = false;
		if("true".equals(storeVO.getFlag_24hour())) timeFlag = true;
		storeVO.set_24hour(timeFlag);
		
		/* 주소 정보 가공 */
		String postCode = storeVO.getPost_code();
		String address = storeVO.getAddress();
		String addDetail = storeVO.getAddress_detail();
		
		String[] addressDiv = address.split(" ");
		
		System.out.println("Check Address Div: " + addressDiv.toString());
		
		storeVO.setSido(addressDiv[0]);
		storeVO.setSigungu(addressDiv[1]);
		
		
		String fullAdd = postCode + ", " + address + ", " + addDetail;
		storeVO.setFull_address(fullAdd);
		
		/* 1차 Insert -> Store 기본 정보 저장 */
		int storeRes = storeWrite.insertNewStore(storeVO);
		
		/* 저장 후 매장 idx get */
		int storeIdx = storeVO.getStore_idx();
		
		/* ------------------------------------------------------------------------------------ */
		/* 프로필 이미지 등록 */
		/* 기본 프로필 이미지 경로 */
		storeVO.setStore_profile_path("/icons/icon_store_profile.png");
		
		/* 프로필 이미지 존재하면 업로드 하고 경로 추출 */
		MultipartFile storeProfileFile = storeVO.getStore_profile_file();
		if(storeProfileFile != null && !storeProfileFile.isEmpty()) storeVO.setStore_profile_path(fileUploadService.saveFileAndGetPath(storeProfileFile));
		
		storeWrite.insertNewImage("imguse_01", storeIdx, storeVO.getStore_profile_path());
		
		/* ------------------------------------------------------------------------------------ */
		/* 휴일 등록 */
		String[] holidayList = storeVO.getHolidays().split(",");
		if(holidayList != null) for(String holiday:holidayList) storeWrite.insertNewHoliday(storeIdx, holiday);

		/* ------------------------------------------------------------------------------------ */
		/* 매장 사진 등록 */
		List<StorePicture> spList = storeVO.getStorePictureList();
		
		if(spList != null && !spList.isEmpty()) {
			
			for(StorePicture pic : spList) {
				
				MultipartFile pictureFile = pic.getStore_picture();
				
				if(pictureFile != null && !pictureFile.isEmpty()) {
					pic.setImage_url(fileUploadService.saveFileAndGetPath(pictureFile));
					/* imguse_02 -> 메장 사진 */
					storeWrite.insertNewImage("imguse_02", storeIdx, pic.getImage_url());
				}
			}
		}
		
		/* ------------------------------------------------------------------------------------ */
		/* 분위기 */
		String[] amenityList = storeVO.getStore_amenity().split(",");
		if(amenityList != null) for(String amenity:amenityList) storeWrite.insertNewAmenity(storeIdx, amenity);

		/* ------------------------------------------------------------------------------------ */
		/* 편의 시설 */
		List<String> atmoList = storeVO.getStore_atmosphere();
		if(atmoList != null && !atmoList.isEmpty()) for(String atmo:atmoList) storeWrite.insertNewAtmosphere(storeIdx, atmo);

		/* ------------------------------------------------------------------------------------ */
		/* 카테고리 */
		List<String> categoryList = storeVO.getStore_category();
		if(categoryList != null && !categoryList.isEmpty()) for(String cat:categoryList) storeWrite.insertNewCategory(storeIdx, cat);

		/* ------------------------------------------------------------------------------------ */
		/* 삭자재 */
		List<StoreIngredient> ingList = storeVO.getIngredientList();
		
		if(ingList != null && !ingList.isEmpty()) {
			for(StoreIngredient ing:ingList) {
				ing.setStore_idx(storeIdx);
				storeWrite.insertNewIngredient(ing);
			}
		}
		
		/* ------------------------------------------------------------------------------------ */
		/* 메뉴 */
		List<StoreMenu> menuList = storeVO.getMenuList();
		
		if(!menuList.isEmpty()) {
			for(StoreMenu menu : menuList) {
				
				/* 기본 메뉴 프로필 */
				menu.setImage_url("/icons/icon_menu_profile.png");
				
				/* 파일 유뮤 확인하고 파일 저장 로직 진행 */
				MultipartFile menuImg = menu.getMenu_picture();
				if(menuImg != null && !menuImg.isEmpty()) menu.setImage_url(fileUploadService.saveFileAndGetPath(menuImg));
				
				/* 메뉴 먼저 저장하고 idx 가져와서 메뉴 이미지 저장 */
				menu.setStore_idx(storeIdx);
				storeWrite.insertNewMenu(menu);
				
				/* imguse_03 -> 메뉴 사진 */
				storeWrite.insertNewImage("imguse_03", menu.getMenu_idx(), menu.getImage_url());
			}
		}
		
		/* ------------------------------------------------------------------------------------ */
		/* 메뉴판 업로드 */
		MultipartFile menuBoardFile = storeVO.getMenu_board_picture();
		if(menuBoardFile != null && !menuBoardFile.isEmpty()) storeVO.setMenu_board_url(fileUploadService.saveFileAndGetPath(menuBoardFile));
		
		/* imguse_04 -> 메뉴판 사진 */
		storeWrite.insertNewImage("imguse_04", storeIdx, storeVO.getMenu_board_url());
	}
	
	/* ------------------------------------------------------------------------------------ */
	/* 매장 정보 업데이트(수정) */
	@Transactional
	public void updateStore(StoreVO storeVO) throws Exception {

		/* ------------------------------------------------------------------------------------ */
		/* 기본 매장 정보 Update */
		/* 이거는 매장 idx */
		int storeIdx = storeVO.getStore_idx();
		
		boolean timeFlag = false;
		if("true".equals(storeVO.getFlag_24hour())) timeFlag = true;
		storeVO.set_24hour(timeFlag);
		
		/* 주소 정보 가공 */
		String postCode = storeVO.getPost_code();
		String address = storeVO.getAddress();
		String addDetail = storeVO.getAddress_detail();
		
		String[] addressDiv = address.split(" ");
		
		System.out.println("Check Address Div: " + addressDiv.toString());
		
		storeVO.setSido(addressDiv[0]);
		storeVO.setSigungu(addressDiv[1]);
		
		System.out.println(storeVO.getSido());
		System.out.println(storeVO.getSigungu());
		
		
		String fullAdd = postCode + ", " + address + ", " + addDetail;
		storeVO.setFull_address(fullAdd);
		
		/* 1차 Insert -> Store 기본 정보 수정 */
		int storeRes = storeWrite.updateStoreBasicData(storeVO);
		System.out.println("Basic Data Updated Result: " + storeRes);
		
		/* ------------------------------------------------------------------------------------ */
		/* 프로필 이미지 수정 */
		MultipartFile storeProfileFile = storeVO.getStore_profile_file();
		/* 파일 업로드한게 있으면 교체하고 따로 없으면 기존꺼 그냥 그대로 쓰는걸로. */
		if(storeProfileFile != null && !storeProfileFile.isEmpty()) {
			
			/* 프로필 사진 경로 */
			String profilePath = storeVO.getStore_profile_path();
			
			storeData.deleteStoreImage(profilePath, "imguse_01");
			fileUploadService.deleteFileByPath(profilePath);
			
			/* 새거 집어넣기 */
			storeVO.setStore_profile_path(fileUploadService.saveFileAndGetPath(storeProfileFile));
			storeWrite.insertNewImage("imguse_01", storeIdx, storeVO.getStore_profile_path());
		}

		/* ------------------------------------------------------------------------------------ */
		/* 휴일 수정 -> 변경 사항 있으면 기존거 다 지우고 새로 업로드 */
		String[] holidayList = storeVO.getHolidays().split(",");
		
		if(holidayList != null) {
			/* 기존 휴일 정보들 삭제 */
			storeData.deleteHolidayByStoreIdx(storeIdx);

			/* 휴일 새로 집어넣기 */
			for(String holiday:holidayList) storeWrite.insertNewHoliday(storeIdx, holiday);
		}
		
		/* ------------------------------------------------------------------------------------ */
		/* 매장 사진 수정 */
		List<StorePicture> spList = storeVO.getStorePictureList();
		
		/* 기존 이미지 정보들 제거 */
		storeData.deleteStoreImageByStoreIdx(storeIdx, "imguse_02");	
		
		if(spList != null && !spList.isEmpty()) {
			
			/* 사진 목록들 돌면서 이미지 insert */
			for(StorePicture pic : spList) {
				
				/* 아무것도 없으면 Pass */
				if(pic.getStore_picture().isEmpty() && pic.getImage_url() == null) continue;
				
				/* 업로드 된 파일이 있으면 그걸로 경로 수정, 그거 아니면 기존거 그대로 유지 */
				MultipartFile pictureFile = pic.getStore_picture();
				if(pictureFile != null && !pictureFile.isEmpty()) pic.setImage_url(fileUploadService.saveFileAndGetPath(pictureFile));

				/* imguse_02 -> 메장 사진 */
				storeWrite.insertNewImage("imguse_02", storeIdx, pic.getImage_url());
			}
		}
		
		/* ------------------------------------------------------------------------------------ */
		/* 분위기 수정 */
//		String[] amenityList = storeVO.getStore_amenity().split(",");
//		if(amenityList != null) for(String amenity:amenityList) storeWrite.insertNewAmenity(storeIdx, amenity);

		/* ------------------------------------------------------------------------------------ */
		/* 편의 시설 수정 */
//		List<String> atmoList = storeVO.getStore_atmosphere();
//		if(atmoList != null && !atmoList.isEmpty()) for(String atmo:atmoList) storeWrite.insertNewAtmosphere(storeIdx, atmo);

		/* ------------------------------------------------------------------------------------ */
		/* 카테고리 수정 */
//		List<String> categoryList = storeVO.getStore_category();
//		if(categoryList != null && !categoryList.isEmpty()) for(String cat:categoryList) storeWrite.insertNewCategory(storeIdx, cat);
		
		
	}

}
