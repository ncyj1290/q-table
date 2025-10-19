package com.itwillbs.qtable.mapper.storeManagementMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.vo.storeManagement.CalculateVO;

@Mapper
public interface StoreCalculate {

	/* 정산 갯수 찾아오는 함수 */
	int countCalculateByMemberidx(@Param("member_idx") int member_idx);
	
	/* 정산 목록 불러오는 쿼리문 */
	List<CalculateVO> selectCaculateListByMemberidx(@Param("member_idx") int member_idx, @Param("start_row") int start_row, @Param("list_limit") int list_limit);
	
	/* 정산 디테일 내용 불러오는 쿼리문 */
	CalculateVO selectCalculateDetail(@Param("jeongsan_idx") int jeongsan_idx, @Param("member_idx") int member_idx);
}
