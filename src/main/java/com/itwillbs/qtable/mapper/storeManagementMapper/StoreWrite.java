package com.itwillbs.qtable.mapper.storeManagementMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;
import com.itwillbs.qtable.vo.storeManagement.StoreVO;

@Mapper
public interface StoreWrite {
	
	/* 매장 등록 쿼리 */
	int insertNewStore(StoreVO storeVO);
	
	
	
}
