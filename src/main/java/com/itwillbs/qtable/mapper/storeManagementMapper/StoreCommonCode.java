package com.itwillbs.qtable.mapper.storeManagementMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;

@Mapper
public interface StoreCommonCode {
	
	List<CommonCodeVO> selectCommonCodeForStore(@Param("group_code") String group_code);
	
}
