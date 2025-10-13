package com.itwillbs.qtable.service.storeManagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.qtable.mapper.storeManagementMapper.StoreCommonCode;
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
		
		if(holidayList != null) {
			for(String holiday:holidayList) {
				storeWrite.insertNewHoliday(storeIdx, holiday);
			}
		}
		
		/* ------------------------------------------------------------------------------------ */
		/* 매장 사진 등록 */
		List<StorePicture> spList = storeVO.getStorePictureList();
		
		if(!spList.isEmpty()) {
			
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
		
		if(amenityList != null) {
			for(String amenity:amenityList) {
				storeWrite.insertNewAmenity(storeIdx, amenity);
			}
		}

		/* ------------------------------------------------------------------------------------ */
		/* 편의 시설 */
		List<String> atmoList = storeVO.getStore_atmosphere();
		
		if(!atmoList.isEmpty()) {
			for(String atmo:atmoList) {
				storeWrite.insertNewAtmosphere(storeIdx, atmo);
			}
		}
		
		/* ------------------------------------------------------------------------------------ */
		/* 카테고리 */
		List<String> categoryList = storeVO.getStore_category();
		
		if(!categoryList.isEmpty()) {
			for(String cat:categoryList) {
				storeWrite.insertNewCategory(storeIdx, cat);
			}
		}
		
		/* ------------------------------------------------------------------------------------ */
		/* 삭자재 */
		List<StoreIngredient> ingList = storeVO.getIngredientList();
		
		if(!ingList.isEmpty()) {
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
	/* 매장 정보 업데이트 */
	@Transactional
	public void updateSotre() {
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
