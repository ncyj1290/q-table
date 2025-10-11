package com.itwillbs.qtable.service.storeManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.itwillbs.qtable.mapper.storeManagementMapper.StoreData;
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
	

}
