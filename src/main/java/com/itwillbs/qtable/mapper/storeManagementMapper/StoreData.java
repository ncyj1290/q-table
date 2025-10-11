package com.itwillbs.qtable.mapper.storeManagementMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.vo.storeManagement.StoreVO;

/* 매장 관련 데이터들 불러오는 쿼리 */
@Mapper
public interface StoreData {

	StoreVO selectStoreProfileByOwnerIdx(@Param("member_idx") int member_idx);
	

}
