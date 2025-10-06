package com.itwillbs.qtable.mapper.storeManagementMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.qtable.vo.commonCode.CommonCodeVO;

@Mapper
public interface StoreCommonCode {
	
	/* 만약 Parent Code 필요 없을 시 Null 전달 필요 */
	List<CommonCodeVO> selectCommonCodeForStore(@Param("group_code") String group_code, @Param("parent_code") String parent_code);
	
}
