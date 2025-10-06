package com.itwillbs.qtable.service.storeManagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.itwillbs.qtable.mapper.storeManagementMapper.StoreCommonCode;
import com.itwillbs.qtable.mapper.storeManagementMapper.StoreWrite;
import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;

@Service
public class StoreWriteService {
	
	@Autowired
	StoreCommonCode storeCommonCode;
	
	@Autowired
	StoreWrite storeWrite;
	
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
	public void insertNewStore(StoreVO storeVO) {
		
		/* ------------------------------------------------------------------------------------ */
//		/* 주소 정보 가공 */
		String postCode = storeVO.getPost_code();
		String address = storeVO.getAddress();
		String addDetail = storeVO.getAddress_detail();
		
		String[] addressDiv = address.split(" ");
		
		System.out.println("Check Address Div: " + addressDiv.toString());
		
		storeVO.setSido(addressDiv[0]);
		storeVO.setSigungu(addressDiv[1]);
		
		
		String fullAdd = postCode + ", " + address + ", " + addDetail;
		storeVO.setFull_address(fullAdd);
		
		/* ------------------------------------------------------------------------------------ */
		/* 1차 Insert -> Store 기본 정보 저장 */
		int storeRes = storeWrite.insertNewStore(storeVO);
		

		/* ------------------------------------------------------------------------------------ */
		/* 휴일 등록 */
		String[] holidayList = storeVO.getHolidays().split(",");
		
		if(holidayList != null) {
			for(String holiday:holidayList) {
				storeWrite.insertNewHoliday(1, holiday);
			}
		}
		
		/* ------------------------------------------------------------------------------------ */
		/* 편의 시설 */
		String[] amenityList = storeVO.getStore_amenity().split(",");
		
		if(amenityList != null) {
			for(String amenity:amenityList) {
				storeWrite.insertNewAmenity(1, amenity);
			}
		}

		/* ------------------------------------------------------------------------------------ */
		/* 편의 시설 */
		List<String> atmoList = storeVO.getStore_atmosphere();
		
		if(atmoList != null) {
			for(String atmo:atmoList) {
				storeWrite.insertNewAtmosphere(1, atmo);
			}
		}
		
		/* ------------------------------------------------------------------------------------ */
		/* 편의 시설 */
		List<String> categoryList = storeVO.getStore_category();
		
		if(categoryList != null) {
			for(String cat:categoryList) {
				storeWrite.insertNewCategory(1, cat);
			}
		}
		
		
	}
	
	
	
	
	
	

}
