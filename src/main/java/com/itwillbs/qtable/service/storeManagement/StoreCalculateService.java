package com.itwillbs.qtable.service.storeManagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.qtable.mapper.storeManagementMapper.StoreCalculate;
import com.itwillbs.qtable.vo.storeManagement.CalculateVO;

@Service
public class StoreCalculateService {

	@Autowired
	StoreCalculate storeCalculate;
	
	/* 정산 갯수 찾아오는 서비스 */
	public int countCalculateByMemberidx(int member_idx) {
		return storeCalculate.countCalculateByMemberidx(member_idx);
	}
	
	/* 정산 목록 불러오는 서비스 */
	public List<CalculateVO> selectCaculateListByMemberidx(int member_idx, int start_row, int list_limit){
		return storeCalculate.selectCaculateListByMemberidx(member_idx, start_row, list_limit);
	}
	
	/* 정산 디테일 내용 불러오는 쿼리문 */
	public CalculateVO selectCalculateDetail(int jeongsan_idx, int member_idx){
		return storeCalculate.selectCalculateDetail(jeongsan_idx, member_idx);
	}
	
}
