package com.itwillbs.qtable.mapper.storeManagementMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.vo.storeManagement.StoreVO;

/* 매장 관련 데이터들 불러오는 쿼리 */
@Mapper
public interface StoreData {

	/* 매장 프로필 정보들 들고오는 쿼리문 */
	StoreVO selectStoreProfileByOwnerIdx(@Param("member_idx") int member_idx);
	
	/* 예약 받기 상태 셀렉트 쿼리문 */
	StoreVO selectAcceptStatus(@Param("store_idx") int store_idx);
	
	/* 예약 받기 상태 토글 쿼리문 */
	int toggleAcceptStatus(@Param("store_idx") int store_idx);
}
